package com.example.domain.engine

data class EnglishFontStyle(
    val id: String,
    val name: String,
    val category: String,
    val transform: (String) -> String,
) {
    val samplePreview: String by lazy { transform("Neon") }
}

data class BengaliDecoratorStyle(
    val id: String,
    val name: String,
    val prefix: String,
    val suffix: String,
    val wordJoiner: String? = null,
) {
    fun decorate(text: String, useWordSpacing: Boolean): String {
        val trimmed = text.trim()
        if (trimmed.isEmpty()) return ""
        val processed = if (useWordSpacing && wordJoiner != null) {
            trimmed.split(Regex("\\s+")).joinToString(wordJoiner)
        } else if (useWordSpacing) {
            trimmed.split(Regex("\\s+")).joinToString(" • ")
        } else {
            trimmed
        }
        return if (prefix.isEmpty() && suffix.isEmpty()) processed else "$prefix$processed$suffix"
    }

    val samplePreview: String by lazy { decorate("বাংলা", false) }
}

object UnicodeFontEngine {

    private fun codePointString(cp: Int): String = String(Character.toChars(cp))

    private fun mapMathBlock(
        text: String,
        upperStart: Int,
        lowerStart: Int,
        digitStart: Int? = null,
        upperExceptions: Map<Char, Int> = emptyMap(),
        lowerExceptions: Map<Char, Int> = emptyMap(),
    ): String {
        val sb = StringBuilder(text.length * 2)
        for (ch in text) {
            when (ch) {
                in 'A'..'Z' -> {
                    val cp = upperExceptions[ch] ?: (upperStart + (ch - 'A'))
                    sb.append(codePointString(cp))
                }
                in 'a'..'z' -> {
                    val cp = lowerExceptions[ch] ?: (lowerStart + (ch - 'a'))
                    sb.append(codePointString(cp))
                }
                in '0'..'9' -> {
                    if (digitStart != null) {
                        sb.append(codePointString(digitStart + (ch - '0')))
                    } else {
                        sb.append(ch)
                    }
                }
                else -> sb.append(ch)
            }
        }
        return sb.toString()
    }

    private fun mapLookup(text: String, upperMap: String, lowerMap: String, digitMap: String? = null): String {
        val upperCps = upperMap.codePoints().toArray()
        val lowerCps = lowerMap.codePoints().toArray()
        val digitCps = digitMap?.codePoints()?.toArray()
        val sb = StringBuilder(text.length * 2)
        for (ch in text) {
            when (ch) {
                in 'A'..'Z' -> sb.append(codePointString(upperCps[ch - 'A']))
                in 'a'..'z' -> sb.append(codePointString(lowerCps[ch - 'a']))
                in '0'..'9' -> if (digitCps != null) sb.append(codePointString(digitCps[ch - '0'])) else sb.append(ch)
                else -> sb.append(ch)
            }
        }
        return sb.toString()
    }

    private fun combineMark(text: String, mark: String): String {
        val sb = StringBuilder(text.length * 2)
        for (ch in text) {
            sb.append(ch)
            if (!ch.isWhitespace()) {
                sb.append(mark)
            }
        }
        return sb.toString()
    }

    private val scriptUpperExceptions = mapOf(
        'B' to 0x212C,
        'E' to 0x2130,
        'F' to 0x2131,
        'H' to 0x210B,
        'I' to 0x2110,
        'L' to 0x2112,
        'M' to 0x2133,
        'R' to 0x211B,
    )

    private val scriptLowerExceptions = mapOf(
        'e' to 0x212F,
        'g' to 0x210A,
        'o' to 0x2134,
    )

    private val frakturUpperExceptions = mapOf(
        'C' to 0x212D,
        'H' to 0x210C,
        'I' to 0x2111,
        'R' to 0x211C,
        'Z' to 0x2128,
    )

    private val doubleStruckUpperExceptions = mapOf(
        'C' to 0x2102,
        'H' to 0x210D,
        'N' to 0x2115,
        'P' to 0x2119,
        'Q' to 0x211A,
        'R' to 0x211D,
        'Z' to 0x2124,
    )

