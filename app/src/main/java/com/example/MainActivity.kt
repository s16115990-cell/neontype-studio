package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.EmojiEmotions
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.EmojiEmotions
import androidx.compose.material.icons.outlined.Keyboard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.domain.model.MainTab
import com.example.ui.components.rememberNeonSweepPhase
import com.example.ui.screens.LightingStudioScreen
import com.example.ui.screens.StickerHubScreen
import com.example.ui.screens.StudioScreen
import com.example.ui.screens.ToolboxScreen
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonMagenta
import com.example.ui.theme.NeonTypeTheme
import com.example.ui.theme.ObsidianBg
import com.example.ui.theme.ObsidianSurface
import com.example.ui.theme.ObsidianSurfaceHighest
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.viewmodel.NeonTypeViewModel
import kotlinx.coroutines.flow.collectLatest

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT),
        )
        setContent {
            NeonTypeTheme {
                NeonTypeAppRoot()
            }
        }
    }
}

@Composable
fun NeonTypeAppRoot(
    viewModel: NeonTypeViewModel = viewModel(),
) {
    val activeTab by viewModel.activeTab.collectAsStateWithLifecycle()
    val settings by viewModel.settings.collectAsStateWithLifecycle()
    val studioState by viewModel.studioState.collectAsStateWithLifecycle()
    val styledOutput by viewModel.styledOutputText.collectAsStateWithLifecycle()
    val stickerState by viewModel.stickerState.collectAsStateWithLifecycle()
    val toolboxState by viewModel.toolboxState.collectAsStateWithLifecycle()
    val repeatedOutput by viewModel.repeatedTextOutput.collectAsStateWithLifecycle()
    val savedClips by viewModel.savedClips.collectAsStateWithLifecycle()

    val neonPhase = rememberNeonSweepPhase()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.toastEvents.collectLatest { msg ->
            snackbarHostState.currentSnackbarData?.dismiss()
            snackbarHostState.showSnackbar(
                message = msg,
                duration = SnackbarDuration.Short,
            )
        }
    }

    if (activeTab != MainTab.STUDIO) {
        BackHandler {
            viewModel.selectTab(MainTab.STUDIO)
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(ObsidianBg),
        containerColor = ObsidianBg,
        contentWindowInsets = WindowInsets.safeDrawing,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = ObsidianSurfaceHighest,
                    contentColor = NeonCyan,
                    shape = RoundedCornerShape(14.dp),
                )
            }
        },
        bottomBar = {
            NavigationBar(
                containerColor = ObsidianSurface,
                contentColor = TextPrimary,
                windowInsets = WindowInsets.navigationBars,
                tonalElevation = 0.dp,
            ) {
                MainTab.entries.forEach { tab ->
                    val selected = activeTab == tab
                    val icon = when (tab) {
                        MainTab.STUDIO -> if (selected) Icons.Filled.Keyboard else Icons.Outlined.Keyboard
                        MainTab.STICKERS -> if (selected) Icons.Filled.EmojiEmotions else Icons.Outlined.EmojiEmotions
                        MainTab.LIGHTING -> if (selected) Icons.Filled.AutoAwesome else Icons.Outlined.AutoAwesome
                        MainTab.TOOLBOX -> if (selected) Icons.Filled.Build else Icons.Outlined.Build
                    }
                    NavigationBarItem(
                        selected = selected,
                        onClick = { viewModel.selectTab(tab) },
                        modifier = Modifier.testTag("nav_tab_${tab.name}"),
                        icon = {
                            Icon(
                                imageVector = icon,
                                contentDescription = tab.title,
                            )
                        },
                        label = {
                            Text(
                                text = tab.title,
                                style = MaterialTheme.typography.labelMedium,
                                maxLines = 1,
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = NeonCyan,
                            selectedTextColor = NeonCyan,
                            indicatorColor = ObsidianSurfaceHighest,
                            unselectedIconColor = TextMuted,
                            unselectedTextColor = TextMuted,
                        ),
                    )
                }
            }
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            ObsidianBg,
                            Color(0xFF0A0C18),
                            ObsidianBg,
                        ),
                    ),
                )
                .padding(innerPadding),
            contentAlignment = Alignment.TopCenter,
        ) {
            BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                val maxContentWidth = if (maxWidth > 760.dp) 720.dp else maxWidth
                Box(
                    modifier = Modifier
                        .widthIn(max = maxContentWidth)
                        .fillMaxSize()
                        .align(Alignment.TopCenter),
                ) {
                    when (activeTab) {
                        MainTab.STUDIO -> StudioScreen(
                            settings = settings,
                            studioState = studioState,
                            styledOutput = styledOutput,
                            neonPhase = neonPhase,
                            onSelectEnglishFont = viewModel::selectEnglishFont,
                            onSelectBengaliDecorator = viewModel::selectBengaliDecorator,
                            onToggleBengaliWordSpacing = viewModel::toggleBengaliWordSpacing,
                            onToggleLanguage = viewModel::toggleLanguage,
                            onToggleShowRaw = viewModel::onToggleShowRaw,
                            onSetRawText = viewModel::setRawText,
                            onCopyText = { viewModel.copyToClipboard(it) },
                            onClearText = viewModel::clearStudioText,
                            onSaveClip = viewModel::saveCurrentStudioClip,
                            onKeyPress = viewModel::onKeyPressed,
                            onBackspace = viewModel::onBackspace,
                            onToggleShift = viewModel::onToggleShift,
                            onToggleSymbols = viewModel::onToggleSymbolsMode,
                        )

                        MainTab.STICKERS -> StickerHubScreen(
                            stickerState = stickerState,
                            lightingTheme = settings.lightingTheme,
                            neonPhase = neonPhase,
                            onSelectCategory = viewModel::selectStickerCategory,
                            onSearchChange = viewModel::updateStickerSearch,
                            onCopySticker = { viewModel.copyToClipboard(it, "Sticker copied!") },
                            onInsertSticker = { content ->
                                viewModel.onInsertLiteral(content, switchToStudio = false)
                                viewModel.copyToClipboard(content, "Added to Studio & copied!")
                            },
                        )

                        MainTab.LIGHTING -> LightingStudioScreen(
                            settings = settings,
                            neonPhase = neonPhase,
                            onSelectLightingTheme = viewModel::setLightingTheme,
                            onSelectBuiltInBackdrop = viewModel::setBuiltInBackdrop,
                            onPickedPhoto = viewModel::applyPickedBackgroundPhoto,
                            onResetPhoto = viewModel::resetBackgroundPhoto,
                            onBlurChange = viewModel::setBlurRadius,
                            onBrightnessChange = viewModel::setBrightness,
                            onSoundToggle = viewModel::setSoundEnabled,
                            onVibrationToggle = viewModel::setVibrationEnabled,
                            onKeyNeonBordersToggle = viewModel::setKeyNeonBorders,
                        )

                        MainTab.TOOLBOX -> ToolboxScreen(
                            toolboxState = toolboxState,
                            repeatedOutput = repeatedOutput,
                            savedClips = savedClips,
                            lightingTheme = settings.lightingTheme,
                            neonPhase = neonPhase,
                            onSelectSubTab = viewModel::selectToolboxSubTab,
                            onBioNameChange = viewModel::updateBioNameInput,
                            onSelectPlatform = viewModel::selectBioPlatform,
                            onRepeaterInputChange = viewModel::updateRepeaterInput,
                            onUseStudioTextInRepeater = viewModel::useStudioTextInRepeater,
                            onRepeatCountChange = viewModel::updateRepeatCount,
                            onSelectRepeatSeparator = viewModel::selectRepeatSeparator,
                            onToggleRepeatIndexed = viewModel::toggleRepeatIndexed,
                            onClipSearchChange = viewModel::updateClipSearch,
                            onSelectClipCategory = viewModel::selectClipCategory,
                            onCopyText = { viewModel.copyToClipboard(it) },
                            onSaveClip = viewModel::saveCustomClip,
                            onTogglePinClip = viewModel::togglePinClip,
                            onDeleteClip = viewModel::deleteClip,
                            onLoadClipIntoStudio = viewModel::loadClipIntoStudio,
                        )
                    }
                }
            }
        }
    }
}
