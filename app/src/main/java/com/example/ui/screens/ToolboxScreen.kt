package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.data.local.SavedClipEntity
import com.example.domain.engine.StickerAndTemplateCatalog
import com.example.domain.model.ClipCategory
import com.example.domain.model.CreatorPlatform
import com.example.domain.model.LightingTheme
import com.example.domain.model.RepeatSeparator
import com.example.domain.model.ToolboxSubTab
import com.example.ui.components.animatedNeonBrush
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonGold
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
import com.example.ui.viewmodel.ToolboxState

@Composable
fun ToolboxScreen(
    toolboxState: ToolboxState,
    repeatedOutput: String,
    savedClips: List<SavedClipEntity>,
    lightingTheme: LightingTheme,
    neonPhase: Float,
    onSelectSubTab: (ToolboxSubTab) -> Unit,
    onBioNameChange: (String) -> Unit,
    onSelectPlatform: (CreatorPlatform) -> Unit,
    onRepeaterInputChange: (String) -> Unit,
    onUseStudioTextInRepeater: () -> Unit,
    onRepeatCountChange: (Int) -> Unit,
    onSelectRepeatSeparator: (RepeatSeparator) -> Unit,
    onToggleRepeatIndexed: () -> Unit,
    onClipSearchChange: (String) -> Unit,
    onSelectClipCategory: (ClipCategory) -> Unit,
    onCopyText: (String) -> Unit,
    onSaveClip: (String, String) -> Unit,
    onTogglePinClip: (SavedClipEntity) -> Unit,
    onDeleteClip: (Int) -> Unit,
    onLoadClipIntoStudio: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Column {
            Text(
                text = "Creator Toolbox",
                style = MaterialTheme.typography.headlineMedium,
                color = TextPrimary,
            )
            Text(
                text = "Name & Bio templates, 500x Repeater & Offline Clipboard",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
            )
        }

        // Sub-tab Segmented Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(ObsidianSurface)
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            ToolboxSubTab.entries.forEach { tab ->
                val selected = toolboxState.subTab == tab
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .height(42.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onSelectSubTab(tab) }
                        .testTag("toolbox_tab_${tab.name}"),
                    shape = RoundedCornerShape(12.dp),
                    color = if (selected) ObsidianSurfaceHighest else Color.Transparent,
                    border = if (selected) {
                        BorderStroke(1.2.dp, animatedNeonBrush(lightingTheme, neonPhase))
                    } else {
                        null
                    },
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = tab.label,
                            style = MaterialTheme.typography.labelLarge,
                            color = if (selected) NeonCyan else TextSecondary,
                            maxLines = 1,
                        )
                    }
                }
            }
        }

        when (toolboxState.subTab) {
            ToolboxSubTab.NAME_GEN -> BioNameGeneratorPane(
                nameInput = toolboxState.nameInput,
                selectedPlatform = toolboxState.selectedPlatform,
                onNameChange = onBioNameChange,
                onSelectPlatform = onSelectPlatform,
                onCopy = onCopyText,
                onSave = { content -> onSaveClip(content, "Name") },
                modifier = Modifier.weight(1f),
            )

            ToolboxSubTab.REPEATER -> TextRepeaterPane(
                toolboxState = toolboxState,
                repeatedOutput = repeatedOutput,
                onInputChange = onRepeaterInputChange,
                onUseStudioText = onUseStudioTextInRepeater,
                onCountChange = onRepeatCountChange,
                onSeparatorSelect = onSelectRepeatSeparator,
                onToggleIndexed = onToggleRepeatIndexed,
                onCopyAll = { onCopyText(repeatedOutput) },
                onSave = { onSaveClip(repeatedOutput, "Caption") },
                modifier = Modifier.weight(1f),
            )

            ToolboxSubTab.CLIPS -> SavedClipsPane(
                clips = savedClips,
                searchQuery = toolboxState.clipSearchQuery,
                selectedCategory = toolboxState.selectedClipCategory,
                onSearchChange = onClipSearchChange,
                onCategorySelect = onSelectClipCategory,
                onAddClip = onSaveClip,
                onTogglePin = onTogglePinClip,
                onDelete = onDeleteClip,
                onCopy = onCopyText,
                onLoadToStudio = onLoadClipIntoStudio,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun BioNameGeneratorPane(
    nameInput: String,
    selectedPlatform: CreatorPlatform,
    onNameChange: (String) -> Unit,
    onSelectPlatform: (CreatorPlatform) -> Unit,
    onCopy: (String) -> Unit,
    onSave: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val activeName = nameInput.ifBlank { "NeonKing" }
    val templates = remember(selectedPlatform) {
        StickerAndTemplateCatalog.bioTemplates.filter {
            selectedPlatform == CreatorPlatform.ALL || it.platform == selectedPlatform
        }
    }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        OutlinedTextField(
            value = nameInput,
            onValueChange = onNameChange,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("bio_name_input"),
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            label = { Text("Your Name / Channel / Gaming Tag") },
            placeholder = { Text("Enter name (English or বাংলা)…", color = TextMuted) },
            trailingIcon = {
                if (nameInput.isNotEmpty()) {
                    IconButton(onClick = { onNameChange("") }) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear name", tint = TextSecondary)
                    }
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = ObsidianSurface,
                unfocusedContainerColor = ObsidianSurface,
                focusedBorderColor = NeonCyan,
                unfocusedBorderColor = ObsidianBorder,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary,
            ),
        )

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(CreatorPlatform.entries) { platform ->
                val selected = platform == selectedPlatform
                FilterChip(
                    selected = selected,
                    onClick = { onSelectPlatform(platform) },
                    label = { Text(platform.label) },
                    modifier = Modifier.testTag("bio_platform_${platform.name}"),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = NeonCyan.copy(alpha = 0.2f),
                        selectedLabelColor = NeonCyan,
                        containerColor = ObsidianSurface,
                        labelColor = TextSecondary,
                    ),
                )
            }
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(bottom = 12.dp),
        ) {
            items(templates, key = { it.id }) { tpl ->
                val rendered = remember(activeName, tpl.id) { tpl.render(activeName) }
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    color = ObsidianSurface,
                    border = BorderStroke(1.dp, ObsidianBorder),
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = tpl.title,
                                style = MaterialTheme.typography.titleMedium,
                                color = TextPrimary,
                            )
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = ObsidianSurfaceHighest,
                            ) {
                                Text(
                                    text = tpl.platform.label,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = NeonMagenta,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                )
                            }
                        }

                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { onCopy(rendered) },
                            shape = RoundedCornerShape(12.dp),
                            color = ObsidianSurfaceElevated,
                        ) {
                            Text(
                                text = rendered,
                                style = MaterialTheme.typography.bodyLarge,
                                color = TextPrimary,
                                modifier = Modifier.padding(12.dp),
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            FilledTonalButton(
                                onClick = { onCopy(rendered) },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(40.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.filledTonalButtonColors(
                                    containerColor = ObsidianSurfaceHighest,
                                    contentColor = NeonCyan,
                                ),
                            ) {
                                Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Copy", style = MaterialTheme.typography.labelLarge)
                            }
                            FilledTonalButton(
                                onClick = { onSave(rendered) },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(40.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.filledTonalButtonColors(
                                    containerColor = NeonMagenta.copy(alpha = 0.18f),
                                    contentColor = NeonMagenta,
                                ),
                            ) {
                                Icon(Icons.Default.BookmarkAdd, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Save", style = MaterialTheme.typography.labelLarge)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TextRepeaterPane(
    toolboxState: ToolboxState,
    repeatedOutput: String,
    onInputChange: (String) -> Unit,
    onUseStudioText: () -> Unit,
    onCountChange: (Int) -> Unit,
    onSeparatorSelect: (RepeatSeparator) -> Unit,
    onToggleIndexed: () -> Unit,
    onCopyAll: () -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            color = ObsidianSurface,
            border = BorderStroke(1.dp, ObsidianBorder),
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text("Text or Emoji to Repeat", style = MaterialTheme.typography.titleMedium, color = TextPrimary)
                    TextButton(onClick = onUseStudioText) {
                        Text("Use Studio Text", color = NeonCyan)
                    }
                }

                OutlinedTextField(
                    value = toolboxState.repeaterInput,
                    onValueChange = onInputChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("repeater_input"),
                    minLines = 2,
                    maxLines = 4,
                    shape = RoundedCornerShape(14.dp),
                    placeholder = { Text("Enter text or emoji…", color = TextMuted) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = ObsidianSurfaceElevated,
                        unfocusedContainerColor = ObsidianSurfaceElevated,
                        focusedBorderColor = NeonCyan,
                        unfocusedBorderColor = ObsidianBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                    ),
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text("Repeat Count (Max 500)", style = MaterialTheme.typography.titleMedium, color = TextPrimary)
                    Text(
                        text = "${toolboxState.repeatCount}×",
                        style = MaterialTheme.typography.titleLarge,
                        color = NeonCyan,
                    )
                }

                Slider(
                    value = toolboxState.repeatCount.toFloat(),
                    onValueChange = { onCountChange(it.toInt()) },
                    valueRange = 1f..500f,
                    modifier = Modifier.testTag("repeater_count_slider"),
                    colors = SliderDefaults.colors(
                        thumbColor = NeonCyan,
                        activeTrackColor = NeonCyan,
                        inactiveTrackColor = ObsidianSurfaceHighest,
                    ),
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    listOf(10, 50, 100, 250, 500).forEach { preset ->
                        val active = toolboxState.repeatCount == preset
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .clickable { onCountChange(preset) }
                                .testTag("repeater_preset_$preset"),
                            shape = RoundedCornerShape(10.dp),
                            color = if (active) NeonCyan.copy(alpha = 0.22f) else ObsidianSurfaceElevated,
                            border = BorderStroke(1.dp, if (active) NeonCyan else ObsidianBorder),
                        ) {
                            Box(
                                modifier = Modifier.padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(
                                    text = "$preset",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = if (active) NeonCyan else TextSecondary,
                                )
                            }
                        }
                    }
                }

                Text("Separator", style = MaterialTheme.typography.labelLarge, color = TextSecondary)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    RepeatSeparator.entries.forEach { sep ->
                        val selected = toolboxState.repeatSeparator == sep
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .clickable { onSeparatorSelect(sep) },
                            shape = RoundedCornerShape(10.dp),
                            color = if (selected) NeonMagenta.copy(alpha = 0.2f) else ObsidianSurfaceElevated,
                            border = BorderStroke(1.dp, if (selected) NeonMagenta else ObsidianBorder),
                        ) {
                            Box(
                                modifier = Modifier.padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(
                                    text = sep.label,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = if (selected) NeonMagenta else TextPrimary,
                                    maxLines = 1,
                                )
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = onToggleIndexed),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text("Add Line Numbers (1. 2. 3.)", style = MaterialTheme.typography.bodyLarge, color = TextPrimary)
                    Switch(
                        checked = toolboxState.repeatIndexed,
                        onCheckedChange = { onToggleIndexed() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = ObsidianBg,
                            checkedTrackColor = NeonCyan,
                        ),
                    )
                }
            }
        }

        // Action Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Button(
                onClick = onCopyAll,
                modifier = Modifier
                    .weight(1.4f)
                    .height(48.dp)
                    .testTag("repeater_copy_all_button"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
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
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Icon(Icons.Default.ContentCopy, contentDescription = null, tint = ObsidianBg, modifier = Modifier.size(18.dp))
                        Text("Copy All (${toolboxState.repeatCount}×)", color = ObsidianBg, fontWeight = FontWeight.Bold)
                    }
                }
            }

            OutlinedButton(
                onClick = onSave,
                modifier = Modifier
                    .weight(0.8f)
                    .height(48.dp),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, NeonCyan.copy(alpha = 0.6f)),
            ) {
                Icon(Icons.Default.BookmarkAdd, contentDescription = "Save repeated text", tint = NeonCyan, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Save", color = NeonCyan)
            }
        }

        // Output Preview
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            color = ObsidianSurface,
            border = BorderStroke(1.dp, ObsidianBorder),
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "Output Preview (${repeatedOutput.length} chars)",
                    style = MaterialTheme.typography.labelLarge,
                    color = NeonCyan,
                )
                Text(
                    text = if (repeatedOutput.isEmpty()) "Enter text above to generate…" else repeatedOutput.take(1500),
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (repeatedOutput.isEmpty()) TextMuted else TextPrimary,
                    modifier = Modifier.testTag("repeater_preview_text"),
                )
                if (repeatedOutput.length > 1500) {
                    Text(
                        text = "… showing first 1,500 chars (${repeatedOutput.length} total copied on tap)",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted,
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
private fun SavedClipsPane(
    clips: List<SavedClipEntity>,
    searchQuery: String,
    selectedCategory: ClipCategory,
    onSearchChange: (String) -> Unit,
    onCategorySelect: (ClipCategory) -> Unit,
    onAddClip: (String, String) -> Unit,
    onTogglePin: (SavedClipEntity) -> Unit,
    onDelete: (Int) -> Unit,
    onCopy: (String) -> Unit,
    onLoadToStudio: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var showAddDialog by remember { mutableStateOf(false) }

    val filteredClips = remember(clips, searchQuery, selectedCategory) {
        val q = searchQuery.trim().lowercase()
        clips.filter { clip ->
            val matchesCat = when (selectedCategory) {
                ClipCategory.ALL -> true
                ClipCategory.PINNED -> clip.isPinned
                else -> clip.category.equals(selectedCategory.label, ignoreCase = true)
            }
            val matchesQuery = q.isEmpty() ||
                clip.content.lowercase().contains(q) ||
                clip.category.lowercase().contains(q)
            matchesCat && matchesQuery
        }
    }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchChange,
                modifier = Modifier
                    .weight(1f)
                    .testTag("clips_search_input"),
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                placeholder = { Text("Search saved clips…", color = TextMuted) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = NeonCyan) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = ObsidianSurface,
                    unfocusedContainerColor = ObsidianSurface,
                    focusedBorderColor = NeonCyan,
                    unfocusedBorderColor = ObsidianBorder,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                ),
            )
            Button(
                onClick = { showAddDialog = true },
                modifier = Modifier
                    .height(54.dp)
                    .testTag("add_clip_button"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = NeonCyan,
                    contentColor = ObsidianBg,
                ),
            ) {
                Icon(Icons.Default.Add, contentDescription = "New Clip")
                Spacer(modifier = Modifier.width(4.dp))
                Text("New", fontWeight = FontWeight.Bold)
            }
        }

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(ClipCategory.entries) { cat ->
                val selected = cat == selectedCategory
                FilterChip(
                    selected = selected,
                    onClick = { onCategorySelect(cat) },
                    label = { Text(cat.label) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = NeonMagenta.copy(alpha = 0.2f),
                        selectedLabelColor = NeonMagenta,
                        containerColor = ObsidianSurface,
                        labelColor = TextSecondary,
                    ),
                )
            }
        }

        if (filteredClips.isEmpty()) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                color = ObsidianSurface,
                border = BorderStroke(1.dp, ObsidianBorder),
            ) {
                Column(
                    modifier = Modifier.padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(text = "📋", style = MaterialTheme.typography.headlineLarge)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("No saved clips yet", style = MaterialTheme.typography.titleMedium, color = TextPrimary)
                    Text(
                        text = "Save captions, hashtags & stylish names from Studio or tap + New",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary,
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .testTag("saved_clips_list"),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 12.dp),
            ) {
                items(filteredClips, key = { it.id }) { clip ->
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("clip_card_${clip.id}"),
                        shape = RoundedCornerShape(18.dp),
                        color = ObsidianSurface,
                        border = BorderStroke(
                            1.dp,
                            if (clip.isPinned) NeonGold.copy(alpha = 0.65f) else ObsidianBorder,
                        ),
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = ObsidianSurfaceHighest,
                                ) {
                                    Text(
                                        text = clip.category,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = NeonCyan,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                    )
                                }
                                Row {
                                    IconButton(onClick = { onTogglePin(clip) }) {
                                        Icon(
                                            imageVector = if (clip.isPinned) Icons.Filled.PushPin else Icons.Outlined.PushPin,
                                            contentDescription = if (clip.isPinned) "Unpin" else "Pin",
                                            tint = if (clip.isPinned) NeonGold else TextSecondary,
                                        )
                                    }
                                    IconButton(onClick = { onDelete(clip.id) }) {
                                        Icon(
                                            imageVector = Icons.Default.DeleteOutline,
                                            contentDescription = "Delete clip",
                                            tint = NeonRed,
                                        )
                                    }
                                }
                            }

                            Text(
                                text = clip.content,
                                style = MaterialTheme.typography.bodyLarge,
                                color = TextPrimary,
                                maxLines = 6,
                                overflow = TextOverflow.Ellipsis,
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                            ) {
                                FilledTonalButton(
                                    onClick = { onCopy(clip.content) },
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(40.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.filledTonalButtonColors(
                                        containerColor = ObsidianSurfaceHighest,
                                        contentColor = NeonCyan,
                                    ),
                                ) {
                                    Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Copy")
                                }
                                FilledTonalButton(
                                    onClick = { onLoadToStudio(clip.content) },
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(40.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.filledTonalButtonColors(
                                        containerColor = NeonViolet.copy(alpha = 0.22f),
                                        contentColor = TextPrimary,
                                    ),
                                ) {
                                    Icon(Icons.Default.Keyboard, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("To Studio")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        var newContent by remember { mutableStateOf("") }
        var newCategory by remember { mutableStateOf("Caption") }
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            containerColor = ObsidianSurfaceElevated,
            title = { Text("Save New Clip", color = TextPrimary) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = newContent,
                        onValueChange = { newContent = it },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        maxLines = 6,
                        placeholder = { Text("Paste caption, hashtags, or stylish text…", color = TextMuted) },
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf("Caption", "Hashtags", "Name", "Custom").forEach { cat ->
                            FilterChip(
                                selected = newCategory == cat,
                                onClick = { newCategory = cat },
                                label = { Text(cat) },
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newContent.isNotBlank()) {
                            onAddClip(newContent, newCategory)
                            showAddDialog = false
                        }
                    },
                ) {
                    Text("Save Clip")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("Cancel", color = TextSecondary)
                }
            },
        )
    }
}
