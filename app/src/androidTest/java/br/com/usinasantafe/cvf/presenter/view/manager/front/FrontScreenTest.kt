package br.com.usinasantafe.cvf.presenter.view.manager.front

import android.annotation.SuppressLint
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.lifecycle.SavedStateHandle
import br.com.usinasantafe.cav.utils.waitUntilTimeout
import br.com.usinasantafe.cvf.HiltTestActivity
import br.com.usinasantafe.cvf.di.provider.BaseUrlModuleTest
import br.com.usinasantafe.cvf.domain.usecases.manager.ListFront
import br.com.usinasantafe.cvf.domain.usecases.update.UpdateTableFront
import br.com.usinasantafe.cvf.external.room.dao.stable.FrontDao
import br.com.usinasantafe.cvf.external.sharedPreferences.IConfigSharedPreferencesDatasource
import br.com.usinasantafe.cvf.external.sharedPreferences.IManagerSharedPreferencesDatasource
import br.com.usinasantafe.cvf.infra.models.room.stable.FrontRoomModel
import br.com.usinasantafe.cvf.infra.models.sharedpreferences.ConfigSharedPreferencesModel
import br.com.usinasantafe.cvf.infra.models.sharedpreferences.ManagerSharedPreferencesModel
import br.com.usinasantafe.cvf.lib.StatusSend
import br.com.usinasantafe.cvf.presenter.navigation.Args.ID_FRONT_ARG
import br.com.usinasantafe.cvf.presenter.theme.TAG_BUTTON_OK_ALERT_DIALOG_SIMPLE
import br.com.usinasantafe.cvf.presenter.view.manager.release.ReleaseViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class FrontScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()

    @Inject
    lateinit var frontDao: FrontDao

    @Inject
    lateinit var managerSharedPreferencesDatasource: IManagerSharedPreferencesDatasource

    @Inject
    lateinit var configSharedPreferencesDatasource: IConfigSharedPreferencesDatasource

    @Inject
    lateinit var listFront: ListFront

    @Inject
    lateinit var updateTableFront: UpdateTableFront

    @Test
    fun check_open_screen() =
        runTest {

            hiltRule.inject()

            setContent()

            composeTestRule.waitUntilTimeout(20_000)

        }

    @Test
    fun check_open_screen_with_data() =
        runTest {

            hiltRule.inject()

            frontDao.insertAll(
                listOf(
                    FrontRoomModel(
                        id = 1,
                        cd = 1,
                        description = "Test1"
                    ),
                    FrontRoomModel(
                        id = 3,
                        cd = 3,
                        description = "Test3"
                    ),
                    FrontRoomModel(
                        id = 2,
                        cd = 2,
                        description = "Test2"
                    ),
                )
            )

            setContent()

            composeTestRule.waitUntilTimeout(20_000)

        }

    @Test
    fun check_open_screen_with_data_and_selected_item() =
        runTest {

            hiltRule.inject()

            frontDao.insertAll(
                listOf(
                    FrontRoomModel(
                        id = 1,
                        cd = 1,
                        description = "Test1"
                    ),
                    FrontRoomModel(
                        id = 3,
                        cd = 3,
                        description = "Test3"
                    ),
                    FrontRoomModel(
                        id = 2,
                        cd = 2,
                        description = "Test2"
                    ),
                )
            )

            managerSharedPreferencesDatasource.save(
                ManagerSharedPreferencesModel(
                    idFront = 2,
                    idRelease = 1
                )
            )

            setContent()

            composeTestRule.waitUntilTimeout(20_000)

        }

    @Test
    fun check_open_screen_with_data_and_selected_item_and_id_selection_is_not_null() =
        runTest {

            hiltRule.inject()

            frontDao.insertAll(
                listOf(
                    FrontRoomModel(
                        id = 1,
                        cd = 1,
                        description = "Test1"
                    ),
                    FrontRoomModel(
                        id = 3,
                        cd = 3,
                        description = "Test3"
                    ),
                    FrontRoomModel(
                        id = 2,
                        cd = 2,
                        description = "Test2"
                    ),
                )
            )

            managerSharedPreferencesDatasource.save(
                ManagerSharedPreferencesModel(
                    idFront = 2,
                    idRelease = 1
                )
            )

            setContent(3)

            composeTestRule.waitUntilTimeout(20_000)

        }

    @Test
    fun check_open_screen_and_failure_if_config_table_is_null() =
        runTest {

            hiltRule.inject()

            frontDao.insertAll(
                listOf(
                    FrontRoomModel(
                        id = 1,
                        cd = 1,
                        description = "Test1"
                    ),
                )
            )

            setContent()

            composeTestRule.onNodeWithText("ATUALIZAR DADOS")
                .performClick()

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertIsDisplayed()
            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertTextEquals("FALHA DE ATUALIZAÇÃO DE DADOS! POR FAVOR, ENTRE EM CONTATO COM TI. FrontViewModel.updateAllDatabase -> FrontViewModel.update -> UiStatusStateUpdateKt.executeUpdateSteps -> UiStatusStateUpdateKt.collectUpdateStep -> IUpdateTableFront -> IGetToken -> IConfigRepository.get -> number is required -> java.lang.NullPointerException: number is required")

            composeTestRule.waitUntilTimeout(20_000)

        }

    @Test
    fun check_open_screen_and_failure_if_service_without_connection() =
        runTest {

            hiltRule.inject()

            frontDao.insertAll(
                listOf(
                    FrontRoomModel(
                        id = 1,
                        cd = 1,
                        description = "Test1"
                    ),
                )
            )

            configSharedPreferencesDatasource.save(
                ConfigSharedPreferencesModel(
                    number = 16997417840,
                    password = "12345",
                    idServ = 1,
                    version = "1.0",
                    statusSend = StatusSend.SENT,
                    flagUpdate = true
                )
            )

            setContent()

            composeTestRule.onNodeWithText("ATUALIZAR DADOS")
                .performClick()

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertIsDisplayed()
            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertTextEquals("FALHA DE ATUALIZAÇÃO DE DADOS! POR FAVOR, ENTRE EM CONTATO COM TI. FrontViewModel.updateAllDatabase -> FrontViewModel.update -> UiStatusStateUpdateKt.executeUpdateSteps -> UiStatusStateUpdateKt.collectUpdateStep -> IUpdateTableFront -> IFrontRepository.listAll -> IFrontRetrofitDatasource.listAll -> java.net.ConnectException: Failed to connect to localhost/127.0.0.1:8080")

            composeTestRule.waitUntilTimeout(20_000)

        }

    @Test
    fun check_return_failure_if_error_url() =
        runTest {

            val server = MockWebServer()
            server.start()
            server.enqueue(
                MockResponse().setBody("{ error : Authorization header is missing }")
            )
            BaseUrlModuleTest.url = server.url("/").toString()

            hiltRule.inject()

            frontDao.insertAll(
                listOf(
                    FrontRoomModel(
                        id = 1,
                        cd = 1,
                        description = "Test1"
                    ),
                )
            )

            configSharedPreferencesDatasource.save(
                ConfigSharedPreferencesModel(
                    number = 16997417840,
                    password = "12345",
                    idServ = 1,
                    version = "1.0",
                    statusSend = StatusSend.SENT,
                    flagUpdate = true
                )
            )

            setContent()

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithText("ATUALIZAR DADOS")
                .performClick()

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertIsDisplayed()
            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertTextEquals("FALHA DE ATUALIZAÇÃO DE DADOS! POR FAVOR, ENTRE EM CONTATO COM TI. FrontViewModel.updateAllDatabase -> FrontViewModel.update -> UiStatusStateUpdateKt.executeUpdateSteps -> UiStatusStateUpdateKt.collectUpdateStep -> IUpdateTableFront -> IFrontRepository.listAll -> IFrontRetrofitDatasource.listAll -> java.lang.IllegalStateException: Expected BEGIN_ARRAY but was BEGIN_OBJECT at line 1 column 2 path \$\n" +
                    "See https://github.com/google/gson/blob/main/Troubleshooting.md#unexpected-json-structure"
            )

            composeTestRule.waitUntilTimeout(20_000)

        }

    @Test
    fun check_update_correct() =
        runTest {

            val response = """
                [
                  {"id":1,"cd":1,"description":"Test1"},
                  {"id":3,"cd":3,"description":"Test3"},
                  {"id":2,"cd":2,"description":"Test2"}
                ]
            """
            val mockWebServer = MockWebServer()
            mockWebServer.start()
            mockWebServer.enqueue(
                MockResponse().setBody(response)
            )
            BaseUrlModuleTest.url = mockWebServer.url("/").toString()

            hiltRule.inject()

            frontDao.insertAll(
                listOf(
                    FrontRoomModel(
                        id = 1,
                        cd = 1,
                        description = "Test1"
                    ),
                )
            )

            configSharedPreferencesDatasource.save(
                ConfigSharedPreferencesModel(
                    number = 16997417840,
                    password = "12345",
                    idServ = 1,
                    version = "1.0",
                    statusSend = StatusSend.SENT,
                    flagUpdate = true
                )
            )

            setContent()

            composeTestRule.onNodeWithText("ATUALIZAR DADOS")
                .performClick()

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag(TAG_BUTTON_OK_ALERT_DIALOG_SIMPLE)
                .performClick()

            composeTestRule.waitUntilTimeout(20_000)

        }

    @Test
    fun check_return_failure_if_not_item_selection() =
        runTest {

            hiltRule.inject()

            frontDao.insertAll(
                listOf(
                    FrontRoomModel(
                        id = 1,
                        cd = 1,
                        description = "Test1"
                    ),
                    FrontRoomModel(
                        id = 3,
                        cd = 3,
                        description = "Test3"
                    ),
                    FrontRoomModel(
                        id = 2,
                        cd = 2,
                        description = "Test2"
                    ),
                )
            )

            setContent()

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithText("AVANÇAR")
                .performClick()

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag(TAG_BUTTON_OK_ALERT_DIALOG_SIMPLE)
                .performClick()

            composeTestRule.waitUntilTimeout(20_000)

        }

    @SuppressLint("ViewModelConstructorInComposable")
    private fun setContent(idFront: Int = 0) {
        composeTestRule.setContent {
            FrontScreen (
                viewModel = FrontViewModel(
                    savedStateHandle = SavedStateHandle(
                        mapOf(ID_FRONT_ARG to idFront)
                    ),
                    updateTableFront = updateTableFront,
                    listFront = listFront
                ),
                onNavRelease = {},
                onNavConfig = {}
            )
        }
    }

}