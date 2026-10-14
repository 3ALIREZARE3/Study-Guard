package com.example.util

import com.example.data.model.AppLanguage

object Localization {

    fun get(key: String, lang: AppLanguage): String {
        val map = if (lang == AppLanguage.FA) persianStrings else englishStrings
        return map[key] ?: englishStrings[key] ?: key
    }

    private val englishStrings = mapOf(
        "app_title" to "StudyGuard",
        "app_subtitle" to "Unlock & App Focus Gate",
        
        // Navigation labels
        "nav_home" to "Home",
        "nav_apps" to "Apps",
        "nav_customize" to "Design",
        "nav_rules" to "Rules",
        "nav_history" to "Stats",
        "nav_about" to "About",
        
        // Dashboard
        "guard_active" to "GUARD ACTIVE",
        "guard_paused" to "GUARD PAUSED",
        "guard_active_desc" to "Intervening on phone unlock & target apps",
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
        "permission_desc" to "Allow 'Display over other apps' to show the prompt immediately.",
        "grant_permission" to "Grant Permission",
        "language_toggle" to "Language / زبان",

        // Apps Gate Tab
        "unlock_gate_title" to "Phone Unlock Gate",
        "unlock_gate_desc" to "Intervene every time phone is unlocked",
        "trigger_modes_title" to "Intervention Triggers",
        "trigger_modes_desc" to "Choose when StudyGuard reflection gate should appear",
        "test_unlock_gate" to "Test Unlock Gate",
        "test_app_gate" to "Test App Gate",
        "configure_apps" to "Manage Apps (%d)",
        "select_all_preset" to "Select All Presets",
        "clear_all_apps" to "Deselect All",
        "gate_status_both" to "Guarding Phone Unlock & %d Selected Apps",
        "gate_status_unlock_only" to "Guarding Phone Unlock only",
        "gate_status_apps_only" to "Guarding %d Selected Apps only",
        "gate_status_none" to "No triggers enabled (Turn on Unlock or Apps gate)",
        "app_gate_title" to "Specific Apps Gate",
        "app_gate_desc" to "Show reflection gate when opening selected distracting apps",
        "usage_access_needed" to "Usage Access Required",
        "usage_access_desc" to "Enable Usage Access so StudyGuard can detect when you launch protected apps.",
        "grant_usage_access" to "Grant Usage Access",
        "protected_apps" to "Protected Apps",
        "apps_selected_count" to "%d apps protected",
        "search_apps" to "Search installed apps...",
        "popular_distractions" to "Popular Distractions",
        "all_installed_apps" to "All Installed Apps",
        "app_grace_title" to "App Cooldown Pass",
        "app_grace_desc" to "Once unlocked, allow using the app for %d minutes before re-prompting.",
        "minutes_pass" to "%d min pass",

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
        "app_focus_gate" to "APP FOCUS GATE",
        "opening_app_prompt" to "Opening %s 📱",
        "reflection_delay_active" to "Reflection Delay",
        "take_3_breaths" to "Take 3 deep breaths before deciding",
        "mindful_delay_complete" to "Delay complete. Decide thoughtfully.",
        "back_to_studying" to "Go Back to Studying 📚",
        "use_phone_anyway" to "Use Phone Anyway 📱",
        "open_app_anyway" to "Open %s Anyway 📱",
        "unlocks_in_s" to "Unlocks in %ds...",
        "press_and_hold_unlock" to "Hold to Unlock (%ds)",
        "unlocked" to "Unlocked!",
        "what_is_intention" to "What is your intention?",
        "proceed_with_intent" to "Continue with Intent",
        "focus_on_dreams" to "FOCUS ON YOUR DREAMS",
        "device_admin_title" to "Automatic Screen Lock (Device Admin)",
        "device_admin_desc" to "Automatically locks and turns off the phone when you tap 'Go Back to Studying' so you return to focus instantly.",
        "device_admin_active" to "Screen Lock Active ✓",
        "device_admin_required" to "Screen Lock Required",
        "activate_device_admin" to "Activate Screen Lock",
        "test_phone_lock" to "Test Lock Phone",
        "gate_locked_toast" to "Focus Gate is active: Tap 'Go Back to Studying' to lock phone, or wait for the reflection timer.",
        "locks_phone_instantly" to "Locks phone automatically",
        "enable_lock_chip" to "Enable screen lock for auto-lock on study",

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
        "app_story" to "Intense study and medical education demand deep, sustained concentration. In our digital age, habitual phone unlocking and opening social apps can quickly steal hours of productivity.\n\nStudyGuard gives you a second chance: a mindful pause to reflect, breathe, and return to your goals.",
        "contact_button" to "Send Email Feedback",
        "version_info" to "Version 2.5 • Unlock & App Gate"
    )