    private val smallCapsMap = mapOf(
        'a' to 'ᴀ', 'b' to 'ʙ', 'c' to 'ᴄ', 'd' to 'ᴅ', 'e' to 'ᴇ',
        'f' to 'ꜰ', 'g' to 'ɢ', 'h' to 'ʜ', 'i' to 'ɪ', 'j' to 'ᴊ',
        'k' to 'ᴋ', 'l' to 'ʟ', 'm' to 'ᴍ', 'n' to 'ɴ', 'o' to 'ᴏ',
        'p' to 'ᴘ', 'q' to 'ǫ', 'r' to 'ʀ', 's' to 'ꜱ', 't' to 'ᴛ',
        'u' to 'ᴜ', 'v' to 'ᴠ', 'w' to 'ᴡ', 'x' to 'x', 'y' to 'ʏ',
        'z' to 'ᴢ',
    )

    private val superscriptMap = mapOf(
        'a' to 'ᵃ', 'b' to 'ᵇ', 'c' to 'ᶜ', 'd' to 'ᵈ', 'e' to 'ᵉ',
        'f' to 'ᶠ', 'g' to 'ᵍ', 'h' to 'ʰ', 'i' to 'ⁱ', 'j' to 'ʲ',
        'k' to 'ᵏ', 'l' to 'ˡ', 'm' to 'ᵐ', 'n' to 'ⁿ', 'o' to 'ᵒ',
        'p' to 'ᵖ', 'q' to 'ᑫ', 'r' to 'ʳ', 's' to 'ˢ', 't' to 'ᵗ',
        'u' to 'ᵘ', 'v' to 'ᵛ', 'w' to 'ʷ', 'x' to 'ˣ', 'y' to 'ʸ',
        'z' to 'ᶻ',
        'A' to 'ᴬ', 'B' to 'ᴮ', 'C' to 'ᶜ', 'D' to 'ᴰ', 'E' to 'ᴱ',
        'F' to 'ᶠ', 'G' to 'ᴳ', 'H' to 'ᴴ', 'I' to 'ᴵ', 'J' to 'ᴶ',
        'K' to 'ᴷ', 'L' to 'ᴸ', 'M' to 'ᴹ', 'N' to 'ᴺ', 'O' to 'ᴼ',
        'P' to 'ᴾ', 'Q' to 'ᑫ', 'R' to 'ᴿ', 'S' to 'ˢ', 'T' to 'ᵀ',
        'U' to 'ᵁ', 'V' to 'ⱽ', 'W' to 'ᵂ', 'X' to 'ˣ', 'Y' to 'ʸ',
        'Z' to 'ᶻ',
        '0' to '⁰', '1' to '¹', '2' to '²', '3' to '³', '4' to '⁴',
        '5' to '⁵', '6' to '⁶', '7' to '⁷', '8' to '⁸', '9' to '⁹',
    )

    private val upsideDownMap: Map<Char, String> = mapOf(
        'a' to "ɐ", 'b' to "q", 'c' to "ɔ", 'd' to "p", 'e' to "ǝ",
        'f' to "ɟ", 'g' to "ƃ", 'h' to "ɥ", 'i' to "ᴉ", 'j' to "ɾ",
        'k' to "ʞ", 'l' to "l", 'm' to "ɯ", 'n' to "u", 'o' to "o",
        'p' to "d", 'q' to "b", 'r' to "ɹ", 's' to "s", 't' to "ʇ",
        'u' to "n", 'v' to "ʌ", 'w' to "ʍ", 'x' to "x", 'y' to "ʎ",
        'z' to "z",
        'A' to "∀", 'B' to "𐐒", 'C' to "Ɔ", 'D' to "◖", 'E' to "Ǝ",
        'F' to "Ⅎ", 'G' to "פ", 'H' to "H", 'I' to "I", 'J' to "ſ",
        'K' to "ʞ", 'L' to "˥", 'M' to "W", 'N' to "N", 'O' to "O",
        'P' to "Ԁ", 'Q' to "Ό", 'R' to "ᴚ", 'S' to "S", 'T' to "⊥",
        'U' to "∩", 'V' to "Λ", 'W' to "M", 'X' to "X", 'Y' to "⅄",
        'Z' to "Z",
        '1' to "Ɩ", '2' to "ᄅ", '3' to "Ɛ", '4' to "ㄣ", '5' to "ϛ",
        '6' to "9", '7' to "ㄥ", '8' to "8", '9' to "6", '0' to "0",
        '!' to "¡", '?' to "¿", '.' to "˙", ',' to "'",
    )

