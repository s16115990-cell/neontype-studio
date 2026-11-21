package com.example.domain.model

enum class MainTab(val title: String) {
    STUDIO("Studio"),
    STICKERS("Stickers"),
    LIGHTING("Lighting"),
    TOOLBOX("Toolbox"),
}

enum class KeyboardLanguage(val label: String, val badge: String) {
    ENGLISH("English", "EN"),
    BENGALI("বাংলা", "বাং"),
}

enum class LightingTheme(
    val id: String,
    val displayName: String,
    val subtitle: String,
) {
    RAINBOW_PULSE("rainbow_pulse", "Rainbow Pulse", "Animated 360° RGB spectrum"),
    CYBER_NEON("cyber_neon", "Cyber Neon", "Electric cyan & magenta pulse"),
    PURE_DARK("pure_dark", "Pure Dark", "Stealth OLED obsidian keys"),
    GOLD_GLOW("gold_glow", "Gold Glow", "Royal molten amber shimmer"),
    ;

    companion object {
        fun fromId(id: String): LightingTheme = entries.find { it.id == id } ?: RAINBOW_PULSE
    }
}

enum class BuiltInBackdrop(
    val id: String,
    val label: String,
) {
    NONE("none", "Default Dark"),
    COSMIC_NEBULA("cosmic_nebula", "Cosmic RGB"),
    CYBER_GRID("cyber_grid", "Cyber Matrix"),
    MIDNIGHT_AURORA("midnight_aurora", "Aurora Wave"),
    CRIMSON_EMBER("crimson_ember", "Crimson Fire"),
    ;

    companion object {
        fun fromId(id: String): BuiltInBackdrop = entries.find { it.id == id } ?: NONE
    }
}

enum class StickerCategory(
    val title: String,
    val subtitle: String,
    val iconEmoji: String,
) {
    SKULL("Skull & কঙ্কাল", "Dark edits & skeleton", "💀"),
    BEWAFA("Sad & Bewafa", "Heartbreak & বেবাফা", "💔"),
    ROYAL("Royal & Attitude", "Crowns & boss vibes", "👑"),
    GAMING("Fire & Gaming", "Clans, FF & PUBG", "🔥"),
    AESTHETIC("Aesthetic Art", "Kaomoji & dividers", "✨"),
}

data class StickerItem(
    val id: String,
    val category: StickerCategory,
    val title: String,
    val content: String,
    val tags: List<String> = emptyList(),
)

enum class CreatorPlatform(val label: String) {
    ALL("All"),
    YOUTUBE("YouTube"),
    INSTAGRAM("Instagram"),
    FACEBOOK("Facebook"),
    GAMING("Gaming"),
}

data class BioTemplate(
    val id: String,
    val platform: CreatorPlatform,
    val title: String,
    val previewSample: String,
    val render: (String) -> String,
)

enum class ToolboxSubTab(val label: String) {
    NAME_GEN("Bio & Names"),
    REPEATER("Repeater"),
    CLIPS("Saved Clips"),
}

enum class RepeatSeparator(val label: String, val value: String) {
    NEW_LINE("New Line", "\n"),
    SPACE("Space", " "),
    DOT("• Dot", " • "),
    NONE("None", ""),
}

enum class ClipCategory(val label: String) {
    ALL("All"),
    PINNED("Pinned"),
    CAPTION("Caption"),
    HASHTAGS("Hashtags"),
    NAME("Name"),
    CUSTOM("Custom"),
}

data class KeyboardSettings(
    val lightingTheme: LightingTheme = LightingTheme.RAINBOW_PULSE,
    val customPhotoPath: String? = null,
    val builtInBackdrop: BuiltInBackdrop = BuiltInBackdrop.NONE,
    val blurRadiusDp: Float = 6f,
    val brightness: Float = 0.72f,
    val soundEnabled: Boolean = true,
    val vibrationEnabled: Boolean = true,
    val keyNeonBorders: Boolean = true,
    val language: KeyboardLanguage = KeyboardLanguage.ENGLISH,
    val selectedEnglishFontId: String = "bold_script",
    val selectedBengaliDecoratorId: String = "royal_sword",
    val bengaliWordSpacing: Boolean = false,
)
