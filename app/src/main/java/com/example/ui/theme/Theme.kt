package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp

private val ColorTokensOutlineVariant = androidx.compose.ui.graphics.Color(0xFF1E223A)

private val NeonDarkColorScheme = darkColorScheme(
    primary = NeonCyan,
    onPrimary = ObsidianBg,
    primaryContainer = ObsidianSurfaceElevated,
    onPrimaryContainer = NeonCyan,
    secondary = NeonMagenta,
    onSecondary = TextPrimary,
    secondaryContainer = ObsidianSurfaceHighest,
    onSecondaryContainer = NeonMagenta,
    tertiary = NeonViolet,
    onTertiary = TextPrimary,
    background = ObsidianBg,
    onBackground = TextPrimary,
    surface = ObsidianSurface,
    onSurface = TextPrimary,
    surfaceVariant = ObsidianSurfaceElevated,
    onSurfaceVariant = TextSecondary,
    outline = ObsidianBorder,
    outlineVariant = ColorTokensOutlineVariant,
    error = NeonRed,
    onError = TextPrimary,
)

val NeonShapes = Shapes(
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(22.dp),
    extraLarge = RoundedCornerShape(28.dp),
)

@Composable
fun NeonTypeTheme(
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = NeonDarkColorScheme,
        typography = AppTypography,
        shapes = NeonShapes,
        content = content,
    )
}