    private val italicExceptions = mapOf('h' to 0x210E)

    private fun toBoldSerif(s: String) = mapMathBlock(s, 0x1D400, 0x1D41A, 0x1D7CE)
    private fun toItalicSerif(s: String) = mapMathBlock(s, 0x1D434, 0x1D44E, lowerExceptions = italicExceptions)
    private fun toBoldItalicSerif(s: String) = mapMathBlock(s, 0x1D468, 0x1D482)
    private fun toSansBold(s: String) = mapMathBlock(s, 0x1D5D4, 0x1D5EE, 0x1D7EC)
    private fun toSansItalic(s: String) = mapMathBlock(s, 0x1D608, 0x1D622)
    private fun toSansBoldItalic(s: String) = mapMathBlock(s, 0x1D63C, 0x1D656)
    private fun toScript(s: String) = mapMathBlock(
        s, 0x1D49C, 0x1D4B6,
        upperExceptions = scriptUpperExceptions,
        lowerExceptions = scriptLowerExceptions,
    )
    private fun toBoldScript(s: String) = mapMathBlock(s, 0x1D4D0, 0x1D4EA)
    private fun toGothic(s: String) = mapMathBlock(s, 0x1D504, 0x1D51E, upperExceptions = frakturUpperExceptions)
    private fun toBoldGothic(s: String) = mapMathBlock(s, 0x1D56C, 0x1D586)
    private fun toDoubleStruck(s: String) = mapMathBlock(
        s, 0x1D538, 0x1D552, 0x1D7D8,
        upperExceptions = doubleStruckUpperExceptions,
    )
    private fun toMonospace(s: String) = mapMathBlock(s, 0x1D670, 0x1D68A, 0x1D7F6)

    private fun toBubble(s: String) = mapLookup(
        s,
        upperMap = "ⒶⒷⒸⒹⒺⒻⒼⒽⒾⒿⓀⓁⓂⓃⓄⓅⓆⓇⓈⓉⓊⓋⓌⓍⓎⓏ",
        lowerMap = "ⓐⓑⓒⓓⓔⓕⓖⓗⓘⓙⓚⓛⓜⓝⓞⓟⓠⓡⓢⓣⓤⓥⓦⓧⓨⓩ",
        digitMap = "⓪①②③④⑤⑥⑦⑧⑨",
    )

    private fun toDarkBubble(s: String) = mapLookup(
        s,
        upperMap = "🅐🅑🅒🅓🅔🅕🅖🅗🅘🅙🅚🅛🅜🅝🅞🅟🅠🅡🅢🅣🅤🅥🅦🅧🅨🅩",
        lowerMap = "🅐🅑🅒🅓🅔🅕🅖🅗🅘🅙🅚🅛🅜🅝🅞🅟🅠🅡🅢🅣🅤🅥🅦🅧🅨🅩",
        digitMap = "⓿❶❷❸❹❺❻❼❽❾",
    )

    private fun toSquare(s: String) = mapLookup(
        s,
        upperMap = "🄰🄱🄲🄳🄴🄵🄶🄷🄸🄹🄺🄻🄼🄽🄾🄿🅀🅁🅂🅃🅄🅅🅆🅇🅈🅉",
        lowerMap = "🄰🄱🄲🄳🄴🄵🄶🄷🄸🄹🄺🄻🄼🄽🄾🄿🅀🅁🅂🅃🅄🅅🅆🅇🅈🅉",
    )

    private fun toDarkSquare(s: String) = mapLookup(
        s,
        upperMap = "🅰🅱🅲🅳🅴🅵🅶🅷🅸🅹🅺🅻🅼🅽🅾🅿🆀🆁🆂🆃🆄🆅🆆🆇🆈🆉",
        lowerMap = "🅰🅱🅲🅳🅴🅵🅶🅷🅸🅹🅺🅻🅼🅽🅾🅿🆀🆁🆂🆃🆄🆅🆆🆇🆈🆉",
    )

