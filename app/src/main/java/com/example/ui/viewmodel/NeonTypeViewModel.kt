package com.example.ui.viewmodel

import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.ClipsRepository
import com.example.data.local.KeyboardPreferencesRepository
import com.example.data.local.SavedClipEntity
import com.example.domain.engine.StickerAndTemplateCatalog
import com.example.domain.engine.UnicodeFontEngine
import com.example.domain.model.BuiltInBackdrop
import com.example.domain.model.ClipCategory
import com.example.domain.model.CreatorPlatform
import com.example.domain.model.KeyboardLanguage
import com.example.domain.model.KeyboardSettings
import com.example.domain.model.LightingTheme
import com.example.domain.model.MainTab
import com.example.domain.model.RepeatSeparator
import com.example.domain.model.StickerCategory
import com.example.domain.model.ToolboxSubTab
import java.io.File
import java.io.FileOutputStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class StudioTextState(
    val rawText: String = "",
    val isShiftEnabled: Boolean = false,
    val isCapsLock: Boolean = false,
    val isSymbolsMode: Boolean = false,
    val showRawInPreview: Boolean = false,
)

data class StickerHubState(
    val selectedCategory: StickerCategory = StickerCategory.SKULL,
    val searchQuery: String = "",
)

data class ToolboxState(
    val subTab: ToolboxSubTab = ToolboxSubTab.NAME_GEN,
    val nameInput: String = "NeonKing",
    val selectedPlatform: CreatorPlatform = CreatorPlatform.ALL,
    val repeaterInput: String = "🔥 NeonType ",
    val repeatCount: Int = 20,
    val repeatSeparator: RepeatSeparator = RepeatSeparator.NEW_LINE,
    val repeatIndexed: Boolean = false,
    val clipSearchQuery: String = "",
    val selectedClipCategory: ClipCategory = ClipCategory.ALL,
)

class NeonTypeViewModel(application: Application) : AndroidViewModel(application) {

    private val prefsRepo = KeyboardPreferencesRepository(application)
    private val clipsRepo = ClipsRepository(
        dao = AppDatabase.getInstance(application).savedClipDao(),
        prefsRepo = prefsRepo,
    )

