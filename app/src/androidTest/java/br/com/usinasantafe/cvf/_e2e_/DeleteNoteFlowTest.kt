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
import br.com.usinasantafe.cvf.presenter.view.configuration.config.TAG_NUMBER_TEXT_FIELD_CONFIG_SCREEN
import br.com.usinasantafe.cvf.presenter.view.configuration.config.TAG_PASSWORD_TEXT_FIELD_CONFIG_SCREEN
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
class DeleteNoteFlowTest {

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
                    {"id":2,"nro":2,"cdOperClass":2,"descOperClass":"Equip2","type":1}
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
            configSharedPreferencesDatasource.setTokenFCM("TOKEN_TEST")
        }
    }

    @Test
    fun flow() =
        runTest(
            timeout = 10.minutes
        ) {

            Log.d("TestDebug", "Position 01")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag(TAG_NUMBER_TEXT_FIELD_CONFIG_SCREEN)
                .performTextInput("16997417840")
            composeTestRule.onNodeWithTag(TAG_PASSWORD_TEXT_FIELD_CONFIG_SCREEN)
                .performTextInput("12345")
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

            composeTestRule.onNodeWithTag("item_check_box_5")
                .performClick()

            Log.d("TestDebug", "Position 07")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithText("AVANÇAR")
                .performClick()

            Log.d("TestDebug", "Position 08")

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

            Log.d("TestDebug", "Position 09")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("button_CANCEL")
                .performClick()

            Log.d("TestDebug", "Position 10")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("tag_top_bar_delete")
                .performClick()

            Log.d("TestDebug", "Position 11")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("tag_button_yes_alert_dialog_check")
                .performClick()

            Log.d("TestDebug", "Position 12")

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

            Log.d("TestDebug", "Position 13")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("button_2")
                .performClick()
            composeTestRule.onNodeWithTag("button_0")
                .performClick()
            composeTestRule.onNodeWithTag("button_0")
                .performClick()
            composeTestRule.onNodeWithTag("button_OK")
                .performClick()

            Log.d("TestDebug", "Position 14")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("button_CANCEL")
                .performClick()

            Log.d("TestDebug", "Position 15")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("tag_top_bar_delete")
                .performClick()

            Log.d("TestDebug", "Position 16")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("tag_button_yes_alert_dialog_check")
                .performClick()

            Log.d("TestDebug", "Position 17")

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

            Log.d("TestDebug", "Position 18")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("button_2")
                .performClick()
            composeTestRule.onNodeWithTag("button_0")
                .performClick()
            composeTestRule.onNodeWithTag("button_0")
                .performClick()
            composeTestRule.onNodeWithTag("button_OK")
                .performClick()

            Log.d("TestDebug", "Position 19")

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

            Log.d("TestDebug", "Position 20")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("button_CANCEL")
                .performClick()

            Log.d("TestDebug", "Position 21")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("tag_top_bar_delete")
                .performClick()

            Log.d("TestDebug", "Position 22")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("tag_button_yes_alert_dialog_check")
                .performClick()

            Log.d("TestDebug", "Position 23")

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

            Log.d("TestDebug", "Position 24")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("button_2")
                .performClick()
            composeTestRule.onNodeWithTag("button_0")
                .performClick()
            composeTestRule.onNodeWithTag("button_0")
                .performClick()
            composeTestRule.onNodeWithTag("button_OK")
                .performClick()

            Log.d("TestDebug", "Position 25")

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

            Log.d("TestDebug", "Position 26")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("button_2")
                .performClick()
            composeTestRule.onNodeWithTag("button_5")
                .performClick()
            composeTestRule.onNodeWithTag("button_0")
                .performClick()
            composeTestRule.onNodeWithTag("button_OK")
                .performClick()

            Log.d("TestDebug", "Position 27")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("button_CANCEL")
                .performClick()

            Log.d("TestDebug", "Position 28")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("tag_top_bar_delete")
                .performClick()

            Log.d("TestDebug", "Position 29")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("tag_button_yes_alert_dialog_check")
                .performClick()

            Log.d("TestDebug", "Position 30")

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

            Log.d("TestDebug", "Position 31")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("button_2")
                .performClick()
            composeTestRule.onNodeWithTag("button_0")
                .performClick()
            composeTestRule.onNodeWithTag("button_0")
                .performClick()
            composeTestRule.onNodeWithTag("button_OK")
                .performClick()

            Log.d("TestDebug", "Position 32")

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

            Log.d("TestDebug", "Position 33")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("button_2")
                .performClick()
            composeTestRule.onNodeWithTag("button_5")
                .performClick()
            composeTestRule.onNodeWithTag("button_0")
                .performClick()
            composeTestRule.onNodeWithTag("button_OK")
                .performClick()

            Log.d("TestDebug", "Position 34")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("button_OK")
                .performClick()

            Log.d("TestDebug", "Position 35")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithText("APAGAR VIAGEM")
                .performClick()

            Log.d("TestDebug", "Position 36")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("tag_button_yes_alert_dialog_check")
                .performClick()

            Log.d("TestDebug", "Position 37")

            composeTestRule.waitUntilTimeout(20_000)

        }

}