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
import br.com.usinasantafe.cvf.infra.models.room.stable.ColabRoomModel
import br.com.usinasantafe.cvf.infra.models.room.stable.EquipRoomModel
import br.com.usinasantafe.cvf.infra.models.room.stable.FrontRoomModel
import br.com.usinasantafe.cvf.infra.models.room.stable.ReleaseRoomModel
import br.com.usinasantafe.cvf.infra.models.sharedpreferences.ConfigSharedPreferencesModel
import br.com.usinasantafe.cvf.lib.StatusSend
import br.com.usinasantafe.cvf.lib.TypeEquip
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
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
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
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.minutes

@HiltAndroidTest
class NoteFlowTest {

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

            asserts()

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

            composeTestRule.onNodeWithTag("button_2")
                .performClick()
            composeTestRule.onNodeWithTag("button_0")
                .performClick()
            composeTestRule.onNodeWithTag("button_0")
                .performClick()
            composeTestRule.onNodeWithTag("button_OK")
                .performClick()

            Log.d("TestDebug", "Position 10")

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

            Log.d("TestDebug", "Position 11")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("button_2")
                .performClick()
            composeTestRule.onNodeWithTag("button_5")
                .performClick()
            composeTestRule.onNodeWithTag("button_0")
                .performClick()
            composeTestRule.onNodeWithTag("button_OK")
                .performClick()

            Log.d("TestDebug", "Position 12")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("button_OK")
                .performClick()

            Log.d("TestDebug", "Position 05")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithText("FINALIZAR VIAGEM")
                .performClick()

            composeTestRule.waitUntilTimeout(20_000)

        }

    private suspend fun asserts() {

        val resultGet = configSharedPreferencesDatasource.get()
        assertEquals(
            resultGet.isSuccess,
            true
        )
        val model = resultGet.getOrNull()!!
        assertEquals(
            ConfigSharedPreferencesModel(
                number = 16997417840,
                password = "12345",
                idServ = 1,
                version = "1.0",
                statusSend = StatusSend.SENT,
                flagUpdate = true
            ),
            model
        )

        val colabRoomModelList = colabDao.all()
        assertEquals(
            2,
            colabRoomModelList.size
        )
        val colabRoomModel1 = colabRoomModelList[0]
        assertEquals(
            ColabRoomModel(
                reg = 18017,
                name = "RONALDO GOMES"
            ),
            colabRoomModel1
        )
        val colabRoomModel2 = colabRoomModelList[1]
        assertEquals(
            ColabRoomModel(
                reg = 19759,
                name = "ANDERSON DA SILVA DELGADO"
            ),
            colabRoomModel2
        )

        val equipRoomModelList = equipDao.all()
        assertEquals(
            2,
            equipRoomModelList.size
        )
        val equipRoomModel1 = equipRoomModelList[0]
        assertEquals(
            EquipRoomModel(
                id = 1,
                nro = 1,
                cdOperClass = 1,
                descOperClass = "Equip1",
                type = TypeEquip.TRUCK
            ),
            equipRoomModel1
        )
        val equipRoomModel2 = equipRoomModelList[1]
        assertEquals(
            EquipRoomModel(
                id = 2,
                nro = 2,
                cdOperClass = 2,
                descOperClass = "Equip2",
                type = TypeEquip.TRUCK
            ),
            equipRoomModel2
        )

        val frontRoomModelList = frontDao.all()
        assertEquals(
            3,
            frontRoomModelList.size
        )
        val frontRoomModel1 = frontRoomModelList[0]
        assertEquals(
            FrontRoomModel(
                id = 1,
                cd = 1,
                description = "Front1"
            ),
            frontRoomModel1
        )
        val frontRoomModel2 = frontRoomModelList[1]
        assertEquals(
            FrontRoomModel(
                id = 2,
                cd = 2,
                description = "Front2"
            ),
            frontRoomModel2
        )
        val frontRoomModel3 = frontRoomModelList[2]
        assertEquals(
            FrontRoomModel(
                id = 3,
                cd = 3,
                description = "Front3"
            ),
            frontRoomModel3
        )

        val releaseRoomModelList = releaseDao.all()
        assertEquals(
            5,
            releaseRoomModelList.size
        )
        val releaseRoomModel1 = releaseRoomModelList[0]
        assertEquals(
            ReleaseRoomModel(
                id = 1,
                nroOS = 123456,
                idPropAgr = 1,
                descPropAgr = "Release1",
                idFront = 1
            ),
            releaseRoomModel1
        )
        val releaseRoomModel2 = releaseRoomModelList[1]
        assertEquals(
            ReleaseRoomModel(
                id = 2,
                nroOS = 234567,
                idPropAgr = 2,
                descPropAgr = "Release2",
                idFront = 2
            ),
            releaseRoomModel2
        )
        val releaseRoomModel3 = releaseRoomModelList[2]
        assertEquals(
            ReleaseRoomModel(
                id = 3,
                nroOS = 345678,
                idPropAgr = 3,
                descPropAgr = "Release3",
                idFront = 3
            ),
            releaseRoomModel3
        )
        val releaseRoomModel4 = releaseRoomModelList[3]
        assertEquals(
            ReleaseRoomModel(
                id = 4,
                nroOS = 456789,
                idPropAgr = 4,
                descPropAgr = "Release4",
                idFront = 2
            ),
            releaseRoomModel4
        )
        val releaseRoomModel5 = releaseRoomModelList[4]
        assertEquals(
            ReleaseRoomModel(
                id = 5,
                nroOS = 225687,
                idPropAgr = 2,
                descPropAgr = "Release5",
                idFront = 3
            ),
            releaseRoomModel5
        )

    }

}