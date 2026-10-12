# StudyGuard 📖🔒

<p align="center">
  <img src="app/src/main/res/drawable/img_medical_focus.jpg" alt="StudyGuard Banner" width="600" style="border-radius: 16px;" />
</p>

<p align="center">
  <b>A mindful Android gatekeeper that interrupts phone unlock reflexes, prompts reflection, and protects deep study focus.</b>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android-green.svg" alt="Platform" />
  <img src="https://img.shields.io/badge/Language-Kotlin%202.2-blue.svg" alt="Kotlin" />
  <img src="https://img.shields.io/badge/UI-Jetpack%20Compose%20M3-purple.svg" alt="Compose" />
  <img src="https://img.shields.io/badge/Localization-English%20%7C%20Persian%20(فارسی)-orange.svg" alt="Bilingual" />
  <img src="https://img.shields.io/badge/License-MIT-lightgrey.svg" alt="License" />
</p>

---

## 🌟 Overview

In modern study routines—especially for intensive pursuits like medical education—habitual phone unlocking breaks concentration and burns hours of productivity. **StudyGuard** acts as an intentional pause button.

Every time you unlock your device, StudyGuard intercepts the unlock event and presents a full-screen mindful reflection gate:
1. **Presents your custom motivational image** (medical study art, library goals, cozy desk, or your own photo).
2. **Prompts your reflection question** (e.g., *"Do you really want to use your phone, or go back to studying?"*).
3. **Enforces a customizable delay** (countdown timer with guided breathing or continuous hold-to-unlock).
4. **Offers two clear paths**:
   - **Go Back to Studying 📚**: Instantly closes the prompt, returns you to your home screen, and tracks your focus streak.
   - **Use Phone Anyway 📱**: Becomes available only after the mindful delay has elapsed.

---

## ✨ Features

- **📱 Pure Lockscreen Interception**: Automatically detects `ACTION_USER_PRESENT` on device unlock and launches the full-screen reflection gate without cluttering your notification shade.
- **🌍 Bilingual Support (English & Persian / فارسی)**:
  - English by default with instant in-app switching to Persian.
  - Native Right-to-Left (RTL) layout support.
  - Persian script typography preserved without cursive breaking.
- **🎨 Visual Customization & Sizing**:
  - **Preset Artwork**: Medical Focus (MD), Cozy Study Desk, Goals & Library, or Minimalist Aura.
  - **Custom Gallery Picker**: Pick any personal inspiration photo from your device via the zero-permission Android Photo Picker.
  - **Display Modes**: Full-Screen Wallpaper (immersive background), Large Card (280dp), Standard Card (200dp), and Compact Banner (120dp).
- **⏱️ Configurable Mindful Delay**:
  - Select between **0s and 60s** (quick presets: 0s, 5s, 10s, 15s, 30s, 60s).
  - **Countdown Timer Mode**: Animated progress ring with guided breathing prompt.
  - **Hold-to-Unlock Mode**: Requires continuous holding to break automatic muscle-memory tapping.
- **📊 Focus Statistics & History**:
  - Live Focus Win Rate percentage.
  - Total interventions logged and estimated study hours saved.
  - Interactive decision log with timestamps and optional intention tags.
- **⚙️ Flexible Scheduling & Grace Periods**:
  - Configurable cooldown grace period (0m, 1m, 3m, 5m, 15m).
  - Study hours scheduling window (e.g., active 08:00 – 22:00, or 24/7).
  - Optional phone intention prompt and haptic feedback.

---

## 🛠️ Tech Stack & Architecture

- **Language**: Kotlin 100%
- **UI Toolkit**: Jetpack Compose with Material Design 3 (M3)
- **Architecture**: Clean Architecture / MVVM with reactive Kotlin Coroutines & `StateFlow`
- **Image Loading**: Coil Compose
- **Permissions**: Modern Android Photo Picker (`ActivityResultContracts.PickVisualMedia`), Foreground Service (`specialUse`), and Overlay Window API
- **Testing**: Robolectric JVM unit tests for repository persistence, localization, and unlock rules

---

## 🚀 Getting Started

### Prerequisites
- Android Studio Ladybug / Meerkat or newer
- Android SDK 34+
- Java 11+
- Gradle 8.x+

### Installation & Build

1. **Clone the repository**:
   ```bash
   git clone https://github.com/your-username/studyguard.git
   cd studyguard
   ```
2. **Open in Android Studio and sync Gradle.**
3. **Build Debug APK**:
   ```bash
   ./gradlew assembleDebug
   ```
4. **Run Unit & Robolectric Tests**:
   ```bash
   ./gradlew :app:testDebugUnitTest
   ```
5. **Deploy to Device / Emulator**:
   Run the app from Android Studio or install the APK:
   ```bash
   adb install -r app/build/outputs/apk/debug/app-debug.apk
   ```

---

## 🔐 Required Device Permissions

To ensure StudyGuard can intervene reliably on phone unlock:

- **Foreground Service**: Keeps the monitor active while your device is locked.
- **Display Over Other Apps (SYSTEM_ALERT_WINDOW)**: Allows the intervention gate to appear immediately when unlocking on Android 10+. A 1-tap setup button is provided directly in the Dashboard tab.
- **Notifications (POST_NOTIFICATIONS)**: Required for the silent Android Foreground Service status indicator.

---

## 👨‍⚕️ Author & Credits

**Alireza Zare, MD**

📧 Email: 3alirezare3@gmail.com

❤️ Made with ❤️ by me

> "As medical students and professionals know, long hours of intense study require uninterrupted concentration. In our digital age, the simple reflex of unlocking our phones can cost hours of deep focus. StudyGuard was created to give you a mindful second chance."

---

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.
