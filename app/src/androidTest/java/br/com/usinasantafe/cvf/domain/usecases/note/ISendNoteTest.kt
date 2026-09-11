package br.com.usinasantafe.cvf.domain.usecases.note

import android.content.SharedPreferences
import br.com.usinasantafe.cvf.di.provider.BaseUrlModuleTest
import br.com.usinasantafe.cvf.external.room.dao.DatabaseRoom
import br.com.usinasantafe.cvf.external.room.dao.variable.CartDao
import br.com.usinasantafe.cvf.external.room.dao.variable.HeaderDao
import br.com.usinasantafe.cvf.infra.datasource.sharedpreferences.ConfigSharedPreferencesDatasource
import br.com.usinasantafe.cvf.infra.models.room.variable.CartRoomModel
import br.com.usinasantafe.cvf.infra.models.room.variable.HeaderRoomModel
import br.com.usinasantafe.cvf.infra.models.sharedpreferences.ConfigSharedPreferencesModel
import br.com.usinasantafe.cvf.lib.StatusSend
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import kotlin.test.assertEquals

@HiltAndroidTest
class ISendNoteTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var usecase: SendNote

    @Inject
    lateinit var configSharedPreferencesDatasource: ConfigSharedPreferencesDatasource

    @Inject
    lateinit var headerDao: HeaderDao

    @Inject
    lateinit var cartDao: CartDao

    @Inject
    lateinit var db: DatabaseRoom

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    @Test
    fun check_return_failure_if_config_shared_preferences_not_have_data() =
        runTest {

            hiltRule.inject()
            val result = usecase()
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "ISendNote -> IToken -> IConfigRepository.get -> number is required",
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
            val result = usecase()
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "ISendNote -> INoteRepository.send -> INoteRetrofitDatasource.send",
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
            val result = usecase()
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "ISendNote -> INoteRepository.send -> INoteRetrofitDatasource.send",
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
            val result = usecase()
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "ISendNote -> INoteRepository.send -> INoteRetrofitDatasource.send",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "com.google.gson.stream.MalformedJsonException: Use JsonReader.setStrictness(Strictness.LENIENT) to accept malformed JSON at line 5 column 15 path \$.data[0].id\n" +
                        "See https://github.com/google/gson/blob/main/Troubleshooting.md#malformed-json",
                result.exceptionOrNull()!!.cause.toString()
            )
            server.shutdown()

        }

    @Test
    fun check_altered_data_if_process_executed_success() =
        runTest {

            val server = MockWebServer()
            server.start()
            server.enqueue(
                MockResponse().setBody(resultSuccess)
            )
            BaseUrlModuleTest.url = server.url("/").toString()

            hiltRule.inject()

            initialRegister()
            val result = usecase()
            assertEquals(
                true,
                result.isSuccess
            )
            val list = headerDao.all()
            assertEquals(
                2,
                list.size
            )
            val model1 = list[0]
            assertEquals(
                HeaderRoomModel(
                    id = 1,
                    regDriver = model1.regDriver,
                    idTruck = model1.idTruck,
                    idServ = 150,
                    statusSend = StatusSend.SENT,
                ).copy(dateHour = model1.dateHour),
                model1
            )
            val model2 = list[1]
            assertEquals(
                HeaderRoomModel(
                    id = 2,
                    regDriver = model2.regDriver,
                    idTruck = model2.idTruck,
                    idServ = 169,
                    statusSend = StatusSend.SENT,
                ).copy(dateHour = model2.dateHour),
                model2
            )
            server.shutdown()
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
            "data": 
            [
                {"id":we1dsaf,"idServ":1}
            }
        }
    """.trimIndent()

    private val resultSuccess = """
        {
            "status": "success",
            "data": 
            [
                {"id":1,"idServ":150},
                {"id":2,"idServ":169}
            ]
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
        headerDao.insert(
            HeaderRoomModel(
                regDriver = 19759,
                idTruck = 120,
            )
        )
        headerDao.insert(
            HeaderRoomModel(
                regDriver = 18017,
                idTruck = 121,
            )
        )
        cartDao.insert(
            CartRoomModel(
                idHeader = 1,
                position = 1,
                idCart = 10
            )
        )
        cartDao.insert(
            CartRoomModel(
                idHeader = 1,
                position = 2,
                idCart = 11
            )
        )
        cartDao.insert(
            CartRoomModel(
                idHeader = 2,
                position = 1,
                idCart = 12
            )
        )
    }
}