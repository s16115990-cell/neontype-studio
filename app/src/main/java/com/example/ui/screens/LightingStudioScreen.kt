package com.example.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.BlurOn
import androidx.compose.material.icons.filled.Brightness6
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.domain.model.BuiltInBackdrop
import com.example.domain.model.KeyboardSettings
import com.example.domain.model.LightingTheme
import com.example.ui.components.CustomKeyboardView
import com.example.ui.components.animatedNeonBrush
import com.example.ui.components.backdropGradientBrush
import com.example.ui.components.lightingColorsFor
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonMagenta
import com.example.ui.theme.NeonViolet
import com.example.ui.theme.ObsidianBg
import com.example.ui.theme.ObsidianBorder
import com.example.ui.theme.ObsidianSurface
import com.example.ui.theme.ObsidianSurfaceElevated
import com.example.ui.theme.ObsidianSurfaceHighest
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlin.math.roundToInt

@Composable
fun LightingStudioScreen(
    settings: KeyboardSettings,
    neonPhase: Float,
    onSelectLightingTheme: (LightingTheme) -> Unit,
    onSelectBuiltInBackdrop: (BuiltInBackdrop) -> Unit,
    onPickedPhoto: (android.net.Uri) -> Unit,
    onResetPhoto: () -> Unit,
    onBlurChange: (Float) -> Unit,
    onBrightnessChange: (Float) -> Unit,
    onSoundToggle: (Boolean) -> Unit,
    onVibrationToggle: (Boolean) -> Unit,
    onKeyNeonBordersToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {
                onPickedPhoto(uri)
            }
        },
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 14.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Column {
            Text(
                text = "Lighting & Keyboard Studio",
                style = MaterialTheme.typography.headlineMedium,
                color = TextPrimary,
            )
            Text(
                text = "Customize gallery photo backdrop, blur, brightness & RGB glow",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
            )
        }

        // Live Keyboard Preview
        CustomKeyboardView(
            settings = settings,
            isShiftEnabled = false,
            isCapsLock = false,
            isSymbolsMode = false,
            activeStyleLabel = settings.lightingTheme.displayName,
            neonPhase = neonPhase,
            onKeyPress = {},
            onBackspace = {},
            onSpace = {},
            onEnter = {},
            onToggleShift = {},
            onToggleSymbols = {},
            onToggleLanguage = {},
            compactPreview = true,
        )

        // Section 1: Dynamic RGB & Rainbow Neon Lighting Themes
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            color = ObsidianSurface,
            border = BorderStroke(1.dp, ObsidianBorder),
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Text(
                    text = "Dynamic RGB Lighting Themes",
                    style = MaterialTheme.typography.titleLarge,
                    color = TextPrimary,
                )

                val themes = LightingTheme.entries
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    for (rowIndex in themes.indices step 2) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            LightingThemeCard(
                                theme = themes[rowIndex],
                                selected = settings.lightingTheme == themes[rowIndex],
                                neonPhase = neonPhase,
                                onClick = { onSelectLightingTheme(themes[rowIndex]) },
                                modifier = Modifier.weight(1f),
                            )
                            if (rowIndex + 1 < themes.size) {
                                LightingThemeCard(
                                    theme = themes[rowIndex + 1],
                                    selected = settings.lightingTheme == themes[rowIndex + 1],
                                    neonPhase = neonPhase,
                                    onClick = { onSelectLightingTheme(themes[rowIndex + 1]) },
                                    modifier = Modifier.weight(1f),
                                )
                            }
                        }
                    }
                }
            }
        }

        // Section 2: Custom Photo Background + Blur & Brightness Sliders
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            color = ObsidianSurface,
            border = BorderStroke(1.dp, ObsidianBorder),
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(
                    text = "Custom Photo & Backdrop",
                    style = MaterialTheme.typography.titleLarge,
                    color = TextPrimary,
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Button(
                        onClick = {
                            photoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly),
                            )
                        },
                        modifier = Modifier
                            .weight(1.4f)
                            .height(48.dp)
                            .testTag("choose_photo_button"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = NeonCyan,
                            contentColor = ObsidianBg,
                        ),
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddPhotoAlternate,
                            contentDescription = "Choose photo from gallery",
                            modifier = Modifier.size(18.dp),
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = stringResource(R.string.choose_photo),
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                        )
                    }

                    OutlinedButton(
                        onClick = onResetPhoto,
                        modifier = Modifier
                            .weight(0.9f)
                            .height(48.dp)
                            .testTag("reset_photo_button"),
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.dp, ObsidianBorder),
                    ) {
                        Icon(
                            imageVector = Icons.Default.RestartAlt,
                            contentDescription = "Reset photo background",
                            tint = TextSecondary,
                            modifier = Modifier.size(18.dp),
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = stringResource(R.string.reset_photo),
                            style = MaterialTheme.typography.labelLarge,
                            color = TextSecondary,
                        )
                    }
                }

                Text(
                    text = "Built-In Neon Backdrops",
                    style = MaterialTheme.typography.labelLarge,
                    color = TextSecondary,
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(vertical = 2.dp),
                ) {
                    items(BuiltInBackdrop.entries) { backdrop ->
                        val active = settings.customPhotoPath == null && settings.builtInBackdrop == backdrop
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .clickable { onSelectBuiltInBackdrop(backdrop) }
                                .testTag("backdrop_${backdrop.id}"),
                            shape = RoundedCornerShape(14.dp),
                            color = ObsidianSurfaceElevated,
                            border = if (active) {
                                BorderStroke(1.5.dp, NeonCyan)
                            } else {
                                BorderStroke(1.dp, ObsidianBorder)
                            },
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(22.dp)
                                        .clip(CircleShape)
                                        .background(backdropGradientBrush(backdrop)),
                                )
                                Text(
                                    text = backdrop.label,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = if (active) NeonCyan else TextPrimary,
                                )
                            }
                        }
                    }
                }

                // Blur Slider
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                        ) {
                            Icon(Icons.Default.BlurOn, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(18.dp))
                            Text("Background Blur", style = MaterialTheme.typography.titleMedium, color = TextPrimary)
                        }
                        Text(
                            text = "${settings.blurRadiusDp.roundToInt()} dp",
                            style = MaterialTheme.typography.labelLarge,
                            color = NeonCyan,
                        )
                    }
                    Slider(
                        value = settings.blurRadiusDp,
                        onValueChange = onBlurChange,
                        valueRange = 0f..24f,
                        modifier = Modifier.testTag("blur_slider"),
                        colors = SliderDefaults.colors(
                            thumbColor = NeonCyan,
                            activeTrackColor = NeonCyan,
                            inactiveTrackColor = ObsidianSurfaceHighest,
                        ),
                    )
                }

                // Brightness Slider
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                        ) {
                            Icon(Icons.Default.Brightness6, contentDescription = null, tint = NeonMagenta, modifier = Modifier.size(18.dp))
                            Text("Backdrop Brightness", style = MaterialTheme.typography.titleMedium, color = TextPrimary)
                        }
                        Text(
                            text = "${(settings.brightness * 100).roundToInt()}%",
                            style = MaterialTheme.typography.labelLarge,
                            color = NeonMagenta,
                        )
                    }
                    Slider(
                        value = settings.brightness,
                        onValueChange = onBrightnessChange,
                        valueRange = 0.10f..1.0f,
                        modifier = Modifier.testTag("brightness_slider"),
                        colors = SliderDefaults.colors(
                            thumbColor = NeonMagenta,
                            activeTrackColor = NeonMagenta,
                            inactiveTrackColor = ObsidianSurfaceHighest,
                        ),
                    )
                }
            }
        }

        // Section 3: Tactile & Key Border Controls
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            color = ObsidianSurface,
            border = BorderStroke(1.dp, ObsidianBorder),
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Text(
                    text = "Tactile & Key Effects",
                    style = MaterialTheme.typography.titleLarge,
                    color = TextPrimary,
                )
                SettingToggleRow(
                    icon = Icons.Default.GraphicEq,
                    title = "Individual Key Neon Borders",
                    subtitle = "Animate RGB rim glow around every keycap",
                    checked = settings.keyNeonBorders,
                    testTag = "toggle_key_neon_borders",
                    onCheckedChange = onKeyNeonBordersToggle,
                )
                SettingToggleRow(
                    icon = Icons.AutoMirrored.Filled.VolumeUp,
                    title = "Key-Press Sound",
                    subtitle = "Crisp mechanical click on every tap",
                    checked = settings.soundEnabled,
                    testTag = "toggle_key_sound",
                    onCheckedChange = onSoundToggle,
                )
                SettingToggleRow(
                    icon = Icons.Default.Vibration,
                    title = "Key-Press Vibration",
                    subtitle = "Subtle haptic pulse while typing",
                    checked = settings.vibrationEnabled,
                    testTag = "toggle_key_vibration",
                    onCheckedChange = onVibrationToggle,
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))
    }
}