    private val persianStrings = mapOf(
        "app_title" to "استادی‌گارد",
        "app_subtitle" to "دروازه تمرکز قفل و برنامه‌ها",
        
        // Navigation labels
        "nav_home" to "خانه",
        "nav_apps" to "برنامه‌ها",
        "nav_customize" to "طراحی",
        "nav_rules" to "قوانین",
        "nav_history" to "آمار",
        "nav_about" to "درباره",

        // Dashboard
        "guard_active" to "محافظ فعال است",
        "guard_paused" to "محافظ متوقف است",
        "guard_active_desc" to "هنگام باز شدن قفل و برنامه‌های هدف فعال است",
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

        // Apps Gate Tab
        "unlock_gate_title" to "دروازه باز شدن قفل گوشی",
        "unlock_gate_desc" to "نمایش صفحه تامل با هر بار باز شدن قفل گوشی",
        "trigger_modes_title" to "موقعیت‌های فعال‌سازی",
        "trigger_modes_desc" to "مشخص کنید دروازه تامل در چه زمان‌هایی نمایش داده شود",
        "test_unlock_gate" to "تست دروازه قفل",
        "test_app_gate" to "تست دروازه برنامه",
        "configure_apps" to "مدیریت برنامه‌ها (%d)",
        "select_all_preset" to "انتخاب برنامه‌های پرطرفدار",
        "clear_all_apps" to "لغو انتخاب همه",
        "gate_status_both" to "فعال هنگام باز شدن قفل و %d برنامه هدف",
        "gate_status_unlock_only" to "فقط فعال هنگام باز شدن قفل گوشی",
        "gate_status_apps_only" to "فقط فعال هنگام باز شدن %d برنامه انتخابی",
        "gate_status_none" to "هیچ حالتی فعال نیست (قفل یا برنامه‌ها را روشن کنید)",
        "app_gate_title" to "دروازه برنامه‌های خاص",
        "app_gate_desc" to "نمایش صفحه تامل هنگام باز کردن برنامه‌های حواس‌پرت‌کن",
        "usage_access_needed" to "دسترسی به میزان استفاده لازم است",
        "usage_access_desc" to "دسترسی Usage Access را فعال کنید تا برنامه بتواند اجرای برنامه‌های محافظت‌شده را تشخیص دهد.",
        "grant_usage_access" to "اعطای دسترسی استفاده",
        "protected_apps" to "برنامه‌های محافظت‌شده",
        "apps_selected_count" to "%d برنامه محافظت‌شده",
        "search_apps" to "جستجوی برنامه‌ها...",
        "popular_distractions" to "برنامه‌های پرحواس‌پرتی",
        "all_installed_apps" to "سایر برنامه‌های نصب‌شده",
        "app_grace_title" to "مهلت موقت پس از باز کردن",
        "app_grace_desc" to "پس از باز کردن، تا %d دقیقه مجدداً صفحه تامل نشان داده نشود.",
        "minutes_pass" to "مهلت %d دقیقه",

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
        "app_focus_gate" to "دروازه تمرکز برنامه",
        "opening_app_prompt" to "در حال باز کردن %s 📱",
        "reflection_delay_active" to "تاخیر تامل فعال است",
        "take_3_breaths" to "۳ نفس عمیق بکشید و تصمیم بگیرید",
        "mindful_delay_complete" to "تاخیر سپری شد. آگاهانه انتخاب کنید.",
        "back_to_studying" to "برگشت به درس 📚",
        "use_phone_anyway" to "استفاده از گوشی 📱",
        "open_app_anyway" to "باز کردن %s 📱",
        "unlocks_in_s" to "باز شدن در %d ثانیه...",
        "press_and_hold_unlock" to "نگه دارید (%d ثانیه)",
        "unlocked" to "باز شد!",
        "what_is_intention" to "هدف شما از استفاده چیست؟",
        "proceed_with_intent" to "ادامه با هدف مشخص",
        "focus_on_dreams" to "روی رویاهایت تمرکز کن",
        "device_admin_title" to "قفل خودکار گوشی (Device Admin)",
        "device_admin_desc" to "هنگام انتخاب 'برگشت به درس'، گوشی بلافاصله قفل و خاموش می‌شود تا بدون حواس‌پرتی درس بخوانید.",
        "device_admin_active" to "قفل خودکار فعال است ✓",
        "device_admin_required" to "مجوز قفل صفحه لازم است",
        "activate_device_admin" to "فعال‌سازی قفل صفحه",
        "test_phone_lock" to "تست قفل کردن گوشی",
        "gate_locked_toast" to "دروازه تمرکز فعال است: برای قفل شدن گوشی روی برگشت به درس بزنید یا تا پایان زمان صبر کنید.",
        "locks_phone_instantly" to "گوشی را خودکار قفل و خاموش می‌کند",
        "enable_lock_chip" to "فعال‌سازی مجوز قفل خودکار صفحه",

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
        "app_story" to "مطالعه طولانی و دوران تحصیل پزشکی نیازمند تمرکز عمیق است. باز کردن غیرارادی قفل گوشی یا باز کردن برنامه‌های اجتماعی می‌تواند ساعت‌ها وقت باارزش را بسوزاند.\n\nاستادی‌گارد با هر بار باز شدن قفل یا اجرای برنامه‌های خاص فرصتی برای یک مکث آگاهانه به شما می‌دهد تا به کتاب‌ها و رویاهایتان بازگردید.",
        "contact_button" to "ارسال ایمیل",
        "version_info" to "نسخه ۲.۵ • دروازه قفل و برنامه"
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