    val settings: StateFlow<KeyboardSettings> = prefsRepo.settingsFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = KeyboardSettings(),
        )

    private val _activeTab = MutableStateFlow(MainTab.STUDIO)
    val activeTab: StateFlow<MainTab> = _activeTab.asStateFlow()

    private val _studioState = MutableStateFlow(StudioTextState())
    val studioState: StateFlow<StudioTextState> = _studioState.asStateFlow()

    private val _stickerState = MutableStateFlow(StickerHubState())
    val stickerState: StateFlow<StickerHubState> = _stickerState.asStateFlow()

    private val _toolboxState = MutableStateFlow(ToolboxState())
    val toolboxState: StateFlow<ToolboxState> = _toolboxState.asStateFlow()

    val savedClips: StateFlow<List<SavedClipEntity>> = clipsRepo.allClips
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList(),
        )

    val styledOutputText: StateFlow<String> = combine(
        _studioState,
        settings,
    ) { studio, cfg ->
        val raw = studio.rawText
        if (raw.isEmpty()) {
            ""
        } else if (studio.showRawInPreview) {
            raw
        } else if (cfg.language == KeyboardLanguage.BENGALI) {
            UnicodeFontEngine.decorateBengali(
                text = raw,
                decoratorId = cfg.selectedBengaliDecoratorId,
                wordSpacing = cfg.bengaliWordSpacing,
            )
        } else {
            UnicodeFontEngine.transformEnglish(raw, cfg.selectedEnglishFontId)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = "",
    )

    val repeatedTextOutput: StateFlow<String> = _toolboxState
        .combine(MutableStateFlow(Unit)) { box, _ ->
            generateRepeatedText(
                source = box.repeaterInput,
                count = box.repeatCount,
                separator = box.repeatSeparator,
                indexed = box.repeatIndexed,
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = "",
        )

    private val _toastEvents = MutableSharedFlow<String>(extraBufferCapacity = 8)
    val toastEvents: SharedFlow<String> = _toastEvents.asSharedFlow()

    init {
        viewModelScope.launch {
            clipsRepo.seedDefaultsIfNeeded()
        }
    }

    fun selectTab(tab: MainTab) {
        _activeTab.value = tab
    }

    // --- Interactive Keyboard Actions ---

    fun onKeyPressed(charText: String) {
        _studioState.update { state ->
            val currentCfg = settings.value
            val toAppend = if (currentCfg.language == KeyboardLanguage.ENGLISH && !state.isSymbolsMode) {
                if (state.isShiftEnabled || state.isCapsLock) charText.uppercase() else charText.lowercase()
            } else {
                charText
            }
            val nextShift = if (state.isCapsLock) true else false
            state.copy(
                rawText = state.rawText + toAppend,
                isShiftEnabled = nextShift,
            )
        }
    }

    fun onInsertLiteral(text: String, switchToStudio: Boolean = false) {
        _studioState.update { it.copy(rawText = it.rawText + text) }
        if (switchToStudio) {
            _activeTab.value = MainTab.STUDIO
        }
    }

    fun onBackspace() {
        _studioState.update { state ->
            if (state.rawText.isEmpty()) return@update state
            // Delete whole surrogate pair or combining sequence safely by codePoint
            val lastCpStart = state.rawText.offsetByCodePoints(state.rawText.length, -1)
            state.copy(rawText = state.rawText.substring(0, lastCpStart))
        }
    }

    fun onToggleShift() {
        _studioState.update { state ->
            if (state.isCapsLock) {
                state.copy(isShiftEnabled = false, isCapsLock = false)
            } else if (state.isShiftEnabled) {
                state.copy(isShiftEnabled = true, isCapsLock = true)
            } else {
                state.copy(isShiftEnabled = true, isCapsLock = false)
            }
        }
    }

    fun onToggleSymbolsMode() {
        _studioState.update { it.copy(isSymbolsMode = !it.isSymbolsMode, isShiftEnabled = false, isCapsLock = false) }
    }

    fun onToggleShowRaw() {
        _studioState.update { it.copy(showRawInPreview = !it.showRawInPreview) }
    }

    fun setRawText(newText: String) {
        _studioState.update { it.copy(rawText = newText) }
    }

    fun clearStudioText() {
        _studioState.update { it.copy(rawText = "") }
    }

    fun toggleLanguage() {
        val next = if (settings.value.language == KeyboardLanguage.ENGLISH) {
            KeyboardLanguage.BENGALI
        } else {
            KeyboardLanguage.ENGLISH
        }
        viewModelScope.launch {
            prefsRepo.setLanguage(next)
            _studioState.update { it.copy(isSymbolsMode = false, isShiftEnabled = false, isCapsLock = false) }
        }
    }

    fun selectEnglishFont(fontId: String) {
        viewModelScope.launch {
            prefsRepo.setEnglishFontId(fontId)
            if (settings.value.language != KeyboardLanguage.ENGLISH) {
                prefsRepo.setLanguage(KeyboardLanguage.ENGLISH)
            }
        }
    }

    fun selectBengaliDecorator(decoratorId: String) {
        viewModelScope.launch {
            prefsRepo.setBengaliDecoratorId(decoratorId)
            if (settings.value.language != KeyboardLanguage.BENGALI) {
                prefsRepo.setLanguage(KeyboardLanguage.BENGALI)
            }
        }
    }

    fun toggleBengaliWordSpacing() {
        viewModelScope.launch {
            prefsRepo.setBengaliWordSpacing(!settings.value.bengaliWordSpacing)
        }
    }

    // --- Clipboard & Save ---

    fun copyToClipboard(text: String, feedback: String = "Copied to clipboard!") {
        if (text.isEmpty()) {
            _toastEvents.tryEmit("Nothing to copy yet")
            return
        }
        val clipboard = getApplication<Application>().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.setPrimaryClip(ClipData.newPlainText("NeonType Stylish Text", text))
        _toastEvents.tryEmit(feedback)
    }

    fun saveCurrentStudioClip() {
        val styled = styledOutputText.value
        if (styled.isBlank()) {
            _toastEvents.tryEmit("Type some text first to save")
            return
        }
        viewModelScope.launch {
            clipsRepo.saveClip(styled, category = "Caption")
            _toastEvents.emit("Saved to Clips!")
        }
    }

    // --- Sticker Hub ---

    fun selectStickerCategory(category: StickerCategory) {
        _stickerState.update { it.copy(selectedCategory = category) }
    }

    fun updateStickerSearch(query: String) {
        _stickerState.update { it.copy(searchQuery = query) }
    }

    fun filteredStickers() = StickerAndTemplateCatalog.stickers.filter { item ->
        val state = _stickerState.value
        val q = state.searchQuery.trim().lowercase()
        if (q.isNotEmpty()) {
            item.title.lowercase().contains(q) ||
                item.content.lowercase().contains(q) ||
                item.tags.any { it.lowercase().contains(q) }
        } else {
            item.category == state.selectedCategory
        }
    }

    // --- Lighting & Customization ---

    fun setLightingTheme(theme: LightingTheme) {
        viewModelScope.launch { prefsRepo.setLightingTheme(theme) }
    }

    fun setBuiltInBackdrop(backdrop: BuiltInBackdrop) {
        viewModelScope.launch { prefsRepo.setBuiltInBackdrop(backdrop) }
    }

    fun setBlurRadius(blur: Float) {
        viewModelScope.launch { prefsRepo.setBlurRadius(blur) }
    }

    fun setBrightness(brightness: Float) {
        viewModelScope.launch { prefsRepo.setBrightness(brightness) }
    }

    fun setSoundEnabled(enabled: Boolean) {
        viewModelScope.launch { prefsRepo.setSoundEnabled(enabled) }
    }

    fun setVibrationEnabled(enabled: Boolean) {
        viewModelScope.launch { prefsRepo.setVibrationEnabled(enabled) }
    }

    fun setKeyNeonBorders(enabled: Boolean) {
        viewModelScope.launch { prefsRepo.setKeyNeonBorders(enabled) }
    }

    fun applyPickedBackgroundPhoto(uri: Uri) {
        viewModelScope.launch {
            val savedPath = withContext(Dispatchers.IO) {
                try {
                    val app = getApplication<Application>()
                    val outFile = File(app.filesDir, "keyboard_bg_${System.currentTimeMillis()}.jpg")
                    // Clean up older background files
                    app.filesDir.listFiles { f -> f.name.startsWith("keyboard_bg_") }?.forEach { it.delete() }
                    app.contentResolver.openInputStream(uri)?.use { input ->
                        FileOutputStream(outFile).use { output ->
                            input.copyTo(output)
                        }
                    }
                    if (outFile.exists() && outFile.length() > 0L) outFile.absolutePath else null
                } catch (_: Exception) {
                    null
                }
            }
            if (savedPath != null) {
                prefsRepo.setCustomPhotoPath(savedPath)
                _toastEvents.emit("Keyboard photo background updated!")
            } else {
                _toastEvents.emit("Could not load selected photo")
            }
        }
    }

    fun resetBackgroundPhoto() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                getApplication<Application>().filesDir
                    .listFiles { f -> f.name.startsWith("keyboard_bg_") }
                    ?.forEach { it.delete() }
            }
            prefsRepo.setCustomPhotoPath(null)
            prefsRepo.setBuiltInBackdrop(BuiltInBackdrop.NONE)
            _toastEvents.emit("Background reset to default")
        }
    }

    // --- Creator Toolbox ---

    fun selectToolboxSubTab(subTab: ToolboxSubTab) {
        _toolboxState.update { it.copy(subTab = subTab) }
    }

    fun updateBioNameInput(name: String) {
        _toolboxState.update { it.copy(nameInput = name) }
    }

    fun selectBioPlatform(platform: CreatorPlatform) {
        _toolboxState.update { it.copy(selectedPlatform = platform) }
    }

    fun updateRepeaterInput(text: String) {
        _toolboxState.update { it.copy(repeaterInput = text) }
    }

    fun useStudioTextInRepeater() {
        val current = styledOutputText.value
        if (current.isNotBlank()) {
            _toolboxState.update { it.copy(repeaterInput = current) }
            _toastEvents.tryEmit("Loaded text from Studio")
        } else {
            _toastEvents.tryEmit("Studio text is empty")
        }
    }

    fun updateRepeatCount(count: Int) {
        _toolboxState.update { it.copy(repeatCount = count.coerceIn(1, 500)) }
    }

    fun selectRepeatSeparator(sep: RepeatSeparator) {
        _toolboxState.update { it.copy(repeatSeparator = sep) }
    }

    fun toggleRepeatIndexed() {
        _toolboxState.update { it.copy(repeatIndexed = !it.repeatIndexed) }
    }

    fun updateClipSearch(query: String) {
        _toolboxState.update { it.copy(clipSearchQuery = query) }
    }

    fun selectClipCategory(category: ClipCategory) {
        _toolboxState.update { it.copy(selectedClipCategory = category) }
    }

    fun saveCustomClip(content: String, category: String) {
        viewModelScope.launch {
            clipsRepo.saveClip(content, category)
            _toastEvents.emit("Saved to Clips!")
        }
    }

    fun togglePinClip(clip: SavedClipEntity) {
        viewModelScope.launch {
            clipsRepo.togglePinned(clip)
        }
    }

    fun deleteClip(clipId: Int) {
        viewModelScope.launch {
            clipsRepo.deleteClip(clipId)
            _toastEvents.emit("Clip deleted")
        }
    }

    fun loadClipIntoStudio(content: String) {
        _studioState.update { it.copy(rawText = content, showRawInPreview = true) }
        _activeTab.value = MainTab.STUDIO
        _toastEvents.tryEmit("Loaded clip into Studio")
    }

    private fun generateRepeatedText(
        source: String,
        count: Int,
        separator: RepeatSeparator,
        indexed: Boolean,
    ): String {
        if (source.isEmpty()) return ""
        val safeCount = count.coerceIn(1, 500)
        return buildString {
            for (i in 1..safeCount) {
                if (i > 1) append(separator.value)
                if (indexed) append("$i. ")
                append(source)
            }
        }
    }
}
