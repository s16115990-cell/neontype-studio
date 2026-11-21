package com.example

import com.example.domain.engine.StickerAndTemplateCatalog
import com.example.domain.engine.UnicodeFontEngine
import com.example.domain.model.CreatorPlatform
import com.example.domain.model.StickerCategory
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun `english unicode font engine provides at least 30 distinct fonts`() {
        assertTrue(UnicodeFontEngine.englishFonts.size >= 30)
        val samples = UnicodeFontEngine.englishFonts.map { it.transform("NeonType 2026") }
        assertTrue(samples.toSet().size >= 30)
    }

    @Test
    fun `script fraktur and double struck handle letterlike exceptions cleanly`() {
        val alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
        val script = UnicodeFontEngine.transformEnglish(alphabet, "script")
        val gothic = UnicodeFontEngine.transformEnglish(alphabet, "gothic")
        val doubleStruck = UnicodeFontEngine.transformEnglish(alphabet, "double_struck")

        assertTrue(script.isNotBlank() && script != alphabet)
        assertTrue(gothic.isNotBlank() && gothic != alphabet)
        assertTrue(doubleStruck.isNotBlank() && doubleStruck != alphabet)
    }

    @Test
    fun `bengali decorators wrap text and support word dot spacing`() {
        val decorated = UnicodeFontEngine.decorateBengali(
            text = "আমার সোনার বাংলা",
            decoratorId = "royal_sword",
            wordSpacing = false,
        )
        assertEquals("꧁༒☬ আমার সোনার বাংলা ☬༒꧂", decorated)

        val floralDot = UnicodeFontEngine.decorateBengali(
            text = "ভালোবাসা দিও",
            decoratorId = "floral_dash",
            wordSpacing = true,
        )
        assertEquals("✿ ── ভালোবাসা ✿ দিও ── ✿", floralDot)
    }

    @Test
    fun `sticker catalog and bio templates cover all categories and platforms`() {
        StickerCategory.entries.forEach { cat ->
            assertTrue(
                "Missing stickers for $cat",
                StickerAndTemplateCatalog.stickers.any { it.category == cat },
            )
        }
        listOf(
            CreatorPlatform.YOUTUBE,
            CreatorPlatform.INSTAGRAM,
            CreatorPlatform.FACEBOOK,
            CreatorPlatform.GAMING,
        ).forEach { platform ->
            assertTrue(
                "Missing templates for $platform",
                StickerAndTemplateCatalog.bioTemplates.any { it.platform == platform },
            )
        }
    }
}
