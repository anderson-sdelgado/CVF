package br.com.usinasantafe.cvf.domain.usecases.note

import br.com.usinasantafe.cvf.di.provider.BaseUrlModuleTest
import br.com.usinasantafe.cvf.external.room.dao.stable.ColabDao
import br.com.usinasantafe.cvf.infra.datasource.sharedpreferences.ConfigSharedPreferencesDatasource
import br.com.usinasantafe.cvf.infra.models.room.stable.ColabRoomModel
import br.com.usinasantafe.cvf.infra.models.sharedpreferences.ConfigSharedPreferencesModel
import br.com.usinasantafe.cvf.utils.CheckNetwork
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
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
class IHasRegColabTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var usecase: HasRegColab

    @BindValue
    @JvmField
    val checkNetwork: CheckNetwork = mock()

    @Inject
    lateinit var configSharedPreferencesDatasource: ConfigSharedPreferencesDatasource

    @Inject
    lateinit var colabDao: ColabDao

    @Before
    fun setup() {
        whenever(checkNetwork.isConnected()).thenReturn(true)
    }

    @Test
    fun check_return_failure_if_value_of_field_is_incorrect() =
        runTest {
            hiltRule.inject()
            val result = usecase("de25")
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IHasRegDriver -> stringToLong",
                result.exceptionOrNull()!!.message,
            )
            assertEquals(
                "java.lang.NumberFormatException: For input string: \"de25\"",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun check_return_failure_if_not_have_data_in_config() =
        runTest {
            hiltRule.inject()
            val result = usecase("123456")
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IHasRegDriver -> IToken -> IConfigRepository.get -> number is required",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.NullPointerException: number is required",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun check_return_failure_if_not_return_web_service() =
        runTest {
            hiltRule.inject()
            initialRegister()
            val result = usecase("123456")
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IHasRegDriver -> IColabRepository.check -> IColabRetrofitDatasource.check",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.net.ConnectException: Failed to connect to localhost/127.0.0.1:8080",
                result.exceptionOrNull()!!.cause.toString()
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

            initialRegister()
            val result = usecase("123456")
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IHasRegDriver -> IColabRepository.check -> IColabRetrofitDatasource.check",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception: Authorization header is missing",
                result.exceptionOrNull()!!.cause.toString()
            )
            server.shutdown()

        }

    @Test
    fun check_return_failure_if_data_is_incorrect() =
        runTest {

            val server = MockWebServer()
            server.start()
            server.enqueue(
                MockResponse().setBody(resultDataIncorrect)
            )
            BaseUrlModuleTest.url = server.url("/").toString()

            hiltRule.inject()

            initialRegister()
            val result = usecase("123456")
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IHasRegDriver -> IColabRepository.check -> IColabRetrofitDatasource.check",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "com.google.gson.stream.MalformedJsonException: Use JsonReader.setStrictness(Strictness.LENIENT) to accept malformed JSON at line 3 column 20 path \$.data.reg\n" +
                        "See https://github.com/google/gson/blob/main/Troubleshooting.md#malformed-json",
                result.exceptionOrNull()!!.cause.toString()
            )
            server.shutdown()

        }

    @Test
    fun check_return_true_if_data_is_existent_and_insert_table() =
        runTest {

            val server = MockWebServer()
            server.start()
            server.enqueue(
                MockResponse().setBody(resultExistent)
            )
            BaseUrlModuleTest.url = server.url("/").toString()

            hiltRule.inject()

            initialRegister()

            val result = usecase("123456")
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                true,
                result.getOrThrow()
            )

            val list = colabDao.all()
            assertEquals(
                1,
                list.size
            )
            assertEquals(
                listOf(
                    ColabRoomModel(
                        reg = 123456,
                        name = "João da Silva"
                    )
                ),
                list
            )

            server.shutdown()

        }

    @Test
    fun check_return_true_and_verify_not_insert_if_data_is_existent() =
        runTest {

            val server = MockWebServer()
            server.start()
            server.enqueue(
                MockResponse().setBody(resultExistent)
            )
            BaseUrlModuleTest.url = server.url("/").toString()

            hiltRule.inject()

            colabDao.insert(
                ColabRoomModel(
                    reg = 123456,
                    name = "João da Silva"
                )
            )

            initialRegister()

            val result = usecase("123456")
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                true,
                result.getOrThrow()
            )

            val list = colabDao.all()
            assertEquals(
                1,
                list.size
            )
            assertEquals(
                listOf(
                    ColabRoomModel(
                        reg = 123456,
                        name = "João da Silva"
                    )
                ),
                list
            )

            server.shutdown()

        }

    @Test
    fun check_return_false_if_non_existent_in_web_service_and_delete() =
        runTest {

            val server = MockWebServer()
            server.start()
            server.enqueue(
                MockResponse().setBody(resultNonExistent)
            )
            BaseUrlModuleTest.url = server.url("/").toString()

            hiltRule.inject()

            colabDao.insertAll(
                listOf(
                    ColabRoomModel(
                        reg = 123456,
                        name = "João da Silva"
                    ),
                    ColabRoomModel(
                        reg = 456789,
                        name = "Maria de Souza"
                    )
                )
            )

            val qtdBefore = colabDao.all().size
            assertEquals(
                2,
                qtdBefore
            )

            initialRegister()

            val result = usecase("123456")
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                false,
                result.getOrThrow()
            )

            val list = colabDao.all()
            assertEquals(
                1,
                list.size
            )
            assertEquals(
                listOf(
                    ColabRoomModel(
                        reg = 456789,
                        name = "Maria de Souza"
                    )
                ),
                list
            )

            server.shutdown()

        }

    @Test
    fun check_return_true_if_web_service_return_timeout_and_existent_in_table_room()=
        runTest(
            timeout = 30.seconds
        ) {

            val server = MockWebServer()
            server.start()
            server.enqueue(
                MockResponse().setBody(resultFailureAuthorization)
                    .setBodyDelay(15, TimeUnit.SECONDS)
            )
            BaseUrlModuleTest.url = server.url("/").toString()

            hiltRule.inject()

            colabDao.insertAll(
                listOf(
                    ColabRoomModel(
                        reg = 123456,
                        name = "João da Silva"
                    ),
                    ColabRoomModel(
                        reg = 456789,
                        name = "Maria de Souza"
                    )
                )
            )

            val qtdBefore = colabDao.all().size
            assertEquals(
                2,
                qtdBefore
            )

            initialRegister()

            val result = usecase("123456")
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                true,
                result.getOrThrow()
            )

            val list = colabDao.all()
            assertEquals(
                2,
                list.size
            )
            assertEquals(
                listOf(
                    ColabRoomModel(
                        reg = 123456,
                        name = "João da Silva"
                    ),
                    ColabRoomModel(
                        reg = 456789,
                        name = "Maria de Souza"
                    )
                ),
                list
            )

            server.shutdown()

        }

    @Test
    fun check_return_false_if_web_service_return_timeout_and_non_existent_in_table_room()=
        runTest(
            timeout = 30.seconds
        ) {

            val server = MockWebServer()
            server.start()
            server.enqueue(
                MockResponse().setBody(resultFailureAuthorization)
                    .setBodyDelay(15, TimeUnit.SECONDS)
            )
            BaseUrlModuleTest.url = server.url("/").toString()

            hiltRule.inject()

            colabDao.insertAll(
                listOf(
                    ColabRoomModel(
                        reg = 123456,
                        name = "João da Silva"
                    ),
                    ColabRoomModel(
                        reg = 456789,
                        name = "Maria de Souza"
                    )
                )
            )

            val qtdBefore = colabDao.all().size
            assertEquals(
                2,
                qtdBefore
            )

            initialRegister()

            val result = usecase("234567")
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                false,
                result.getOrThrow()
            )

            val list = colabDao.all()
            assertEquals(
                2,
                list.size
            )
            assertEquals(
                listOf(
                    ColabRoomModel(
                        reg = 123456,
                        name = "João da Silva"
                    ),
                    ColabRoomModel(
                        reg = 456789,
                        name = "Maria de Souza"
                    )
                ),
                list
            )

            server.shutdown()

        }

    @Test
    fun check_return_true_if_no_connection_and_existent_in_table_room() =
        runTest {
            whenever(checkNetwork.isConnected()).thenReturn(false)
            hiltRule.inject()

            colabDao.insert(
                ColabRoomModel(
                    reg = 123456,
                    name = "João da Silva"
                )
            )

            val result = usecase("123456")
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                true,
                result.getOrThrow()
            )
        }

    @Test
    fun check_return_false_if_no_connection_and_no_existent_in_table_room() =
        runTest {
            whenever(checkNetwork.isConnected()).thenReturn(false)
            hiltRule.inject()

            val result = usecase("123456")
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                false,
                result.getOrThrow()
            )
        }

    private val resultFailureAuthorization = """
        {
            "status": "error",
            "failure": "Authorization header is missing"
        }
    """.trimIndent()

    private val resultDataIncorrect = """
        {
            "status": "success",
            "data": {"reg":sad457,"name":"João da Silva"}
        }
    """.trimIndent()

    private val resultExistent = """
        {
            "status": "success",
            "data": {"reg":123456,"name":"João da Silva"}
        }
    """.trimIndent()

    private val resultNonExistent = """
        {
            "status": "success",
            "data": {"reg":0,"name":"Non Existent"}
        }
    """.trimIndent()

    private suspend fun initialRegister() {
        configSharedPreferencesDatasource.save(
            ConfigSharedPreferencesModel(
                number = 16997417840,
                version = "1.00",
                password = "12345",
                idServ = 1
            )
        )
    }
}