package com.example.util

import com.example.data.model.AppLanguage

object Localization {

    fun get(key: String, lang: AppLanguage): String {
        val map = if (lang == AppLanguage.FA) persianStrings else englishStrings
        return map[key] ?: englishStrings[key] ?: key
    }

    private val englishStrings = mapOf(
        "app_title" to "StudyGuard",
        "app_subtitle" to "Unlock Focus Gate",
        
        // Navigation labels (concise 1-word labels to prevent clipping on mobile)
        "nav_home" to "Home",
        "nav_customize" to "Design",
        "nav_rules" to "Rules",
        "nav_history" to "Stats",
        "nav_about" to "About",
        
        // Dashboard
        "guard_active" to "GUARD ACTIVE",
        "guard_paused" to "GUARD PAUSED",
        "guard_active_desc" to "Intervening on phone unlock",
        "guard_paused_desc" to "Intervention is paused",
        "guard_detail" to "Delay: %ds • Reflection gate active",
        "guard_paused_detail" to "Turn switch on to guard your study sessions.",
        "focus_saves" to "Focus Saves",
        "chose_studying" to "Chose studying",
        "time_saved" to "Time Saved",
        "estimated_study_time" to "Study time saved",
        "active_gate_preview" to "Gate Preview",
        "test_gate_now" to "Test Gate",
        "preview" to "Test",
        "permission_recommended" to "Permission Recommended",
        "permission_desc" to "Allow 'Display over other apps' to show the prompt immediately upon phone unlock.",
        "grant_permission" to "Grant Permission",
        "language_toggle" to "Language / زبان",

        // Customize
        "set_gate_picture" to "Gate Picture",
        "set_picture_desc" to "Choose your inspiring visual.",
        "cozy_desk" to "Desk",
        "study_goals" to "Goals",
        "medical_focus" to "Medical (MD)",
        "pick_from_gallery" to "Gallery",
        "custom_photo_saved" to "Custom Photo ✓",
        "minimalist_icon" to "Minimal",
        "picture_display_size" to "Picture Display Style",
        "style_fullscreen" to "Full-Screen Wallpaper",
        "style_large" to "Large Card (280dp)",
        "style_medium" to "Standard Card (200dp)",
        "style_compact" to "Compact Banner (120dp)",
        
        "set_prompt_text" to "Prompt Message",
        "intervention_question" to "Intervention Question",
        "subtitle_reminder" to "Subtitle / Goal Reminder",
        "quick_presets" to "Preset Quotes (Tap to apply):",
        
        "set_reflection_delay" to "Reflection Delay",
        "delay_friction_desc" to "Mindful pause before unlock button activates.",
        "delay_mode_title" to "Delay Mode:",
        "countdown_timer" to "Countdown Timer",
        "countdown_desc" to "Timer with breathing guide",
        "hold_to_unlock" to "Hold to Unlock",
        "hold_desc" to "Must hold button continuously",
        "test_customized_gate" to "Test Gate Preview",

        // Intervention Screen
        "mindful_focus_gate" to "MINDFUL FOCUS GATE",
        "reflection_delay_active" to "Reflection Delay",
        "take_3_breaths" to "Take 3 deep breaths before deciding",
        "mindful_delay_complete" to "Delay complete. Decide thoughtfully.",
        "back_to_studying" to "Go Back to Studying 📚",
        "use_phone_anyway" to "Use Phone Anyway 📱",
        "unlocks_in_s" to "Unlocks in %ds...",
        "press_and_hold_unlock" to "Hold to Unlock (%ds)",
        "unlocked" to "Unlocked!",
        "what_is_intention" to "What is your intention?",
        "proceed_with_intent" to "Continue with Intent",
        "focus_on_dreams" to "FOCUS ON YOUR DREAMS",

        // Rules
        "grace_period" to "Grace Period (Cooldown)",
        "grace_period_desc" to "How long before StudyGuard prompts again after phone use?",
        "every_unlock" to "Every Unlock (0m)",
        "study_hours_schedule" to "Study Hours Schedule",
        "haptic_feedback" to "Haptic Feedback Vibration",
        "ask_intention" to "Ask for Phone Intention",
        "ask_intention_desc" to "Prompts for reason when choosing phone",

        // History
        "performance_title" to "Focus Performance",
        "win_rate" to "Focus Win Rate",
        "total_interventions" to "Total Prompts",
        "recent_log" to "Recent Activity",
        "no_interventions_yet" to "No Interventions Yet",
        "clear_history" to "Clear",

        // About Page
        "about_title" to "About StudyGuard",
        "developer_name" to "Alireza Zare, MD",
        "developer_email" to "3alirezare3@gmail.com",
        "made_with_heart" to "Made with ❤️ by me",
        "app_purpose_headline" to "Mindful Attention in a Distracted World",
        "app_story" to "Intense study and medical education demand deep, sustained concentration. In our digital age, habitual phone unlocking can quickly steal hours of productivity.\n\nStudyGuard gives you a second chance every time you unlock your phone: a mindful pause to reflect, breathe, and return to your goals.",
        "contact_button" to "Send Email Feedback",
        "version_info" to "Version 2.0 • Mindful Gate"
    )

