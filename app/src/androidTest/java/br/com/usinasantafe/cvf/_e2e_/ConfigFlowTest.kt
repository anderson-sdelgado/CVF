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
import br.com.usinasantafe.cvf.infra.models.room.stable.ColabRoomModel
import br.com.usinasantafe.cvf.infra.models.room.stable.EquipRoomModel
import br.com.usinasantafe.cvf.infra.models.room.stable.FrontRoomModel
import br.com.usinasantafe.cvf.infra.models.room.stable.ReleaseRoomModel
import br.com.usinasantafe.cvf.infra.models.sharedpreferences.ConfigSharedPreferencesModel
import br.com.usinasantafe.cvf.lib.StatusSend
import br.com.usinasantafe.cvf.lib.WEB_ALL_COLAB
import br.com.usinasantafe.cvf.lib.WEB_ALL_EQUIP
import br.com.usinasantafe.cvf.lib.WEB_ALL_FRONT
import br.com.usinasantafe.cvf.lib.WEB_ALL_RELEASE
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
class ConfigFlowTest {

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

    companion object {

        private lateinit var mockWebServer: MockWebServer

        private val resultToken = """
            {
                "idServ": 1
            }
        """.trimIndent()

        private val resultColab = """
            [
                {"reg":19759,"name":"ANDERSON DA SILVA DELGADO"},
                {"reg":18017,"name":"RONALDO GOMES"}
            ]
        """.trimIndent()

        private val resultEquip = """
            [
                {"id":1,"nro":1,"cdOperClass":1,"description":"Equip1"},
                {"id":2,"nro":2,"cdOperClass":2,"description":"Equip2"}
            ]
        """.trimIndent()

        private val resultFront = """
            [
                {"id":1,"cd":1,"description":"Front1"},
                {"id":3,"cd":3,"description":"Front3"},
                {"id":2,"cd":2,"description":"Front2"}
            ]
        """.trimIndent()

        val resultRelease = """
            [
              {"id":1,"nroOS":1,"idPropAgr":1,"descPropAgr":"Release1","idFront":1},
              {"id":2,"nroOS":2,"idPropAgr":2,"descPropAgr":"Release2","idFront":2}
            ]
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

            Log.d("TestDebug", "Position 1")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag(TAG_NUMBER_TEXT_FIELD_CONFIG_SCREEN)
                .performTextInput("16997417840")
            composeTestRule.onNodeWithTag(TAG_PASSWORD_TEXT_FIELD_CONFIG_SCREEN)
                .performTextInput("12345")
            composeTestRule.onNodeWithText("SALVAR")
                .performClick()

            Log.d("TestDebug", "Position 2")

            composeTestRule.waitUntilTimeout()

            asserts()

            Log.d("TestDebug", "Position 3")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag(TAG_BUTTON_OK_ALERT_DIALOG_SIMPLE)
                .performClick()

            Log.d("TestDebug", "Position 4")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithText("RETORNAR")
                .performClick()

            Log.d("TestDebug", "Position 5")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithText("SALVAR")
                .performClick()

            Log.d("TestDebug", "Position 6")

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag(TAG_BUTTON_OK_ALERT_DIALOG_SIMPLE)
                .performClick()

            Log.d("TestDebug", "Position 7")

            composeTestRule.waitUntilTimeout(3_000)

            composeTestRule.onNodeWithTag("item_check_box_3")
                .performClick()

            Log.d("TestDebug", "Position 8")

            composeTestRule.waitUntilTimeout(3_000)

            composeTestRule.onNodeWithTag("item_check_box_1")
                .performClick()

            Log.d("TestDebug", "Position 9")

            composeTestRule.waitUntilTimeout(10_000)

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
                description = "Equip1"
            ),
            equipRoomModel1
        )
        val equipRoomModel2 = equipRoomModelList[1]
        assertEquals(
            EquipRoomModel(
                id = 2,
                nro = 2,
                cdOperClass = 2,
                description = "Equip2"
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
            2,
            releaseRoomModelList.size
        )
        val releaseRoomModel1 = releaseRoomModelList[0]
        assertEquals(
            ReleaseRoomModel(
                id = 1,
                nroOS = 1,
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
                nroOS = 2,
                idPropAgr = 2,
                descPropAgr = "Release2",
                idFront = 2
            ),
            releaseRoomModel2
        )

    }

}