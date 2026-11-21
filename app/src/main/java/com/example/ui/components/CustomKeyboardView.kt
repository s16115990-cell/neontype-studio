package com.example.ui.components

import android.os.Build
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.automirrored.filled.KeyboardReturn
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.SpaceBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.domain.model.BuiltInBackdrop
import com.example.domain.model.KeyboardLanguage
import com.example.domain.model.KeyboardSettings
import com.example.domain.model.LightingTheme
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonGold
import com.example.ui.theme.NeonMagenta
import com.example.ui.theme.ObsidianBg
import com.example.ui.theme.ObsidianBorder
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.io.File

@Composable
fun CustomKeyboardView(
    settings: KeyboardSettings,
    isShiftEnabled: Boolean,
    isCapsLock: Boolean,
    isSymbolsMode: Boolean,
    activeStyleLabel: String,
    neonPhase: Float,
    onKeyPress: (String) -> Unit,
    onBackspace: () -> Unit,
    onSpace: () -> Unit,
    onEnter: () -> Unit,
    onToggleShift: () -> Unit,
    onToggleSymbols: () -> Unit,
    onToggleLanguage: () -> Unit,
    modifier: Modifier = Modifier,
    compactPreview: Boolean = false,
    compactHeight: Boolean = false,
) {
    val context = LocalContext.current
    val view = LocalView.current

    val keyHeight = when {
        compactPreview -> 30.dp
        compactHeight -> 27.dp
        else -> 42.dp
    }
    val stripHeight = when {
        compactPreview -> 24.dp
        compactHeight -> 22.dp
        else -> 32.dp
    }
    val rowSpacing = if (compactPreview || compactHeight) 3.dp else 5.dp
    val keySpacing = if (compactPreview) 3.dp else 4.dp
    val keyboardShape = RoundedCornerShape(22.dp)

    val triggerFeedback: () -> Unit = {
        if (!compactPreview) {
            TactileFeedbackHelper.triggerKeyFeedback(
                context = context,
                view = view,
                soundEnabled = settings.soundEnabled,
                vibrationEnabled = settings.vibrationEnabled,
            )
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(keyboardShape)
            .background(ObsidianBg)
            .neonBorder(
                theme = settings.lightingTheme,
                phase = neonPhase,
                borderWidth = 2.dp,
                cornerRadius = 22.dp,
                glowAlpha = 0.34f,
            )
            .testTag(if (compactPreview) "keyboard_preview" else "custom_keyboard_container"),
    ) {
        // Layer 1: Custom Photo or Built-In Gradient Backdrop
        val photoFile = settings.customPhotoPath?.let { File(it) }?.takeIf { it.exists() }
        val blurModifier = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && settings.blurRadiusDp > 0f) {
            Modifier.blur(settings.blurRadiusDp.dp)
        } else {
            Modifier
        }

        if (photoFile != null) {
            AsyncImage(
                model = photoFile,
                contentDescription = "Custom keyboard photo background",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .matchParentSize()
                    .then(blurModifier),
            )
        } else {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(backdropGradientBrush(settings.builtInBackdrop))
                    .then(blurModifier),
            )
        }

        // Layer 2: Brightness + Frosted Softness Scrim
        val dimAlpha = (1f - settings.brightness).coerceIn(0.05f, 0.90f)
        val frostBoost = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            (settings.blurRadiusDp / 24f) * 0.28f
        } else {
            0f
        }
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(Color.Black.copy(alpha = (dimAlpha + frostBoost).coerceAtMost(0.92f))),
        )

        // Layer 3: Keys Grid
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 6.dp, vertical = if (compactPreview || compactHeight) 5.dp else 9.dp),
            verticalArrangement = Arrangement.spacedBy(rowSpacing),
        ) {
            val keyBorderBrush = if (settings.keyNeonBorders) {
                animatedNeonBrush(settings.lightingTheme, neonPhase)
            } else {
                Brush.linearGradient(listOf(ObsidianBorder.copy(alpha = 0.65f), ObsidianBorder.copy(alpha = 0.65f)))
            }

            if (settings.language == KeyboardLanguage.BENGALI && !isSymbolsMode) {
                // Bengali Top Strip (Kar / Digits)
                val topStrip = if (isShiftEnabled) {
                    listOf("০", "১", "২", "৩", "৪", "৫", "৬", "৭", "৮", "৯", "।")
                } else {
                    listOf("া", "ি", "ী", "ু", "ূ", "ৃ", "ে", "ৈ", "ো", "ৌ", "্")
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(keySpacing),
                ) {
                    topStrip.forEach { ch ->
                        KeyboardCharKey(
                            label = ch,
                            height = stripHeight,
                            fontSize = if (compactPreview) 11.sp else 14.sp,
                            borderBrush = keyBorderBrush,
                            isSpecialAccent = ch == "্" || ch == "।",
                            theme = settings.lightingTheme,
                            onClick = {
                                triggerFeedback()
                                onKeyPress(ch)
                            },
                        )
                    }
                }

                // Bengali Row 1
                val row1 = if (isShiftEnabled) {
                    listOf("অ", "আ", "ই", "ঈ", "উ", "ঊ", "ঋ", "এ", "ঐ", "ও")
                } else {
                    listOf("ক", "খ", "গ", "ঘ", "চ", "ছ", "জ", "ঝ", "ট", "ঠ")
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(keySpacing),
                ) {
                    row1.forEach { ch ->
                        KeyboardCharKey(
                            label = ch,
                            height = keyHeight,
                            fontSize = if (compactPreview) 12.sp else 16.sp,
                            borderBrush = keyBorderBrush,
                            theme = settings.lightingTheme,
                            onClick = {
                                triggerFeedback()
                                onKeyPress(ch)
                            },
                        )
                    }
                }

                // Bengali Row 2
                val row2 = if (isShiftEnabled) {
                    listOf("ঔ", "ঙ", "ঞ", "ণ", "ড়", "ঢ়", "য়", "ৎ", "ং", "ঃ")
                } else {
                    listOf("ড", "ঢ", "ত", "থ", "দ", "ধ", "ন", "প", "ফ", "ব")
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(keySpacing),
                ) {
                    row2.forEach { ch ->
                        KeyboardCharKey(
                            label = ch,
                            height = keyHeight,
                            fontSize = if (compactPreview) 12.sp else 16.sp,
                            borderBrush = keyBorderBrush,
                            theme = settings.lightingTheme,
                            onClick = {
                                triggerFeedback()
                                onKeyPress(ch)
                            },
                        )
                    }
                }

                // Bengali Row 3
                val row3 = if (isShiftEnabled) {
                    listOf("ঁ", "৳", ",", ";", ":", "!", "?", "-")
                } else {
                    listOf("ভ", "ম", "য", "র", "ল", "শ", "ষ", "স", "হ")
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(keySpacing),
                ) {
                    KeyboardActionKey(
                        label = if (isShiftEnabled) "১ম" else "২য়",
                        icon = Icons.Default.ArrowUpward,
                        weight = 1.25f,
                        height = keyHeight,
                        isActive = isShiftEnabled,
                        borderBrush = keyBorderBrush,
                        theme = settings.lightingTheme,
                        testTag = "key_shift_bn",
                        onClick = {
                            triggerFeedback()
                            onToggleShift()
                        },
                    )
                    row3.forEach { ch ->
                        KeyboardCharKey(
                            label = ch,
                            height = keyHeight,
                            fontSize = if (compactPreview) 12.sp else 16.sp,
                            borderBrush = keyBorderBrush,
                            theme = settings.lightingTheme,
                            onClick = {
                                triggerFeedback()
                                onKeyPress(ch)
                            },
                        )
                    }
                    KeyboardActionKey(
                        icon = Icons.AutoMirrored.Filled.Backspace,
                        contentDescription = "Backspace",
                        weight = 1.25f,
                        height = keyHeight,
                        borderBrush = keyBorderBrush,
                        theme = settings.lightingTheme,
                        testTag = "key_backspace_bn",
                        onClick = {
                            triggerFeedback()
                            onBackspace()
                        },
                    )
                }
            } else if (isSymbolsMode) {
                // Symbols Layout
                val symRow0 = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "0")
                val symRow1 = listOf("@", "#", "$", "%", "&", "-", "+", "(", ")", "/")
                val symRow2 = listOf("*", "\"", "'", ":", ";", "!", "?", "~", "_", "=")
                val symRow3 = listOf("[", "]", "{", "}", "<", ">", "\\", "|")

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(keySpacing),
                ) {
                    symRow0.forEach { ch ->
                        KeyboardCharKey(
                            label = ch,
                            height = stripHeight,
                            fontSize = if (compactPreview) 11.sp else 13.sp,
                            borderBrush = keyBorderBrush,
                            theme = settings.lightingTheme,
                            onClick = {
                                triggerFeedback()
                                onKeyPress(ch)
                            },
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(keySpacing),
                ) {
                    symRow1.forEach { ch ->
                        KeyboardCharKey(
                            label = ch,
                            height = keyHeight,
                            fontSize = if (compactPreview) 12.sp else 16.sp,
                            borderBrush = keyBorderBrush,
                            theme = settings.lightingTheme,
                            onClick = {
                                triggerFeedback()
                                onKeyPress(ch)
                            },
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(keySpacing),
                ) {
                    symRow2.forEach { ch ->
                        KeyboardCharKey(
                            label = ch,
                            height = keyHeight,
                            fontSize = if (compactPreview) 12.sp else 16.sp,
                            borderBrush = keyBorderBrush,
                            theme = settings.lightingTheme,
                            onClick = {
                                triggerFeedback()
                                onKeyPress(ch)
                            },
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(keySpacing),
                ) {
                    KeyboardActionKey(
                        label = "ABC",
                        weight = 1.35f,
                        height = keyHeight,
                        isActive = true,
                        borderBrush = keyBorderBrush,
                        theme = settings.lightingTheme,
                        testTag = "key_abc_return",
                        onClick = {
                            triggerFeedback()
                            onToggleSymbols()
                        },
                    )
                    symRow3.forEach { ch ->
                        KeyboardCharKey(
                            label = ch,
                            height = keyHeight,
                            fontSize = if (compactPreview) 12.sp else 15.sp,
                            borderBrush = keyBorderBrush,
                            theme = settings.lightingTheme,
                            onClick = {
                                triggerFeedback()
                                onKeyPress(ch)
                            },
                        )
                    }
                    KeyboardActionKey(
                        icon = Icons.AutoMirrored.Filled.Backspace,
                        contentDescription = "Backspace",
                        weight = 1.35f,
                        height = keyHeight,
                        borderBrush = keyBorderBrush,
                        theme = settings.lightingTheme,
                        testTag = "key_backspace_sym",
                        onClick = {
                            triggerFeedback()
                            onBackspace()
                        },
                    )
                }
            } else {
                // English QWERTY Layout
                val numRow = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "0")
                val row1 = listOf("q", "w", "e", "r", "t", "y", "u", "i", "o", "p")
                val row2 = listOf("a", "s", "d", "f", "g", "h", "j", "k", "l")
                val row3 = listOf("z", "x", "c", "v", "b", "n", "m")

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(keySpacing),
                ) {
                    numRow.forEach { ch ->
                        KeyboardCharKey(
                            label = ch,
                            height = stripHeight,
                            fontSize = if (compactPreview) 10.sp else 13.sp,
                            borderBrush = keyBorderBrush,
                            theme = settings.lightingTheme,
                            onClick = {
                                triggerFeedback()
                                onKeyPress(ch)
                            },
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(keySpacing),
                ) {
                    row1.forEach { ch ->
                        val label = if (isShiftEnabled || isCapsLock) ch.uppercase() else ch
                        KeyboardCharKey(
                            label = label,
                            height = keyHeight,
                            fontSize = if (compactPreview) 12.sp else 17.sp,
                            borderBrush = keyBorderBrush,
                            theme = settings.lightingTheme,
                            onClick = {
                                triggerFeedback()
                                onKeyPress(label)
                            },
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = if (compactPreview) 10.dp else 14.dp),
                    horizontalArrangement = Arrangement.spacedBy(keySpacing),
                ) {
                    row2.forEach { ch ->
                        val label = if (isShiftEnabled || isCapsLock) ch.uppercase() else ch
                        KeyboardCharKey(
                            label = label,
                            height = keyHeight,
                            fontSize = if (compactPreview) 12.sp else 17.sp,
                            borderBrush = keyBorderBrush,
                            theme = settings.lightingTheme,
                            onClick = {
                                triggerFeedback()
                                onKeyPress(label)
                            },
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(keySpacing),
                ) {
                    KeyboardActionKey(
                        icon = Icons.Default.ArrowUpward,
                        contentDescription = if (isCapsLock) "Caps Lock On" else "Shift",
                        label = if (isCapsLock) "⇪" else null,
                        weight = 1.45f,
                        height = keyHeight,
                        isActive = isShiftEnabled || isCapsLock,
                        borderBrush = keyBorderBrush,
                        theme = settings.lightingTheme,
                        testTag = "key_shift_en",
                        onClick = {
                            triggerFeedback()
                            onToggleShift()
                        },
                    )
                    row3.forEach { ch ->
                        val label = if (isShiftEnabled || isCapsLock) ch.uppercase() else ch
                        KeyboardCharKey(
                            label = label,
                            height = keyHeight,
                            fontSize = if (compactPreview) 12.sp else 17.sp,
                            borderBrush = keyBorderBrush,
                            theme = settings.lightingTheme,
                            onClick = {
                                triggerFeedback()
                                onKeyPress(label)
                            },
                        )
                    }
                    KeyboardActionKey(
                        icon = Icons.AutoMirrored.Filled.Backspace,
                        contentDescription = "Backspace",
                        weight = 1.45f,
                        height = keyHeight,
                        borderBrush = keyBorderBrush,
                        theme = settings.lightingTheme,
                        testTag = "key_backspace_en",
                        onClick = {
                            triggerFeedback()
                            onBackspace()
                        },
                    )
                }
            }

            // Bottom Control Row (Shared across English, Bengali & Symbols)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(keySpacing),
            ) {
                KeyboardActionKey(
                    label = if (isSymbolsMode) "ABC" else "?123",
                    weight = 1.25f,
                    height = keyHeight,
                    isActive = isSymbolsMode,
                    borderBrush = keyBorderBrush,
                    theme = settings.lightingTheme,
                    testTag = "key_symbols_toggle",
                    onClick = {
                        triggerFeedback()
                        onToggleSymbols()
                    },
                )
                KeyboardActionKey(
                    label = settings.language.badge,
                    icon = if (compactPreview) null else Icons.Default.Language,
                    weight = 1.35f,
                    height = keyHeight,
                    isActive = settings.language == KeyboardLanguage.BENGALI,
                    borderBrush = keyBorderBrush,
                    theme = settings.lightingTheme,
                    testTag = "key_language_toggle",
                    onClick = {
                        triggerFeedback()
                        onToggleLanguage()
                    },
                )
                KeyboardCharKey(
                    label = if (settings.language == KeyboardLanguage.BENGALI) "।" else ",",
                    weight = 0.9f,
                    height = keyHeight,
                    fontSize = if (compactPreview) 12.sp else 16.sp,
                    borderBrush = keyBorderBrush,
                    theme = settings.lightingTheme,
                    onClick = {
                        triggerFeedback()
                        onKeyPress(if (settings.language == KeyboardLanguage.BENGALI) "।" else ",")
                    },
                )

                // Spacebar showing active style badge
                SpacebarKey(
                    label = if (compactPreview) "NeonType" else activeStyleLabel,
                    weight = 3.6f,
                    height = keyHeight,
                    borderBrush = keyBorderBrush,
                    theme = settings.lightingTheme,
                    onClick = {
                        triggerFeedback()
                        onSpace()
                    },
                )

                KeyboardCharKey(
                    label = ".",
                    weight = 0.9f,
                    height = keyHeight,
                    fontSize = if (compactPreview) 12.sp else 16.sp,
                    borderBrush = keyBorderBrush,
                    theme = settings.lightingTheme,
                    onClick = {
                        triggerFeedback()
                        onKeyPress(".")
                    },
                )
                KeyboardActionKey(
                    icon = Icons.AutoMirrored.Filled.KeyboardReturn,
                    contentDescription = "Enter",
                    weight = 1.45f,
                    height = keyHeight,
                    isPrimary = true,
                    borderBrush = keyBorderBrush,
                    theme = settings.lightingTheme,
                    testTag = "key_enter",
                    onClick = {
                        triggerFeedback()
                        onEnter()
                    },
                )
            }
        }
    }
}

@Composable
private fun RowScope.KeyboardCharKey(
    label: String,
    height: Dp,
    fontSize: androidx.compose.ui.unit.TextUnit,
    borderBrush: Brush,
    theme: LightingTheme,
    onClick: () -> Unit,
    weight: Float = 1f,
    isSpecialAccent: Boolean = false,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val accentColor = when (theme) {
        LightingTheme.GOLD_GLOW -> NeonGold
        LightingTheme.CYBER_NEON -> NeonCyan
        LightingTheme.RAINBOW_PULSE -> NeonMagenta
        LightingTheme.PURE_DARK -> TextSecondary
    }
    val bgColor by animateColorAsState(
        targetValue = when {
            isPressed -> accentColor.copy(alpha = 0.34f)
            isSpecialAccent -> Color(0xC71D223A)
            else -> Color(0xB8111425)
        },
        label = "key_bg",
    )

    Surface(
        modifier = Modifier
            .weight(weight)
            .height(height)
            .scale(if (isPressed) 0.94f else 1f)
            .clip(RoundedCornerShape(9.dp))
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
            )
            .testTag("key_$label"),
        shape = RoundedCornerShape(9.dp),
        color = bgColor,
        border = BorderStroke(1.dp, borderBrush),
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Text(
                text = label,
                color = if (isSpecialAccent) accentColor else TextPrimary,
                fontSize = fontSize,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
            )
        }
    }
}

@Composable
private fun RowScope.KeyboardActionKey(
    weight: Float,
    height: Dp,
    borderBrush: Brush,
    theme: LightingTheme,
    testTag: String,
    onClick: () -> Unit,
    label: String? = null,
    icon: ImageVector? = null,
    contentDescription: String? = label,
    isActive: Boolean = false,
    isPrimary: Boolean = false,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val activeAccent = when (theme) {
        LightingTheme.GOLD_GLOW -> NeonGold
        LightingTheme.CYBER_NEON -> NeonCyan
        LightingTheme.RAINBOW_PULSE -> NeonCyan
        LightingTheme.PURE_DARK -> TextPrimary
    }
    val bgColor by animateColorAsState(
        targetValue = when {
            isPressed -> activeAccent.copy(alpha = 0.42f)
            isPrimary -> activeAccent.copy(alpha = 0.24f)
            isActive -> activeAccent.copy(alpha = 0.22f)
            else -> Color(0xC71A1E33)
        },
        label = "action_key_bg",
    )

    Surface(
        modifier = Modifier
            .weight(weight)
            .height(height)
            .scale(if (isPressed) 0.94f else 1f)
            .clip(RoundedCornerShape(9.dp))
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
            )
            .testTag(testTag),
        shape = RoundedCornerShape(9.dp),
        color = bgColor,
        border = BorderStroke(if (isActive || isPrimary) 1.3.dp else 1.dp, borderBrush),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 3.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = contentDescription,
                    tint = if (isActive || isPrimary) activeAccent else TextPrimary,
                    modifier = Modifier.size(16.dp),
                )
            }
            if (label != null) {
                if (icon != null) Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium,
                    color = if (isActive || isPrimary) activeAccent else TextPrimary,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Composable
private fun RowScope.SpacebarKey(
    label: String,
    weight: Float,
    height: Dp,
    borderBrush: Brush,
    theme: LightingTheme,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val accent = when (theme) {
        LightingTheme.GOLD_GLOW -> NeonGold
        LightingTheme.CYBER_NEON -> NeonCyan
        LightingTheme.RAINBOW_PULSE -> NeonCyan
        LightingTheme.PURE_DARK -> TextSecondary
    }
    val bgColor by animateColorAsState(
        targetValue = if (isPressed) accent.copy(alpha = 0.30f) else Color(0xB814182C),
        label = "space_bg",
    )

    Surface(
        modifier = Modifier
            .weight(weight)
            .height(height)
            .scale(if (isPressed) 0.97f else 1f)
            .clip(RoundedCornerShape(9.dp))
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
            )
            .testTag("key_spacebar"),
        shape = RoundedCornerShape(9.dp),
        color = bgColor,
        border = BorderStroke(1.dp, borderBrush),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Icon(
                imageVector = Icons.Default.SpaceBar,
                contentDescription = "Space",
                tint = accent.copy(alpha = 0.85f),
                modifier = Modifier.size(15.dp),
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = TextSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}
