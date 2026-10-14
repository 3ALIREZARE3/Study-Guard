package com.example.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.MainActivity
import com.example.R
import com.example.data.repository.SettingsRepository
import com.example.ui.InterventionActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class UnlockMonitorService : Service() {

    private lateinit var settingsRepository: SettingsRepository
    private var isReceiverRegistered = false
    private val serviceScope = CoroutineScope(Dispatchers.Default + Job())
    private var appMonitorJob: Job? = null
    private var isScreenOn = true

    private val screenReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            when (intent?.action) {
                Intent.ACTION_USER_PRESENT -> {
                    handleDeviceUnlocked(context)
                    startAppMonitoring()
                }
                Intent.ACTION_SCREEN_ON -> {
                    isScreenOn = true
                    startAppMonitoring()
                }
                Intent.ACTION_SCREEN_OFF -> {
                    isScreenOn = false
                    stopAppMonitoring()
                }
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        settingsRepository = SettingsRepository.getInstance(applicationContext)
        createNotificationChannels()
        registerScreenReceiver()
        startAppMonitoring()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = buildForegroundNotification()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(
                NOTIFICATION_ID_SERVICE,
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
            )
        } else {
            startForeground(NOTIFICATION_ID_SERVICE, notification)
        }

        return START_STICKY
    }

    private fun registerScreenReceiver() {
        if (!isReceiverRegistered) {
            val filter = IntentFilter().apply {
                addAction(Intent.ACTION_USER_PRESENT)
                addAction(Intent.ACTION_SCREEN_ON)
                addAction(Intent.ACTION_SCREEN_OFF)
            }
            registerReceiver(screenReceiver, filter)
            isReceiverRegistered = true
        }
    }

    private fun handleDeviceUnlocked(context: Context) {
        if (InterventionActivity.isVisible) return
        if (!settingsRepository.shouldInterveneNow()) {
            return
        }

        val activityIntent = Intent(context, InterventionActivity::class.java).apply {
            addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK or
                Intent.FLAG_ACTIVITY_CLEAR_TOP or
                Intent.FLAG_ACTIVITY_SINGLE_TOP
            )
            putExtra(InterventionActivity.EXTRA_IS_APP_TRIGGER, false)
        }

        try {
            context.startActivity(activityIntent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Periodically inspects foreground activity changes when screen is active
     * to intercept launches of specific blocked apps.
     */
    private fun startAppMonitoring() {
        if (appMonitorJob?.isActive == true) return

        appMonitorJob = serviceScope.launch {
            val usageStatsManager = getSystemService(Context.USAGE_STATS_SERVICE) as? UsageStatsManager
            var lastTriggeredPackage = ""
            var lastTriggerTime = 0L

            while (isActive && isScreenOn) {
                try {
                    if (InterventionActivity.isVisible) {
                        delay(500L)
                        continue
                    }
                    val settings = settingsRepository.loadSettings()
                    if (settings.isEnabled && settings.appGateEnabled && usageStatsManager != null && settingsRepository.hasUsageStatsPermission()) {
                        val now = System.currentTimeMillis()
                        val events = usageStatsManager.queryEvents(now - 3000, now)
                        val event = UsageEvents.Event()
                        var currentForegroundPackage: String? = null

                        while (events.hasNextEvent()) {
                            events.getNextEvent(event)
                            if (event.eventType == UsageEvents.Event.ACTIVITY_RESUMED) {
                                currentForegroundPackage = event.packageName
                            }
                        }

                        if (currentForegroundPackage != null && currentForegroundPackage != packageName) {
                            if (settingsRepository.isPackageBlocked(currentForegroundPackage)) {
                                // Prevent rapid back-to-back triggers for the same app launch within 3 seconds
                                if (currentForegroundPackage != lastTriggeredPackage || (now - lastTriggerTime) > 3000) {
                                    lastTriggeredPackage = currentForegroundPackage
                                    lastTriggerTime = now
                                    launchAppIntervention(currentForegroundPackage)
                                }
                            }
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                delay(500L) // Responsive check every 500ms
            }
        }
    }

    private fun stopAppMonitoring() {
        appMonitorJob?.cancel()
        appMonitorJob = null
    }

    private fun launchAppIntervention(targetPackage: String) {
        val pm = packageManager
        val appName = try {
            val appInfo = pm.getApplicationInfo(targetPackage, 0)
            pm.getApplicationLabel(appInfo).toString()
        } catch (_: Exception) {
            targetPackage
        }

        val activityIntent = Intent(this, InterventionActivity::class.java).apply {
            addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK or
                Intent.FLAG_ACTIVITY_CLEAR_TOP or
                Intent.FLAG_ACTIVITY_SINGLE_TOP
            )
            putExtra(InterventionActivity.EXTRA_IS_APP_TRIGGER, true)
            putExtra(InterventionActivity.EXTRA_TARGET_PACKAGE, targetPackage)
            putExtra(InterventionActivity.EXTRA_TARGET_APP_NAME, appName)
        }

        try {
            startActivity(activityIntent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun buildForegroundNotification(): Notification {
        val openAppIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            openAppIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, CHANNEL_ID_SERVICE)
            .setContentTitle(getString(R.string.service_notification_title))
            .setContentText(getString(R.string.service_notification_text))
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_MIN)
            .setShowWhen(false)
            .setSilent(true)
            .build()
    }

    private fun createNotificationChannels() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val serviceChannel = NotificationChannel(
            CHANNEL_ID_SERVICE,
            getString(R.string.service_notification_channel_name),
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = getString(R.string.service_notification_channel_desc)
            setShowBadge(false)
            setSound(null, null)
            enableVibration(false)
        }
        notificationManager.createNotificationChannel(serviceChannel)
    }

    override fun onDestroy() {
        stopAppMonitoring()
        if (isReceiverRegistered) {
            try {
                unregisterReceiver(screenReceiver)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            isReceiverRegistered = false
        }
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    companion object {
        const val CHANNEL_ID_SERVICE = "study_guard_monitor_channel"
        const val NOTIFICATION_ID_SERVICE = 1001

        fun start(context: Context) {
            val intent = Intent(context, UnlockMonitorService::class.java)
            ContextCompat.startForegroundService(context, intent)
        }

        fun stop(context: Context) {
            val intent = Intent(context, UnlockMonitorService::class.java)
            context.stopService(intent)
        }
    }
}
