package br.com.usinasantafe.cvf.presenter.view.note.driver

import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import br.com.usinasantafe.cav.utils.waitUntilTimeout
import br.com.usinasantafe.cvf.HiltTestActivity
import br.com.usinasantafe.cvf.presenter.theme.TAG_TOP_BAR_MENU
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class DriverScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()

    @Test
    fun check_open_screen() =
        runTest {

            hiltRule.inject()

            setContent()

            composeTestRule.waitUntilTimeout(30_000)

        }

    @Test
    fun check_open_screen_and_click_menu() =
        runTest {

            hiltRule.inject()

            setContent()

            composeTestRule.waitUntilTimeout(3_000)

            composeTestRule.onNodeWithTag(TAG_TOP_BAR_MENU)
                .performClick()

            composeTestRule.waitUntilTimeout(3_000)

            composeTestRule.onNodeWithTag(TAG_TOP_BAR_MENU)
                .performClick()

            composeTestRule.waitUntilTimeout(20_000)

        }

    @Test
    fun check_open_screen_and_input_number() =
        runTest {

            hiltRule.inject()

            setContent()

            composeTestRule.waitUntilTimeout(3_000)

            composeTestRule.onNodeWithTag("button_1")
                .performClick()
            composeTestRule.onNodeWithTag("button_9")
                .performClick()
            composeTestRule.onNodeWithTag("button_7")
                .performClick()

            composeTestRule.waitUntilTimeout(3_000)

            composeTestRule.onNodeWithTag("button_CLEAN")
                .performClick()
            composeTestRule.onNodeWithTag("button_CLEAN")
                .performClick()
            composeTestRule.onNodeWithTag("button_CLEAN")
                .performClick()

            composeTestRule.waitUntilTimeout(3_000)

            composeTestRule.onNodeWithTag("button_1")
                .performClick()
            composeTestRule.onNodeWithTag("button_8")
                .performClick()
            composeTestRule.onNodeWithTag("button_0")
                .performClick()
            composeTestRule.onNodeWithTag("button_1")
                .performClick()
            composeTestRule.onNodeWithTag("button_7")
                .performClick()

            composeTestRule.waitUntilTimeout(20_000)

        }

    private fun setContent(){
        composeTestRule.setContent {
            DriverScreen()
        }
    }

}