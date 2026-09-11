package br.com.usinasantafe.cvf.domain.usecases.note

import br.com.usinasantafe.cvf.di.provider.BaseUrlModuleTest
import br.com.usinasantafe.cvf.external.room.dao.stable.EquipDao
import br.com.usinasantafe.cvf.infra.datasource.sharedpreferences.ConfigSharedPreferencesDatasource
import br.com.usinasantafe.cvf.infra.models.room.stable.EquipRoomModel
import br.com.usinasantafe.cvf.infra.models.sharedpreferences.ConfigSharedPreferencesModel
import br.com.usinasantafe.cvf.lib.TypeEquip
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
class IHasNroEquipTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var usecase: HasNroEquip

    @BindValue
    @JvmField
    val checkNetwork: CheckNetwork = mock()

    @Inject
    lateinit var configSharedPreferencesDatasource: ConfigSharedPreferencesDatasource

    @Inject
    lateinit var equipDao: EquipDao

    @Before
    fun setup() {
        whenever(checkNetwork.isConnected()).thenReturn(true)
    }

    @Test
    fun check_return_failure_if_value_of_field_is_incorrect() =
        runTest {
            hiltRule.inject()
            val result = usecase("de25", 2)
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IHasNroCart -> stringToInt",
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
            val result = usecase("123456", 3)
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IHasNroCart -> IToken -> IConfigRepository.get -> number is required",
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
            val result = usecase("123456", 1)
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IHasNroCart -> IEquipRepository.check -> IEquipRetrofitDatasource.checkByNro",
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
            val result = usecase("123456", 2)
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IHasNroCart -> IEquipRepository.check -> IEquipRetrofitDatasource.checkByNro",
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
            val result = usecase("123456", 3)
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IHasNroCart -> IEquipRepository.check -> IEquipRetrofitDatasource.checkByNro",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "com.google.gson.stream.MalformedJsonException: Use JsonReader.setStrictness(Strictness.LENIENT) to accept malformed JSON at line 3 column 19 path \$.data.id\n" +
                        "See https://github.com/google/gson/blob/main/Troubleshooting.md#malformed-json",
                result.exceptionOrNull()!!.cause.toString()
            )
            server.shutdown()

        }

    @Test
    fun check_return_true_if_data_is_existent_and_insert_table_and_type_is_truck() =
        runTest {

            val server = MockWebServer()
            server.start()
            server.enqueue(
                MockResponse().setBody(resultExistentTruck)
            )
            BaseUrlModuleTest.url = server.url("/").toString()

            hiltRule.inject()

            initialRegister()

            val result = usecase("123456", 0)
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                true,
                result.getOrThrow()
            )
            val list = equipDao.all()
            assertEquals(
                1,
                list.size
            )
            assertEquals(
                listOf(
                    EquipRoomModel(
                        id = 1,
                        nro = 1,
                        cdOperClass = 1,
                        descOperClass = "Equip1",
                        type = TypeEquip.TRUCK
                    )
                ),
                list
            )

            server.shutdown()

        }

    @Test
    fun check_return_true_if_data_is_existent_and_insert_table_and_type_is_cart() =
        runTest {

            val server = MockWebServer()
            server.start()
            server.enqueue(
                MockResponse().setBody(resultExistentCart)
            )
            BaseUrlModuleTest.url = server.url("/").toString()

            hiltRule.inject()

            initialRegister()

            val result = usecase("123456", 2)
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                true,
                result.getOrThrow()
            )
            val list = equipDao.all()
            assertEquals(
                1,
                list.size
            )
            assertEquals(
                listOf(
                    EquipRoomModel(
                        id = 1,
                        nro = 1,
                        cdOperClass = 1,
                        descOperClass = "Equip1",
                        type = TypeEquip.CART
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
                MockResponse().setBody(resultExistentCart)
            )
            BaseUrlModuleTest.url = server.url("/").toString()

            hiltRule.inject()

            equipDao.insert(
                EquipRoomModel(
                    id = 1,
                    nro = 1,
                    cdOperClass = 1,
                    descOperClass = "Equip1",
                    type = TypeEquip.CART
                )
            )

            initialRegister()

            val result = usecase("123456", 2)
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                true,
                result.getOrThrow()
            )

            val list = equipDao.all()
            assertEquals(
                1,
                list.size
            )
            assertEquals(
                listOf(
                    EquipRoomModel(
                        id = 1,
                        nro = 1,
                        cdOperClass = 1,
                        descOperClass = "Equip1",
                        type = TypeEquip.CART
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

            equipDao.insertAll(
                listOf(
                    EquipRoomModel(
                        id = 1,
                        nro = 100,
                        cdOperClass = 1,
                        descOperClass = "Equip1",
                        type = TypeEquip.CART
                    ),
                    EquipRoomModel(
                        id = 2,
                        nro = 2,
                        cdOperClass = 2,
                        descOperClass = "Equip2",
                        type = TypeEquip.TRUCK
                    )
                )
            )

            val qtdBefore = equipDao.all().size
            assertEquals(
                2,
                qtdBefore
            )

            initialRegister()

            val result = usecase("100", 2)
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                false,
                result.getOrThrow()
            )

            val list = equipDao.all()
            assertEquals(
                1,
                list.size
            )
            assertEquals(
                listOf(
                    EquipRoomModel(
                        id = 2,
                        nro = 2,
                        cdOperClass = 2,
                        descOperClass = "Equip2",
                        type = TypeEquip.TRUCK
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

            equipDao.insertAll(
                listOf(
                    EquipRoomModel(
                        id = 1,
                        nro = 100,
                        cdOperClass = 1,
                        descOperClass = "Equip1",
                        type = TypeEquip.CART
                    ),
                    EquipRoomModel(
                        id = 2,
                        nro = 2,
                        cdOperClass = 2,
                        descOperClass = "Equip2",
                        type = TypeEquip.TRUCK
                    )
                )
            )

            val qtdBefore = equipDao.all().size
            assertEquals(
                2,
                qtdBefore
            )

            initialRegister()

            val result = usecase("100", 3)
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                true,
                result.getOrThrow()
            )

            val list = equipDao.all()
            assertEquals(
                2,
                list.size
            )
            assertEquals(
                listOf(
                    EquipRoomModel(
                        id = 1,
                        nro = 100,
                        cdOperClass = 1,
                        descOperClass = "Equip1",
                        type = TypeEquip.CART
                    ),
                    EquipRoomModel(
                        id = 2,
                        nro = 2,
                        cdOperClass = 2,
                        descOperClass = "Equip2",
                        type = TypeEquip.TRUCK
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

            equipDao.insertAll(
                listOf(
                    EquipRoomModel(
                        id = 1,
                        nro = 100,
                        cdOperClass = 1,
                        descOperClass = "Equip1",
                        type = TypeEquip.CART
                    ),
                    EquipRoomModel(
                        id = 2,
                        nro = 2,
                        cdOperClass = 2,
                        descOperClass = "Equip2",
                        type = TypeEquip.TRUCK
                    )
                )
            )

            val qtdBefore = equipDao.all().size
            assertEquals(
                2,
                qtdBefore
            )

            initialRegister()

            val result = usecase("1026", 3)
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                false,
                result.getOrThrow()
            )

            val list = equipDao.all()
            assertEquals(
                2,
                list.size
            )
            assertEquals(
                listOf(
                    EquipRoomModel(
                        id = 1,
                        nro = 100,
                        cdOperClass = 1,
                        descOperClass = "Equip1",
                        type = TypeEquip.CART
                    ),
                    EquipRoomModel(
                        id = 2,
                        nro = 2,
                        cdOperClass = 2,
                        descOperClass = "Equip2",
                        type = TypeEquip.TRUCK
                    )
                ),
                list
            )

        }

    @Test
    fun check_return_true_if_no_connection_and_existent_in_table_room() =
        runTest {
            whenever(checkNetwork.isConnected()).thenReturn(false)
            hiltRule.inject()

            equipDao.insert(
                EquipRoomModel(
                    id = 1,
                    nro = 100,
                    cdOperClass = 1,
                    descOperClass = "Equip1",
                    type = TypeEquip.CART
                )
            )

            val result = usecase("100", 3)
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
    fun check_return_false_if_no_connection_and_existent_in_table_room() =
        runTest {
            whenever(checkNetwork.isConnected()).thenReturn(false)
            hiltRule.inject()

            equipDao.insert(
                EquipRoomModel(
                    id = 1,
                    nro = 100,
                    cdOperClass = 1,
                    descOperClass = "Equip1",
                    type = TypeEquip.CART
                )
            )

            val result = usecase("1570", 3)
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
            "data": {"id":we1dsaf,"nro":1,"cdOperClass":1,"descOperClass":"Equip1","type":1}
        }
    """.trimIndent()

    private val resultExistentTruck = """
        {
            "status": "success",
            "data": {"id":1,"nro":1,"cdOperClass":1,"descOperClass":"Equip1","type":1}
        }
    """.trimIndent()

    private val resultExistentCart = """
        {
            "status": "success",
            "data": {"id":1,"nro":1,"cdOperClass":1,"descOperClass":"Equip1","type":2}
        }
    """.trimIndent()

    private val resultNonExistent = """
        {
            "status": "success",
            "data": {"id":0,"nro":0,"cdOperClass":0,"descOperClass":"Non Existent","type":0}
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