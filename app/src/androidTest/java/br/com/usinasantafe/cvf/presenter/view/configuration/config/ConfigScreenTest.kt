package br.com.usinasantafe.cvf.presenter.view.configuration.config

import android.annotation.SuppressLint
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.lifecycle.SavedStateHandle
import br.com.usinasantafe.cav.utils.waitUntilTimeout
import br.com.usinasantafe.cvf.HiltTestActivity
import br.com.usinasantafe.cvf.di.provider.BaseUrlModuleTest
import br.com.usinasantafe.cvf.domain.usecases.config.GetConfig
import br.com.usinasantafe.cvf.domain.usecases.config.SetFinishUpdateAllTable
import br.com.usinasantafe.cvf.domain.usecases.config.UpdateConfig
import br.com.usinasantafe.cvf.domain.usecases.update.UpdateTableColab
import br.com.usinasantafe.cvf.domain.usecases.update.UpdateTableEquip
import br.com.usinasantafe.cvf.domain.usecases.update.UpdateTableFront
import br.com.usinasantafe.cvf.domain.usecases.update.UpdateTableRelease
import br.com.usinasantafe.cvf.external.room.dao.stable.ColabDao
import br.com.usinasantafe.cvf.external.room.dao.stable.EquipDao
import br.com.usinasantafe.cvf.external.room.dao.stable.FrontDao
import br.com.usinasantafe.cvf.external.room.dao.stable.ReleaseDao
import br.com.usinasantafe.cvf.external.sharedPreferences.IManagerSharedPreferencesDatasource
import br.com.usinasantafe.cvf.infra.datasource.sharedpreferences.ConfigSharedPreferencesDatasource
import br.com.usinasantafe.cvf.infra.models.room.stable.ColabRoomModel
import br.com.usinasantafe.cvf.infra.models.room.stable.EquipRoomModel
import br.com.usinasantafe.cvf.infra.models.room.stable.FrontRoomModel
import br.com.usinasantafe.cvf.infra.models.room.stable.ReleaseRoomModel
import br.com.usinasantafe.cvf.infra.models.sharedpreferences.ConfigSharedPreferencesModel
import br.com.usinasantafe.cvf.infra.models.sharedpreferences.ManagerSharedPreferencesModel
import br.com.usinasantafe.cvf.lib.Option
import br.com.usinasantafe.cvf.lib.StatusSend
import br.com.usinasantafe.cvf.lib.WEB_ALL_COLAB
import br.com.usinasantafe.cvf.lib.WEB_ALL_EQUIP
import br.com.usinasantafe.cvf.lib.WEB_ALL_FRONT
import br.com.usinasantafe.cvf.lib.WEB_ALL_RELEASE
import br.com.usinasantafe.cvf.lib.WEB_SAVE_TOKEN
import br.com.usinasantafe.cvf.presenter.navigation.Args.OPTION_ARG
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.minutes

