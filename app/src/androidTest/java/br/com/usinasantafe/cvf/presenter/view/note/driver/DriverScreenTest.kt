package br.com.usinasantafe.cvf.presenter.view.note.driver

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import br.com.usinasantafe.cav.utils.waitUntilTimeout
import br.com.usinasantafe.cvf.HiltTestActivity
import br.com.usinasantafe.cvf.di.provider.BaseUrlModuleTest
import br.com.usinasantafe.cvf.external.room.dao.stable.ColabDao
import br.com.usinasantafe.cvf.external.room.dao.stable.FrontDao
import br.com.usinasantafe.cvf.external.room.dao.stable.ReleaseDao
import br.com.usinasantafe.cvf.external.sharedPreferences.IConfigSharedPreferencesDatasource
import br.com.usinasantafe.cvf.external.sharedPreferences.IHeaderSharedPreferencesDatasource
import br.com.usinasantafe.cvf.external.sharedPreferences.IManagerSharedPreferencesDatasource
import br.com.usinasantafe.cvf.infra.models.room.stable.ColabRoomModel
import br.com.usinasantafe.cvf.infra.models.room.stable.FrontRoomModel
import br.com.usinasantafe.cvf.infra.models.room.stable.ReleaseRoomModel
import br.com.usinasantafe.cvf.infra.models.sharedpreferences.ConfigSharedPreferencesModel
import br.com.usinasantafe.cvf.lib.StatusSend
import br.com.usinasantafe.cvf.presenter.theme.TAG_TOP_BAR_TITLE
import br.com.usinasantafe.cvf.utils.CheckNetwork
import br.com.usinasantafe.cvf.utils.TestConfig
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.seconds