@Composable
private fun LightingThemeCard(
    theme: LightingTheme,
    selected: Boolean,
    neonPhase: Float,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val border = if (selected) {
        BorderStroke(1.8.dp, animatedNeonBrush(theme, neonPhase))
    } else {
        BorderStroke(1.dp, ObsidianBorder)
    }

    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .testTag("lighting_theme_${theme.id}"),
        shape = RoundedCornerShape(16.dp),
        color = if (selected) ObsidianSurfaceHighest else ObsidianSurfaceElevated,
        border = border,
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .height(10.dp)
                        .width(44.dp)
                        .clip(RoundedCornerShape(50))
                        .background(Brush.horizontalGradient(lightingColorsFor(theme))),
                )
                if (selected) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Selected",
                        tint = NeonCyan,
                        modifier = Modifier.size(16.dp),
                    )
                }
            }
            Text(
                text = theme.displayName,
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary,
            )
            Text(
                text = theme.subtitle,
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted,
            )
        }
    }
}

@Composable
private fun SettingToggleRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    testTag: String,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onCheckedChange(!checked) }
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Icon(icon, contentDescription = null, tint = NeonViolet, modifier = Modifier.size(20.dp))
            Column {
                Text(title, style = MaterialTheme.typography.titleMedium, color = TextPrimary)
                Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = TextMuted)
            }
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            modifier = Modifier.testTag(testTag),
            colors = SwitchDefaults.colors(
                checkedThumbColor = ObsidianBg,
                checkedTrackColor = NeonCyan,
                uncheckedThumbColor = TextSecondary,
                uncheckedTrackColor = ObsidianSurfaceHighest,
            ),
        )
    }
}
