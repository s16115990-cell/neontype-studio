package com.example.ui.screens

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.domain.engine.UnicodeFontEngine
import com.example.domain.model.KeyboardLanguage
import com.example.domain.model.KeyboardSettings
import com.example.ui.components.CustomKeyboardView
import com.example.ui.components.animatedNeonBrush
import com.example.ui.components.neonSurface
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.NeonMagenta
import com.example.ui.theme.NeonRed
import com.example.ui.theme.NeonViolet
import com.example.ui.theme.ObsidianBg
import com.example.ui.theme.ObsidianBorder
import com.example.ui.theme.ObsidianSurface
import com.example.ui.theme.ObsidianSurfaceElevated
import com.example.ui.theme.ObsidianSurfaceHighest
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.StudioTextState
import kotlinx.coroutines.delay

@Composable
fun StudioScreen(
    settings: KeyboardSettings,
    studioState: StudioTextState,
    styledOutput: String,
    neonPhase: Float,
    onSelectEnglishFont: (String) -> Unit,
    onSelectBengaliDecorator: (String) -> Unit,
    onToggleBengaliWordSpacing: () -> Unit,
    onToggleLanguage: () -> Unit,
    onToggleShowRaw: () -> Unit,
    onSetRawText: (String) -> Unit,
    onCopyText: (String) -> Unit,
    onClearText: () -> Unit,
    onSaveClip: () -> Unit,
    onKeyPress: (String) -> Unit,
    onBackspace: () -> Unit,
    onToggleShift: () -> Unit,
    onToggleSymbols: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    var showCopiedBadge by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }

    LaunchedEffect(showCopiedBadge) {
        if (showCopiedBadge) {
            delay(1400)
            showCopiedBadge = false
        }
    }

    val activeStyleName = remember(settings) {
        if (settings.language == KeyboardLanguage.BENGALI) {
            UnicodeFontEngine.bengaliDecorators
                .find { it.id == settings.selectedBengaliDecoratorId }?.name ?: "সাধারণ"
        } else {
            UnicodeFontEngine.englishFonts
                .find { it.id == settings.selectedEnglishFontId }?.name ?: "Cursive Bold"
        }
    }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp, vertical = 4.dp),
    ) {
        val isCompactHeight = maxHeight < 520.dp
        val isUltraShort = maxHeight < 480.dp
        val verticalGap = if (isCompactHeight) 4.dp else 8.dp
        val containerModifier = if (isUltraShort) {
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        } else {
            Modifier.fillMaxSize()
        }

        Column(
            modifier = containerModifier,
            verticalArrangement = Arrangement.spacedBy(verticalGap),
        ) {
        // Studio Header Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "NeonType Studio",
                    style = if (isCompactHeight) MaterialTheme.typography.titleLarge else MaterialTheme.typography.headlineMedium,
                    color = TextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                if (!isCompactHeight) {
                    Text(
                        text = if (settings.language == KeyboardLanguage.BENGALI) {
                            "বাংলা স্টাইলিশ কীবোর্ড ও ডেকোরেটর"
                        } else {
                            "36+ Stylish Unicode Fonts • 100% Offline"
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }

            // Language Switcher Pill (EN / বাংলা)
            Surface(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .clickable(onClick = onToggleLanguage)
                    .testTag("studio_language_pill"),
                shape = RoundedCornerShape(50),
                color = ObsidianSurfaceElevated,
                border = BorderStroke(1.2.dp, animatedNeonBrush(settings.lightingTheme, neonPhase)),
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Icon(
                        imageVector = Icons.Default.Translate,
                        contentDescription = "Switch Language",
                        tint = NeonCyan,
                        modifier = Modifier.size(16.dp),
                    )
                    Text(
                        text = if (settings.language == KeyboardLanguage.BENGALI) "বাংলা ⇄ EN" else "EN ⇄ বাংলা",
                        style = MaterialTheme.typography.labelLarge,
                        color = TextPrimary,
                    )
                }
            }
        }

        // Top Font / Decorator Selector Bar
        if (settings.language == KeyboardLanguage.BENGALI) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 2.dp),
                modifier = Modifier.testTag("bengali_decorator_bar"),
            ) {
                item {
                    val active = settings.bengaliWordSpacing
                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .clickable(onClick = onToggleBengaliWordSpacing)
                            .testTag("toggle_bengali_word_dot"),
                        shape = RoundedCornerShape(14.dp),
                        color = if (active) NeonMagenta.copy(alpha = 0.22f) else ObsidianSurfaceElevated,
                        border = BorderStroke(1.dp, if (active) NeonMagenta else ObsidianBorder),
                    ) {
                        Column(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text(
                                text = "শব্দ • ডট",
                                style = MaterialTheme.typography.labelLarge,
                                color = if (active) NeonMagenta else TextPrimary,
                            )
                            Text(
                                text = if (active) "ON" else "OFF",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextSecondary,
                            )
                        }
                    }
                }
                items(UnicodeFontEngine.bengaliDecorators, key = { it.id }) { dec ->
                    val selected = dec.id == settings.selectedBengaliDecoratorId
                    FontSelectorChip(
                        title = dec.samplePreview,
                        subtitle = dec.name,
                        selected = selected,
                        neonPhase = neonPhase,
                        lightingTheme = settings.lightingTheme,
                        testTag = "decorator_chip_${dec.id}",
                        onClick = { onSelectBengaliDecorator(dec.id) },
                    )
                }
            }
        } else {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 2.dp),
                modifier = Modifier.testTag("english_font_bar"),
            ) {
                items(UnicodeFontEngine.englishFonts, key = { it.id }) { font ->
                    val selected = font.id == settings.selectedEnglishFontId
                    FontSelectorChip(
                        title = font.samplePreview,
                        subtitle = font.name,
                        selected = selected,
                        neonPhase = neonPhase,
                        lightingTheme = settings.lightingTheme,
                        testTag = "font_chip_${font.id}",
                        onClick = { onSelectEnglishFont(font.id) },
                    )
                }
            }
        }

        // Live Output Preview Card
        val previewBoxModifier = if (isUltraShort) {
            Modifier
                .fillMaxWidth()
                .height(88.dp)
        } else {
            Modifier
                .fillMaxWidth()
                .weight(1f)
                .heightIn(min = 96.dp)
        }
        Box(
            modifier = previewBoxModifier
                .neonSurface(
                    theme = settings.lightingTheme,
                    phase = neonPhase,
                    shape = RoundedCornerShape(18.dp),
                    containerColor = ObsidianSurface,
                    borderWidth = 1.5.dp,
                    cornerRadius = 18.dp,
                )
                .padding(if (isCompactHeight) 10.dp else 14.dp)
                .testTag("live_preview_card"),
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(if (styledOutput.isNotEmpty()) NeonGreen else TextMuted),
                        )
                        Text(
                            text = if (studioState.showRawInPreview) "RAW INPUT" else activeStyleName.uppercase(),
                            style = MaterialTheme.typography.labelSmall,
                            color = NeonCyan,
                            maxLines = 1,
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        Text(
                            text = "${styledOutput.length}c",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextMuted,
                        )
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable(onClick = onToggleShowRaw)
                                .testTag("toggle_raw_preview"),
                            shape = RoundedCornerShape(8.dp),
                            color = ObsidianSurfaceHighest,
                        ) {
                            Text(
                                text = if (studioState.showRawInPreview) "Styled" else "Raw",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextSecondary,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                            )
                        }
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { showEditDialog = true }
                                .testTag("edit_preview_button"),
                            shape = RoundedCornerShape(8.dp),
                            color = ObsidianSurfaceHighest,
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(3.dp),
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Paste or edit text directly",
                                    tint = NeonCyan,
                                    modifier = Modifier.size(13.dp),
                                )
                                Text(
                                    text = "Edit",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = NeonCyan,
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(if (isCompactHeight) 3.dp else 6.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                ) {
                    if (styledOutput.isEmpty()) {
                        Text(
                            text = stringResource(R.string.preview_placeholder),
                            style = if (isCompactHeight) MaterialTheme.typography.bodyMedium else MaterialTheme.typography.bodyLarge,
                            color = TextMuted,
                        )
                    } else {
                        Text(
                            text = styledOutput,
                            fontFamily = FontFamily.Default,
                            fontSize = if (isCompactHeight) 17.sp else 20.sp,
                            lineHeight = if (isCompactHeight) 22.sp else 28.sp,
                            color = TextPrimary,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("live_preview_output"),
                        )
                    }
                }

                if (!isCompactHeight || showCopiedBadge) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "${styledOutput.length} chars",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextMuted,
                        )
                        AnimatedVisibility(visible = showCopiedBadge) {
                            Text(
                                text = "✓ Copied!",
                                style = MaterialTheme.typography.labelSmall,
                                color = NeonGreen,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                    }
                }
            }
        }

        // One-tap Action Bar Right Above Keyboard
        val actionHeight = if (isCompactHeight) 40.dp else 48.dp
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Button(
                onClick = {
                    if (styledOutput.isNotEmpty()) {
                        onCopyText(styledOutput)
                        showCopiedBadge = true
                    } else {
                        onCopyText("")
                    }
                },
                modifier = Modifier
                    .weight(1.45f)
                    .height(actionHeight)
                    .testTag("copy_text_button"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = ObsidianBg,
                ),
                contentPadding = PaddingValues(0.dp),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.horizontalGradient(listOf(NeonCyan, NeonViolet, NeonMagenta)),
                            shape = RoundedCornerShape(14.dp),
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        Icon(
                            imageVector = if (showCopiedBadge) Icons.Default.Check else Icons.Default.ContentCopy,
                            contentDescription = "Copy Text",
                            tint = ObsidianBg,
                            modifier = Modifier.size(18.dp),
                        )
                        Text(
                            text = if (showCopiedBadge) stringResource(R.string.copied_feedback) else stringResource(R.string.copy_text),
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = ObsidianBg,
                        )
                    }
                }
            }

            OutlinedButton(
                onClick = onClearText,
                modifier = Modifier
                    .weight(0.9f)
                    .height(actionHeight)
                    .testTag("clear_text_button"),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, NeonRed.copy(alpha = 0.55f)),
                contentPadding = PaddingValues(horizontal = 8.dp),
            ) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "Clear text",
                    tint = NeonRed,
                    modifier = Modifier.size(18.dp),
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = stringResource(R.string.clear_text),
                    style = MaterialTheme.typography.labelLarge,
                    color = NeonRed,
                    maxLines = 1,
                )
            }

            FilledTonalIconButton(
                onClick = onSaveClip,
                modifier = Modifier
                    .size(actionHeight)
                    .testTag("save_studio_clip_button"),
                colors = IconButtonDefaults.filledTonalIconButtonColors(
                    containerColor = ObsidianSurfaceElevated,
                    contentColor = NeonCyan,
                ),
            ) {
                Icon(
                    imageVector = Icons.Default.BookmarkAdd,
                    contentDescription = "Save to Clips",
                )
            }

            FilledTonalIconButton(
                onClick = {
                    if (styledOutput.isNotEmpty()) {
                        val sendIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, styledOutput)
                        }
                        context.startActivity(Intent.createChooser(sendIntent, "Share Stylish Text"))
                    } else {
                        onCopyText("")
                    }
                },
                modifier = Modifier
                    .size(actionHeight)
                    .testTag("share_studio_text_button"),
                colors = IconButtonDefaults.filledTonalIconButtonColors(
                    containerColor = ObsidianSurfaceElevated,
                    contentColor = NeonMagenta,
                ),
            ) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Share Text",
                )
            }
        }

        // Interactive Custom Keyboard
        CustomKeyboardView(
            settings = settings,
            isShiftEnabled = studioState.isShiftEnabled,
            isCapsLock = studioState.isCapsLock,
            isSymbolsMode = studioState.isSymbolsMode,
            activeStyleLabel = activeStyleName,
            neonPhase = neonPhase,
            compactHeight = isCompactHeight,
            onKeyPress = onKeyPress,
            onBackspace = onBackspace,
            onSpace = { onKeyPress(" ") },
            onEnter = { onKeyPress("\n") },
            onToggleShift = onToggleShift,
            onToggleSymbols = onToggleSymbols,
            onToggleLanguage = onToggleLanguage,
        )
        }
    }

    if (showEditDialog) {
        var draft by remember(studioState.rawText) { mutableStateOf(studioState.rawText) }
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            containerColor = ObsidianSurfaceElevated,
            title = {
                Text("Edit or Paste Raw Text", color = TextPrimary)
            },
            text = {
                OutlinedTextField(
                    value = draft,
                    onValueChange = { draft = it },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    maxLines = 6,
                    placeholder = { Text("Paste or type text here…", color = TextMuted) },
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onSetRawText(draft)
                        showEditDialog = false
                    },
                ) {
                    Text("Apply")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) {
                    Text("Cancel", color = TextSecondary)
                }
            },
        )
    }
}

@Composable
private fun FontSelectorChip(
    title: String,
    subtitle: String,
    selected: Boolean,
    neonPhase: Float,
    lightingTheme: com.example.domain.model.LightingTheme,
    testTag: String,
    onClick: () -> Unit,
) {
    val border = if (selected) {
        BorderStroke(1.5.dp, animatedNeonBrush(lightingTheme, neonPhase))
    } else {
        BorderStroke(1.dp, ObsidianBorder)
    }
    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .testTag(testTag),
        shape = RoundedCornerShape(14.dp),
        color = if (selected) ObsidianSurfaceHighest else ObsidianSurfaceElevated,
        border = border,
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 11.dp, vertical = 5.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = title,
                fontFamily = FontFamily.Default,
                fontSize = 14.sp,
                color = if (selected) NeonCyan else TextPrimary,
                maxLines = 1,
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall,
                color = if (selected) TextPrimary else TextMuted,
                maxLines = 1,
            )
        }
    }
}
