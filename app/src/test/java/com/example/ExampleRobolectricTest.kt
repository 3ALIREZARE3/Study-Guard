package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.AppLanguage
import com.example.data.model.DelayMode
import com.example.data.model.FocusChoice
import com.example.data.model.ImageDisplayStyle
import com.example.data.model.ImageSourceType
import com.example.data.repository.SettingsRepository
import com.example.data.repository.StatsRepository
import com.example.util.Localization
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("StudyGuard", appName)
  }

  @Test
  fun `verify default settings and updates`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val settingsRepo = SettingsRepository(context)
    val settings = settingsRepo.loadSettings()

    assertTrue(settings.isEnabled)
    assertEquals(AppLanguage.EN, settings.appLanguage)
    assertEquals(10, settings.delaySeconds)
    assertEquals(DelayMode.COUNTDOWN, settings.delayMode)

    // Update settings with Persian, Medical preset, and Fullscreen style
    val updated = settings.copy(
      appLanguage = AppLanguage.FA,
      promptTitle = "وقت مطالعه است!",
      delaySeconds = 15,
      imageType = ImageSourceType.PRESET_MEDICAL,
      imageDisplayStyle = ImageDisplayStyle.FULL_SCREEN_BACKGROUND
    )
    settingsRepo.updateSettings(updated)

    val reloaded = settingsRepo.loadSettings()
    assertEquals(AppLanguage.FA, reloaded.appLanguage)
    assertEquals("وقت مطالعه است!", reloaded.promptTitle)
    assertEquals(15, reloaded.delaySeconds)
    assertEquals(ImageSourceType.PRESET_MEDICAL, reloaded.imageType)
    assertEquals(ImageDisplayStyle.FULL_SCREEN_BACKGROUND, reloaded.imageDisplayStyle)
  }

  @Test
  fun `verify bilingual localization`() {
    val enTitle = Localization.get("about_title", AppLanguage.EN)
    val faTitle = Localization.get("about_title", AppLanguage.FA)

    assertEquals("About StudyGuard", enTitle)
    assertEquals("درباره برنامه", faTitle)
    assertEquals("Alireza Zare, MD", Localization.get("developer_name", AppLanguage.EN))
  }

  @Test
  fun `verify stats recording`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val statsRepo = StatsRepository(context)
    statsRepo.resetStats()

    statsRepo.recordChoice(FocusChoice.STUDIED, "Chose study")
    statsRepo.recordChoice(FocusChoice.STUDIED, "Chose study again")
    statsRepo.recordChoice(FocusChoice.PHONE_USED, "Quick message")

    val stats = statsRepo.loadStats()
    assertEquals(3, stats.totalInterventions)
    assertEquals(2, stats.studyChoices)
    assertEquals(1, stats.phoneChoices)
    assertEquals(66, stats.focusRatePercent)
    assertEquals(40, stats.estimatedMinutesSaved)
  }
}