    private fun toParenthesized(s: String) = mapLookup(
        s,
        upperMap = "🄐🄑🄒🄓🄔🄕🄖🄗🄘🄙🄚🄛🄜🄝🄞🄟🄠🄡🄢🄣🄤🄥🄦🄧🄨🄩",
        lowerMap = "⒜⒝⒞⒟⒠⒡⒢⒣⒤⒥⒦⒧⒨⒩⒪⒫⒬⒭⒮⒯⒰⒱⒲⒳⒴⒵",
        digitMap = "⓪⑴⑵⑶⑷⑸⑹⑺⑻⑼",
    )

    private fun toSmallCaps(s: String): String = buildString {
        for (ch in s) {
            append(smallCapsMap[ch.lowercaseChar()] ?: ch)
        }
    }

    private fun toSuperscript(s: String): String = buildString {
        for (ch in s) {
            append(superscriptMap[ch] ?: ch)
        }
    }

    private fun toFullwidth(s: String): String = buildString {
        for (ch in s) {
            when (ch) {
                ' ' -> append('\u3000')
                in '!'..'~' -> append((ch.code + 0xFEE0).toChar())
                else -> append(ch)
            }
        }
    }

    private fun toUpsideDown(s: String): String = buildString {
        for (ch in s.reversed()) {
            append(upsideDownMap[ch] ?: ch)
        }
    }

    private fun toGlitchLight(s: String): String = combineMark(s, "\u0334")
    private fun toGlitchHeavy(s: String): String = combineMark(s, "\u0337\u0307\u0323")
    private fun toStrikethrough(s: String): String = combineMark(s, "\u0336")
    private fun toDoubleUnderline(s: String): String = combineMark(s, "\u0333")
    private fun toSlashThrough(s: String): String = combineMark(s, "\u0338")
    private fun toLightningMark(s: String): String = combineMark(s, "\u035C")
    private fun toVaporSpace(s: String): String = s.toCharArray().joinToString(" ") { it.toString() }.let(::toFullwidth)

    val englishFonts: List<EnglishFontStyle> = listOf(
        EnglishFontStyle("normal", "Normal", "Clean") { it },
        EnglishFontStyle("bold_script", "Cursive Bold", "Cursive", ::toBoldScript),
        EnglishFontStyle("script", "Cursive Light", "Cursive", ::toScript),
        EnglishFontStyle("bold_gothic", "Gothic Bold", "Gothic", ::toBoldGothic),
        EnglishFontStyle("gothic", "Gothic Classic", "Gothic", ::toGothic),
        EnglishFontStyle("double_struck", "Double Struck", "Outlined", ::toDoubleStruck),
        EnglishFontStyle("sans_bold", "Sans Bold", "Bold", ::toSansBold),
        EnglishFontStyle("sans_bold_italic", "Bold Italic", "Bold", ::toSansBoldItalic),
        EnglishFontStyle("sans_italic", "Sans Italic", "Italic", ::toSansItalic),
        EnglishFontStyle("serif_bold", "Serif Bold", "Bold", ::toBoldSerif),
        EnglishFontStyle("serif_italic", "Serif Italic", "Italic", ::toItalicSerif),
        EnglishFontStyle("serif_bold_italic", "Serif B-Italic", "Italic", ::toBoldItalicSerif),
        EnglishFontStyle("bubble", "Bubble Light", "Bubble", ::toBubble),
        EnglishFontStyle("dark_bubble", "Bubble Dark", "Bubble", ::toDarkBubble),
        EnglishFontStyle("square", "Square Box", "Box", ::toSquare),
        EnglishFontStyle("dark_square", "Square Dark", "Box", ::toDarkSquare),
        EnglishFontStyle("small_caps", "Small Caps", "Clean", ::toSmallCaps),
        EnglishFontStyle("monospace", "Typewriter", "Mono", ::toMonospace),
        EnglishFontStyle("fullwidth", "Vaporwave", "Wide", ::toFullwidth),
        EnglishFontStyle("vapor_space", "Aesthetic Wide", "Wide", ::toVaporSpace),
        EnglishFontStyle("superscript", "Tiny Script", "Mini", ::toSuperscript),
        EnglishFontStyle("parenthesized", "Bracketed", "Box", ::toParenthesized),
        EnglishFontStyle("upside_down", "Upside Down", "Fun", ::toUpsideDown),
        EnglishFontStyle("glitch_light", "Glitch Pulse", "Glitch", ::toGlitchLight),
        EnglishFontStyle("glitch_heavy", "Zalgo Glitch", "Glitch", ::toGlitchHeavy),
        EnglishFontStyle("strikethrough", "Strikethrough", "Effect", ::toStrikethrough),
        EnglishFontStyle("double_underline", "Underline", "Effect", ::toDoubleUnderline),
        EnglishFontStyle("slash_through", "Slash Cut", "Effect", ::toSlashThrough),
        EnglishFontStyle("wave_under", "Wave Underline", "Effect", ::toLightningMark),
        EnglishFontStyle("winged_sword", "Winged Clan", "Framed") { s ->
            if (s.isBlank()) "" else "꧁༒☬ ${toBoldGothic(s)} ☬༒꧂"
        },
        EnglishFontStyle("royal_crown", "Royal Crown", "Framed") { s ->
            if (s.isBlank()) "" else "👑 ${toBoldScript(s)} 👑"
        },
        EnglishFontStyle("cyber_bracket", "Cyber Bracket", "Framed") { s ->
            if (s.isBlank()) "" else "『 ${toMonospace(s)} 』"
        },
        EnglishFontStyle("star_aura", "Star Aura", "Framed") { s ->
            if (s.isBlank()) "" else "★彡[ ${toSansBold(s)} ]彡★"
        },
        EnglishFontStyle("sparkle_cursive", "Sparkle Glow", "Framed") { s ->
            if (s.isBlank()) "" else "✨ ${toScript(s)} ✨"
        },
        EnglishFontStyle("fire_gaming", "Fire Gaming", "Framed") { s ->
            if (s.isBlank()) "" else "🔥⚡ ${toSansBoldItalic(s)} ⚡🔥"
        },
        EnglishFontStyle("heart_beat", "Heartbeat", "Framed") { s ->
            if (s.isBlank()) "" else "❥━── ${toBoldScript(s)} ──━❥"
        },
    )

