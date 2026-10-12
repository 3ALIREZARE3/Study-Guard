package com.example.ui

import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
import com.example.data.model.AppLanguage
import com.example.data.model.DelayMode
import com.example.data.model.ImageDisplayStyle
import com.example.data.model.ImageSourceType
import com.example.data.model.StudyGuardSettings
import com.example.util.Localization
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun InterventionScreen(
    settings: StudyGuardSettings,
    onBackToStudying: () -> Unit,
    onUsePhone: (note: String) -> Unit
) {
    val context = LocalContext.current
    val lang = settings.appLanguage
    val isRtl = lang == AppLanguage.FA
    val totalDelaySeconds = settings.delaySeconds
    var secondsLeft by remember { mutableIntStateOf(totalDelaySeconds) }
    var holdProgress by remember { mutableFloatStateOf(0f) }
    var selectedReason by remember {
        mutableStateOf(if (isRtl) "کار ضروری" else "Quick task")
    }
    var showReasonSelector by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    val infiniteTransition = rememberInfiniteTransition(label = "breathing")
    val breathScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "breathScale"
    )

    LaunchedEffect(key1 = settings.delayMode, key2 = totalDelaySeconds) {
        if (settings.delayMode == DelayMode.COUNTDOWN && totalDelaySeconds > 0) {
            secondsLeft = totalDelaySeconds
            while (secondsLeft > 0) {
                delay(1000L)
                secondsLeft--
            }
        } else {
            secondsLeft = 0
        }
    }

    BackHandler {
        onBackToStudying()
    }

    val isUnlockUnlocked = if (settings.delayMode == DelayMode.COUNTDOWN) {
        secondsLeft <= 0
    } else {
        holdProgress >= 1f
    }

    fun vibrateShort() {
        if (!settings.vibrationEnabled) return
        try {
            val vibrator = context.getSystemService(Vibrator::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(50)
            }
        } catch (_: Exception) {}
    }

    CompositionLocalProvider(
        LocalLayoutDirection provides if (isRtl) LayoutDirection.Rtl else LayoutDirection.Ltr
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF0F172A)),
            contentAlignment = Alignment.Center
        ) {
            // Fullscreen Background Mode
            if (settings.imageDisplayStyle == ImageDisplayStyle.FULL_SCREEN_BACKGROUND &&
                settings.imageType != ImageSourceType.MINIMAL_NO_IMAGE
            ) {
                GateImageContent(
                    imageType = settings.imageType,
                    customUri = settings.customImageUri,
                    modifier = Modifier.fillMaxSize(),
                    breathScale = breathScale,
                    contentScale = ContentScale.Crop
                )
                // Dark ambient readability scrim
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color.Black.copy(alpha = 0.65f),
                                    Color.Black.copy(alpha = 0.75f),
                                    Color.Black.copy(alpha = 0.92f)
                                )
                            )
                        )
                )
            } else {
                // Default gradient background
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color(0xFF0F172A),
                                    Color(0xFF1E1B4B),
                                    Color(0xFF0F172A)
                                )
                            )
                        )
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .widthIn(max = 540.dp)
                    .statusBarsPadding()
                    .navigationBarsPadding()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 14.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top Mindful Badge (No letter-spacing in Persian to prevent cursive disconnect)
                Row(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color(0xFF312E81).copy(alpha = 0.85f))
                        .padding(horizontal = 14.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.SelfImprovement,
                        contentDescription = null,
                        tint = Color(0xFFFBBF24),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = Localization.get("mindful_focus_gate", lang),
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = Color(0xFFE0E7FF),
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            letterSpacing = if (isRtl) 0.sp else 1.sp
                        ),
                        maxLines = 1
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Card Picture Display (when not in full screen wallpaper mode)
                if (settings.imageDisplayStyle != ImageDisplayStyle.FULL_SCREEN_BACKGROUND) {
                    val cardHeight = when (settings.imageDisplayStyle) {
                        ImageDisplayStyle.LARGE_CARD -> 260.dp
                        ImageDisplayStyle.MEDIUM_CARD -> 190.dp
                        ImageDisplayStyle.COMPACT_BANNER -> 120.dp
                        else -> 190.dp
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(cardHeight)
                            .clip(RoundedCornerShape(18.dp)),
                        shape = RoundedCornerShape(18.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))
                    ) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            GateImageContent(
                                imageType = settings.imageType,
                                customUri = settings.customImageUri,
                                modifier = Modifier.fillMaxSize(),
                                breathScale = breathScale,
                                contentScale = ContentScale.Crop
                            )

                            // Soft gradient bottom fade
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.verticalGradient(
                                            0.5f to Color.Transparent,
                                            1.0f to Color(0xFF0F172A).copy(alpha = 0.85f)
                                        )
                                    )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                }

                // Question Prompt Text (Responsive line height and text sizing)
                Text(
                    text = settings.promptTitle,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        fontSize = 18.sp,
                        lineHeight = 26.sp
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 6.dp)
                        .testTag("intervention_prompt_title"),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Subtitle Reminder
                Text(
                    text = settings.promptSubtitle,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color(0xFFCBD5E1),
                        fontSize = 13.sp,
                        lineHeight = 19.sp
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                        .testTag("intervention_prompt_subtitle"),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Delay / Mindful Countdown Block (Responsive, single-line text where possible)
                if (totalDelaySeconds > 0) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF1E293B).copy(alpha = 0.85f)
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            if (settings.delayMode == DelayMode.COUNTDOWN) {
                                if (secondsLeft > 0) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            CircularProgressIndicator(
                                                progress = { (totalDelaySeconds - secondsLeft).toFloat() / totalDelaySeconds },
                                                modifier = Modifier.size(46.dp),
                                                color = Color(0xFFFBBF24),
                                                trackColor = Color(0xFF334155),
                                                strokeWidth = 3.5.dp
                                            )
                                            Text(
                                                text = "${secondsLeft}s",
                                                style = MaterialTheme.typography.titleMedium.copy(
                                                    color = Color(0xFFFBBF24),
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 13.sp
                                                )
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = Localization.get("reflection_delay_active", lang),
                                                style = MaterialTheme.typography.bodyMedium.copy(
                                                    color = Color.White,
                                                    fontWeight = FontWeight.SemiBold,
                                                    fontSize = 13.sp
                                                ),
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                            Text(
                                                text = Localization.get("take_3_breaths", lang),
                                                style = MaterialTheme.typography.bodySmall.copy(
                                                    color = Color(0xFF94A3B8),
                                                    fontSize = 11.sp
                                                ),
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                    }
                                } else {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = null,
                                            tint = Color(0xFF34D399),
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = Localization.get("mindful_delay_complete", lang),
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                color = Color(0xFF34D399),
                                                fontWeight = FontWeight.Medium,
                                                fontSize = 12.sp
                                            ),
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                            } else {
                                Text(
                                    text = if (holdProgress >= 1f)
                                        Localization.get("unlocked", lang)
                                    else
                                        String.format(Localization.get("press_and_hold_unlock", lang), totalDelaySeconds),
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = if (holdProgress >= 1f) Color(0xFF34D399) else Color(0xFFCBD5E1),
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 12.sp
                                    ),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Action Buttons
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // PRIMARY CHOICE: "Go Back to Studying 📚"
                    Button(
                        onClick = {
                            vibrateShort()
                            onBackToStudying()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .testTag("action_back_to_studying"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF10B981),
                            contentColor = Color.White
                        ),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.MenuBook,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = Localization.get("back_to_studying", lang),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    // SECONDARY CHOICE: "Use Phone Anyway 📱"
                    if (settings.delayMode == DelayMode.HOLD_BUTTON && !isUnlockUnlocked) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF334155))
                                .border(1.dp, Color(0xFF475569), RoundedCornerShape(12.dp))
                                .pointerInput(totalDelaySeconds) {
                                    detectTapGestures(
                                        onPress = {
                                            val startTime = System.currentTimeMillis()
                                            val holdJob = coroutineScope.launch {
                                                while (holdProgress < 1f) {
                                                    val elapsed = System.currentTimeMillis() - startTime
                                                    holdProgress = (elapsed.toFloat() / (totalDelaySeconds * 1000f)).coerceIn(0f, 1f)
                                                    delay(50)
                                                }
                                            }
                                            tryAwaitRelease()
                                            holdJob.cancel()
                                            if (holdProgress < 1f) {
                                                holdProgress = 0f
                                            }
                                        }
                                    )
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(holdProgress)
                                    .fillMaxSize()
                                    .background(Color(0xFF4F46E5).copy(alpha = 0.7f))
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 14.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = null,
                                    tint = Color.White.copy(alpha = 0.8f),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = String.format(
                                        Localization.get("press_and_hold_unlock", lang),
                                        (totalDelaySeconds * (1f - holdProgress)).toInt().coerceAtLeast(1)
                                    ),
                                    color = Color.White,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 12.sp
                                    ),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    } else {
                        OutlinedButton(
                            onClick = {
                                if (isUnlockUnlocked) {
                                    if (settings.requireIntentionNote && !showReasonSelector) {
                                        showReasonSelector = true
                                    } else {
                                        vibrateShort()
                                        onUsePhone(selectedReason)
                                    }
                                }
                            },
                            enabled = isUnlockUnlocked,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("action_use_phone_anyway"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = if (isUnlockUnlocked) Color(0xFFF1F5F9) else Color(0xFF64748B),
                                disabledContentColor = Color(0xFF64748B)
                            ),
                            border = ButtonDefaults.outlinedButtonBorder.copy(
                                brush = Brush.horizontalGradient(
                                    listOf(
                                        if (isUnlockUnlocked) Color(0xFF6366F1) else Color(0xFF334155),
                                        if (isUnlockUnlocked) Color(0xFF818CF8) else Color(0xFF334155)
                                    )
                                )
                            )
                        ) {
                            Icon(
                                imageVector = if (isUnlockUnlocked) Icons.Default.PhoneAndroid else Icons.Default.HourglassBottom,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = when {
                                    !isUnlockUnlocked -> String.format(Localization.get("unlocks_in_s", lang), secondsLeft)
                                    showReasonSelector -> Localization.get("proceed_with_intent", lang)
                                    else -> Localization.get("use_phone_anyway", lang)
                                },
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 13.sp
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    // Intention Selector (Scrollable row so chips never wrap awkwardly)
                    AnimatedVisibility(
                        visible = showReasonSelector,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 2.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = Localization.get("what_is_intention", lang),
                                style = MaterialTheme.typography.labelMedium.copy(
                                    color = Color(0xFF94A3B8),
                                    fontSize = 11.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            val reasonChips = if (isRtl) {
                                listOf("کار ضروری", "پیام مهم", "جستجوی درسی", "استراحت")
                            } else {
                                listOf("Quick check", "Urgent reply", "Study research", "Break")
                            }
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState())
                            ) {
                                reasonChips.forEach { reason ->
                                    FilterChip(
                                        selected = selectedReason == reason,
                                        onClick = { selectedReason = reason },
                                        label = { Text(reason, fontSize = 11.sp) },
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = Color(0xFF4F46E5),
                                            selectedLabelColor = Color.White
                                        )
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Button(
                                onClick = {
                                    vibrateShort()
                                    onUsePhone(selectedReason)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(44.dp),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text(Localization.get("proceed_with_intent", lang), fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GateImageContent(
    imageType: ImageSourceType,
    customUri: String?,
    modifier: Modifier = Modifier,
    breathScale: Float = 1f,
    contentScale: ContentScale = ContentScale.Crop
) {
    when (imageType) {
        ImageSourceType.PRESET_DESK -> {
            Image(
                painter = painterResource(id = R.drawable.img_study_desk),
                contentDescription = "Study desk image",
                modifier = modifier,
                contentScale = contentScale
            )
        }
        ImageSourceType.PRESET_GOALS -> {
            Image(
                painter = painterResource(id = R.drawable.img_goals_focus),
                contentDescription = "Study goals image",
                modifier = modifier,
                contentScale = contentScale
            )
        }
        ImageSourceType.PRESET_MEDICAL -> {
            Image(
                painter = painterResource(id = R.drawable.img_medical_focus),
                contentDescription = "Medical study image",
                modifier = modifier,
                contentScale = contentScale
            )
        }
        ImageSourceType.CUSTOM_PHOTO -> {
            if (!customUri.isNullOrEmpty() && File(customUri).exists()) {
                AsyncImage(
                    model = File(customUri),
                    contentDescription = "Custom study photo",
                    modifier = modifier,
                    contentScale = contentScale
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.img_study_desk),
                    contentDescription = "Fallback study desk image",
                    modifier = modifier,
                    contentScale = contentScale
                )
            }
        }
        ImageSourceType.MINIMAL_NO_IMAGE -> {
            Box(
                modifier = modifier.background(
                    Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF4338CA),
                            Color(0xFF1E1B4B)
                        )
                    )
                ),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.MenuBook,
                        contentDescription = null,
                        tint = Color(0xFFFBBF24),
                        modifier = Modifier
                            .size(56.dp)
                            .scale(breathScale)
                    )
                }
            }
        }
    }
}
