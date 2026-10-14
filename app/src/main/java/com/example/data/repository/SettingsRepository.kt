package com.example.data.repository

import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Process
import com.example.data.model.AppLanguage
import com.example.data.model.DelayMode
import com.example.data.model.ImageDisplayStyle
import com.example.data.model.ImageSourceType
import com.example.data.model.StudyGuardSettings
import com.example.data.model.TargetAppInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.util.Calendar
import java.util.concurrent.ConcurrentHashMap

class SettingsRepository(private val context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private val _settingsFlow = MutableStateFlow(loadSettings())
    val settingsFlow: StateFlow<StudyGuardSettings> = _settingsFlow.asStateFlow()

    // In-memory cache for app temporary unlock timestamps
    private val appUnlockTimestamps = ConcurrentHashMap<String, Long>()

    companion object {
        private const val PREFS_NAME = "study_guard_preferences"
        private const val KEY_IS_ENABLED = "is_enabled"
        private const val KEY_UNLOCK_GATE_ENABLED = "unlock_gate_enabled"
        private const val KEY_APP_LANGUAGE = "app_language"
        private const val KEY_PROMPT_TITLE = "prompt_title"
        private const val KEY_PROMPT_SUBTITLE = "prompt_subtitle"
        private const val KEY_IMAGE_TYPE = "image_type"
        private const val KEY_IMAGE_DISPLAY_STYLE = "image_display_style"
        private const val KEY_CUSTOM_IMAGE_URI = "custom_image_uri"
        private const val KEY_DELAY_SECONDS = "delay_seconds"
        private const val KEY_DELAY_MODE = "delay_mode"
        private const val KEY_COOLDOWN_MINUTES = "cooldown_minutes"
        private const val KEY_SCHEDULE_ENABLED = "schedule_enabled"
        private const val KEY_START_HOUR = "start_hour"
        private const val KEY_START_MINUTE = "start_minute"
        private const val KEY_END_HOUR = "end_hour"
        private const val KEY_END_MINUTE = "end_minute"
        private const val KEY_VIBRATION_ENABLED = "vibration_enabled"
        private const val KEY_REQUIRE_INTENTION = "require_intention"
        private const val KEY_LAST_PHONE_USE_TIME = "last_phone_use_time"
        
        // App Blocker Keys
        private const val KEY_APP_GATE_ENABLED = "app_gate_enabled"
        private const val KEY_BLOCKED_PACKAGES = "blocked_packages"
        private const val KEY_APP_GRACE_MINUTES = "app_grace_minutes"

        val POPULAR_DISTRACTION_PACKAGES = listOf(
            TargetAppInfo("com.instagram.android", "Instagram", isPopularPreset = true),
            TargetAppInfo("com.zhiliaoapp.musically", "TikTok", isPopularPreset = true),
            TargetAppInfo("com.google.android.youtube", "YouTube", isPopularPreset = true),
            TargetAppInfo("com.twitter.android", "X / Twitter", isPopularPreset = true),
            TargetAppInfo("org.telegram.messenger", "Telegram", isPopularPreset = true),
            TargetAppInfo("com.reddit.frontpage", "Reddit", isPopularPreset = true),
            TargetAppInfo("com.whatsapp", "WhatsApp", isPopularPreset = true),
            TargetAppInfo("com.snapchat.android", "Snapchat", isPopularPreset = true),
            TargetAppInfo("com.facebook.katana", "Facebook", isPopularPreset = true)
        )

        @Volatile
        private var INSTANCE: SettingsRepository? = null

        fun getInstance(context: Context): SettingsRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: SettingsRepository(context.applicationContext).also { INSTANCE = it }
            }
        }
    }

    fun loadSettings(): StudyGuardSettings {
        val isEnabled = prefs.getBoolean(KEY_IS_ENABLED, true)
        val unlockGateEnabled = prefs.getBoolean(KEY_UNLOCK_GATE_ENABLED, true)
        val langStr = prefs.getString(KEY_APP_LANGUAGE, AppLanguage.EN.name)
        val language = try {
            AppLanguage.valueOf(langStr ?: AppLanguage.EN.name)
        } catch (_: Exception) {
            AppLanguage.EN
        }

        val defaultTitle = if (language == AppLanguage.FA)
            "آیا واقعاً می‌خواهی از گوشی استفاده کنی یا برگردی سر درس؟"
        else
            "Do you really want to use your phone, or go back to studying?"

        val defaultSubtitle = if (language == AppLanguage.FA)
            "یک نفس عمیق بکش. آینده‌ات چشم‌انتظار تلاش امروز توست! 📚"
        else
            "Take a breath. Your study goals and future self are waiting for you! 📚"

        val title = prefs.getString(KEY_PROMPT_TITLE, defaultTitle) ?: defaultTitle
        val subtitle = prefs.getString(KEY_PROMPT_SUBTITLE, defaultSubtitle) ?: defaultSubtitle

        val imageTypeName = prefs.getString(KEY_IMAGE_TYPE, ImageSourceType.PRESET_DESK.name)
        val imageType = try {
            ImageSourceType.valueOf(imageTypeName ?: ImageSourceType.PRESET_DESK.name)
        } catch (_: Exception) {
            ImageSourceType.PRESET_DESK
        }

        val displayStyleName = prefs.getString(KEY_IMAGE_DISPLAY_STYLE, ImageDisplayStyle.MEDIUM_CARD.name)
        val displayStyle = try {
            ImageDisplayStyle.valueOf(displayStyleName ?: ImageDisplayStyle.MEDIUM_CARD.name)
        } catch (_: Exception) {
            ImageDisplayStyle.MEDIUM_CARD
        }

        val customImageUri = prefs.getString(KEY_CUSTOM_IMAGE_URI, null)
        val delaySeconds = prefs.getInt(KEY_DELAY_SECONDS, 10)
        val delayModeName = prefs.getString(KEY_DELAY_MODE, DelayMode.COUNTDOWN.name)
        val delayMode = try {
            DelayMode.valueOf(delayModeName ?: DelayMode.COUNTDOWN.name)
        } catch (_: Exception) {
            DelayMode.COUNTDOWN
        }
        val cooldownMinutes = prefs.getInt(KEY_COOLDOWN_MINUTES, 0)
        val scheduleEnabled = prefs.getBoolean(KEY_SCHEDULE_ENABLED, false)
        val startHour = prefs.getInt(KEY_START_HOUR, 8)
        val startMinute = prefs.getInt(KEY_START_MINUTE, 0)
        val endHour = prefs.getInt(KEY_END_HOUR, 22)
        val endMinute = prefs.getInt(KEY_END_MINUTE, 0)
        val vibrationEnabled = prefs.getBoolean(KEY_VIBRATION_ENABLED, true)
        val requireIntention = prefs.getBoolean(KEY_REQUIRE_INTENTION, false)

        val appGateEnabled = prefs.getBoolean(KEY_APP_GATE_ENABLED, true)
        val defaultBlocked = setOf(
            "com.instagram.android",
            "com.zhiliaoapp.musically",
            "com.google.android.youtube",
            "com.twitter.android",
            "org.telegram.messenger",
            "com.reddit.frontpage"
        )
        val blockedPackages = prefs.getStringSet(KEY_BLOCKED_PACKAGES, defaultBlocked) ?: defaultBlocked
        val appGraceMinutes = prefs.getInt(KEY_APP_GRACE_MINUTES, 5)

        return StudyGuardSettings(
            isEnabled = isEnabled,
            unlockGateEnabled = unlockGateEnabled,
            appLanguage = language,
            promptTitle = title,
            promptSubtitle = subtitle,
            imageType = imageType,
            imageDisplayStyle = displayStyle,
            customImageUri = customImageUri,
            delaySeconds = delaySeconds,
            delayMode = delayMode,
            cooldownMinutes = cooldownMinutes,
            scheduleEnabled = scheduleEnabled,
            startHour = startHour,
            startMinute = startMinute,
            endHour = endHour,
            endMinute = endMinute,
            vibrationEnabled = vibrationEnabled,
            requireIntentionNote = requireIntention,
            appGateEnabled = appGateEnabled,
            blockedPackages = blockedPackages,
            appGraceMinutes = appGraceMinutes
        )
    }

    fun updateSettings(newSettings: StudyGuardSettings) {
        prefs.edit().apply {
            putBoolean(KEY_IS_ENABLED, newSettings.isEnabled)
            putBoolean(KEY_UNLOCK_GATE_ENABLED, newSettings.unlockGateEnabled)
            putString(KEY_APP_LANGUAGE, newSettings.appLanguage.name)
            putString(KEY_PROMPT_TITLE, newSettings.promptTitle)
            putString(KEY_PROMPT_SUBTITLE, newSettings.promptSubtitle)
            putString(KEY_IMAGE_TYPE, newSettings.imageType.name)
            putString(KEY_IMAGE_DISPLAY_STYLE, newSettings.imageDisplayStyle.name)
            putString(KEY_CUSTOM_IMAGE_URI, newSettings.customImageUri)
            putInt(KEY_DELAY_SECONDS, newSettings.delaySeconds)
            putString(KEY_DELAY_MODE, newSettings.delayMode.name)
            putInt(KEY_COOLDOWN_MINUTES, newSettings.cooldownMinutes)
            putBoolean(KEY_SCHEDULE_ENABLED, newSettings.scheduleEnabled)
            putInt(KEY_START_HOUR, newSettings.startHour)
            putInt(KEY_START_MINUTE, newSettings.startMinute)
            putInt(KEY_END_HOUR, newSettings.endHour)
            putInt(KEY_END_MINUTE, newSettings.endMinute)
            putBoolean(KEY_VIBRATION_ENABLED, newSettings.vibrationEnabled)
            putBoolean(KEY_REQUIRE_INTENTION, newSettings.requireIntentionNote)
            putBoolean(KEY_APP_GATE_ENABLED, newSettings.appGateEnabled)
            putStringSet(KEY_BLOCKED_PACKAGES, newSettings.blockedPackages)
            putInt(KEY_APP_GRACE_MINUTES, newSettings.appGraceMinutes)
            apply()
        }
        _settingsFlow.value = newSettings
    }

    fun recordPhoneUseTime(timeMs: Long = System.currentTimeMillis()) {
        prefs.edit().putLong(KEY_LAST_PHONE_USE_TIME, timeMs).apply()
    }

    fun getLastPhoneUseTime(): Long {
        return prefs.getLong(KEY_LAST_PHONE_USE_TIME, 0L)
    }

    fun recordAppUnlockPass(packageName: String, timeMs: Long = System.currentTimeMillis()) {
        appUnlockTimestamps[packageName] = timeMs
    }

    fun isAppInGracePeriod(packageName: String): Boolean {
        val lastTime = appUnlockTimestamps[packageName] ?: return false
        val graceMs = loadSettings().appGraceMinutes * 60 * 1000L
        return (System.currentTimeMillis() - lastTime) < graceMs
    }

    fun isPackageBlocked(packageName: String): Boolean {
        val s = loadSettings()
        if (!s.isEnabled || !s.appGateEnabled) return false
        if (packageName == context.packageName) return false // Never block ourselves!
        if (isAppInGracePeriod(packageName)) return false
        return s.blockedPackages.contains(packageName)
    }

    fun shouldInterveneNow(): Boolean {
        val settings = loadSettings()
        if (!settings.isEnabled || !settings.unlockGateEnabled) return false

        if (settings.cooldownMinutes > 0) {
            val lastUsed = getLastPhoneUseTime()
            val cooldownMs = settings.cooldownMinutes * 60 * 1000L
            if (System.currentTimeMillis() - lastUsed < cooldownMs) {
                return false
            }
        }

        if (settings.scheduleEnabled) {
            val calendar = Calendar.getInstance()
            val currentMinutesOfDay = calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE)
            val startMinutes = settings.startHour * 60 + settings.startMinute
            val endMinutes = settings.endHour * 60 + settings.endMinute

            if (startMinutes <= endMinutes) {
                if (currentMinutesOfDay !in startMinutes..endMinutes) return false
            } else {
                if (currentMinutesOfDay < startMinutes && currentMinutesOfDay > endMinutes) return false
            }
        }

        return true
    }

    fun hasUsageStatsPermission(): Boolean {
        val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as? AppOpsManager ?: return false
        val mode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            appOps.unsafeCheckOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                Process.myUid(),
                context.packageName
            )
        } else {
            @Suppress("DEPRECATION")
            appOps.checkOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                Process.myUid(),
                context.packageName
            )
        }
        return mode == AppOpsManager.MODE_ALLOWED
    }

    fun getInstalledAppsList(): List<TargetAppInfo> {
        val pm = context.packageManager
        val blocked = loadSettings().blockedPackages
        val result = mutableListOf<TargetAppInfo>()

        // 1. Add popular presets first
        val addedPackages = mutableSetOf<String>()
        POPULAR_DISTRACTION_PACKAGES.forEach { preset ->
            result.add(preset.copy(isBlocked = blocked.contains(preset.packageName)))
            addedPackages.add(preset.packageName)
        }

        // 2. Query installed launchable apps
        val intent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }

        try {
            val resolveInfos = pm.queryIntentActivities(intent, 0)
            for (info in resolveInfos) {
                val pkg = info.activityInfo.packageName
                if (pkg == context.packageName) continue
                if (!addedPackages.contains(pkg)) {
                    val label = info.loadLabel(pm)?.toString() ?: pkg
                    result.add(
                        TargetAppInfo(
                            packageName = pkg,
                            appName = label,
                            isBlocked = blocked.contains(pkg),
                            isPopularPreset = false
                        )
                    )
                    addedPackages.add(pkg)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return result.sortedWith(
            compareByDescending<TargetAppInfo> { it.isBlocked }
                .thenByDescending { it.isPopularPreset }
                .thenBy { it.appName.lowercase() }
        )
    }

    fun saveCustomImageFromUri(uri: Uri): String? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri) ?: return null
            val destFile = File(context.filesDir, "study_custom_picture.jpg")
            val outputStream = FileOutputStream(destFile)
            inputStream.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }
            val absolutePath = destFile.absolutePath
            val current = _settingsFlow.value
            val updated = current.copy(
                imageType = ImageSourceType.CUSTOM_PHOTO,
                customImageUri = absolutePath
            )
            updateSettings(updated)
            absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