@HiltAndroidTest
class ConfigScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()

    @Inject
    lateinit var configSharedPreferencesDatasource: ConfigSharedPreferencesDatasource

    @Inject
    lateinit var managerSharedPreferencesDatasource: IManagerSharedPreferencesDatasource

    @Inject
    lateinit var getConfig: GetConfig

    @Inject
    lateinit var updateConfig: UpdateConfig

    @Inject
    lateinit var setFinishUpdateAllTable: SetFinishUpdateAllTable

    @Inject
    lateinit var updateTableColab: UpdateTableColab

    @Inject
    lateinit var updateTableEquip: UpdateTableEquip

    @Inject
    lateinit var updateTableFront: UpdateTableFront

    @Inject
    lateinit var updateTableRelease: UpdateTableRelease

    @Inject
    lateinit var colabDao: ColabDao

    @Inject
    lateinit var equipDao: EquipDao

    @Inject
    lateinit var frontDao: FrontDao

    @Inject
    lateinit var releaseDao: ReleaseDao

    private val resultTokenFailure = """{"status": "success", "idServ": "1a"}""".trimIndent()

    private val resultToken = """{"status": "success", "idServ": 1}""".trimIndent()

    private val resultColabFailure = """
        {
            "status": "success",
            "data": [
                {"reg":"19759a","name":"ANDERSON DA SILVA DELGADO"},
                {"reg":18017,"name":"RONALDO"}
            ]
        }
    """.trimIndent()

    private val resultColabRepeated = """
        {
            "status": "success",
            "data": [
                {"reg":19759,"name":"ANDERSON DA SILVA DELGADO"},
                {"reg":19759,"name":"ANDERSON DA SILVA DELGADO"}
            ]
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

    private val resultEquipFailure = """
        {
            "status": "success",
            "data": [
                {"id":"1a","nro":1,"cdOperClass":1,"description":"Equip1"},
                {"id":2,"nro":2,"cdOperClass":2,"description":"Equip2"}
            ]
        }
    """.trimIndent()

    private val resultEquipRepeated = """
        {
            "status": "success",
            "data": [
                {"id":1,"nro":1,"cdOperClass":1,"description":"Equip1"},
                {"id":1,"nro":1,"cdOperClass":1,"description":"Equip1"}
            ]
        }
    """.trimIndent()

    private val resultEquip = """
        {
            "status": "success",
            "data": [
                {"id":1,"nro":1,"cdOperClass":1,"description":"Equip1"},
                {"id":2,"nro":2,"cdOperClass":2,"description":"Equip2"}
            ]
        }
    """.trimIndent()

    private val resultFrontFailure = """
        {
            "status": "success",
            "data": [
                {"id":"1a","cd":1,"description":"Front1"},
                {"id":2,"cd":2,"description":"Front2"}
            ]
        }
    """.trimIndent()

    private val resultFrontRepeated = """
        {
            "status": "success",
            "data": [
                {"id":1,"cd":1,"description":"Front1"},
                {"id":1,"cd":1,"description":"Front2"}
            ]
        }
    """.trimIndent()

    private val resultFront = """
        {
            "status": "success",
            "data": [
                {"id":1,"cd":1,"description":"Front1"},
                {"id":2,"cd":2,"description":"Front2"}
            ]
        }
    """.trimIndent()

    val resultReleaseFailure = """
        {
            "status": "success",
            "data": [
              {"id":"1a","nroOS":1,"idPropAgr":1,"descPropAgr":"Release1","idFront":1},
              {"id":2,"nroOS":2,"idPropAgr":2,"descPropAgr":"Release2","idFront":2}
            ]
        }
    """.trimIndent()

    val resultReleaseRepeated = """
        {
            "status": "success",
            "data": [
              {"id":1,"nroOS":1,"idPropAgr":1,"descPropAgr":"Release1","idFront":1},
              {"id":1,"nroOS":1,"idPropAgr":1,"descPropAgr":"Release1","idFront":1}
            ]
        }
    """.trimIndent()

    val resultRelease = """
        {
            "status": "success",
            "data": [
              {"id":1,"nroOS":1,"idPropAgr":1,"descPropAgr":"Release1","idFront":1},
              {"id":2,"nroOS":2,"idPropAgr":2,"descPropAgr":"Release2","idFront":2}
            ]
        }
    """.trimIndent()

    private val dispatcherTokenFailure: Dispatcher = object : Dispatcher() {
        @Throws(InterruptedException::class)
        override fun dispatch(request: RecordedRequest): MockResponse {
            return when (request.path) {
                "/$WEB_SAVE_TOKEN" -> MockResponse().setBody(resultTokenFailure)
                "/$WEB_ALL_COLAB" -> MockResponse().setBody("")
                "/$WEB_ALL_EQUIP" -> MockResponse().setBody("")
                "/$WEB_ALL_FRONT" -> MockResponse().setBody("")
                "/$WEB_ALL_RELEASE" -> MockResponse().setBody("")
                else -> MockResponse().setResponseCode(404)
            }
        }
    }

    private val dispatcherToken: Dispatcher = object : Dispatcher() {
        @Throws(InterruptedException::class)
        override fun dispatch(request: RecordedRequest): MockResponse {
            return when (request.path) {
                "/$WEB_SAVE_TOKEN" -> MockResponse().setBody(resultToken)
                "/$WEB_ALL_COLAB" -> MockResponse().setBody("")
                "/$WEB_ALL_EQUIP" -> MockResponse().setBody("")
                "/$WEB_ALL_FRONT" -> MockResponse().setBody("")
                "/$WEB_ALL_RELEASE" -> MockResponse().setBody("")
                else -> MockResponse().setResponseCode(404)
            }
        }
    }

    private val dispatcherColabFailure: Dispatcher = object : Dispatcher() {
        @Throws(InterruptedException::class)
        override fun dispatch(request: RecordedRequest): MockResponse {
            return when (request.path) {
                "/$WEB_SAVE_TOKEN" -> MockResponse().setBody(resultToken)
                "/$WEB_ALL_COLAB" -> MockResponse().setBody(resultColabFailure)
                "/$WEB_ALL_EQUIP" -> MockResponse().setBody("")
                "/$WEB_ALL_FRONT" -> MockResponse().setBody("")
                "/$WEB_ALL_RELEASE" -> MockResponse().setBody("")
                else -> MockResponse().setResponseCode(404)
            }
        }
    }

    private val dispatcherColabRepeated: Dispatcher = object : Dispatcher() {
        @Throws(InterruptedException::class)
        override fun dispatch(request: RecordedRequest): MockResponse {
            return when (request.path) {
                "/$WEB_SAVE_TOKEN" -> MockResponse().setBody(resultToken)
                "/$WEB_ALL_COLAB" -> MockResponse().setBody(resultColabRepeated)
                "/$WEB_ALL_EQUIP" -> MockResponse().setBody("")
                "/$WEB_ALL_FRONT" -> MockResponse().setBody("")
                "/$WEB_ALL_RELEASE" -> MockResponse().setBody("")
                else -> MockResponse().setResponseCode(404)
            }
        }
    }

    private val dispatcherColab: Dispatcher = object : Dispatcher() {
        @Throws(InterruptedException::class)
        override fun dispatch(request: RecordedRequest): MockResponse {
            return when (request.path) {
                "/$WEB_SAVE_TOKEN" -> MockResponse().setBody(resultToken)
                "/$WEB_ALL_COLAB" -> MockResponse().setBody(resultColab)
                "/$WEB_ALL_EQUIP" -> MockResponse().setBody("")
                "/$WEB_ALL_FRONT" -> MockResponse().setBody("")
                "/$WEB_ALL_RELEASE" -> MockResponse().setBody("")
                else -> MockResponse().setResponseCode(404)
            }
        }
    }

    private val dispatcherEquipFailure: Dispatcher = object : Dispatcher() {
        @Throws(InterruptedException::class)
        override fun dispatch(request: RecordedRequest): MockResponse {
            return when (request.path) {
                "/$WEB_SAVE_TOKEN" -> MockResponse().setBody(resultToken)
                "/$WEB_ALL_COLAB" -> MockResponse().setBody(resultColab)
                "/$WEB_ALL_EQUIP" -> MockResponse().setBody(resultEquipFailure)
                "/$WEB_ALL_FRONT" -> MockResponse().setBody("")
                "/$WEB_ALL_RELEASE" -> MockResponse().setBody("")
                else -> MockResponse().setResponseCode(404)
            }
        }
    }

    private val dispatcherEquipRepeated: Dispatcher = object : Dispatcher() {
        @Throws(InterruptedException::class)
        override fun dispatch(request: RecordedRequest): MockResponse {
            return when (request.path) {
                "/$WEB_SAVE_TOKEN" -> MockResponse().setBody(resultToken)
                "/$WEB_ALL_COLAB" -> MockResponse().setBody(resultColab)
                "/$WEB_ALL_EQUIP" -> MockResponse().setBody(resultEquipRepeated)
                "/$WEB_ALL_FRONT" -> MockResponse().setBody("")
                "/$WEB_ALL_RELEASE" -> MockResponse().setBody("")
                else -> MockResponse().setResponseCode(404)
            }
        }
    }

    private val dispatcherEquip: Dispatcher = object : Dispatcher() {
        @Throws(InterruptedException::class)
        override fun dispatch(request: RecordedRequest): MockResponse {
            return when (request.path) {
                "/$WEB_SAVE_TOKEN" -> MockResponse().setBody(resultToken)
                "/$WEB_ALL_COLAB" -> MockResponse().setBody(resultColab)
                "/$WEB_ALL_EQUIP" -> MockResponse().setBody(resultEquip)
                "/$WEB_ALL_FRONT" -> MockResponse().setBody("")
                "/$WEB_ALL_RELEASE" -> MockResponse().setBody("")
                else -> MockResponse().setResponseCode(404)
            }
        }
    }

    private val dispatcherFrontFailure: Dispatcher = object : Dispatcher() {
        @Throws(InterruptedException::class)
        override fun dispatch(request: RecordedRequest): MockResponse {
            return when (request.path) {
                "/$WEB_SAVE_TOKEN" -> MockResponse().setBody(resultToken)
                "/$WEB_ALL_COLAB" -> MockResponse().setBody(resultColab)
                "/$WEB_ALL_EQUIP" -> MockResponse().setBody(resultEquip)
                "/$WEB_ALL_FRONT" -> MockResponse().setBody(resultFrontFailure)
                "/$WEB_ALL_RELEASE" -> MockResponse().setBody("")
                else -> MockResponse().setResponseCode(404)
            }
        }
    }

    private val dispatcherFrontRepeated: Dispatcher = object : Dispatcher() {
        @Throws(InterruptedException::class)
        override fun dispatch(request: RecordedRequest): MockResponse {
            return when (request.path) {
                "/$WEB_SAVE_TOKEN" -> MockResponse().setBody(resultToken)
                "/$WEB_ALL_COLAB" -> MockResponse().setBody(resultColab)
                "/$WEB_ALL_EQUIP" -> MockResponse().setBody(resultEquip)
                "/$WEB_ALL_FRONT" -> MockResponse().setBody(resultFrontRepeated)
                "/$WEB_ALL_RELEASE" -> MockResponse().setBody("")
                else -> MockResponse().setResponseCode(404)
            }
        }
    }

    private val dispatcherFront: Dispatcher = object : Dispatcher() {
        @Throws(InterruptedException::class)
        override fun dispatch(request: RecordedRequest): MockResponse {
            return when (request.path) {
                "/$WEB_SAVE_TOKEN" -> MockResponse().setBody(resultToken)
                "/$WEB_ALL_COLAB" -> MockResponse().setBody(resultColab)
                "/$WEB_ALL_EQUIP" -> MockResponse().setBody(resultEquip)
                "/$WEB_ALL_FRONT" -> MockResponse().setBody(resultFront)
                "/$WEB_ALL_RELEASE" -> MockResponse().setBody("")
                else -> MockResponse().setResponseCode(404)
            }
        }
    }

    private val dispatcherReleaseFailure: Dispatcher = object : Dispatcher() {
        @Throws(InterruptedException::class)
        override fun dispatch(request: RecordedRequest): MockResponse {
            return when (request.path) {
                "/$WEB_SAVE_TOKEN" -> MockResponse().setBody(resultToken)
                "/$WEB_ALL_COLAB" -> MockResponse().setBody(resultColab)
                "/$WEB_ALL_EQUIP" -> MockResponse().setBody(resultEquip)
                "/$WEB_ALL_FRONT" -> MockResponse().setBody(resultFront)
                "/$WEB_ALL_RELEASE" -> MockResponse().setBody(resultReleaseFailure)
                else -> MockResponse().setResponseCode(404)
            }
        }
    }

    private val dispatcherReleaseRepeated: Dispatcher = object : Dispatcher() {
        @Throws(InterruptedException::class)
        override fun dispatch(request: RecordedRequest): MockResponse {
            return when (request.path) {
                "/$WEB_SAVE_TOKEN" -> MockResponse().setBody(resultToken)
                "/$WEB_ALL_COLAB" -> MockResponse().setBody(resultColab)
                "/$WEB_ALL_EQUIP" -> MockResponse().setBody(resultEquip)
                "/$WEB_ALL_FRONT" -> MockResponse().setBody(resultFront)
                "/$WEB_ALL_RELEASE" -> MockResponse().setBody(resultReleaseRepeated)
                else -> MockResponse().setResponseCode(404)
            }
        }
    }

    private val dispatcherRelease: Dispatcher = object : Dispatcher() {
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

    @Test
    fun check_open_screen() =
        runTest {

            hiltRule.inject()

            setContent()

            composeTestRule.waitUntilTimeout(20_000)

        }

    @Test
    fun check_open_screen_if_option_is_edit() =
        runTest {

            hiltRule.inject()

            setContent(Option.EDIT)

            composeTestRule.waitUntilTimeout(20_000)

        }

    @Test
    fun verify_check_msg_all_field_empty() =
        runTest {

            hiltRule.inject()

            setContent()

            composeTestRule.onNodeWithText("SALVAR")
                .performClick()

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertIsDisplayed()
            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertTextEquals("CAMPO VAZIO! POR FAVOR, PREENCHA TODOS OS CAMPOS PARA SALVAR AS CONFIGURAÇÕES E ATUALIZAR TODAS AS BASES DE DADOS.")

            composeTestRule.waitUntilTimeout(20_000)

        }

    @Test
    fun verify_check_msg_number_field_empty() =
        runTest {

            hiltRule.inject()

            setContent()

            composeTestRule.onNodeWithTag(TAG_NUMBER_TEXT_FIELD_CONFIG_SCREEN)
                .performTextInput("16997417840")
            composeTestRule.onNodeWithText("SALVAR")
                .performClick()

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertIsDisplayed()
            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertTextEquals("CAMPO VAZIO! POR FAVOR, PREENCHA TODOS OS CAMPOS PARA SALVAR AS CONFIGURAÇÕES E ATUALIZAR TODAS AS BASES DE DADOS.")

            composeTestRule.waitUntilTimeout(20_000)

        }

    @Test
    fun verify_check_msg_password_field_empty() =
        runTest {

            hiltRule.inject()

            setContent()

            composeTestRule.onNodeWithTag(TAG_PASSWORD_TEXT_FIELD_CONFIG_SCREEN)
                .performTextInput("12345")
            composeTestRule.onNodeWithText("SALVAR")
                .performClick()

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertIsDisplayed()
            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertTextEquals("CAMPO VAZIO! POR FAVOR, PREENCHA TODOS OS CAMPOS PARA SALVAR AS CONFIGURAÇÕES E ATUALIZAR TODAS AS BASES DE DADOS.")

            composeTestRule.waitUntilTimeout(10_000)

        }

    @Test
    fun verify_check_open_screen_config_and_service_without_connection() =
        runTest {

            hiltRule.inject()

            setContent()

            composeTestRule.onNodeWithTag(TAG_NUMBER_TEXT_FIELD_CONFIG_SCREEN)
                .performTextInput("16997417840")
            composeTestRule.onNodeWithTag(TAG_PASSWORD_TEXT_FIELD_CONFIG_SCREEN)
                .performTextInput("12345")
            composeTestRule.onNodeWithText("SALVAR")
                .performClick()

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertIsDisplayed()
            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertTextEquals("FALHA DE RECUPERACAO DE TOKEN! POR FAVOR ENTRE EM CONTATO COM TI. ConfigViewModel.onSaveAndUpdate -> UiStatusStateUpdateKt.collectUpdateStep -> IUpdateConfig -> IConfigRepository.send -> IConfigRetrofitDatasource.recoverToken -> java.net.ConnectException: Failed to connect to localhost/127.0.0.1:8080")

            composeTestRule.waitUntilTimeout()

            val result = configSharedPreferencesDatasource.has()
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                false,
                result.getOrNull()!!
            )

            composeTestRule.waitUntilTimeout(10_000)

        }

    @Test
    fun check_open_screen_and_msg_if_web_service_return_data_token_incorrect() =
        runTest(
            timeout = 1.minutes
        ) {

            val mockWebServer = MockWebServer()
            mockWebServer.dispatcher = dispatcherTokenFailure
            mockWebServer.start()

            BaseUrlModuleTest.url = mockWebServer.url("/").toString()

            hiltRule.inject()

            setContent()

            composeTestRule.onNodeWithTag(TAG_NUMBER_TEXT_FIELD_CONFIG_SCREEN)
                .performTextInput("16997417840")
            composeTestRule.onNodeWithTag(TAG_PASSWORD_TEXT_FIELD_CONFIG_SCREEN)
                .performTextInput("12345")
            composeTestRule.onNodeWithText("SALVAR")
                .performClick()

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertIsDisplayed()
            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertTextEquals("FALHA DE RECUPERACAO DE TOKEN! POR FAVOR ENTRE EM CONTATO COM TI. ConfigViewModel.onSaveAndUpdate -> UiStatusStateUpdateKt.collectUpdateStep -> IUpdateConfig -> IConfigRepository.send -> IConfigRetrofitDatasource.recoverToken -> com.google.gson.JsonSyntaxException: java.lang.NumberFormatException: For input string: \"1a\" ")

            composeTestRule.waitUntilTimeout()

            val result = configSharedPreferencesDatasource.has()
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                false,
                result.getOrNull()!!
            )

            composeTestRule.waitUntilTimeout(10_000)
            mockWebServer.shutdown()
        }

    @Test
    fun check_open_screen_and_msg_if_web_service_return_data_token_correct() =
        runTest(
            timeout = 1.minutes
        ) {

            val mockWebServer = MockWebServer()
            mockWebServer.dispatcher = dispatcherToken
            mockWebServer.start()

            BaseUrlModuleTest.url = mockWebServer.url("/").toString()

            hiltRule.inject()

            setContent()

            composeTestRule.onNodeWithTag(TAG_NUMBER_TEXT_FIELD_CONFIG_SCREEN)
                .performTextInput("16997417840")
            composeTestRule.onNodeWithTag(TAG_PASSWORD_TEXT_FIELD_CONFIG_SCREEN)
                .performTextInput("12345")
            composeTestRule.onNodeWithText("SALVAR")
                .performClick()

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertIsDisplayed()
            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertTextEquals("FALHA DE ATUALIZAÇÃO DE DADOS! POR FAVOR, ENTRE EM CONTATO COM TI. ConfigViewModel.updateAllDatabase -> ConfigViewModel.onSaveAndUpdate -> UiStatusStateUpdateKt.executeUpdateSteps -> UiStatusStateUpdateKt.collectUpdateStep -> IUpdateTableColab -> IColabRepository.listAll -> IColabRetrofitDatasource.listAll -> java.io.EOFException: End of input at line 1 column 1 path \$")

            composeTestRule.waitUntilTimeout()

            val result = configSharedPreferencesDatasource.has()
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                true,
                result.getOrNull()!!
            )

            val resultGet = configSharedPreferencesDatasource.get()
            assertEquals(
                true,
                resultGet.isSuccess
            )
            val config = resultGet.getOrNull()!!
            assertEquals(
                ConfigSharedPreferencesModel(
                    number = 16997417840,
                    password = "12345",
                    idServ = 1,
                    version = "1.0",
                    statusSend = StatusSend.STARTED,
                    flagUpdate = false
                ),
                config
            )

            composeTestRule.waitUntilTimeout(10_000)
            mockWebServer.shutdown()
        }

    @Test
    fun check_open_screen_and_msg_if_web_service_return_data_colab_incorrect() =
        runTest(
            timeout = 1.minutes
        ) {

            val mockWebServer = MockWebServer()
            mockWebServer.dispatcher = dispatcherColabFailure
            mockWebServer.start()

            BaseUrlModuleTest.url = mockWebServer.url("/").toString()

            hiltRule.inject()

            setContent()

            composeTestRule.onNodeWithTag(TAG_NUMBER_TEXT_FIELD_CONFIG_SCREEN)
                .performTextInput("16997417840")
            composeTestRule.onNodeWithTag(TAG_PASSWORD_TEXT_FIELD_CONFIG_SCREEN)
                .performTextInput("12345")
            composeTestRule.onNodeWithText("SALVAR")
                .performClick()

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertIsDisplayed()
            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertTextEquals("FALHA DE ATUALIZAÇÃO DE DADOS! POR FAVOR, ENTRE EM CONTATO COM TI. ConfigViewModel.updateAllDatabase -> ConfigViewModel.onSaveAndUpdate -> UiStatusStateUpdateKt.executeUpdateSteps -> UiStatusStateUpdateKt.collectUpdateStep -> IUpdateTableColab -> IColabRepository.listAll -> IColabRetrofitDatasource.listAll -> com.google.gson.JsonSyntaxException: java.lang.NumberFormatException: For input string: \"19759a\"")

            composeTestRule.waitUntilTimeout()

            val result = configSharedPreferencesDatasource.has()
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                true,
                result.getOrNull()!!
            )

            val resultGet = configSharedPreferencesDatasource.get()
            assertEquals(
                true,
                resultGet.isSuccess
            )
            val model = resultGet.getOrNull()!!
            assertEquals(
                ConfigSharedPreferencesModel(
                    number = 16997417840,
                    password = "12345",
                    idServ = 1,
                    version = "1.0",
                    statusSend = StatusSend.STARTED,
                    flagUpdate = false
                ),
                model
            )

            val count = colabDao.all().size
            assertEquals(
                0,
                count
            )

            composeTestRule.waitUntilTimeout(10_000)
            mockWebServer.shutdown()
        }

    @Test
    fun check_open_screen_and_msg_if_web_service_return_data_colab_repeated() =
        runTest(
            timeout = 1.minutes
        ) {

            val mockWebServer = MockWebServer()
            mockWebServer.dispatcher = dispatcherColabRepeated
            mockWebServer.start()

            BaseUrlModuleTest.url = mockWebServer.url("/").toString()

            hiltRule.inject()

            setContent()

            composeTestRule.onNodeWithTag(TAG_NUMBER_TEXT_FIELD_CONFIG_SCREEN)
                .performTextInput("16997417840")
            composeTestRule.onNodeWithTag(TAG_PASSWORD_TEXT_FIELD_CONFIG_SCREEN)
                .performTextInput("12345")
            composeTestRule.onNodeWithText("SALVAR")
                .performClick()

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertIsDisplayed()
            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertTextEquals("FALHA DE ATUALIZAÇÃO DE DADOS! POR FAVOR, ENTRE EM CONTATO COM TI. ConfigViewModel.updateAllDatabase -> ConfigViewModel.onSaveAndUpdate -> UiStatusStateUpdateKt.executeUpdateSteps -> UiStatusStateUpdateKt.collectUpdateStep -> IUpdateTableColab -> IColabRepository.addAll -> IColabRoomDatasource.addAll -> android.database.sqlite.SQLiteConstraintException: UNIQUE constraint failed: tb_colab.reg (code 1555 SQLITE_CONSTRAINT_PRIMARYKEY[1555])")

            composeTestRule.waitUntilTimeout()

            val result = configSharedPreferencesDatasource.has()
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                true,
                result.getOrNull()!!
            )

            val resultGet = configSharedPreferencesDatasource.get()
            assertEquals(
                true,
                resultGet.isSuccess
            )
            val model = resultGet.getOrNull()!!
            assertEquals(
                ConfigSharedPreferencesModel(
                    number = 16997417840,
                    password = "12345",
                    idServ = 1,
                    version = "1.0",
                    statusSend = StatusSend.STARTED,
                    flagUpdate = false
                ),
                model
            )

            val count = colabDao.all().size
            assertEquals(
                0,
                count
            )

            composeTestRule.waitUntilTimeout(10_000)
            mockWebServer.shutdown()

        }

    @Test
    fun check_open_screen_and_msg_if_web_service_return_data_colab_correct() =
        runTest(
            timeout = 1.minutes
        ) {

            val mockWebServer = MockWebServer()
            mockWebServer.dispatcher = dispatcherColab
            mockWebServer.start()

            BaseUrlModuleTest.url = mockWebServer.url("/").toString()

            hiltRule.inject()

            setContent()

            composeTestRule.onNodeWithTag(TAG_NUMBER_TEXT_FIELD_CONFIG_SCREEN)
                .performTextInput("16997417840")
            composeTestRule.onNodeWithTag(TAG_PASSWORD_TEXT_FIELD_CONFIG_SCREEN)
                .performTextInput("12345")
            composeTestRule.onNodeWithText("SALVAR")
                .performClick()

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertIsDisplayed()
            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertTextEquals("FALHA DE ATUALIZAÇÃO DE DADOS! POR FAVOR, ENTRE EM CONTATO COM TI. ConfigViewModel.onSaveAndUpdate -> ConfigViewModel.updateAllDatabase -> UiStatusStateUpdateKt.executeUpdateSteps -> UiStatusStateUpdateKt.collectUpdateStep -> IUpdateTableEquip -> IEquipRepository.listAll -> IEquipRetrofitDatasource.listAll -> java.io.EOFException: End of input at line 1 column 1 path \$")

            composeTestRule.waitUntilTimeout()

            asserts(1)

            composeTestRule.waitUntilTimeout(10_000)
            mockWebServer.shutdown()

        }

    @Test
    fun check_open_screen_and_msg_if_web_service_return_data_equip_incorrect() =
        runTest(
            timeout = 1.minutes
        ) {

            val mockWebServer = MockWebServer()
            mockWebServer.dispatcher = dispatcherEquipFailure
            mockWebServer.start()

            BaseUrlModuleTest.url = mockWebServer.url("/").toString()

            hiltRule.inject()

            setContent()

            composeTestRule.onNodeWithTag(TAG_NUMBER_TEXT_FIELD_CONFIG_SCREEN)
                .performTextInput("16997417840")
            composeTestRule.onNodeWithTag(TAG_PASSWORD_TEXT_FIELD_CONFIG_SCREEN)
                .performTextInput("12345")
            composeTestRule.onNodeWithText("SALVAR")
                .performClick()

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertIsDisplayed()
            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertTextEquals("FALHA DE ATUALIZAÇÃO DE DADOS! POR FAVOR, ENTRE EM CONTATO COM TI. ConfigViewModel.onSaveAndUpdate -> ConfigViewModel.updateAllDatabase -> UiStatusStateUpdateKt.executeUpdateSteps -> UiStatusStateUpdateKt.collectUpdateStep -> IUpdateTableEquip -> IEquipRepository.listAll -> IEquipRetrofitDatasource.listAll -> com.google.gson.JsonSyntaxException: java.lang.NumberFormatException: For input string: \"1a\"")

            composeTestRule.waitUntilTimeout()

            asserts(1)

            val count = equipDao.all().size
            assertEquals(
                0,
                count
            )

            composeTestRule.waitUntilTimeout(10_000)
            mockWebServer.shutdown()

        }

    @Test
    fun check_open_screen_and_msg_if_web_service_return_data_equip_repeated() =
        runTest(
            timeout = 1.minutes
        ) {

            val mockWebServer = MockWebServer()
            mockWebServer.dispatcher = dispatcherEquipRepeated
            mockWebServer.start()

            BaseUrlModuleTest.url = mockWebServer.url("/").toString()

            hiltRule.inject()

            setContent()

            composeTestRule.onNodeWithTag(TAG_NUMBER_TEXT_FIELD_CONFIG_SCREEN)
                .performTextInput("16997417840")
            composeTestRule.onNodeWithTag(TAG_PASSWORD_TEXT_FIELD_CONFIG_SCREEN)
                .performTextInput("12345")
            composeTestRule.onNodeWithText("SALVAR")
                .performClick()

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertIsDisplayed()
            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertTextEquals("FALHA DE ATUALIZAÇÃO DE DADOS! POR FAVOR, ENTRE EM CONTATO COM TI. ConfigViewModel.onSaveAndUpdate -> ConfigViewModel.updateAllDatabase -> UiStatusStateUpdateKt.executeUpdateSteps -> UiStatusStateUpdateKt.collectUpdateStep -> IUpdateTableEquip -> IEquipRepository.addAll -> IEquipRoomDatasource.addAll -> android.database.sqlite.SQLiteConstraintException: UNIQUE constraint failed: tb_equip.id (code 1555 SQLITE_CONSTRAINT_PRIMARYKEY[1555])")

            composeTestRule.waitUntilTimeout()

            asserts(1)

            val count = equipDao.all().size
            assertEquals(
                0,
                count
            )

            composeTestRule.waitUntilTimeout(10_000)
            mockWebServer.shutdown()

        }

    @Test
    fun check_open_screen_and_msg_if_web_service_return_data_equip_correct() =
        runTest(
            timeout = 1.minutes
        ) {

            val mockWebServer = MockWebServer()
            mockWebServer.dispatcher = dispatcherEquip
            mockWebServer.start()

            BaseUrlModuleTest.url = mockWebServer.url("/").toString()

            hiltRule.inject()

            setContent()

            composeTestRule.onNodeWithTag(TAG_NUMBER_TEXT_FIELD_CONFIG_SCREEN)
                .performTextInput("16997417840")
            composeTestRule.onNodeWithTag(TAG_PASSWORD_TEXT_FIELD_CONFIG_SCREEN)
                .performTextInput("12345")
            composeTestRule.onNodeWithText("SALVAR")
                .performClick()

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertIsDisplayed()
            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertTextEquals("FALHA DE ATUALIZAÇÃO DE DADOS! POR FAVOR, ENTRE EM CONTATO COM TI. ConfigViewModel.onSaveAndUpdate -> ConfigViewModel.updateAllDatabase -> UiStatusStateUpdateKt.executeUpdateSteps -> UiStatusStateUpdateKt.collectUpdateStep -> IUpdateTableFront -> IFrontRepository.listAll -> IFrontRetrofitDatasource.listAll -> java.io.EOFException: End of input at line 1 column 1 path \$")

            composeTestRule.waitUntilTimeout()

            asserts(2)

            val count = frontDao.all().size
            assertEquals(
                0,
                count
            )

            composeTestRule.waitUntilTimeout(10_000)
            mockWebServer.shutdown()

        }

    @Test
    fun check_open_screen_and_msg_if_web_service_return_data_front_incorrect() =
        runTest(
            timeout = 1.minutes
        ) {

            val mockWebServer = MockWebServer()
            mockWebServer.dispatcher = dispatcherFrontFailure
            mockWebServer.start()

            BaseUrlModuleTest.url = mockWebServer.url("/").toString()

            hiltRule.inject()

            setContent()

            composeTestRule.onNodeWithTag(TAG_NUMBER_TEXT_FIELD_CONFIG_SCREEN)
                .performTextInput("16997417840")
            composeTestRule.onNodeWithTag(TAG_PASSWORD_TEXT_FIELD_CONFIG_SCREEN)
                .performTextInput("12345")
            composeTestRule.onNodeWithText("SALVAR")
                .performClick()

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertIsDisplayed()
            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertTextEquals("FALHA DE ATUALIZAÇÃO DE DADOS! POR FAVOR, ENTRE EM CONTATO COM TI. ConfigViewModel.onSaveAndUpdate -> ConfigViewModel.updateAllDatabase -> UiStatusStateUpdateKt.executeUpdateSteps -> UiStatusStateUpdateKt.collectUpdateStep -> IUpdateTableFront -> IFrontRepository.listAll -> IFrontRetrofitDatasource.listAll -> com.google.gson.JsonSyntaxException: java.lang.NumberFormatException: For input string: \"1a\"")

            composeTestRule.waitUntilTimeout()

            asserts(2)

            val count = frontDao.all().size
            assertEquals(
                0,
                count
            )

            composeTestRule.waitUntilTimeout(10_000)
            mockWebServer.shutdown()

        }

    @Test
    fun check_open_screen_and_msg_if_web_service_return_data_front_repeated() =
        runTest(
            timeout = 1.minutes
        ) {

            val mockWebServer = MockWebServer()
            mockWebServer.dispatcher = dispatcherFrontRepeated
            mockWebServer.start()

            BaseUrlModuleTest.url = mockWebServer.url("/").toString()

            hiltRule.inject()

            setContent()

            composeTestRule.onNodeWithTag(TAG_NUMBER_TEXT_FIELD_CONFIG_SCREEN)
                .performTextInput("16997417840")
            composeTestRule.onNodeWithTag(TAG_PASSWORD_TEXT_FIELD_CONFIG_SCREEN)
                .performTextInput("12345")
            composeTestRule.onNodeWithText("SALVAR")
                .performClick()

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertIsDisplayed()
            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertTextEquals("FALHA DE ATUALIZAÇÃO DE DADOS! POR FAVOR, ENTRE EM CONTATO COM TI. ConfigViewModel.onSaveAndUpdate -> ConfigViewModel.updateAllDatabase -> UiStatusStateUpdateKt.executeUpdateSteps -> UiStatusStateUpdateKt.collectUpdateStep -> IUpdateTableFront -> IFrontRepository.addAll -> IFrontRoomDatasource.addAll -> android.database.sqlite.SQLiteConstraintException: UNIQUE constraint failed: tb_front.id (code 1555 SQLITE_CONSTRAINT_PRIMARYKEY[1555])")

            composeTestRule.waitUntilTimeout()

            asserts(2)

            val count = frontDao.all().size
            assertEquals(
                0,
                count
            )

            composeTestRule.waitUntilTimeout(10_000)
            mockWebServer.shutdown()

        }

    @Test
    fun check_open_screen_and_msg_if_web_service_return_data_front_correct() =
        runTest(
            timeout = 1.minutes
        ) {

            val mockWebServer = MockWebServer()
            mockWebServer.dispatcher = dispatcherFront
            mockWebServer.start()

            BaseUrlModuleTest.url = mockWebServer.url("/").toString()

            hiltRule.inject()

            setContent()

            composeTestRule.onNodeWithTag(TAG_NUMBER_TEXT_FIELD_CONFIG_SCREEN)
                .performTextInput("16997417840")
            composeTestRule.onNodeWithTag(TAG_PASSWORD_TEXT_FIELD_CONFIG_SCREEN)
                .performTextInput("12345")
            composeTestRule.onNodeWithText("SALVAR")
                .performClick()

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertIsDisplayed()
            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertTextEquals("FALHA DE ATUALIZAÇÃO DE DADOS! POR FAVOR, ENTRE EM CONTATO COM TI. ConfigViewModel.onSaveAndUpdate -> ConfigViewModel.updateAllDatabase -> UiStatusStateUpdateKt.executeUpdateSteps -> UiStatusStateUpdateKt.collectUpdateStep -> IUpdateTableRelease -> IReleaseRepository.listAll -> IReleaseRetrofitDatasource.listAll -> java.io.EOFException: End of input at line 1 column 1 path \$")

            composeTestRule.waitUntilTimeout()

            asserts(3)

            val count = releaseDao.all().size
            assertEquals(
                0,
                count
            )

            composeTestRule.waitUntilTimeout(10_000)
            mockWebServer.shutdown()

        }

    @Test
    fun check_open_screen_and_msg_if_web_service_return_data_release_incorrect() =
        runTest(
            timeout = 1.minutes
        ) {

            val mockWebServer = MockWebServer()
            mockWebServer.dispatcher = dispatcherReleaseFailure
            mockWebServer.start()

            BaseUrlModuleTest.url = mockWebServer.url("/").toString()

            hiltRule.inject()

            setContent()

            composeTestRule.onNodeWithTag(TAG_NUMBER_TEXT_FIELD_CONFIG_SCREEN)
                .performTextInput("16997417840")
            composeTestRule.onNodeWithTag(TAG_PASSWORD_TEXT_FIELD_CONFIG_SCREEN)
                .performTextInput("12345")
            composeTestRule.onNodeWithText("SALVAR")
                .performClick()

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertIsDisplayed()
            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertTextEquals("FALHA DE ATUALIZAÇÃO DE DADOS! POR FAVOR, ENTRE EM CONTATO COM TI. ConfigViewModel.onSaveAndUpdate -> ConfigViewModel.updateAllDatabase -> UiStatusStateUpdateKt.executeUpdateSteps -> UiStatusStateUpdateKt.collectUpdateStep -> IUpdateTableRelease -> IReleaseRepository.listAll -> IReleaseRetrofitDatasource.listAll -> com.google.gson.JsonSyntaxException: java.lang.NumberFormatException: For input string: \"1a\"")

            composeTestRule.waitUntilTimeout()

            asserts(3)

            val count = releaseDao.all().size
            assertEquals(
                0,
                count
            )

            composeTestRule.waitUntilTimeout(10_000)
            mockWebServer.shutdown()

        }

    @Test
    fun check_open_screen_and_msg_if_web_service_return_data_release_repeated() =
        runTest(
            timeout = 1.minutes
        ) {

            val mockWebServer = MockWebServer()
            mockWebServer.dispatcher = dispatcherReleaseRepeated
            mockWebServer.start()

            BaseUrlModuleTest.url = mockWebServer.url("/").toString()

            hiltRule.inject()

            setContent()

            composeTestRule.onNodeWithTag(TAG_NUMBER_TEXT_FIELD_CONFIG_SCREEN)
                .performTextInput("16997417840")
            composeTestRule.onNodeWithTag(TAG_PASSWORD_TEXT_FIELD_CONFIG_SCREEN)
                .performTextInput("12345")
            composeTestRule.onNodeWithText("SALVAR")
                .performClick()

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertIsDisplayed()
            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertTextEquals("FALHA DE ATUALIZAÇÃO DE DADOS! POR FAVOR, ENTRE EM CONTATO COM TI. ConfigViewModel.onSaveAndUpdate -> ConfigViewModel.updateAllDatabase -> UiStatusStateUpdateKt.executeUpdateSteps -> UiStatusStateUpdateKt.collectUpdateStep -> IUpdateTableRelease -> IReleaseRepository.addAll -> IReleaseRoomDatasource.addAll -> android.database.sqlite.SQLiteConstraintException: UNIQUE constraint failed: tb_release.id (code 1555 SQLITE_CONSTRAINT_PRIMARYKEY[1555])")

            composeTestRule.waitUntilTimeout()

            asserts(3)

            val count = releaseDao.all().size
            assertEquals(
                0,
                count
            )

            composeTestRule.waitUntilTimeout(10_000)
            mockWebServer.shutdown()

        }

    @Test
    fun check_open_screen_and_msg_if_web_service_return_data_release_correct_and_finish_updated() =
        runTest(
            timeout = 1.minutes
        ) {

            val mockWebServer = MockWebServer()
            mockWebServer.dispatcher = dispatcherRelease
            mockWebServer.start()

            BaseUrlModuleTest.url = mockWebServer.url("/").toString()

            hiltRule.inject()

            setContent()

            managerSharedPreferencesDatasource.save(
                ManagerSharedPreferencesModel(
                    idRelease = 1,
                    idFront = 1
                )
            )

            val modelConfigBefore = configSharedPreferencesDatasource.get().getOrThrow()
            assertEquals(
                false,
                modelConfigBefore.flagUpdate
            )
            val hasManagerAfter = managerSharedPreferencesDatasource.has().getOrThrow()
            assertEquals(
                true,
                hasManagerAfter
            )

            composeTestRule.onNodeWithTag(TAG_NUMBER_TEXT_FIELD_CONFIG_SCREEN)
                .performTextInput("16997417840")
            composeTestRule.onNodeWithTag(TAG_PASSWORD_TEXT_FIELD_CONFIG_SCREEN)
                .performTextInput("12345")
            composeTestRule.onNodeWithText("SALVAR")
                .performClick()

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertIsDisplayed()
            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertTextEquals("ATUALIZAÇÃO DE DADOS REALIZADO COM SUCESSO!")

            composeTestRule.waitUntilTimeout()

            val modelAfter = configSharedPreferencesDatasource.get().getOrThrow()
            assertEquals(
                true,
                modelAfter.flagUpdate
            )
            val modelManagerAfter = managerSharedPreferencesDatasource.has().getOrThrow()
            assertEquals(
                false,
                modelManagerAfter
            )

            asserts(4, true)

            composeTestRule.waitUntilTimeout(10_000)
            mockWebServer.shutdown()

        }

    @Test
    fun check_open_screen_and_msg_if_config_shared_preferences_missing_all_field() =
        runTest(
            timeout = 1.minutes
        ) {

            hiltRule.inject()

            configSharedPreferencesDatasource.save(
                ConfigSharedPreferencesModel()
            )

            setContent()

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertIsDisplayed()
            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertTextEquals("FALHA INESPERADA NO APLICATIVO! POR FAVOR ENTRE EM CONTATO COM TI. ConfigViewModel.updateState -> UiStatusStateUpdateKt -> ConfigViewModel.recoverData -> IGetConfig -> IConfigRepository.get -> IConfigSharedPreferencesDatasource.get -> java.lang.NullPointerException: number is required")

            composeTestRule.waitUntilTimeout()

            composeTestRule.waitUntilTimeout(20_000)

        }

    @Test
    fun check_open_screen_and_msg_if_config_shared_preferences_missing_some_field() =
        runTest(
            timeout = 1.minutes
        ) {

            hiltRule.inject()

            configSharedPreferencesDatasource.save(
                ConfigSharedPreferencesModel(
                    number = 16997417840
                )
            )

            setContent()

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertIsDisplayed()
            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertTextEquals("FALHA INESPERADA NO APLICATIVO! POR FAVOR ENTRE EM CONTATO COM TI. ConfigViewModel.updateState -> UiStatusStateUpdateKt -> ConfigViewModel.recoverData -> IGetConfig -> IConfigRepository.get -> IConfigSharedPreferencesDatasource.get -> java.lang.NullPointerException: password is required")

            composeTestRule.waitUntilTimeout(20_000)

        }

    @Test
    fun check_open_screen_and_return_data_of_config_shared_preferences() =
        runTest(
            timeout = 1.minutes
        ) {

            hiltRule.inject()

            configSharedPreferencesDatasource.save(
                ConfigSharedPreferencesModel(
                    number = 16997417840,
                    password = "ABC1234"
                )
            )

            setContent()

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag(TAG_NUMBER_TEXT_FIELD_CONFIG_SCREEN)
                .assertTextEquals("16997417840")

            composeTestRule.waitUntilTimeout(20_000)

        }

    @SuppressLint("ViewModelConstructorInComposable")
    private fun setContent(option: Option = Option.INSERT) {
        composeTestRule.setContent {
            ConfigScreen (
                viewModel = ConfigViewModel(
                    savedStateHandle = SavedStateHandle(
                        mapOf(OPTION_ARG to option.ordinal)
                    ),
                    getConfig = getConfig,
                    updateConfig = updateConfig,
                    setFinishUpdateAllTable = setFinishUpdateAllTable,
                    updateTableColab = updateTableColab,
                    updateTableEquip = updateTableEquip,
                    updateTableFront = updateTableFront,
                    updateTableRelease = updateTableRelease
                ),
                onNavFront = {},
                onNavNote = {}
            )
        }
    }

    private suspend fun asserts(level: Int, flagUpdate: Boolean = false) {

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
                statusSend = if(flagUpdate) StatusSend.SENT else StatusSend.STARTED,
                flagUpdate = flagUpdate
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

        if(level == 1) return

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

        if(level == 2) return

        val frontRoomModelList = frontDao.all()
        assertEquals(
            2,
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

        if(level == 3) return

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

        if(level == 4) return

    }

}