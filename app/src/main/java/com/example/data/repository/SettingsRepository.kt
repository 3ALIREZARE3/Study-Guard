package com.example.data.repository

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import com.example.data.model.AppLanguage
import com.example.data.model.DelayMode
import com.example.data.model.ImageDisplayStyle
import com.example.data.model.ImageSourceType
import com.example.data.model.StudyGuardSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File
import java.io.FileOutputStream
import java.util.Calendar

class SettingsRepository(private val context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private val _settingsFlow = MutableStateFlow(loadSettings())
    val settingsFlow: StateFlow<StudyGuardSettings> = _settingsFlow.asStateFlow()

    companion object {
        private const val PREFS_NAME = "study_guard_preferences"
        private const val KEY_IS_ENABLED = "is_enabled"
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

        return StudyGuardSettings(
            isEnabled = isEnabled,
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
            requireIntentionNote = requireIntention
        )
    }

    fun updateSettings(newSettings: StudyGuardSettings) {
        prefs.edit().apply {
            putBoolean(KEY_IS_ENABLED, newSettings.isEnabled)
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

    fun shouldInterveneNow(): Boolean {
        val settings = loadSettings()
        if (!settings.isEnabled) return false

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
