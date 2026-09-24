package br.com.usinasantafe.cvf.presenter.view.note.truck

import android.content.SharedPreferences
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import br.com.usinasantafe.cav.utils.waitUntilTimeout
import br.com.usinasantafe.cvf.HiltTestActivity
import br.com.usinasantafe.cvf.di.provider.BaseUrlModuleTest
import br.com.usinasantafe.cvf.external.room.dao.DatabaseRoom
import br.com.usinasantafe.cvf.external.room.dao.stable.EquipDao
import br.com.usinasantafe.cvf.external.room.dao.stable.FrontDao
import br.com.usinasantafe.cvf.external.room.dao.stable.ReleaseDao
import br.com.usinasantafe.cvf.external.sharedPreferences.IConfigSharedPreferencesDatasource
import br.com.usinasantafe.cvf.external.sharedPreferences.IHeaderSharedPreferencesDatasource
import br.com.usinasantafe.cvf.external.sharedPreferences.IManagerSharedPreferencesDatasource
import br.com.usinasantafe.cvf.infra.models.room.stable.EquipRoomModel
import br.com.usinasantafe.cvf.infra.models.room.stable.FrontRoomModel
import br.com.usinasantafe.cvf.infra.models.room.stable.ReleaseRoomModel
import br.com.usinasantafe.cvf.infra.models.sharedpreferences.ConfigSharedPreferencesModel
import br.com.usinasantafe.cvf.lib.StatusSend
import br.com.usinasantafe.cvf.lib.TypeEquip
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
class TruckScreenTest {

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
    lateinit var equipDao: EquipDao

    @Inject
    lateinit var db: DatabaseRoom

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    @BindValue
    @JvmField
    val checkNetwork: CheckNetwork = mock()

    @Before
    fun setUp() {
        hiltRule.inject()
        db.clearAllTables()
        sharedPreferences.edit().clear().commit()
        TestConfig.skipPermissionRequest = true
    }

    @After
    fun tearDown() {
        TestConfig.skipPermissionRequest = false
    }

    @Test
    fun check_open_screen_and_title_is_empty_if_manager_table_is_empty() =
        runTest {

            setContent()

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag(TAG_TOP_BAR_TITLE).assertTextEquals("")
            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertDoesNotExist()

        }

    @Test
    fun check_open_screen_and_title_is_empty_if_idFront_is_null() =
        runTest {

            initialRegister()

            setContent()

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag(TAG_TOP_BAR_TITLE).assertTextEquals("")
            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertDoesNotExist()

        }

    @Test
    fun check_open_screen_correct_if_release_table_is_empty() =
        runTest {

            initialRegister(2)

            setContent()

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag(TAG_TOP_BAR_TITLE).assertTextEquals("")
            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertDoesNotExist()

        }

    @Test
    fun check_open_screen_correct_if_front_table_is_empty() =
        runTest {

            initialRegister(3)

            setContent()

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag(TAG_TOP_BAR_TITLE).assertTextEquals("")
            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertDoesNotExist()

        }

    @Test
    fun check_open_screen_correct() =
        runTest {

            initialRegister(4)

            setContent()

            composeTestRule.waitUntilTimeout(5_000)

            composeTestRule.onNodeWithTag(TAG_TOP_BAR_TITLE).assertTextEquals("FRENTE: Test2\nLIBERAÇÃO: 1\nO.S.: 1\nPROPRIEDADE: Test1")

        }

    @Test
    fun check_open_screen_correct_and_reg_driver_is_not_null() =
        runTest {

            initialRegister(4)

            headerSharedPreferencesDatasource.setRegDriver(19759)

            setContent()

            composeTestRule.waitUntilTimeout(5_000)

            composeTestRule.onNodeWithTag(TAG_TOP_BAR_TITLE).assertTextEquals("FRENTE: Test2\nLIBERAÇÃO: 1\nO.S.: 1\nPROPRIEDADE: Test1")

        }

    @Test
    fun check_return_failure_if_config_table_is_null() =
        runTest {

            initialRegister(4)

            whenever(checkNetwork.isConnected()).thenReturn(true)

            setContent()

            composeTestRule.onNodeWithTag("button_2")
                .performClick()
            composeTestRule.onNodeWithTag("button_0")
                .performClick()
            composeTestRule.onNodeWithTag("button_0")
                .performClick()
            composeTestRule.onNodeWithTag("button_OK")
                .performClick()

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertIsDisplayed()
            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertTextContains("idServ is required")

        }

    @Test
    fun check_return_failure_if_service_without_connection() =
        runTest {

            initialRegister(5)

            whenever(checkNetwork.isConnected()).thenReturn(true)

            setContent()

            composeTestRule.onNodeWithTag("button_2")
                .performClick()
            composeTestRule.onNodeWithTag("button_0")
                .performClick()
            composeTestRule.onNodeWithTag("button_0")
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

            initialRegister(5)

            whenever(checkNetwork.isConnected()).thenReturn(true)

            setContent()

            composeTestRule.onNodeWithTag("button_2")
                .performClick()
            composeTestRule.onNodeWithTag("button_0")
                .performClick()
            composeTestRule.onNodeWithTag("button_0")
                .performClick()
            composeTestRule.onNodeWithTag("button_OK")
                .performClick()

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertIsDisplayed()
            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertTextContains("java.lang.Exception: Authorization header is missing")

            server.shutdown()
        }