    private val persianStrings = mapOf(
        "app_title" to "استادی‌گارد",
        "app_subtitle" to "دروازه تمرکز مطالعه",
        
        // Navigation labels (concise 1-word labels to prevent clipping on mobile)
        "nav_home" to "خانه",
        "nav_customize" to "طراحی",
        "nav_rules" to "قوانین",
        "nav_history" to "آمار",
        "nav_about" to "درباره",

        // Dashboard
        "guard_active" to "محافظ فعال است",
        "guard_paused" to "محافظ متوقف است",
        "guard_active_desc" to "هنگام باز شدن قفل نمایش داده می‌شود",
        "guard_paused_desc" to "محافظت موقتاً غیرفعال است",
        "guard_detail" to "تاخیر: %d ثانیه • دروازه تامل فعال",
        "guard_paused_detail" to "کلید را روشن کنید تا تمرکز شما حفظ شود.",
        "focus_saves" to "نجات تمرکز",
        "chose_studying" to "انتخاب درس",
        "time_saved" to "زمان ذخیره",
        "estimated_study_time" to "زمان مفید درس",
        "active_gate_preview" to "پیش‌نمایش دروازه",
        "test_gate_now" to "تست دروازه",
        "preview" to "تست",
        "permission_recommended" to "دسترسی پیشنهادی",
        "permission_desc" to "اجازه «نمایش روی سایر برنامه‌ها» را برای نمایش فوری فعال کنید.",
        "grant_permission" to "اعطای دسترسی",
        "language_toggle" to "زبان / Language",

        // Customize
        "set_gate_picture" to "تصویر دروازه",
        "set_picture_desc" to "تصویر انگیزشی خود را انتخاب کنید.",
        "cozy_desk" to "میز دنج",
        "study_goals" to "اهداف",
        "medical_focus" to "پزشکی (MD)",
        "pick_from_gallery" to "گالری",
        "custom_photo_saved" to "عکس شما ✓",
        "minimalist_icon" to "مینیمال",
        "picture_display_size" to "حالت نمایش تصویر",
        "style_fullscreen" to "تصویر پس‌زمینه کامل",
        "style_large" to "کارت بزرگ (280dp)",
        "style_medium" to "کارت استاندارد (200dp)",
        "style_compact" to "بنر فشرده (120dp)",

        "set_prompt_text" to "متن و پیام هشدار",
        "intervention_question" to "سوال اصلی تامل",
        "subtitle_reminder" to "زیرنویس / یادآوری هدف",
        "quick_presets" to "جملات آماده (لمس برای انتخاب):",

        "set_reflection_delay" to "زمان تاخیر تامل",
        "delay_friction_desc" to "مکث کوتاه قبل از فعال شدن دکمه گوشی.",
        "delay_mode_title" to "حالت تاخیر:",
        "countdown_timer" to "شمارش معکوس",
        "countdown_desc" to "کاهش ثانیه با راهنمای تنفس",
        "hold_to_unlock" to "نگه داشتن دکمه",
        "hold_desc" to "نگه داشتن ممتد چند ثانیه‌ای",
        "test_customized_gate" to "تست پیش‌نمایش",

        // Intervention Screen
        "mindful_focus_gate" to "دروازه هوشیاری و تمرکز",
        "reflection_delay_active" to "تاخیر تامل فعال است",
        "take_3_breaths" to "۳ نفس عمیق بکشید و تصمیم بگیرید",
        "mindful_delay_complete" to "تاخیر سپری شد. آگاهانه انتخاب کنید.",
        "back_to_studying" to "برگشت به درس 📚",
        "use_phone_anyway" to "استفاده از گوشی 📱",
        "unlocks_in_s" to "باز شدن در %d ثانیه...",
        "press_and_hold_unlock" to "نگه دارید (%d ثانیه)",
        "unlocked" to "باز شد!",
        "what_is_intention" to "هدف شما از استفاده از گوشی چیست؟",
        "proceed_with_intent" to "ادامه با هدف مشخص",
        "focus_on_dreams" to "روی رویاهایت تمرکز کن",

        // Rules
        "grace_period" to "مهلت وقفه (Cooldown)",
        "grace_period_desc" to "در صورت استفاده، تا چند دقیقه بعد دوباره نپرسد؟",
        "every_unlock" to "هر بار باز شدن قفل (۰ دقیقه)",
        "study_hours_schedule" to "برنامه ساعات مطالعه",
        "haptic_feedback" to "لرزش بازخورد لمسی (ویبره)",
        "ask_intention" to "پرسیدن دلیل استفاده از گوشی",
        "ask_intention_desc" to "هنگام انتخاب گوشی، دلیل را بپرسد",

        // History
        "performance_title" to "کارنامه تمرکز",
        "win_rate" to "درصد موفقیت",
        "total_interventions" to "مجموع تامل‌ها",
        "recent_log" to "فعالیت‌های اخیر",
        "no_interventions_yet" to "هنوز داده‌ای ثبت نشده است",
        "clear_history" to "پاک کردن",

        // About Page
        "about_title" to "درباره برنامه",
        "developer_name" to "دکتر علیرضا زارع",
        "developer_email" to "3alirezare3@gmail.com",
        "made_with_heart" to "ساخته شده با ❤️ توسط من",
        "app_purpose_headline" to "محافظت از تمرکز در دنیای دیجیتال",
        "app_story" to "مطالعه طولانی و دوران تحصیل پزشکی نیازمند تمرکز عمیق است. باز کردن غیرارادی قفل گوشی می‌تواند ساعت‌ها وقت باارزش را بسوزاند.\n\nاستادی‌گارد با هر بار باز کردن گوشی فرصتی برای یک مکث آگاهانه به شما می‌دهد تا به کتاب‌ها و رویاهایتان بازگردید.",
        "contact_button" to "ارسال ایمیل",
        "version_info" to "نسخه ۲.۰ • گیت تمرکز"
    )