@HiltAndroidTest
class DriverScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()

    @Inject
    lateinit var managerSharedPreferencesDatasource: IManagerSharedPreferencesDatasource

    @Inject
    lateinit var releaseDao: ReleaseDao

    @Inject
    lateinit var frontDao: FrontDao

    @Inject
    lateinit var headerSharedPreferencesDatasource: IHeaderSharedPreferencesDatasource

    @Inject
    lateinit var configSharedPreferencesDatasource: IConfigSharedPreferencesDatasource

    @Inject
    lateinit var colabDao: ColabDao

    @BindValue
    @JvmField
    val checkNetwork: CheckNetwork = mock()

    @Before
    fun setUp() {
        TestConfig.skipPermissionRequest = true
    }

    @After
    fun tearDown() {
        TestConfig.skipPermissionRequest = false
    }

    @Test
    fun check_open_screen_and_title_is_empty_if_manager_table_is_empty() =
        runTest {

            hiltRule.inject()

            setContent()

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag(TAG_TOP_BAR_TITLE).assertTextEquals("")
            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertDoesNotExist()

        }

    @Test
    fun check_open_screen_and_title_is_empty_if_idFront_is_null() =
        runTest {

            hiltRule.inject()

            initialRegister()

            setContent()

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag(TAG_TOP_BAR_TITLE).assertTextEquals("")
            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertDoesNotExist()

        }

    @Test
    fun check_open_screen_correct_if_release_table_is_empty() =
        runTest {

            hiltRule.inject()

            initialRegister(2)

            setContent()

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag(TAG_TOP_BAR_TITLE).assertTextEquals("")
            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertDoesNotExist()

        }

    @Test
    fun check_open_screen_correct_if_front_table_is_empty() =
        runTest {

            hiltRule.inject()

            initialRegister(3)

            setContent()

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag(TAG_TOP_BAR_TITLE).assertTextEquals("")
            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertDoesNotExist()

        }

    @Test
    fun check_open_screen_correct() =
        runTest {

            hiltRule.inject()

            initialRegister(4)

            setContent()

            composeTestRule.waitUntilTimeout(5_000)

            composeTestRule.onNodeWithTag(TAG_TOP_BAR_TITLE).assertTextEquals("FRENTE: Test2\nLIBERAÇÃO: 1\nO.S.: 1\nPROPRIEDADE: Test1")

        }

    @Test
    fun check_open_screen_correct_and_reg_driver_is_not_null() =
        runTest {

            hiltRule.inject()

            initialRegister(4)

            headerSharedPreferencesDatasource.setRegDriver(19759)

            setContent()

            composeTestRule.waitUntilTimeout(5_000)

            composeTestRule.onNodeWithTag(TAG_TOP_BAR_TITLE).assertTextEquals("FRENTE: Test2\nLIBERAÇÃO: 1\nO.S.: 1\nPROPRIEDADE: Test1")

        }

    @Test
    fun check_return_failure_if_config_table_is_null() =
        runTest {

            hiltRule.inject()

            initialRegister(4)

            whenever(checkNetwork.isConnected()).thenReturn(true)

            setContent()

            composeTestRule.onNodeWithTag("button_1")
                .performClick()
            composeTestRule.onNodeWithTag("button_9")
                .performClick()
            composeTestRule.onNodeWithTag("button_7")
                .performClick()
            composeTestRule.onNodeWithTag("button_5")
                .performClick()
            composeTestRule.onNodeWithTag("button_9")
                .performClick()
            composeTestRule.onNodeWithTag("button_OK")
                .performClick()

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertIsDisplayed()
            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertTextEquals("FALHA INESPERADA NO APLICATIVO! POR FAVOR ENTRE EM CONTATO COM TI. DriverViewModel.updateState -> UiStatusStateUpdateKt -> ButtonsKt -> DriverViewModel.onTextField -> DriverViewModel.set -> IToken.invoke -> token -> number is required -> null")

        }

    @Test
    fun check_return_failure_if_service_without_connection() =
        runTest {

            hiltRule.inject()

            initialRegister(5)

            whenever(checkNetwork.isConnected()).thenReturn(true)

            setContent()

            composeTestRule.onNodeWithTag("button_1")
                .performClick()
            composeTestRule.onNodeWithTag("button_9")
                .performClick()
            composeTestRule.onNodeWithTag("button_7")
                .performClick()
            composeTestRule.onNodeWithTag("button_5")
                .performClick()
            composeTestRule.onNodeWithTag("button_9")
                .performClick()
            composeTestRule.onNodeWithTag("button_OK")
                .performClick()

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertIsDisplayed()
            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertTextContains("java.net.ConnectException: Failed to connect to localhost/127.0.0.1:")

        }

    @Test
    fun check_return_failure_if_error_url() =
        runTest {

            val result = """
                {
                    "status": "error",
                    "failure": "Authorization header is missing"
                }
            """.trimIndent()

            val server = MockWebServer()
            server.start()
            server.enqueue(
                MockResponse().setBody(result)
            )
            BaseUrlModuleTest.url = server.url("/").toString()

            hiltRule.inject()

            initialRegister(5)

            whenever(checkNetwork.isConnected()).thenReturn(true)

            setContent()

            composeTestRule.onNodeWithTag("button_1")
                .performClick()
            composeTestRule.onNodeWithTag("button_9")
                .performClick()
            composeTestRule.onNodeWithTag("button_7")
                .performClick()
            composeTestRule.onNodeWithTag("button_5")
                .performClick()
            composeTestRule.onNodeWithTag("button_9")
                .performClick()
            composeTestRule.onNodeWithTag("button_OK")
                .performClick()

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertIsDisplayed()
            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertTextEquals("FALHA INESPERADA NO APLICATIVO! POR FAVOR ENTRE EM CONTATO COM TI. DriverViewModel.updateState -> UiStatusStateUpdateKt -> ButtonsKt -> DriverViewModel.onTextField -> DriverViewModel.set -> IHasRegColab -> IColabRepository.check -> IColabRetrofitDatasource.checkByReg -> java.lang.Exception: Authorization header is missing")

            server.shutdown()
        }

    @Test
    fun check_return_failure_if_data_web_service_is_incorrect() =
        runTest {

            val result = """
                {
                    "status": "success",
                    "data": {"reg":as1adas5,"name":"João da Silva"}
                }
            """.trimIndent()

            val server = MockWebServer()
            server.start()
            server.enqueue(
                MockResponse().setBody(result)
            )
            BaseUrlModuleTest.url = server.url("/").toString()

            hiltRule.inject()

            initialRegister(5)

            whenever(checkNetwork.isConnected()).thenReturn(true)

            setContent()

            composeTestRule.onNodeWithTag("button_1")
                .performClick()
            composeTestRule.onNodeWithTag("button_9")
                .performClick()
            composeTestRule.onNodeWithTag("button_7")
                .performClick()
            composeTestRule.onNodeWithTag("button_5")
                .performClick()
            composeTestRule.onNodeWithTag("button_9")
                .performClick()
            composeTestRule.onNodeWithTag("button_OK")
                .performClick()

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertIsDisplayed()
            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertTextEquals("FALHA INESPERADA NO APLICATIVO! POR FAVOR ENTRE EM CONTATO COM TI. DriverViewModel.updateState -> UiStatusStateUpdateKt -> ButtonsKt -> DriverViewModel.onTextField -> DriverViewModel.set -> IHasRegColab -> IColabRepository.check -> IColabRetrofitDatasource.checkByReg -> com.google.gson.stream.MalformedJsonException: Use JsonReader.setStrictness(Strictness.LENIENT) to accept malformed JSON at line 3 column 20 path \$.data.reg\n" +
                    "See https://github.com/google/gson/blob/main/Troubleshooting.md#malformed-json")

            server.shutdown()
        }

    @Test
    fun check_add_data_if_data_web_service_is_correct_and_reg_existent_of_web_service() =
        runTest {

            val result = """
                {
                    "status": "success",
                    "data": {"reg":19759,"name":"ANDERSON DA SILVA DELGADO"}
                }
            """.trimIndent()

            val server = MockWebServer()
            server.start()
            server.enqueue(
                MockResponse().setBody(result)
            )
            BaseUrlModuleTest.url = server.url("/").toString()

            hiltRule.inject()

            initialRegister(5)

            whenever(checkNetwork.isConnected()).thenReturn(true)

            colabDao.insertAll(
                listOf(
                    ColabRoomModel(
                        reg = 18017,
                        name = "RONALDO GOMES"
                    )
                )
            )
            val listBefore = colabDao.all()
            assertEquals(
                1,
                listBefore.size
            )
            val modelBefore1 = listBefore[0]
            assertEquals(
                ColabRoomModel(
                    reg = 18017,
                    name = "RONALDO GOMES"
                ),
                modelBefore1
            )

            setContent()

            composeTestRule.onNodeWithTag("button_1")
                .performClick()
            composeTestRule.onNodeWithTag("button_9")
                .performClick()
            composeTestRule.onNodeWithTag("button_7")
                .performClick()
            composeTestRule.onNodeWithTag("button_5")
                .performClick()
            composeTestRule.onNodeWithTag("button_9")
                .performClick()
            composeTestRule.onNodeWithTag("button_OK")
                .performClick()

            composeTestRule.waitUntilTimeout(10_000)

            val listAfter = colabDao.all()
            assertEquals(
                2,
                listAfter.size
            )
            val modelAfter1 = listAfter[0]
            assertEquals(
                ColabRoomModel(
                    reg = 18017,
                    name = "RONALDO GOMES"
                ),
                modelAfter1
            )
            val modelAfter2 = listAfter[1]
            assertEquals(
                ColabRoomModel(
                    reg = 19759,
                    name = "ANDERSON DA SILVA DELGADO"
                ),
                modelAfter2
            )

            server.shutdown()
        }

    @Test
    fun check_rem_data_if_data_web_service_is_correct_and_reg_existent_of_web_service() =
        runTest {

            val result = """
                {
                    "status": "success",
                    "data": {"reg":0,"name":"NON_INEXISTENT"}
                }
            """.trimIndent()

            val server = MockWebServer()
            server.start()
            server.enqueue(
                MockResponse().setBody(result)
            )
            BaseUrlModuleTest.url = server.url("/").toString()

            hiltRule.inject()

            initialRegister(5)

            whenever(checkNetwork.isConnected()).thenReturn(true)

            colabDao.insertAll(
                listOf(
                    ColabRoomModel(
                        reg = 18017,
                        name = "RONALDO GOMES"
                    ),
                    ColabRoomModel(
                        reg = 19759,
                        name = "ANDERSON DA SILVA DELGADO"
                    )
                )
            )
            val listBefore = colabDao.all()
            assertEquals(
                2,
                listBefore.size
            )

            setContent()

            composeTestRule.onNodeWithTag("button_1")
                .performClick()
            composeTestRule.onNodeWithTag("button_9")
                .performClick()
            composeTestRule.onNodeWithTag("button_7")
                .performClick()
            composeTestRule.onNodeWithTag("button_5")
                .performClick()
            composeTestRule.onNodeWithTag("button_9")
                .performClick()
            composeTestRule.onNodeWithTag("button_OK")
                .performClick()

            composeTestRule.waitUntilTimeout(10_000)

            val listAfter = colabDao.all()
            assertEquals(
                1,
                listAfter.size
            )
            val modelAfter1 = listAfter[0]
            assertEquals(
                ColabRoomModel(
                    reg = 18017,
                    name = "RONALDO GOMES"
                ),
                modelAfter1
            )

            server.shutdown()
        }

    @Test
    fun check_not_msg_if_web_service_return_timeout_and_existent_in_table_room()=
        runTest(
            timeout = 60.seconds
        ) {

            val result = """
                {
                    "status": "success",
                    "data": {"reg":0,"name":"NON_INEXISTENT"}
                }
            """.trimIndent()

            val server = MockWebServer()
            server.start()
            server.enqueue(
                MockResponse().setBody(result)
                    .setBodyDelay(10, TimeUnit.SECONDS)
            )
            BaseUrlModuleTest.url = server.url("/").toString()

            hiltRule.inject()

            initialRegister(5)

            whenever(checkNetwork.isConnected()).thenReturn(true)

            colabDao.insertAll(
                listOf(
                    ColabRoomModel(
                        reg = 18017,
                        name = "RONALDO GOMES"
                    ),
                    ColabRoomModel(
                        reg = 19759,
                        name = "ANDERSON DA SILVA DELGADO"
                    )
                )
            )

            setContent()

            composeTestRule.onNodeWithTag("button_1")
                .performClick()
            composeTestRule.onNodeWithTag("button_9")
                .performClick()
            composeTestRule.onNodeWithTag("button_7")
                .performClick()
            composeTestRule.onNodeWithTag("button_5")
                .performClick()
            composeTestRule.onNodeWithTag("button_9")
                .performClick()
            composeTestRule.onNodeWithTag("button_OK")
                .performClick()

            composeTestRule.waitUntilTimeout(20_000)

            val listAfter = colabDao.all()
            assertEquals(
                2,
                listAfter.size
            )

            server.shutdown()
        }

    @Test
    fun check_msg_if_web_service_return_timeout_and_non_existent_in_table_room()=
        runTest(
            timeout = 60.seconds
        ) {

            val result = """
                {
                    "status": "success",
                    "data": {"reg":0,"name":"NON_INEXISTENT"}
                }
            """.trimIndent()

            val server = MockWebServer()
            server.start()
            server.enqueue(
                MockResponse().setBody(result)
                    .setBodyDelay(10, TimeUnit.SECONDS)
            )
            BaseUrlModuleTest.url = server.url("/").toString()

            hiltRule.inject()

            initialRegister(5)

            whenever(checkNetwork.isConnected()).thenReturn(true)

            colabDao.insertAll(
                listOf(
                    ColabRoomModel(
                        reg = 18017,
                        name = "RONALDO GOMES"
                    )
                )
            )

            setContent()

            composeTestRule.onNodeWithTag("button_1")
                .performClick()
            composeTestRule.onNodeWithTag("button_9")
                .performClick()
            composeTestRule.onNodeWithTag("button_7")
                .performClick()
            composeTestRule.onNodeWithTag("button_5")
                .performClick()
            composeTestRule.onNodeWithTag("button_9")
                .performClick()
            composeTestRule.onNodeWithTag("button_OK")
                .performClick()

            composeTestRule.waitUntilTimeout(20_000)

            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertIsDisplayed()
            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertTextEquals("DADO INVÁLIDO! POR FAVOR, VERIFIQUE SE O CAMPO \"MATRIC. MOTORISTA\" FOI DIGITADO CORRETAMENTE OU ATUALIZE OS DADOS PARA VERIFICAR SE OS MESMOS NÃO ESTÃO DESATUALIZADOS.")

            server.shutdown()
        }

    @Test
    fun check_not_msg_if_no_connection_and_existent_in_table_room() =
        runTest {

            whenever(checkNetwork.isConnected()).thenReturn(false)

            hiltRule.inject()

            initialRegister(5)

            colabDao.insertAll(
                listOf(
                    ColabRoomModel(
                        reg = 18017,
                        name = "RONALDO GOMES"
                    ),
                    ColabRoomModel(
                        reg = 19759,
                        name = "ANDERSON DA SILVA DELGADO"
                    )
                )
            )

            setContent()

            composeTestRule.onNodeWithTag("button_1")
                .performClick()
            composeTestRule.onNodeWithTag("button_9")
                .performClick()
            composeTestRule.onNodeWithTag("button_7")
                .performClick()
            composeTestRule.onNodeWithTag("button_5")
                .performClick()
            composeTestRule.onNodeWithTag("button_9")
                .performClick()
            composeTestRule.onNodeWithTag("button_OK")
                .performClick()

            composeTestRule.waitUntilTimeout()

            val listAfter = colabDao.all()
            assertEquals(
                2,
                listAfter.size
            )

        }

    @Test
    fun check_msg_if_no_connection_and_no_existent_in_table_room() =
        runTest {

            whenever(checkNetwork.isConnected()).thenReturn(false)

            hiltRule.inject()

            initialRegister(5)

            colabDao.insertAll(
                listOf(
                    ColabRoomModel(
                        reg = 18017,
                        name = "RONALDO GOMES"
                    )
                )
            )

            setContent()

            composeTestRule.onNodeWithTag("button_1")
                .performClick()
            composeTestRule.onNodeWithTag("button_9")
                .performClick()
            composeTestRule.onNodeWithTag("button_7")
                .performClick()
            composeTestRule.onNodeWithTag("button_5")
                .performClick()
            composeTestRule.onNodeWithTag("button_9")
                .performClick()
            composeTestRule.onNodeWithTag("button_OK")
                .performClick()

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertIsDisplayed()
            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertTextEquals("DADO INVÁLIDO! POR FAVOR, VERIFIQUE SE O CAMPO \"MATRIC. MOTORISTA\" FOI DIGITADO CORRETAMENTE OU ATUALIZE OS DADOS PARA VERIFICAR SE OS MESMOS NÃO ESTÃO DESATUALIZADOS.")

        }
    private fun setContent(){
        composeTestRule.setContent {
            DriverScreen(
                onNavTruck = {},
                onNavPassword = {}
            )
        }
    }

    private suspend fun initialRegister(level: Int = 1) {

        managerSharedPreferencesDatasource.setIdRelease(1)

        if (level == 1) return

        managerSharedPreferencesDatasource.setIdFront(2)

        if (level == 2) return

        releaseDao.insertAll(
            listOf(
                ReleaseRoomModel(
                    id = 1,
                    nroOS = 1,
                    idPropAgr = 1,
                    descPropAgr = "Test1",
                    idFront = 2
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
                    idFront = 2
                ),
            )
        )

        if (level == 3) return

        frontDao.insertAll(
            listOf(
                FrontRoomModel(
                    id = 1,
                    cd = 1,
                    description = "Test1"
                ),
                FrontRoomModel(
                    id = 2,
                    cd = 2,
                    description = "Test2"
                ),
                FrontRoomModel(
                    id = 3,
                    cd = 3,
                    description = "Test3"
                ),
            )
        )

        if (level == 4) return

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

        if (level == 5) return

    }


}
