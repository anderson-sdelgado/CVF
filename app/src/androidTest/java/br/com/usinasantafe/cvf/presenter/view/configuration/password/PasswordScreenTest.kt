package br.com.usinasantafe.cvf.presenter.view.configuration.password

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import br.com.usinasantafe.cav.utils.waitUntilTimeout
import br.com.usinasantafe.cvf.HiltTestActivity
import br.com.usinasantafe.cvf.infra.datasource.sharedpreferences.ConfigSharedPreferencesDatasource
import br.com.usinasantafe.cvf.infra.models.sharedpreferences.ConfigSharedPreferencesModel
import br.com.usinasantafe.cvf.lib.StatusSend
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class PasswordScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()

    @Inject
    lateinit var configSharedPreferencesDatasource: ConfigSharedPreferencesDatasource

    @Test
    fun check_open_screen() =
        runTest {

            hiltRule.inject()

            setContent()

            composeTestRule.waitUntilTimeout(20_000)

        }

    @Test
    fun check_open_screen_and_return_failure_if_password_is_empty() =
        runTest {

            hiltRule.inject()

            setContent()

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithText("OK")
                .performClick()

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertIsDisplayed()
            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertTextEquals("FALHA INESPERADA NO APLICATIVO! POR FAVOR ENTRE EM CONTATO COM TI. PasswordViewModel.onCheckAccess -> PasswordViewModel.onCheckAccess -> ICheckPassword -> IConfigRepository.getPassword -> IConfigSharedPreferencesDatasource.getPassword -> java.lang.NullPointerException: password is required")

            composeTestRule.waitUntilTimeout(20_000)

        }

    @Test
    fun check_open_screen_and_return_failure_if_password_is_empty_and_table_config_have_data() =
        runTest {

            hiltRule.inject()

            configSharedPreferencesDatasource.save(
                ConfigSharedPreferencesModel(
                    number = 16997417840,
                    password = "12345",
                    idServ = 1,
                    version = "1.0",
                )
            )

            setContent()

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithText("OK")
                .performClick()

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertIsDisplayed()
            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertTextEquals("SENHA INVÁLIDA!")

            composeTestRule.waitUntilTimeout(20_000)

        }

    @Test
    fun check_open_screen_and_return_failure_if_password_input_is_incorrect_and_table_config_have_data() =
        runTest {

            hiltRule.inject()

            configSharedPreferencesDatasource.save(
                ConfigSharedPreferencesModel(
                    number = 16997417840,
                    password = "12345",
                    idServ = 1,
                    version = "1.0",
                )
            )

            setContent()

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag(TAG_PASSWORD_TEXT_FIELD_SCREEN)
                .performTextInput("123456")
            composeTestRule.onNodeWithText("OK")
                .performClick()

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertIsDisplayed()
            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertTextEquals("SENHA INVÁLIDA!")

            composeTestRule.waitUntilTimeout(20_000)

        }

    private fun setContent() {
        composeTestRule.setContent {
            PasswordScreen(
                onNavConfig = {},
                onNavFront = {},
                onNavRelease = {},
                onNavNote = {}
            )
        }
    }

}