    val bengaliDecorators: List<BengaliDecoratorStyle> = listOf(
        BengaliDecoratorStyle("plain", "সাধারণ (Plain)", "", ""),
        BengaliDecoratorStyle("royal_sword", "রয়্যাল তলোয়ার", "꧁༒☬ ", " ☬༒꧂"),
        BengaliDecoratorStyle("floral_dash", "ফ্লাওয়ার বর্ডার", "✿ ── ", " ── ✿", " ✿ "),
        BengaliDecoratorStyle("cyber_frame", "সাইবার ব্র্যাকেট", "『 ", " 』", " • "),
        BengaliDecoratorStyle("bewafa_heart", "বেবাফা / স্যাড", "💔🥀 ", " 🥀💔"),
        BengaliDecoratorStyle("royal_king", "রয়্যাল অ্যাট্টিটিউড", "👑⚡ ", " ⚡👑"),
        BengaliDecoratorStyle("star_frame", "স্টার ফ্রেম", "★彡[ ", " ]彡★"),
        BengaliDecoratorStyle("love_line", "লাভ লাইন", "❥━─── ", " ───━❥"),
        BengaliDecoratorStyle("skull_fire", "কঙ্কাল ফায়ার", "💀🔥 ", " 🔥💀"),
        BengaliDecoratorStyle("retro_wave", "রেট্রো ওয়েভ", "•°¯`•• ", " ••´¯°•"),
        BengaliDecoratorStyle("moonlight", "মুনলাইট", "☾⋆⁺₊⋆ ", " ⋆⁺₊⋆☽"),
        BengaliDecoratorStyle("dark_gothic", "ডার্ক গথিক", "◤✞ ", " ✞◥"),
        BengaliDecoratorStyle("sparkle_divider", "স্পার্কেল", "✦•┈๑⋅⋯ ", " ⋯⋅๑┈•✦"),
        BengaliDecoratorStyle("starry_wing", "স্টারি উইং", "╰☆☆ ", " ☆☆╮"),
    )

    fun transformEnglish(text: String, fontId: String): String {
        val style = englishFonts.find { it.id == fontId } ?: englishFonts.first()
        return style.transform(text)
    }

    fun decorateBengali(text: String, decoratorId: String, wordSpacing: Boolean): String {
        val style = bengaliDecorators.find { it.id == decoratorId } ?: bengaliDecorators.first()
        return style.decorate(text, wordSpacing)
    }
}
