package com.example.ui.components

import android.content.Context
import android.media.AudioManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.view.SoundEffectConstants
import android.view.View
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.domain.model.LightingTheme
import com.example.ui.theme.CyberNeonColors
import com.example.ui.theme.GoldGlowColors
import com.example.ui.theme.ObsidianSurface
import com.example.ui.theme.ObsidianSurfaceElevated
import com.example.ui.theme.PureDarkColors
import com.example.ui.theme.RainbowColors
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun rememberNeonSweepPhase(durationMillis: Int = 3600): Float {
    val transition = rememberInfiniteTransition(label = "neon_sweep")
    val phase by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = durationMillis, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "neon_phase",
    )
    return phase
}

fun lightingColorsFor(theme: LightingTheme): List<Color> = when (theme) {
    LightingTheme.RAINBOW_PULSE -> RainbowColors
    LightingTheme.CYBER_NEON -> CyberNeonColors
    LightingTheme.PURE_DARK -> PureDarkColors
    LightingTheme.GOLD_GLOW -> GoldGlowColors
}

fun animatedNeonBrush(theme: LightingTheme, phase: Float): Brush {
    val colors = lightingColorsFor(theme)
    val angle = phase * 2f * PI.toFloat()
    val dx = cos(angle) * 420f
    val dy = sin(angle) * 420f
    return Brush.linearGradient(
        colors = colors,
        start = Offset(420f - dx, 240f - dy),
        end = Offset(420f + dx, 240f + dy),
    )
}

fun Modifier.neonBorder(
    theme: LightingTheme,
    phase: Float,
    borderWidth: Dp = 1.75.dp,
    cornerRadius: Dp = 20.dp,
    glowAlpha: Float = 0.25f,
): Modifier = this.drawBehind {
    val brush = animatedNeonBrush(theme, phase)
    val radiusPx = cornerRadius.toPx()
    val strokePx = borderWidth.toPx()
    if (theme != LightingTheme.PURE_DARK && glowAlpha > 0f) {
        drawRoundRect(
            brush = brush,
            cornerRadius = CornerRadius(radiusPx, radiusPx),
            style = Stroke(width = strokePx * 3.4f),
            alpha = glowAlpha,
        )
    }
    drawRoundRect(
        brush = brush,
        cornerRadius = CornerRadius(radiusPx, radiusPx),
        style = Stroke(width = strokePx),
    )
}

fun Modifier.neonSurface(
    theme: LightingTheme,
    phase: Float,
    shape: Shape = RoundedCornerShape(20.dp),
    containerColor: Color = ObsidianSurface,
    borderWidth: Dp = 1.5.dp,
    cornerRadius: Dp = 20.dp,
): Modifier = this
    .clip(shape)
    .background(containerColor)
    .neonBorder(theme = theme, phase = phase, borderWidth = borderWidth, cornerRadius = cornerRadius)

fun staticThemeBorderStroke(theme: LightingTheme, width: Dp = 1.25.dp): BorderStroke {
    val brush = Brush.linearGradient(lightingColorsFor(theme))
    return BorderStroke(width, brush)
}

object TactileFeedbackHelper {
    @Suppress("DEPRECATION")
    fun triggerKeyFeedback(
        context: Context,
        view: View,
        soundEnabled: Boolean,
        vibrationEnabled: Boolean,
    ) {
        if (soundEnabled) {
            val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as? AudioManager
            if (audioManager != null) {
                audioManager.playSoundEffect(AudioManager.FX_KEY_CLICK, 0.55f)
            } else {
                view.playSoundEffect(SoundEffectConstants.CLICK)
            }
        }
        if (vibrationEnabled) {
            try {
                val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    val vm = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                    vm?.defaultVibrator
                } else {
                    context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                }
                if (vibrator?.hasVibrator() == true) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        vibrator.vibrate(VibrationEffect.createOneShot(18L, 85))
                    } else {
                        vibrator.vibrate(18L)
                    }
                }
            } catch (_: SecurityException) {
                // Ignore if device restricts vibrator
            }
        }
    }
}

fun backdropGradientBrush(id: com.example.domain.model.BuiltInBackdrop): Brush {
    return when (id) {
        com.example.domain.model.BuiltInBackdrop.NONE -> Brush.verticalGradient(
            listOf(ObsidianSurface, ObsidianSurfaceElevated),
        )
        com.example.domain.model.BuiltInBackdrop.COSMIC_NEBULA -> Brush.linearGradient(
            listOf(Color(0xFF1A0B2E), Color(0xFF0E2954), Color(0xFF3A0CA3)),
        )
        com.example.domain.model.BuiltInBackdrop.CYBER_GRID -> Brush.linearGradient(
            listOf(Color(0xFF061B29), Color(0xFF0B3B49), Color(0xFF1B1035)),
        )
        com.example.domain.model.BuiltInBackdrop.MIDNIGHT_AURORA -> Brush.linearGradient(
            listOf(Color(0xFF071D1A), Color(0xFF0D2D3E), Color(0xFF1F1135)),
        )
        com.example.domain.model.BuiltInBackdrop.CRIMSON_EMBER -> Brush.linearGradient(
            listOf(Color(0xFF2B0917), Color(0xFF3D1308), Color(0xFF140B1A)),
        )
    }
}
