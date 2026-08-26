package br.com.usinasantafe.cvf.domain.usecases.manager

import br.com.usinasantafe.cvf.di.provider.BaseUrlModuleTest
import br.com.usinasantafe.cvf.external.sharedPreferences.IManagerSharedPreferencesDatasource
import br.com.usinasantafe.cvf.infra.datasource.sharedpreferences.ConfigSharedPreferencesDatasource
import br.com.usinasantafe.cvf.infra.models.sharedpreferences.ConfigSharedPreferencesModel
import br.com.usinasantafe.cvf.infra.models.sharedpreferences.ManagerSharedPreferencesModel
import br.com.usinasantafe.cvf.lib.StatusSend
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import kotlin.test.assertEquals

@HiltAndroidTest
class ISendManagerTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var usecase: SendManager

    @Inject
    lateinit var configSharedPreferencesDatasource: ConfigSharedPreferencesDatasource

    @Inject
    lateinit var managerSharedPreferencesDatasource: IManagerSharedPreferencesDatasource

    private val resultFailureAuthorization = """
        {
            "status": "error",
            "failure": "Authorization header is missing"
        }
    """.trimIndent()

    private val resultFailureInfoIncorrect = """
        {
            "status": "success",
            "idServ": s4dq
        }
    """.trimIndent()

    private val result = """
        {
            "status": "success",
            "idServ": 1
        }
    """.trimIndent()

    @Test
    fun check_return_failure_if_not_have_data() =
        runTest {
            hiltRule.inject()
            val result = usecase()
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "ISendManager -> IToken -> IConfigRepository.get -> number is required",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.NullPointerException: number is required",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun check_return_failure_if_idServ_is_null() =
        runTest {
            hiltRule.inject()
            configSharedPreferencesDatasource.save(
                ConfigSharedPreferencesModel(
                    number = 169,
                    password = "12345"
                )
            )
            val result = usecase()
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "ISendManager -> IToken -> token",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.NullPointerException: idServ is required",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun check_return_failure_if_manager_data_is_empty() =
        runTest {
            hiltRule.inject()
            configSharedPreferencesDatasource.save(
                ConfigSharedPreferencesModel(
                    number = 169,
                    password = "12345",
                    idServ = 1,
                    version = "1.0"
                )
            )
            val result = usecase()
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "ISendManager -> IManagerRepository.send -> IManagerSharedPreferencesDatasource.get",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.NullPointerException",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun check_return_failure_if_not_return_web_service() =
        runTest {
            hiltRule.inject()
            configSharedPreferencesDatasource.save(
                ConfigSharedPreferencesModel(
                    number = 169,
                    password = "12345",
                    idServ = 1,
                    version = "1.0"
                )
            )
            managerSharedPreferencesDatasource.setIdRelease(
                ManagerSharedPreferencesModel(
                    idFront = 1,
                    idRelease = 1
                )
            )
            val resultBefore = managerSharedPreferencesDatasource.get()
            assertEquals(
                true,
                resultBefore.isSuccess
            )
            val modelBefore = resultBefore.getOrNull()!!
            assertEquals(
                StatusSend.SEND,
                modelBefore.statusSend
            )
            val result = usecase()
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "ISendManager -> IManagerRepository.send -> IManagerRetrofitDatasource.send",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.net.ConnectException: Failed to connect to localhost/127.0.0.1:8080",
                result.exceptionOrNull()!!.cause.toString()
            )
            val resultAfter = managerSharedPreferencesDatasource.get()
            assertEquals(
                true,
                resultAfter.isSuccess
            )
            val modelAfter = resultAfter.getOrNull()!!
            assertEquals(
                StatusSend.SEND,
                modelAfter.statusSend
            )
        }

    @Test
    fun check_return_failure_if_token_is_invalid() =
        runTest {
            val server = MockWebServer()
            server.start()
            server.enqueue(
                MockResponse().setBody(resultFailureAuthorization)
            )
            BaseUrlModuleTest.url = server.url("/").toString()
            hiltRule.inject()
            configSharedPreferencesDatasource.save(
                ConfigSharedPreferencesModel(
                    number = 169,
                    password = "12345",
                    idServ = 1,
                    version = "1.0"
                )
            )
            managerSharedPreferencesDatasource.setIdRelease(
                ManagerSharedPreferencesModel(
                    idFront = 1,
                    idRelease = 1
                )
            )
            val resultBefore = managerSharedPreferencesDatasource.get()
            assertEquals(
                true,
                resultBefore.isSuccess
            )
            val modelBefore = resultBefore.getOrNull()!!
            assertEquals(
                StatusSend.SEND,
                modelBefore.statusSend
            )
            val result = usecase()
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "ISendManager -> IManagerRepository.send -> IManagerRetrofitDatasource.send",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception: Authorization header is missing",
                result.exceptionOrNull()!!.cause.toString()
            )
            val resultAfter = managerSharedPreferencesDatasource.get()
            assertEquals(
                true,
                resultAfter.isSuccess
            )
            val modelAfter = resultAfter.getOrNull()!!
            assertEquals(
                StatusSend.SEND,
                modelAfter.statusSend
            )
        }

    @Test
    fun check_return_failure_if_web_service_return_data_incorrect() =
        runTest {
            val server = MockWebServer()
            server.start()
            server.enqueue(
                MockResponse().setBody(resultFailureInfoIncorrect)
            )
            BaseUrlModuleTest.url = server.url("/").toString()
            hiltRule.inject()
            configSharedPreferencesDatasource.save(
                ConfigSharedPreferencesModel(
                    number = 169,
                    password = "12345",
                    idServ = 1,
                    version = "1.0"
                )
            )
            managerSharedPreferencesDatasource.setIdRelease(
                ManagerSharedPreferencesModel(
                    idFront = 1,
                    idRelease = 1
                )
            )
            val resultBefore = managerSharedPreferencesDatasource.get()
            assertEquals(
                true,
                resultBefore.isSuccess
            )
            val modelBefore = resultBefore.getOrNull()!!
            assertEquals(
                StatusSend.SEND,
                modelBefore.statusSend
            )
            val result = usecase()
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "ISendManager -> IManagerRepository.send -> IManagerRetrofitDatasource.send",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "com.google.gson.stream.MalformedJsonException: Use JsonReader.setStrictness(Strictness.LENIENT) to accept malformed JSON at line 3 column 15 path \$.idServ\n" +
                        "See https://github.com/google/gson/blob/main/Troubleshooting.md#malformed-json",
                result.exceptionOrNull()!!.cause.toString()
            )
            val resultAfter = managerSharedPreferencesDatasource.get()
            assertEquals(
                true,
                resultAfter.isSuccess
            )
            val modelAfter = resultAfter.getOrNull()!!
            assertEquals(
                StatusSend.SEND,
                modelAfter.statusSend
            )
        }

    @Test
    fun check_return_failure_if_web_service_return_error_404() =
        runTest {
            val server = MockWebServer()
            server.start()
            server.enqueue(
                MockResponse().setResponseCode(404)
            )
            BaseUrlModuleTest.url = server.url("/").toString()
            hiltRule.inject()
            configSharedPreferencesDatasource.save(
                ConfigSharedPreferencesModel(
                    number = 169,
                    password = "12345",
                    idServ = 1,
                    version = "1.0"
                )
            )
            managerSharedPreferencesDatasource.setIdRelease(
                ManagerSharedPreferencesModel(
                    idFront = 1,
                    idRelease = 1
                )
            )
            val resultBefore = managerSharedPreferencesDatasource.get()
            assertEquals(
                true,
                resultBefore.isSuccess
            )
            val modelBefore = resultBefore.getOrNull()!!
            assertEquals(
                StatusSend.SEND,
                modelBefore.statusSend
            )
            val result = usecase()
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "ISendManager -> IManagerRepository.send -> IManagerRetrofitDatasource.send",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.NullPointerException",
                result.exceptionOrNull()!!.cause.toString()
            )
            val resultAfter = managerSharedPreferencesDatasource.get()
            assertEquals(
                true,
                resultAfter.isSuccess
            )
            val modelAfter = resultAfter.getOrNull()!!
            assertEquals(
                StatusSend.SEND,
                modelAfter.statusSend
            )
        }

    @Test
    fun check_execute_usecase_if_all_process_is_success() =
        runTest {
            val server = MockWebServer()
            server.start()
            server.enqueue(
                MockResponse().setBody(result)
            )
            BaseUrlModuleTest.url = server.url("/").toString()
            hiltRule.inject()
            configSharedPreferencesDatasource.save(
                ConfigSharedPreferencesModel(
                    number = 169,
                    password = "12345",
                    idServ = 1,
                    version = "1.0"
                )
            )
            managerSharedPreferencesDatasource.setIdRelease(
                ManagerSharedPreferencesModel(
                    idFront = 1,
                    idRelease = 1
                )
            )
            val resultBefore = managerSharedPreferencesDatasource.get()
            assertEquals(
                true,
                resultBefore.isSuccess
            )
            val modelBefore = resultBefore.getOrNull()!!
            assertEquals(
                StatusSend.SEND,
                modelBefore.statusSend
            )
            val result = usecase()
            assertEquals(
                true,
                result.isSuccess
            )
            val resultAfter = managerSharedPreferencesDatasource.get()
            assertEquals(
                true,
                resultAfter.isSuccess
            )
            val modelAfter = resultAfter.getOrNull()!!
            assertEquals(
                StatusSend.SENT,
                modelAfter.statusSend
            )
        }
}