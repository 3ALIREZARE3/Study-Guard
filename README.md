# StudyGuard (استادی‌گارد) 📚🛡️
### Mindful Phone Unlock & Specific Apps Focus Gate for Android

> **"Do you really want to use your phone, or go back to studying?"**  
> *"آیا واقعاً می‌خواهی از گوشی استفاده کنی یا برگردی سر درس؟"*

---

## 🌟 Overview

**StudyGuard** is an open-source Android focus intervention app engineered for students, medical researchers, and deep-work professionals. It disrupts reflexive, dopamine-driven phone checks and mindless app launches by introducing an intentional, mindful friction layer:

1. **Phone Unlock Gate**: Every time you unlock your device, StudyGuard intercepts the screen with an inspiring study visual, customizable motivational questions, and an enforced reflection countdown or hold-to-unlock delay.
2. **Specific Apps Gate (App Blocker)**: Beyond phone unlock, StudyGuard actively guards against opening specific distracting applications (e.g., Instagram, TikTok, YouTube, X/Twitter, Telegram, Reddit, or any custom installed app).
3. **App Cooldown Pass**: Once you thoughtfully decide to use a guarded app, you get a configurable grace window (1m, 5m, 10m, 15m) before the reflection gate appears again.
4. **Bilingual Support (English & Persian / فارسی)**: Full native localization with automatic RTL (Right-to-Left) layout support, Persian typography, and custom quotes.
5. **Privacy First & Local Storage**: No cloud analytics, no remote trackers. All settings and focus stats remain 100% on your device.

---

## ✨ Features

- **Dual Intervention Triggers**:
  - 🔓 **Phone Unlock Gate**: Toggle on/off to intervene on every lockscreen unlock.
  - 📱 **Specific Apps Gate**: Toggle on/off to intervene whenever selected distracting apps are opened.
  - Can be used independently or together.
- **Configurable Reflection Delays**:
  - ⏳ **Countdown Timer**: 5s to 60s with an animated breathing circle to induce calm.
  - 🔒 **Hold-to-Unlock**: Physical friction requiring continuous tactile contact before accessing your phone or app.
- **Visuals & Presets**:
  - Full-screen wallpaper mode, large card, standard card, or compact banner.
  - Built-in presets: Cozy Study Desk, Vision & Goals, and Medical (MD) Focus.
  - Custom image picker from device photo gallery.
- **Customizable Prompts & Quotes**:
  - Edit the main question and subtitle reminders.
  - Quick presets curated for medical students and deep focus.
- **Intention Requirement**:
  - Optional prompt asking "What is your intention?" (Urgent task, Study search, Important message, Break) before entering the phone.
- **Focus Statistics & History**:
  - Track total interventions, study decisions, focus win rate, and estimated minutes of study time saved.
  - Activity log detailing every mindful choice.
- **About Developer & Medical Mission**:
  - Created by **Alireza Zare, MD** (`3alirezare3@gmail.com`).
  - Dedicated to helping students preserve their mental clarity and master their education.

---

## 📱 Screenshots & Flow

```
[Phone Unlocked or Blocked App Opened]
                 │
                 ▼
       ┌──────────────────┐
       │   StudyGuard     │
       │ Reflection Gate  │
       │ (Cozy Desk / MD) │
       └─────────┬────────┘
                 │
    ┌────────────┴────────────┐
    │                         │
    ▼                         ▼
[Go Back to Studying]    [Delay Countdown (e.g. 10s)]
  • Records focus win        • Breathing guide
  • Sends to Home Screen      • Unlocks "Open Anyway"
  • Protects your time        • Allows intentional use
```

---

## 🛠️ Tech Stack & Architecture

- **Language**: Kotlin 2.0+
- **UI Framework**: Jetpack Compose (Material Design 3)
- **Architecture**: MVVM with Clean Repository Pattern
- **Async & Reactive**: Kotlin Coroutines & StateFlow
- **Image Loading**: Coil Compose
- **System Integrations**:
  - `ForegroundService` with `ACTION_USER_PRESENT` / `ACTION_SCREEN_ON` broadcast interception
  - `UsageStatsManager` for real-time foreground app launch detection
  - `SYSTEM_ALERT_WINDOW` & `USE_FULL_SCREEN_INTENT` for reliable lockscreen display
  - Android Photo Picker (`PickVisualMedia`) for safe zero-permission image selection

---

## 🚀 Getting Started

### Prerequisites
- Android Studio Ladybug | 2024.2.1 or newer
- Android SDK 35 (Min SDK 24)
- JDK 17 or newer

### Installation & Setup

1. **Clone the repository**:
   ```bash
   git clone https://github.com/your-username/StudyGuard.git
   cd StudyGuard
   ```

2. **Open in Android Studio**:
   Open the root project folder in Android Studio and let Gradle sync.

3. **Required Android Permissions**:
   Upon first launch, enable the following in Android Settings:
   - **Display over other apps** (Overlay Permission): Allows StudyGuard to display immediately over lockscreen and apps.
   - **Usage Access**: Allows StudyGuard to detect when guarded distracting apps are opened.
   - **Notifications**: Keeps the lightweight foreground monitoring service alive.

4. **Build and Run**:
   ```bash
   gradle assembleDebug
   ```

---

## 👨‍⚕️ Author

- **Developer**: Alireza Zare, MD
- **Email**: [3alirezare3@gmail.com](mailto:3alirezare3@gmail.com)
- **Tagline**: Made with ❤️ by me

---

## 📄 License

This project is open-source under the [MIT License](LICENSE).
