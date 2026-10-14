package com.example.data.model

enum class AppLanguage {
    EN,
    FA
}

enum class ImageSourceType {
    PRESET_DESK,
    PRESET_GOALS,
    PRESET_MEDICAL,
    CUSTOM_PHOTO,
    MINIMAL_NO_IMAGE
}

enum class ImageDisplayStyle {
    FULL_SCREEN_BACKGROUND,
    LARGE_CARD,
    MEDIUM_CARD,
    COMPACT_BANNER
}

enum class DelayMode {
    COUNTDOWN,
    HOLD_BUTTON
}

enum class FocusChoice {
    STUDIED,
    PHONE_USED
}

data class FocusRecord(
    val id: String = System.currentTimeMillis().toString(),
    val timestamp: Long = System.currentTimeMillis(),
    val choice: FocusChoice,
    val note: String = ""
)

data class TargetAppInfo(
    val packageName: String,
    val appName: String,
    val isBlocked: Boolean = false,
    val isPopularPreset: Boolean = false
)

data class StudyGuardSettings(
    val isEnabled: Boolean = true,
    val unlockGateEnabled: Boolean = true,
    val appLanguage: AppLanguage = AppLanguage.EN,
    val promptTitle: String = "Do you really want to use your phone, or go back to studying?",
    val promptSubtitle: String = "Take a breath. Your study goals and future self are waiting for you! 📚",
    val imageType: ImageSourceType = ImageSourceType.PRESET_DESK,
    val imageDisplayStyle: ImageDisplayStyle = ImageDisplayStyle.MEDIUM_CARD,
    val customImageUri: String? = null,
    val delaySeconds: Int = 10,
    val delayMode: DelayMode = DelayMode.COUNTDOWN,
    val cooldownMinutes: Int = 0, // 0 = Intervene on every single unlock
    val scheduleEnabled: Boolean = false,
    val startHour: Int = 8,
    val startMinute: Int = 0,
    val endHour: Int = 22,
    val endMinute: Int = 0,
    val vibrationEnabled: Boolean = true,
    val requireIntentionNote: Boolean = false,
    
    // New Feature: Specific Apps Gate
    val appGateEnabled: Boolean = true,
    val blockedPackages: Set<String> = setOf(
        "com.instagram.android",
        "com.zhiliaoapp.musically",
        "com.google.android.youtube",
        "com.twitter.android",
        "org.telegram.messenger",
        "com.reddit.frontpage"
    ),
    val appGraceMinutes: Int = 5 // Grace period in minutes after user chooses "Use App"
)