    data class PresetOption(
        val titleEn: String,
        val subtitleEn: String,
        val titleFa: String,
        val subtitleFa: String
    )

    val PRESET_QUOTES = listOf(
        PresetOption(
            titleEn = "Do you really want to use phone, or go back to studying?",
            subtitleEn = "Your future self will thank you for focusing today! 📚",
            titleFa = "آیا واقعاً می‌خواهی از گوشی استفاده کنی یا برگردی سر درس؟",
            subtitleFa = "آینده‌ات چشم‌انتظار تلاش امروز توست! 📚"
        ),
        PresetOption(
            titleEn = "Doctor in the making! Stay focused on your patients & future.",
            subtitleEn = "Every page mastered brings you closer to saving lives. 🩺",
            titleFa = "پزشک آینده! روی هدف و بیماران فردایت تمرکز کن.",
            subtitleFa = "هر صفحه‌ای که یاد می‌گیری گامی به سوی نجات انسان‌هاست. 🩺"
        ),
        PresetOption(
            titleEn = "Is this urgent or just a habit reflex?",
            subtitleEn = "Take a deep breath and return to deep work.",
            titleFa = "آیا این کار ضروری است یا فقط عادت غیرارادی؟",
            subtitleFa = "یک نفس عمیق بکش و به کار عمیق و مطالعه برگرد."
        ),
        PresetOption(
            titleEn = "Exams are coming up! Every minute counts.",
            subtitleEn = "Can you push for just 25 more minutes of study?",
            titleFa = "امتحانات نزدیک است! هر دقیقه ارزش طلا دارد.",
            subtitleFa = "می‌توانی فقط ۲۵ دقیقه دیگر مطالعه کنی؟"
        ),
        PresetOption(
            titleEn = "Will this scroll matter in 5 years?",
            subtitleEn = "What you study today will shape who you become tomorrow.",
            titleFa = "آیا این چرخیدن در گوشی ۵ سال دیگر اهمیتی دارد؟",
            subtitleFa = "آنچه امروز می‌خوانی، فردای تو را خواهد ساخت."
        ),
        PresetOption(
            titleEn = "Pause. Breathe. What is your top priority right now?",
            subtitleEn = "Protect your focus. Put the phone down.",
            titleFa = "مکث کن. نفس بکش. اولویت شماره یک تو الان چیست؟",
            subtitleFa = "از تمرکزت محافظت کن و گوشی را کنار بگذار."
        )
    )
}
