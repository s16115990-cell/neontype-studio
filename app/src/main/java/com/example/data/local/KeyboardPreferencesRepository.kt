package com.example.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.domain.model.BuiltInBackdrop
import com.example.domain.model.KeyboardLanguage
import com.example.domain.model.KeyboardSettings
import com.example.domain.model.LightingTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.keyboardDataStore: DataStore<Preferences> by preferencesDataStore(name = "neontype_prefs")

class KeyboardPreferencesRepository(private val context: Context) {

    private object Keys {
        val LIGHTING_THEME = stringPreferencesKey("lighting_theme")
        val CUSTOM_PHOTO_PATH = stringPreferencesKey("custom_photo_path")
        val BUILT_IN_BACKDROP = stringPreferencesKey("built_in_backdrop")
        val BLUR_RADIUS = floatPreferencesKey("blur_radius")
        val BRIGHTNESS = floatPreferencesKey("brightness")
        val SOUND_ENABLED = booleanPreferencesKey("sound_enabled")
        val VIBRATION_ENABLED = booleanPreferencesKey("vibration_enabled")
        val KEY_NEON_BORDERS = booleanPreferencesKey("key_neon_borders")
        val LANGUAGE = stringPreferencesKey("language")
        val ENGLISH_FONT_ID = stringPreferencesKey("english_font_id")
        val BENGALI_DECORATOR_ID = stringPreferencesKey("bengali_decorator_id")
        val BENGALI_WORD_SPACING = booleanPreferencesKey("bengali_word_spacing")
        val HAS_SEEDED_CLIPS = booleanPreferencesKey("has_seeded_clips")
    }

    val settingsFlow: Flow<KeyboardSettings> = context.keyboardDataStore.data.map { prefs ->
        KeyboardSettings(
            lightingTheme = LightingTheme.fromId(prefs[Keys.LIGHTING_THEME] ?: LightingTheme.RAINBOW_PULSE.id),
            customPhotoPath = prefs[Keys.CUSTOM_PHOTO_PATH]?.takeIf { it.isNotBlank() },
            builtInBackdrop = BuiltInBackdrop.fromId(prefs[Keys.BUILT_IN_BACKDROP] ?: BuiltInBackdrop.NONE.id),
            blurRadiusDp = prefs[Keys.BLUR_RADIUS] ?: 6f,
            brightness = prefs[Keys.BRIGHTNESS] ?: 0.72f,
            soundEnabled = prefs[Keys.SOUND_ENABLED] ?: true,
            vibrationEnabled = prefs[Keys.VIBRATION_ENABLED] ?: true,
            keyNeonBorders = prefs[Keys.KEY_NEON_BORDERS] ?: true,
            language = if (prefs[Keys.LANGUAGE] == KeyboardLanguage.BENGALI.name) {
                KeyboardLanguage.BENGALI
            } else {
                KeyboardLanguage.ENGLISH
            },
            selectedEnglishFontId = prefs[Keys.ENGLISH_FONT_ID] ?: "bold_script",
            selectedBengaliDecoratorId = prefs[Keys.BENGALI_DECORATOR_ID] ?: "royal_sword",
            bengaliWordSpacing = prefs[Keys.BENGALI_WORD_SPACING] ?: false,
        )
    }

    suspend fun setLightingTheme(theme: LightingTheme) {
        context.keyboardDataStore.edit { it[Keys.LIGHTING_THEME] = theme.id }
    }

    suspend fun setCustomPhotoPath(path: String?) {
        context.keyboardDataStore.edit {
            if (path.isNullOrBlank()) {
                it.remove(Keys.CUSTOM_PHOTO_PATH)
            } else {
                it[Keys.CUSTOM_PHOTO_PATH] = path
                it[Keys.BUILT_IN_BACKDROP] = BuiltInBackdrop.NONE.id
            }
        }
    }

    suspend fun setBuiltInBackdrop(backdrop: BuiltInBackdrop) {
        context.keyboardDataStore.edit {
            it[Keys.BUILT_IN_BACKDROP] = backdrop.id
            if (backdrop != BuiltInBackdrop.NONE) {
                it.remove(Keys.CUSTOM_PHOTO_PATH)
            }
        }
    }

    suspend fun setBlurRadius(blurDp: Float) {
        context.keyboardDataStore.edit { it[Keys.BLUR_RADIUS] = blurDp.coerceIn(0f, 24f) }
    }

    suspend fun setBrightness(brightness: Float) {
        context.keyboardDataStore.edit { it[Keys.BRIGHTNESS] = brightness.coerceIn(0.1f, 1f) }
    }

    suspend fun setSoundEnabled(enabled: Boolean) {
        context.keyboardDataStore.edit { it[Keys.SOUND_ENABLED] = enabled }
    }

    suspend fun setVibrationEnabled(enabled: Boolean) {
        context.keyboardDataStore.edit { it[Keys.VIBRATION_ENABLED] = enabled }
    }

    suspend fun setKeyNeonBorders(enabled: Boolean) {
        context.keyboardDataStore.edit { it[Keys.KEY_NEON_BORDERS] = enabled }
    }

    suspend fun setLanguage(language: KeyboardLanguage) {
        context.keyboardDataStore.edit { it[Keys.LANGUAGE] = language.name }
    }

    suspend fun setEnglishFontId(fontId: String) {
        context.keyboardDataStore.edit { it[Keys.ENGLISH_FONT_ID] = fontId }
    }

    suspend fun setBengaliDecoratorId(decoratorId: String) {
        context.keyboardDataStore.edit { it[Keys.BENGALI_DECORATOR_ID] = decoratorId }
    }

    suspend fun setBengaliWordSpacing(enabled: Boolean) {
        context.keyboardDataStore.edit { it[Keys.BENGALI_WORD_SPACING] = enabled }
    }

    suspend fun hasSeededClips(): Boolean {
        return context.keyboardDataStore.data.first()[Keys.HAS_SEEDED_CLIPS] ?: false
    }

    suspend fun markClipsSeeded() {
        context.keyboardDataStore.edit { it[Keys.HAS_SEEDED_CLIPS] = true }
    }
}
