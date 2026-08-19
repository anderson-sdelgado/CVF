package br.com.usinasantafe.cvf.presenter.view.manager.release

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
import br.com.usinasantafe.cvf.domain.usecases.manager.ListRelease
import br.com.usinasantafe.cvf.domain.usecases.manager.SaveManager
import br.com.usinasantafe.cvf.domain.usecases.update.UpdateTableRelease
import br.com.usinasantafe.cvf.external.room.dao.stable.ReleaseDao
import br.com.usinasantafe.cvf.external.sharedPreferences.IConfigSharedPreferencesDatasource
import br.com.usinasantafe.cvf.external.sharedPreferences.IManagerSharedPreferencesDatasource
import br.com.usinasantafe.cvf.infra.models.room.stable.ReleaseRoomModel
import br.com.usinasantafe.cvf.infra.models.sharedpreferences.ConfigSharedPreferencesModel
import br.com.usinasantafe.cvf.infra.models.sharedpreferences.ManagerSharedPreferencesModel
import br.com.usinasantafe.cvf.lib.StatusSend
import br.com.usinasantafe.cvf.presenter.navigation.Args.ID_FRONT_ARG
import br.com.usinasantafe.cvf.utils.CheckNetwork
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class ReleaseScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()

    @Inject
    lateinit var listRelease: ListRelease

    @Inject
    lateinit var updateTableRelease: UpdateTableRelease

    @Inject
    lateinit var saveManager: SaveManager

    @Inject
    lateinit var checkNetwork: CheckNetwork

    @Inject
    lateinit var releaseDao: ReleaseDao

    @Inject
    lateinit var managerSharedPreferencesDatasource: IManagerSharedPreferencesDatasource

    @Inject
    lateinit var configSharedPreferencesDatasource: IConfigSharedPreferencesDatasource

    val list = listOf(
            ReleaseRoomModel(
                id = 1,
                nroOS = 1,
                idPropAgr = 1,
                descPropAgr = "Test1",
                idFront = 1
            ),
            ReleaseRoomModel(
                id = 2,
                nroOS = 2,
                idPropAgr = 2,
                descPropAgr = "Test2",
                idFront = 2
            ),
            ReleaseRoomModel(
                id = 3,
                nroOS = 3,
                idPropAgr = 3,
                descPropAgr = "Test3",
                idFront = 3
            ),
            ReleaseRoomModel(
                id = 4,
                nroOS = 4,
                idPropAgr = 4,
                descPropAgr = "Test4",
                idFront = 3
            )
        )

    @Test
    fun check_open_screen_and_list_empty() =
        runTest {

            hiltRule.inject()

            setContent()

            composeTestRule.waitUntilTimeout(20_000)

        }

    @Test
    fun check_open_screen_and_list_empty_if_idFront_is_non_existent() =
        runTest {

            hiltRule.inject()

            releaseDao.insertAll(list)

            setContent(4)

            composeTestRule.waitUntilTimeout(20_000)

        }

    @Test
    fun check_open_screen_and_list_if_idFront_is_existent() =
        runTest {

            hiltRule.inject()

            releaseDao.insertAll(list)

            setContent(3)

            composeTestRule.waitUntilTimeout(20_000)

        }

    @Test
    fun check_open_screen_and_list_and_item_selection() =
        runTest {

            hiltRule.inject()

            releaseDao.insertAll(list)

            managerSharedPreferencesDatasource.save(
                ManagerSharedPreferencesModel(
                    idFront = 0,
                    idRelease = 4
                )
            )
            setContent(3)

            composeTestRule.waitUntilTimeout(20_000)

        }

    @Test
    fun check_open_screen_and_failure_if_config_table_is_null() =
        runTest {

            hiltRule.inject()

            releaseDao.insertAll(list)

            setContent(3)

            composeTestRule.onNodeWithText("ATUALIZAR DADOS")
                .performClick()

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertIsDisplayed()
            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertTextEquals("FALHA DE ATUALIZAÇÃO DE DADOS! POR FAVOR, ENTRE EM CONTATO COM TI. ReleaseViewModel.updateAllDatabase -> ReleaseViewModel.update -> UiStatusStateUpdateKt.executeUpdateSteps -> UiStatusStateUpdateKt.collectUpdateStep -> IUpdateTableRelease -> IReleaseRepository.listAll -> IReleaseRetrofitDatasource.listAll -> IGetToken -> IConfigRepository.get -> number is required -> java.lang.NullPointerException: number is required")

            composeTestRule.waitUntilTimeout(20_000)

        }

    @Test
    fun check_open_screen_and_failure_if_service_without_connection() =
        runTest {

            hiltRule.inject()

            releaseDao.insertAll(list)

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

            setContent(3)

            composeTestRule.onNodeWithText("ATUALIZAR DADOS")
                .performClick()

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertIsDisplayed()
            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertTextEquals("FALHA DE ATUALIZAÇÃO DE DADOS! POR FAVOR, ENTRE EM CONTATO COM TI. ReleaseViewModel.updateAllDatabase -> ReleaseViewModel.update -> UiStatusStateUpdateKt.executeUpdateSteps -> UiStatusStateUpdateKt.collectUpdateStep -> IUpdateTableRelease -> IReleaseRepository.listAll -> IReleaseRetrofitDatasource.listAll -> java.net.ConnectException: Failed to connect to localhost/127.0.0.1:8080")

            composeTestRule.waitUntilTimeout(20_000)

        }

    @Test
    fun check_return_failure_if_error_url() =
        runTest {

            val server = MockWebServer()
            server.start()
            server.enqueue(
                MockResponse().setBody("{ \"status\": \"error\", \"failure\": \"Authorization header is missing\" }")
            )
            BaseUrlModuleTest.url = server.url("/").toString()

            hiltRule.inject()

            releaseDao.insertAll(list)

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

            setContent(3)

            composeTestRule.onNodeWithText("ATUALIZAR DADOS")
                .performClick()

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertIsDisplayed()
            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertTextEquals("FALHA DE ATUALIZAÇÃO DE DADOS! POR FAVOR, ENTRE EM CONTATO COM TI. ReleaseViewModel.updateAllDatabase -> ReleaseViewModel.update -> UiStatusStateUpdateKt.executeUpdateSteps -> UiStatusStateUpdateKt.collectUpdateStep -> IUpdateTableRelease -> IReleaseRepository.listAll -> IReleaseRetrofitDatasource.listAll -> java.lang.Exception: Authorization header is missing")

            composeTestRule.waitUntilTimeout(20_000)
            server.shutdown()
        }

    @Test
    fun check_update_correct() =
        runTest {

            val response = """
                {
                    "status": "success",
                    "data": [
                      {"id":6,"nroOS":6,"idPropAgr":6,"descPropAgr":"Test6","idFront":3},
                      {"id":7,"nroOS":7,"idPropAgr":7,"descPropAgr":"Test7","idFront":3}
                    ]
                }
            """.trimIndent()
            val server = MockWebServer()
            server.start()
            server.enqueue(
                MockResponse().setBody(response)
            )
            BaseUrlModuleTest.url = server.url("/").toString()

            hiltRule.inject()

            releaseDao.insertAll(list)

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

            setContent(3)

            composeTestRule.onNodeWithText("ATUALIZAR DADOS")
                .performClick()

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("tag_button_ok_alert_dialog_simple")
                .performClick()

            composeTestRule.waitUntilTimeout(20_000)
            server.shutdown()
        }

    @Test
    fun check_update_correct_open_screen() =
        runTest {

            val response = """
                {
                    "status": "success",
                    "data": [
                      {"id":6,"nroOS":6,"idPropAgr":6,"descPropAgr":"Test6","idFront":3},
                      {"id":7,"nroOS":7,"idPropAgr":7,"descPropAgr":"Test7","idFront":3}
                    ]
                }
            """.trimIndent()
            val server = MockWebServer()
            server.start()
            server.enqueue(
                MockResponse().setBody(response)
            )
            BaseUrlModuleTest.url = server.url("/").toString()

            hiltRule.inject()

            releaseDao.insertAll(list)

            managerSharedPreferencesDatasource.save(
                ManagerSharedPreferencesModel(
                    idFront = 3,
                    idRelease = 4
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

            setContent(0)

            composeTestRule.waitUntilTimeout(20_000)
            server.shutdown()
        }

    @SuppressLint("ViewModelConstructorInComposable")
    private fun setContent(idFront: Int = 1) {
        composeTestRule.setContent {
            ReleaseScreen (
                viewModel = ReleaseViewModel(
                    savedStateHandle = SavedStateHandle(
                        mapOf(ID_FRONT_ARG to idFront)
                    ),
                    listRelease = listRelease,
                    updateTableRelease = updateTableRelease,
                    saveManager = saveManager,
                    checkNetwork = checkNetwork
                ),
                onNavFront = {},
                onNavDriver = {}
            )
        }
    }

}