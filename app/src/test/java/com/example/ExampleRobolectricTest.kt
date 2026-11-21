package com.example

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36], qualifiers = "w411dp-h915dp-xxhdpi")
class ExampleRobolectricTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun `read app name from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("NeonType", appName)
    }

    @Test
    fun `studio keyboard typing and language switch work end to end`() {
        composeTestRule.waitForIdle()

        // Type 'h' 'i' on English keyboard
        composeTestRule.onNodeWithTag("key_h").performClick()
        composeTestRule.onNodeWithTag("key_i").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithTag("live_preview_output").assertIsDisplayed()
        composeTestRule.onNodeWithTag("copy_text_button").assertIsDisplayed()
        composeTestRule.onNodeWithTag("clear_text_button").performClick()
        composeTestRule.waitForIdle()

        // Switch to Bengali keyboard
        composeTestRule.onNodeWithTag("studio_language_pill").performClick()
        composeTestRule.waitUntil(timeoutMillis = 3000) {
            composeTestRule.onAllNodesWithTag("bengali_decorator_bar").fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNodeWithTag("bengali_decorator_bar").assertIsDisplayed()
        composeTestRule.onNodeWithTag("key_ক").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("live_preview_output").assertTextContains("ক", substring = true)
    }

    @Test
    fun `navigation across stickers lighting and toolbox tabs works`() {
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithTag("nav_tab_STICKERS").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("sticker_grid").assertIsDisplayed()

        composeTestRule.onNodeWithTag("nav_tab_LIGHTING").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("keyboard_preview").assertIsDisplayed()

        composeTestRule.onNodeWithTag("nav_tab_TOOLBOX").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("bio_name_input").assertIsDisplayed()

        composeTestRule.onNodeWithTag("toolbox_tab_REPEATER").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("repeater_preset_500").performScrollTo().performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("repeater_copy_all_button").performScrollTo().assertIsDisplayed()
    }
}
