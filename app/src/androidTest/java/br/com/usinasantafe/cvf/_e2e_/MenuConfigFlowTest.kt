package br.com.usinasantafe.cvf._e2e_

import android.util.Log
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import br.com.usinasantafe.cav.utils.waitUntilTimeout
import br.com.usinasantafe.cvf.di.provider.BaseUrlModuleTest
import br.com.usinasantafe.cvf.external.room.dao.stable.ColabDao
import br.com.usinasantafe.cvf.external.room.dao.stable.EquipDao
import br.com.usinasantafe.cvf.external.room.dao.stable.FrontDao
import br.com.usinasantafe.cvf.external.room.dao.stable.ReleaseDao
import br.com.usinasantafe.cvf.infra.datasource.sharedpreferences.ConfigSharedPreferencesDatasource
import br.com.usinasantafe.cvf.infra.datasource.sharedpreferences.ManagerSharedPreferencesDatasource
import br.com.usinasantafe.cvf.infra.models.sharedpreferences.ConfigSharedPreferencesModel
import br.com.usinasantafe.cvf.lib.StatusSend
import br.com.usinasantafe.cvf.lib.WEB_ALL_COLAB
import br.com.usinasantafe.cvf.lib.WEB_ALL_EQUIP
import br.com.usinasantafe.cvf.lib.WEB_ALL_FRONT
import br.com.usinasantafe.cvf.lib.WEB_ALL_RELEASE
import br.com.usinasantafe.cvf.lib.WEB_CHECK_NRO_EQUIP
import br.com.usinasantafe.cvf.lib.WEB_CHECK_REG_COLAB
import br.com.usinasantafe.cvf.lib.WEB_SAVE_MANAGER
import br.com.usinasantafe.cvf.lib.WEB_SAVE_TOKEN
import br.com.usinasantafe.cvf.presenter.MainActivity
import br.com.usinasantafe.cvf.presenter.theme.TAG_BUTTON_OK_ALERT_DIALOG_SIMPLE
import br.com.usinasantafe.cvf.presenter.view.configuration.password.TAG_PASSWORD_TEXT_FIELD_SCREEN
import br.com.usinasantafe.cvf.utils.TestConfig
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import kotlin.time.Duration.Companion.minutes

