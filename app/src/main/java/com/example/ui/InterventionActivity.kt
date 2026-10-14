package com.example.ui

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.data.repository.SettingsRepository
import com.example.data.repository.StatsRepository
import com.example.ui.theme.MyApplicationTheme
import com.example.util.DeviceAdminHelper
import com.example.util.Localization

class InterventionActivity : ComponentActivity() {

    companion object {
        const val EXTRA_IS_APP_TRIGGER = "extra_is_app_trigger"
        const val EXTRA_TARGET_PACKAGE = "extra_target_package"
        const val EXTRA_TARGET_APP_NAME = "extra_target_app_name"

        @Volatile
        var isVisible: Boolean = false
    }

    private var requestedLockOnResume: Boolean = false

    override fun onStart() {
        super.onStart()
        isVisible = true
    }

    override fun onStop() {
        super.onStop()
        isVisible = false
    }

    override fun onResume() {
        super.onResume()
        hideSystemBars()
        if (requestedLockOnResume && DeviceAdminHelper.isDeviceAdminActive(applicationContext)) {
            requestedLockOnResume = false
            DeviceAdminHelper.lockDevice(applicationContext)
            sendToHomeScreen()
            finish()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        // Enforce mindful focus gate: prevent closing gate without waiting
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            hideSystemBars()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setupLockscreenVisibility()

        val settingsRepo = SettingsRepository.getInstance(applicationContext)
        val statsRepo = StatsRepository.getInstance(applicationContext)

        // Block hardware and gesture back navigation completely
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val lang = settingsRepo.loadSettings().appLanguage
                Toast.makeText(
                    this@InterventionActivity,
                    Localization.get("gate_locked_toast", lang),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

        val isAppTrigger = intent.getBooleanExtra(EXTRA_IS_APP_TRIGGER, false)
        val targetPackage = intent.getStringExtra(EXTRA_TARGET_PACKAGE)
        val targetAppName = intent.getStringExtra(EXTRA_TARGET_APP_NAME)

        setContent {
            MyApplicationTheme(darkTheme = true) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    InterventionScreen(
                        settings = settingsRepo.loadSettings(),
                        isAppTrigger = isAppTrigger,
                        targetAppName = targetAppName,
                        onBackToStudying = {
                            val note = if (isAppTrigger && !targetAppName.isNullOrEmpty()) {
                                "Avoided $targetAppName"
                            } else {
                                "Chose studying on unlock"
                            }
                            statsRepo.recordChoice(
                                com.example.data.model.FocusChoice.STUDIED,
                                note
                            )

                            if (DeviceAdminHelper.isDeviceAdminActive(applicationContext)) {
                                DeviceAdminHelper.lockDevice(applicationContext)
                                sendToHomeScreen()
                                finish()
                            } else {
                                requestedLockOnResume = true
                                val lang = settingsRepo.loadSettings().appLanguage
                                Toast.makeText(
                                    this@InterventionActivity,
                                    Localization.get("device_admin_required", lang),
                                    Toast.LENGTH_LONG
                                ).show()
                                DeviceAdminHelper.requestDeviceAdmin(this@InterventionActivity)
                            }
                        },
                        onUsePhone = { note ->
                            if (isAppTrigger && !targetPackage.isNullOrEmpty()) {
                                settingsRepo.recordAppUnlockPass(targetPackage)
                            } else {
                                settingsRepo.recordPhoneUseTime()
                            }
                            val recordNote = if (isAppTrigger && !targetAppName.isNullOrEmpty()) {
                                "Opened $targetAppName ($note)"
                            } else {
                                note
                            }
                            statsRepo.recordChoice(
                                com.example.data.model.FocusChoice.PHONE_USED,
                                recordNote
                            )
                            finish()
                        }
                    )
                }
            }
        }
    }

    private fun setupLockscreenVisibility() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        } else {
            @Suppress("DEPRECATION")
            window.addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
            )
        }
        hideSystemBars()
    }

    private fun hideSystemBars() {
        try {
            val windowInsetsController = WindowInsetsControllerCompat(window, window.decorView)
            windowInsetsController.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
        } catch (_: Exception) {}
    }

    private fun sendToHomeScreen() {
        val homeIntent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_HOME)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(homeIntent)
    }
}