    @Test
    fun check_return_failure_if_data_web_service_is_incorrect() =
        runTest {

            val result = """
                {
                    "status": "success",
                    "data": {"id":as1adas5,"nro":200,"cdOperClass":1,"descOperClass":"TRUCK","type":1}
                }
            """.trimIndent()

            val server = MockWebServer()
            server.start()
            server.enqueue(
                MockResponse().setBody(result)
            )
            BaseUrlModuleTest.url = server.url("/").toString()

            initialRegister(5)

            whenever(checkNetwork.isConnected()).thenReturn(true)

            setContent()

            composeTestRule.onNodeWithTag("button_2")
                .performClick()
            composeTestRule.onNodeWithTag("button_0")
                .performClick()
            composeTestRule.onNodeWithTag("button_0")
                .performClick()
            composeTestRule.onNodeWithTag("button_OK")
                .performClick()

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertIsDisplayed()
            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertTextContains("com.google.gson.stream.MalformedJsonException")

            server.shutdown()
        }

    @Test
    fun check_add_data_if_data_web_service_is_correct_and_nro_existent_of_web_service() =
        runTest {

            val result = """
                {
                    "status": "success",
                    "data": {"id":10,"nro":200,"cdOperClass":1,"descOperClass":"TRUCK_TEST","type":1}
                }
            """.trimIndent()

            val server = MockWebServer()
            server.start()
            server.enqueue(
                MockResponse().setBody(result)
            )
            BaseUrlModuleTest.url = server.url("/").toString()

            initialRegister(5)

            whenever(checkNetwork.isConnected()).thenReturn(true)

            equipDao.insertAll(
                listOf(
                    EquipRoomModel(
                        id = 1,
                        nro = 100,
                        cdOperClass = 1,
                        descOperClass = "TRUCK_EXISTENT",
                        type = TypeEquip.TRUCK
                    )
                )
            )
            val listBefore = equipDao.all()
            assertEquals(1, listBefore.size)

            setContent()

            composeTestRule.onNodeWithTag("button_2")
                .performClick()
            composeTestRule.onNodeWithTag("button_0")
                .performClick()
            composeTestRule.onNodeWithTag("button_0")
                .performClick()
            composeTestRule.onNodeWithTag("button_OK")
                .performClick()

            composeTestRule.waitUntilTimeout(10_000)

            val listAfter = equipDao.all()
            assertEquals(2, listAfter.size)
            assertEquals("TRUCK_TEST", listAfter.find { it.nro == 200 }?.descOperClass)

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
                    "data": {"id":0,"nro":0,"cdOperClass":0,"descOperClass":"","type":0}
                }
            """.trimIndent()

            val server = MockWebServer()
            server.start()
            server.enqueue(
                MockResponse().setBody(result)
                    .setBodyDelay(10, TimeUnit.SECONDS)
            )
            BaseUrlModuleTest.url = server.url("/").toString()

            initialRegister(5)

            whenever(checkNetwork.isConnected()).thenReturn(true)

            equipDao.insertAll(
                listOf(
                    EquipRoomModel(
                        id = 1,
                        nro = 100,
                        cdOperClass = 1,
                        descOperClass = "TRUCK_EXISTENT",
                        type = TypeEquip.TRUCK
                    )
                )
            )

            setContent()

            composeTestRule.onNodeWithTag("button_2")
                .performClick()
            composeTestRule.onNodeWithTag("button_0")
                .performClick()
            composeTestRule.onNodeWithTag("button_0")
                .performClick()
            composeTestRule.onNodeWithTag("button_OK")
                .performClick()

            composeTestRule.waitUntilTimeout(20_000)

            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertIsDisplayed()
            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertTextEquals("DADO INVÁLIDO! POR FAVOR, VERIFIQUE SE O CAMPO \"NRO. CAMINHÃO\" FOI DIGITADO CORRETAMENTE OU ATUALIZE OS DADOS PARA VERIFICAR SE OS MESMOS NÃO ESTÃO DESATUALIZADOS.")

            server.shutdown()
        }

    @Test
    fun check_msg_if_no_connection_and_no_existent_in_table_room() =
        runTest {

            whenever(checkNetwork.isConnected()).thenReturn(false)

            initialRegister(5)

            equipDao.insertAll(
                listOf(
                    EquipRoomModel(
                        id = 1,
                        nro = 100,
                        cdOperClass = 1,
                        descOperClass = "TRUCK_EXISTENT",
                        type = TypeEquip.TRUCK
                    )
                )
            )

            setContent()

            composeTestRule.onNodeWithTag("button_2")
                .performClick()
            composeTestRule.onNodeWithTag("button_0")
                .performClick()
            composeTestRule.onNodeWithTag("button_0")
                .performClick()
            composeTestRule.onNodeWithTag("button_OK")
                .performClick()

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertIsDisplayed()
            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertTextEquals("DADO INVÁLIDO! POR FAVOR, VERIFIQUE SE O CAMPO \"NRO. CAMINHÃO\" FOI DIGITADO CORRETAMENTE OU ATUALIZE OS DADOS PARA VERIFICAR SE OS MESMOS NÃO ESTÃO DESATUALIZADOS.")

        }

    private fun setContent(){
        composeTestRule.setContent {
            TruckScreen(
                onNavDriver = {},
                onNavPassword = {},
                onNavCart = {}
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
                )
            )
        )

        if (level == 3) return

        frontDao.insertAll(
            listOf(
                FrontRoomModel(
                    id = 2,
                    cd = 2,
                    description = "Test2"
                )
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