@HiltAndroidTest
class MenuConfigFlowTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Inject
    lateinit var configSharedPreferencesDatasource: ConfigSharedPreferencesDatasource

    @Inject
    lateinit var colabDao: ColabDao

    @Inject
    lateinit var equipDao: EquipDao

    @Inject
    lateinit var frontDao: FrontDao

    @Inject
    lateinit var releaseDao: ReleaseDao

    @Inject
    lateinit var managerSharedPreferencesDatasource: ManagerSharedPreferencesDatasource

    companion object {

        private lateinit var mockWebServer: MockWebServer

        private val resultToken = """
            {
                "status": "success",
                "idServ": 1
            }
        """.trimIndent()

        private val resultColab = """
            {
                "status": "success",
                "data": [
                    {"reg":19759,"name":"ANDERSON DA SILVA DELGADO"},
                    {"reg":18017,"name":"RONALDO GOMES"}
                ]
            }
        """.trimIndent()

        private val resultEquip = """
            {
                "status": "success",
                "data": [
                    {"id":1,"nro":1,"cdOperClass":1,"descOperClass":"Equip1","type":1},
                    {"id":2,"nro":2,"cdOperClass":2,"descOperClass":"Equip2","type":1},
                    {"id":10,"nro":200,"cdOperClass":1,"descOperClass":"TRUCK","type":1},
                    {"id":13,"nro":1500,"cdOperClass":5,"descOperClass":"CART1","type":2},
                    {"id":104,"nro":250,"cdOperClass":5,"descOperClass":"CART2","type":2}
                ]
            }
        """.trimIndent()

        private val resultFront = """
            {
                "status": "success",
                "data": [
                    {"id":1,"cd":1,"description":"Front1"},
                    {"id":3,"cd":3,"description":"Front3"},
                    {"id":2,"cd":2,"description":"Front2"}
                ]
            }
        """.trimIndent()

        val resultRelease = """
            {
                "status": "success",
                "data": [
                  {"id":1,"nroOS":123456,"idPropAgr":1,"descPropAgr":"Release1","idFront":1},
                  {"id":2,"nroOS":234567,"idPropAgr":2,"descPropAgr":"Release2","idFront":2},
                  {"id":3,"nroOS":345678,"idPropAgr":3,"descPropAgr":"Release3","idFront":3},
                  {"id":4,"nroOS":456789,"idPropAgr":4,"descPropAgr":"Release4","idFront":2},
                  {"id":5,"nroOS":225687,"idPropAgr":2,"descPropAgr":"Release5","idFront":3}
                ]
            }
        """.trimIndent()

        private val resultManager = """
            {
                "status": "success",
                "idServ": 1
            }
        """.trimIndent()


        private val resultColabSingle = """
            {
                "status": "success",
                "data": {"reg":123456,"name":"João da Silva"}
            }
        """.trimIndent()

        private val resultTruckSingle = """
            {
                "status": "success",
                "data": {"id":10,"nro":200,"cdOperClass":1,"descOperClass":"TRUCK","type":1}
            }
        """.trimIndent()

        private val resultCart1Single = """
            {
                "status": "success",
                "data": {"id":13,"nro":1500,"cdOperClass":5,"descOperClass":"CART1","type":2}
            }
        """.trimIndent()

        private val resultCart2Single = """
            {
                "status": "success",
                "data": {"id":104,"nro":250,"cdOperClass":5,"descOperClass":"CART2","type":2}
            }
        """.trimIndent()

        @BeforeClass
        @JvmStatic
        fun setupClass() {

            val dispatcherSuccess: Dispatcher = object : Dispatcher() {
                @Throws(InterruptedException::class)
                override fun dispatch(request: RecordedRequest): MockResponse {
                    return when (request.path) {
                        "/$WEB_SAVE_TOKEN" -> MockResponse().setBody(resultToken)
                        "/$WEB_ALL_COLAB" -> MockResponse().setBody(resultColab)
                        "/$WEB_ALL_EQUIP" -> MockResponse().setBody(resultEquip)
                        "/$WEB_ALL_FRONT" -> MockResponse().setBody(resultFront)
                        "/$WEB_ALL_RELEASE" -> MockResponse().setBody(resultRelease)
                        "/$WEB_SAVE_MANAGER" -> MockResponse().setBody(resultManager)
                        "/$WEB_CHECK_REG_COLAB" -> MockResponse().setBody(resultColabSingle)
                        "/$WEB_CHECK_NRO_EQUIP" -> {
                            val body = request.body.readUtf8()
                            when {
                                body.contains("200") -> MockResponse().setBody(resultTruckSingle)
                                body.contains("1500") -> MockResponse().setBody(resultCart1Single)
                                body.contains("250") -> MockResponse().setBody(resultCart2Single)
                                else -> MockResponse().setResponseCode(404)
                            }
                        }
                        else -> MockResponse().setResponseCode(404)
                    }
                }
            }

            mockWebServer = MockWebServer()
            mockWebServer.dispatcher = dispatcherSuccess
            mockWebServer.start()

            BaseUrlModuleTest.url = mockWebServer.url("/").toString()
        }

        @AfterClass
        @JvmStatic
        fun tearDownClass() {
            mockWebServer.shutdown()
        }

    }

    @Before
    fun setup() {
        hiltRule.inject()
        TestConfig.skipPermissionRequest = true
        runBlocking {
            configSharedPreferencesDatasource.save(
                ConfigSharedPreferencesModel(
                    number = 16997417840,
                    password = "12345",
                    idServ = 1,
                    version = "1.0",
                    statusSend = StatusSend.STARTED,
                    flagUpdate = false,
                    tokenFCM = "TOKEN_FCM"
                )
            )
        }
    }

    @Test
    fun flow() =
        runTest(
            timeout = 10.minutes
        ) {

            Log.d("TestDebug", "Position 01")

            composeTestRule.waitUntilTimeout()

//            composeTestRule.onNodeWithTag(TAG_NUMBER_TEXT_FIELD_CONFIG_SCREEN)
//                .performTextInput("16997417840")
//            composeTestRule.onNodeWithTag(TAG_PASSWORD_TEXT_FIELD_CONFIG_SCREEN)
//                .performTextInput("12345")
            composeTestRule.onNodeWithText("SALVAR")
                .performClick()

            Log.d("TestDebug", "Position 02")

            composeTestRule.waitUntilTimeout()

            Log.d("TestDebug", "Position 03")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag(TAG_BUTTON_OK_ALERT_DIALOG_SIMPLE)
                .performClick()

            Log.d("TestDebug", "Position 04")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("item_check_box_3")
                .performClick()

            Log.d("TestDebug", "Position 05")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithText("AVANÇAR")
                .performClick()

            Log.d("TestDebug", "Position 06")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("item_check_box_3")
                .performClick()

            Log.d("TestDebug", "Position 07")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithText("AVANÇAR")
                .performClick()

            Log.d("TestDebug", "Position 08")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("tag_top_bar_menu")
                .performClick()

            Log.d("TestDebug", "Position 09")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("tag_top_bar_menu_item_config")
                .performClick()

            Log.d("TestDebug", "Position 10")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithText("CANCELAR")
                .performClick()

            Log.d("TestDebug", "Position 11")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("tag_top_bar_menu")
                .performClick()

            Log.d("TestDebug", "Position 12")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("tag_top_bar_menu_item_config")
                .performClick()

            Log.d("TestDebug", "Position 13")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag(TAG_PASSWORD_TEXT_FIELD_SCREEN)
                .performTextInput("12345")
            composeTestRule.onNodeWithText("OK")
                .performClick()

            Log.d("TestDebug", "Position 14")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithText("RETORNAR")
                .performClick()

            Log.d("TestDebug", "Position 15")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("tag_top_bar_menu")
                .performClick()

            Log.d("TestDebug", "Position 16")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("tag_top_bar_menu_item_config")
                .performClick()

            Log.d("TestDebug", "Position 17")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag(TAG_PASSWORD_TEXT_FIELD_SCREEN)
                .performTextInput("12345")
            composeTestRule.onNodeWithText("OK")
                .performClick()

            Log.d("TestDebug", "Position 18")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithText("SALVAR")
                .performClick()

            Log.d("TestDebug", "Position 19")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag(TAG_BUTTON_OK_ALERT_DIALOG_SIMPLE)
                .performClick()

            Log.d("TestDebug", "Position 20")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithText("RETORNAR")
                .performClick()

            Log.d("TestDebug", "Position 21")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithText("SALVAR")
                .performClick()

            Log.d("TestDebug", "Position 22")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag(TAG_BUTTON_OK_ALERT_DIALOG_SIMPLE)
                .performClick()

            Log.d("TestDebug", "Position 23")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("item_check_box_3")
                .performClick()

            Log.d("TestDebug", "Position 24")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithText("AVANÇAR")
                .performClick()

            Log.d("TestDebug", "Position 25")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("item_check_box_5")
                .performClick()

            Log.d("TestDebug", "Position 26")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithText("AVANÇAR")
                .performClick()

            Log.d("TestDebug", "Position 27")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("tag_top_bar_menu")
                .performClick()

            Log.d("TestDebug", "Position 28")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("tag_top_bar_menu_item_front")
                .performClick()

            Log.d("TestDebug", "Position 29")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithText("CANCELAR")
                .performClick()

            Log.d("TestDebug", "Position 30")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("tag_top_bar_menu")
                .performClick()

            Log.d("TestDebug", "Position 31")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("tag_top_bar_menu_item_front")
                .performClick()

            Log.d("TestDebug", "Position 32")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag(TAG_PASSWORD_TEXT_FIELD_SCREEN)
                .performTextInput("12345")
            composeTestRule.onNodeWithText("OK")
                .performClick()

            Log.d("TestDebug", "Position 33")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithText("RETORNAR")
                .performClick()

            Log.d("TestDebug", "Position 34")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("tag_top_bar_menu")
                .performClick()

            Log.d("TestDebug", "Position 35")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("tag_top_bar_menu_item_front")
                .performClick()

            Log.d("TestDebug", "Position 36")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag(TAG_PASSWORD_TEXT_FIELD_SCREEN)
                .performTextInput("12345")
            composeTestRule.onNodeWithText("OK")
                .performClick()

            Log.d("TestDebug", "Position 37")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithText("AVANÇAR")
                .performClick()

            Log.d("TestDebug", "Position 38")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithText("RETORNAR")
                .performClick()

            Log.d("TestDebug", "Position 39")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithText("RETORNAR")
                .performClick()

            Log.d("TestDebug", "Position 40")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag(TAG_BUTTON_OK_ALERT_DIALOG_SIMPLE)
                .performClick()

            Log.d("TestDebug", "Position 42")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithText("AVANÇAR")
                .performClick()

            Log.d("TestDebug", "Position 43")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("item_check_box_3")
                .performClick()

            Log.d("TestDebug", "Position 44")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithText("AVANÇAR")
                .performClick()

            Log.d("TestDebug", "Position 45")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("tag_top_bar_menu")
                .performClick()

            Log.d("TestDebug", "Position 46")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("tag_top_bar_menu_item_release")
                .performClick()

            Log.d("TestDebug", "Position 47")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithText("CANCELAR")
                .performClick()

            Log.d("TestDebug", "Position 48")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("tag_top_bar_menu")
                .performClick()

            Log.d("TestDebug", "Position 49")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("tag_top_bar_menu_item_release")
                .performClick()

            Log.d("TestDebug", "Position 50")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag(TAG_PASSWORD_TEXT_FIELD_SCREEN)
                .performTextInput("12345")
            composeTestRule.onNodeWithText("OK")
                .performClick()

            Log.d("TestDebug", "Position 51")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithText("RETORNAR")
                .performClick()

            Log.d("TestDebug", "Position 52")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("tag_top_bar_menu")
                .performClick()

            Log.d("TestDebug", "Position 53")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("tag_top_bar_menu_item_release")
                .performClick()

            Log.d("TestDebug", "Position 54")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag(TAG_PASSWORD_TEXT_FIELD_SCREEN)
                .performTextInput("12345")
            composeTestRule.onNodeWithText("OK")
                .performClick()

            Log.d("TestDebug", "Position 55")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("item_check_box_5")
                .performClick()

            Log.d("TestDebug", "Position 56")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithText("AVANÇAR")
                .performClick()

            Log.d("TestDebug", "Position 57")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("button_1")
                .performClick()
            composeTestRule.onNodeWithTag("button_2")
                .performClick()
            composeTestRule.onNodeWithTag("button_3")
                .performClick()
            composeTestRule.onNodeWithTag("button_4")
                .performClick()
            composeTestRule.onNodeWithTag("button_5")
                .performClick()
            composeTestRule.onNodeWithTag("button_6")
                .performClick()
            composeTestRule.onNodeWithTag("button_OK")
                .performClick()

            Log.d("TestDebug", "Position 58")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("tag_top_bar_menu")
                .performClick()

            Log.d("TestDebug", "Position 59")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("tag_top_bar_menu_item_config")
                .performClick()

            Log.d("TestDebug", "Position 60")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithText("CANCELAR")
                .performClick()

            Log.d("TestDebug", "Position 61")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("tag_top_bar_menu")
                .performClick()

            Log.d("TestDebug", "Position 62")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("tag_top_bar_menu_item_config")
                .performClick()

            Log.d("TestDebug", "Position 63")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag(TAG_PASSWORD_TEXT_FIELD_SCREEN)
                .performTextInput("12345")
            composeTestRule.onNodeWithText("OK")
                .performClick()

            Log.d("TestDebug", "Position 64")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithText("RETORNAR")
                .performClick()

            Log.d("TestDebug", "Position 65")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("tag_top_bar_menu")
                .performClick()

            Log.d("TestDebug", "Position 66")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("tag_top_bar_menu_item_config")
                .performClick()

            Log.d("TestDebug", "Position 67")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag(TAG_PASSWORD_TEXT_FIELD_SCREEN)
                .performTextInput("12345")
            composeTestRule.onNodeWithText("OK")
                .performClick()

            Log.d("TestDebug", "Position 68")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithText("SALVAR")
                .performClick()

            Log.d("TestDebug", "Position 69")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag(TAG_BUTTON_OK_ALERT_DIALOG_SIMPLE)
                .performClick()

            Log.d("TestDebug", "Position 70")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithText("RETORNAR")
                .performClick()

            Log.d("TestDebug", "Position 71")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithText("SALVAR")
                .performClick()

            Log.d("TestDebug", "Position 72")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag(TAG_BUTTON_OK_ALERT_DIALOG_SIMPLE)
                .performClick()

            Log.d("TestDebug", "Position 73")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("item_check_box_3")
                .performClick()

            Log.d("TestDebug", "Position 74")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithText("AVANÇAR")
                .performClick()

            Log.d("TestDebug", "Position 75")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("item_check_box_5")
                .performClick()

            Log.d("TestDebug", "Position 76")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithText("AVANÇAR")
                .performClick()

            Log.d("TestDebug", "Position 77")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("tag_top_bar_menu")
                .performClick()

            Log.d("TestDebug", "Position 78")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("tag_top_bar_menu_item_front")
                .performClick()

            Log.d("TestDebug", "Position 79")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithText("CANCELAR")
                .performClick()

            Log.d("TestDebug", "Position 80")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("tag_top_bar_menu")
                .performClick()

            Log.d("TestDebug", "Position 81")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("tag_top_bar_menu_item_front")
                .performClick()

            Log.d("TestDebug", "Position 82")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag(TAG_PASSWORD_TEXT_FIELD_SCREEN)
                .performTextInput("12345")
            composeTestRule.onNodeWithText("OK")
                .performClick()

            Log.d("TestDebug", "Position 83")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithText("RETORNAR")
                .performClick()

            Log.d("TestDebug", "Position 84")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("tag_top_bar_menu")
                .performClick()

            Log.d("TestDebug", "Position 85")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("tag_top_bar_menu_item_front")
                .performClick()

            Log.d("TestDebug", "Position 86")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag(TAG_PASSWORD_TEXT_FIELD_SCREEN)
                .performTextInput("12345")
            composeTestRule.onNodeWithText("OK")
                .performClick()

            Log.d("TestDebug", "Position 87")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithText("AVANÇAR")
                .performClick()

            Log.d("TestDebug", "Position 88")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithText("RETORNAR")
                .performClick()

            Log.d("TestDebug", "Position 89")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithText("RETORNAR")
                .performClick()

            Log.d("TestDebug", "Position 90")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag(TAG_BUTTON_OK_ALERT_DIALOG_SIMPLE)
                .performClick()

            Log.d("TestDebug", "Position 91")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithText("AVANÇAR")
                .performClick()

            Log.d("TestDebug", "Position 92")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("item_check_box_3")
                .performClick()

            Log.d("TestDebug", "Position 93")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithText("AVANÇAR")
                .performClick()

            Log.d("TestDebug", "Position 94")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("tag_top_bar_menu")
                .performClick()

            Log.d("TestDebug", "Position 95")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("tag_top_bar_menu_item_release")
                .performClick()

            Log.d("TestDebug", "Position 96")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithText("CANCELAR")
                .performClick()

            Log.d("TestDebug", "Position 97")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("tag_top_bar_menu")
                .performClick()

            Log.d("TestDebug", "Position 98")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("tag_top_bar_menu_item_release")
                .performClick()

            Log.d("TestDebug", "Position 99")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag(TAG_PASSWORD_TEXT_FIELD_SCREEN)
                .performTextInput("12345")
            composeTestRule.onNodeWithText("OK")
                .performClick()

            Log.d("TestDebug", "Position 100")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithText("RETORNAR")
                .performClick()

            Log.d("TestDebug", "Position 101")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("tag_top_bar_menu")
                .performClick()

            Log.d("TestDebug", "Position 102")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("tag_top_bar_menu_item_release")
                .performClick()

            Log.d("TestDebug", "Position 103")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag(TAG_PASSWORD_TEXT_FIELD_SCREEN)
                .performTextInput("12345")
            composeTestRule.onNodeWithText("OK")
                .performClick()

            Log.d("TestDebug", "Position 104")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("item_check_box_5")
                .performClick()

            Log.d("TestDebug", "Position 105")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithText("AVANÇAR")
                .performClick()

            Log.d("TestDebug", "Position 106")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("button_2")
                .performClick()
            composeTestRule.onNodeWithTag("button_0")
                .performClick()
            composeTestRule.onNodeWithTag("button_0")
                .performClick()
            composeTestRule.onNodeWithTag("button_OK")
                .performClick()

            Log.d("TestDebug", "Position 107")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("tag_top_bar_menu")
                .performClick()

            Log.d("TestDebug", "Position 108")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("tag_top_bar_menu_item_config")
                .performClick()

            Log.d("TestDebug", "Position 109")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithText("CANCELAR")
                .performClick()

            Log.d("TestDebug", "Position 110")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("tag_top_bar_menu")
                .performClick()

            Log.d("TestDebug", "Position 111")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("tag_top_bar_menu_item_config")
                .performClick()

            Log.d("TestDebug", "Position 112")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag(TAG_PASSWORD_TEXT_FIELD_SCREEN)
                .performTextInput("12345")
            composeTestRule.onNodeWithText("OK")
                .performClick()

            Log.d("TestDebug", "Position 113")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithText("RETORNAR")
                .performClick()

            Log.d("TestDebug", "Position 114")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("tag_top_bar_menu")
                .performClick()

            Log.d("TestDebug", "Position 115")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("tag_top_bar_menu_item_config")
                .performClick()

            Log.d("TestDebug", "Position 116")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag(TAG_PASSWORD_TEXT_FIELD_SCREEN)
                .performTextInput("12345")
            composeTestRule.onNodeWithText("OK")
                .performClick()

            Log.d("TestDebug", "Position 117")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithText("SALVAR")
                .performClick()

            Log.d("TestDebug", "Position 118")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag(TAG_BUTTON_OK_ALERT_DIALOG_SIMPLE)
                .performClick()

            Log.d("TestDebug", "Position 119")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithText("RETORNAR")
                .performClick()

            Log.d("TestDebug", "Position 120")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithText("SALVAR")
                .performClick()

            Log.d("TestDebug", "Position 121")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag(TAG_BUTTON_OK_ALERT_DIALOG_SIMPLE)
                .performClick()

            Log.d("TestDebug", "Position 122")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("item_check_box_3")
                .performClick()

            Log.d("TestDebug", "Position 123")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithText("AVANÇAR")
                .performClick()

            Log.d("TestDebug", "Position 124")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("item_check_box_5")
                .performClick()

            Log.d("TestDebug", "Position 125")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithText("AVANÇAR")
                .performClick()

            Log.d("TestDebug", "Position 126")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("tag_top_bar_menu")
                .performClick()

            Log.d("TestDebug", "Position 127")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("tag_top_bar_menu_item_front")
                .performClick()

            Log.d("TestDebug", "Position 128")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithText("CANCELAR")
                .performClick()

            Log.d("TestDebug", "Position 129")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("tag_top_bar_menu")
                .performClick()

            Log.d("TestDebug", "Position 130")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("tag_top_bar_menu_item_front")
                .performClick()

            Log.d("TestDebug", "Position 131")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag(TAG_PASSWORD_TEXT_FIELD_SCREEN)
                .performTextInput("12345")
            composeTestRule.onNodeWithText("OK")
                .performClick()

            Log.d("TestDebug", "Position 132")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithText("RETORNAR")
                .performClick()

            Log.d("TestDebug", "Position 133")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("tag_top_bar_menu")
                .performClick()

            Log.d("TestDebug", "Position 134")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("tag_top_bar_menu_item_front")
                .performClick()

            Log.d("TestDebug", "Position 135")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag(TAG_PASSWORD_TEXT_FIELD_SCREEN)
                .performTextInput("12345")
            composeTestRule.onNodeWithText("OK")
                .performClick()

            Log.d("TestDebug", "Position 136")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithText("AVANÇAR")
                .performClick()

            Log.d("TestDebug", "Position 137")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithText("RETORNAR")
                .performClick()

            Log.d("TestDebug", "Position 138")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithText("RETORNAR")
                .performClick()

            Log.d("TestDebug", "Position 139")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag(TAG_BUTTON_OK_ALERT_DIALOG_SIMPLE)
                .performClick()

            Log.d("TestDebug", "Position 140")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithText("AVANÇAR")
                .performClick()

            Log.d("TestDebug", "Position 141")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("item_check_box_3")
                .performClick()

            Log.d("TestDebug", "Position 142")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithText("AVANÇAR")
                .performClick()

            Log.d("TestDebug", "Position 143")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("tag_top_bar_menu")
                .performClick()

            Log.d("TestDebug", "Position 144")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("tag_top_bar_menu_item_release")
                .performClick()

            Log.d("TestDebug", "Position 145")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithText("CANCELAR")
                .performClick()

            Log.d("TestDebug", "Position 146")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("tag_top_bar_menu")
                .performClick()

            Log.d("TestDebug", "Position 147")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("tag_top_bar_menu_item_release")
                .performClick()

            Log.d("TestDebug", "Position 148")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag(TAG_PASSWORD_TEXT_FIELD_SCREEN)
                .performTextInput("12345")
            composeTestRule.onNodeWithText("OK")
                .performClick()

            Log.d("TestDebug", "Position 149")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithText("RETORNAR")
                .performClick()

            Log.d("TestDebug", "Position 150")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("tag_top_bar_menu")
                .performClick()

            Log.d("TestDebug", "Position 151")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("tag_top_bar_menu_item_release")
                .performClick()

            Log.d("TestDebug", "Position 152")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag(TAG_PASSWORD_TEXT_FIELD_SCREEN)
                .performTextInput("12345")
            composeTestRule.onNodeWithText("OK")
                .performClick()

            Log.d("TestDebug", "Position 153")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("item_check_box_5")
                .performClick()

            Log.d("TestDebug", "Position 154")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithText("AVANÇAR")
                .performClick()

            Log.d("TestDebug", "Position 155")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("button_1")
                .performClick()
            composeTestRule.onNodeWithTag("button_5")
                .performClick()
            composeTestRule.onNodeWithTag("button_0")
                .performClick()
            composeTestRule.onNodeWithTag("button_0")
                .performClick()
            composeTestRule.onNodeWithTag("button_OK")
                .performClick()

            Log.d("TestDebug", "Position 156")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("tag_top_bar_menu")
                .performClick()

            Log.d("TestDebug", "Position 157")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("tag_top_bar_menu_item_config")
                .performClick()

            Log.d("TestDebug", "Position 158")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithText("CANCELAR")
                .performClick()

            Log.d("TestDebug", "Position 159")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("tag_top_bar_menu")
                .performClick()

            Log.d("TestDebug", "Position 160")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("tag_top_bar_menu_item_config")
                .performClick()

            Log.d("TestDebug", "Position 161")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag(TAG_PASSWORD_TEXT_FIELD_SCREEN)
                .performTextInput("12345")
            composeTestRule.onNodeWithText("OK")
                .performClick()

            Log.d("TestDebug", "Position 162")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithText("RETORNAR")
                .performClick()

            Log.d("TestDebug", "Position 163")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("tag_top_bar_menu")
                .performClick()

            Log.d("TestDebug", "Position 164")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("tag_top_bar_menu_item_config")
                .performClick()

            Log.d("TestDebug", "Position 165")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag(TAG_PASSWORD_TEXT_FIELD_SCREEN)
                .performTextInput("12345")
            composeTestRule.onNodeWithText("OK")
                .performClick()

            Log.d("TestDebug", "Position 166")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithText("SALVAR")
                .performClick()

            Log.d("TestDebug", "Position 167")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag(TAG_BUTTON_OK_ALERT_DIALOG_SIMPLE)
                .performClick()

            Log.d("TestDebug", "Position 168")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithText("RETORNAR")
                .performClick()

            Log.d("TestDebug", "Position 169")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithText("SALVAR")
                .performClick()

            Log.d("TestDebug", "Position 170")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag(TAG_BUTTON_OK_ALERT_DIALOG_SIMPLE)
                .performClick()

            Log.d("TestDebug", "Position 171")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("item_check_box_3")
                .performClick()

            Log.d("TestDebug", "Position 172")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithText("AVANÇAR")
                .performClick()

            Log.d("TestDebug", "Position 173")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("item_check_box_5")
                .performClick()

            Log.d("TestDebug", "Position 174")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithText("AVANÇAR")
                .performClick()

            Log.d("TestDebug", "Position 175")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("tag_top_bar_menu")
                .performClick()

            Log.d("TestDebug", "Position 176")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("tag_top_bar_menu_item_front")
                .performClick()

            Log.d("TestDebug", "Position 177")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithText("CANCELAR")
                .performClick()

            Log.d("TestDebug", "Position 178")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("tag_top_bar_menu")
                .performClick()

            Log.d("TestDebug", "Position 179")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("tag_top_bar_menu_item_front")
                .performClick()

            Log.d("TestDebug", "Position 180")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag(TAG_PASSWORD_TEXT_FIELD_SCREEN)
                .performTextInput("12345")
            composeTestRule.onNodeWithText("OK")
                .performClick()

            Log.d("TestDebug", "Position 181")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithText("RETORNAR")
                .performClick()

            Log.d("TestDebug", "Position 182")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("tag_top_bar_menu")
                .performClick()

            Log.d("TestDebug", "Position 183")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("tag_top_bar_menu_item_front")
                .performClick()

            Log.d("TestDebug", "Position 184")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag(TAG_PASSWORD_TEXT_FIELD_SCREEN)
                .performTextInput("12345")
            composeTestRule.onNodeWithText("OK")
                .performClick()

            Log.d("TestDebug", "Position 185")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithText("AVANÇAR")
                .performClick()

            Log.d("TestDebug", "Position 186")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithText("RETORNAR")
                .performClick()

            Log.d("TestDebug", "Position 187")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithText("RETORNAR")
                .performClick()

            Log.d("TestDebug", "Position 188")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag(TAG_BUTTON_OK_ALERT_DIALOG_SIMPLE)
                .performClick()

            Log.d("TestDebug", "Position 189")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithText("AVANÇAR")
                .performClick()

            Log.d("TestDebug", "Position 190")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("item_check_box_3")
                .performClick()

            Log.d("TestDebug", "Position 191")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithText("AVANÇAR")
                .performClick()

            Log.d("TestDebug", "Position 192")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("tag_top_bar_menu")
                .performClick()

            Log.d("TestDebug", "Position 193")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("tag_top_bar_menu_item_release")
                .performClick()

            Log.d("TestDebug", "Position 194")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithText("CANCELAR")
                .performClick()

            Log.d("TestDebug", "Position 195")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("tag_top_bar_menu")
                .performClick()

            Log.d("TestDebug", "Position 196")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("tag_top_bar_menu_item_release")
                .performClick()

            Log.d("TestDebug", "Position 197")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag(TAG_PASSWORD_TEXT_FIELD_SCREEN)
                .performTextInput("12345")
            composeTestRule.onNodeWithText("OK")
                .performClick()

            Log.d("TestDebug", "Position 198")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithText("RETORNAR")
                .performClick()

            Log.d("TestDebug", "Position 199")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("tag_top_bar_menu")
                .performClick()

            Log.d("TestDebug", "Position 200")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("tag_top_bar_menu_item_release")
                .performClick()

            Log.d("TestDebug", "Position 201")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag(TAG_PASSWORD_TEXT_FIELD_SCREEN)
                .performTextInput("12345")
            composeTestRule.onNodeWithText("OK")
                .performClick()

            Log.d("TestDebug", "Position 202")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("item_check_box_5")
                .performClick()

            Log.d("TestDebug", "Position 203")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithText("AVANÇAR")
                .performClick()

            Log.d("TestDebug", "Position 204")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("button_2")
                .performClick()
            composeTestRule.onNodeWithTag("button_5")
                .performClick()
            composeTestRule.onNodeWithTag("button_0")
                .performClick()
            composeTestRule.onNodeWithTag("button_OK")
                .performClick()

            Log.d("TestDebug", "Position 205")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("button_OK")
                .performClick()

            Log.d("TestDebug", "Position 206")

            composeTestRule.waitUntilTimeout(10_000)

        }


}