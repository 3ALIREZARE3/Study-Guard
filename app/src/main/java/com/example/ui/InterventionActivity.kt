package com.example.ui

import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.data.repository.SettingsRepository
import com.example.data.repository.StatsRepository
import com.example.ui.theme.MyApplicationTheme

class InterventionActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setupLockscreenVisibility()

        val settingsRepo = SettingsRepository.getInstance(applicationContext)
        val statsRepo = StatsRepository.getInstance(applicationContext)

        setContent {
            MyApplicationTheme(darkTheme = true) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    InterventionScreen(
                        settings = settingsRepo.loadSettings(),
                        onBackToStudying = {
                            statsRepo.recordChoice(
                                com.example.data.model.FocusChoice.STUDIED,
                                "Chose studying over phone"
                            )
                            sendToHomeScreen()
                            finish()
                        },
                        onUsePhone = { note ->
                            statsRepo.recordChoice(
                                com.example.data.model.FocusChoice.PHONE_USED,
                                note
                            )
                            settingsRepo.recordPhoneUseTime()
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
            val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
            keyguardManager.requestDismissKeyguard(this, null)
        } else {
            @Suppress("DEPRECATION")
            window.addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON or
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
            )
        }
    }

    private fun sendToHomeScreen() {
        val homeIntent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_HOME)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(homeIntent)
    }
}
