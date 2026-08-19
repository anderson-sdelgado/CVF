package br.com.usinasantafe.cvf.domain.usecases.update

import br.com.usinasantafe.cvf.di.provider.BaseUrlModuleTest
import br.com.usinasantafe.cvf.external.room.dao.stable.FrontDao
import br.com.usinasantafe.cvf.infra.datasource.sharedpreferences.ConfigSharedPreferencesDatasource
import br.com.usinasantafe.cvf.infra.models.room.stable.FrontRoomModel
import br.com.usinasantafe.cvf.infra.models.sharedpreferences.ConfigSharedPreferencesModel
import br.com.usinasantafe.cvf.lib.Errors
import br.com.usinasantafe.cvf.lib.LevelUpdate
import br.com.usinasantafe.cvf.utils.UiStatusStateUpdate
import br.com.usinasantafe.cvf.utils.updatePercentage
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import kotlin.test.assertEquals

@HiltAndroidTest
class IUpdateTableFrontTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var usecase: UpdateTableFront

    @Inject
    lateinit var configSharedPreferencesDatasource: ConfigSharedPreferencesDatasource

    @Inject
    lateinit var equipDao: FrontDao

    @Test
    fun check_return_failure_if_not_have_data_config_internal() =
        runTest {

            hiltRule.inject()

            val result = usecase(
                sizeAll = 16f,
                count = 1f
            )
            val list = result.toList()
            assertEquals(
                2,
                list.count()
            )
            assertEquals(
                UiStatusStateUpdate(
                    flagProgress = true,
                    levelUpdate = LevelUpdate.RECOVERY,
                    tableUpdate = "tb_front",
                    currentProgress = updatePercentage(1f, 1f, 16f)
                ),
                list[0]
            )
            assertEquals(
                UiStatusStateUpdate(
                    errors = Errors.UPDATE,
                    flagDialog = true,
                    flagFailure = true,
                    failure = "IUpdateTableFront -> IFrontRepository.listAll -> IFrontRetrofitDatasource.listAll -> IGetToken -> IConfigRepository.get -> number is required -> java.lang.NullPointerException: number is required",
                    currentProgress = 1f,
                    levelUpdate = null,
                ),
                list[1]
            )

        }

    @Test
    fun check_return_failure_if_not_return_web_service() =
        runTest {

            hiltRule.inject()

            initialRegister()

            val result = usecase(
                sizeAll = 16f,
                count = 1f
            )
            val list = result.toList()
            assertEquals(
                2,
                list.count()
            )
            assertEquals(
                UiStatusStateUpdate(
                    flagProgress = true,
                    levelUpdate = LevelUpdate.RECOVERY,
                    tableUpdate = "tb_front",
                    currentProgress = updatePercentage(1f, 1f, 16f)
                ),
                list[0]
            )
            assertEquals(
                UiStatusStateUpdate(
                    errors = Errors.UPDATE,
                    flagDialog = true,
                    flagFailure = true,
                    failure = "IUpdateTableFront -> IFrontRepository.listAll -> IFrontRetrofitDatasource.listAll -> java.net.ConnectException: Failed to connect to localhost/127.0.0.1:8080",
                    currentProgress = 1f,
                    levelUpdate = null,
                ),
                list[1]
            )
        }

    @Test
    fun check_return_failure_if_error_url() =
        runTest {

            val server = MockWebServer()
            server.start()
            server.enqueue(
                MockResponse().setBody("{ \"status\": \"error\", \"failure\": \"Authorization header is missing\" }")
            )
            BaseUrlModuleTest.url = server.url("/").toString()

            hiltRule.inject()

            initialRegister()

            val result = usecase(
                sizeAll = 16f,
                count = 1f
            )
            val list = result.toList()
            assertEquals(
                2,
                list.count()
            )
            assertEquals(
                UiStatusStateUpdate(
                    flagProgress = true,
                    levelUpdate = LevelUpdate.RECOVERY,
                    tableUpdate = "tb_front",
                    currentProgress = updatePercentage(1f, 1f, 16f)
                ),
                list[0]
            )
            assertEquals(
                UiStatusStateUpdate(
                    errors = Errors.UPDATE,
                    flagDialog = true,
                    flagFailure = true,
                    failure = "IUpdateTableFront -> IFrontRepository.listAll -> IFrontRetrofitDatasource.listAll -> java.lang.Exception: Authorization header is missing",
                    currentProgress = 1f,
                    levelUpdate = null,
                ),
                list[1]
            )
            server.shutdown()
        }

    @Test
    fun check_return_failure_if_have_error_404() =
        runTest {

            val server = MockWebServer()
            server.start()
            server.enqueue(
                MockResponse().setResponseCode(404)
            )
            BaseUrlModuleTest.url = server.url("/").toString()

            hiltRule.inject()

            initialRegister()

            val result = usecase(
                sizeAll = 16f,
                count = 1f
            )
            val list = result.toList()
            assertEquals(
                2,
                list.count()
            )
            assertEquals(
                UiStatusStateUpdate(
                    flagProgress = true,
                    levelUpdate = LevelUpdate.RECOVERY,
                    tableUpdate = "tb_front",
                    currentProgress = updatePercentage(1f, 1f, 16f)
                ),
                list[0]
            )
            assertEquals(
                UiStatusStateUpdate(
                    errors = Errors.UPDATE,
                    flagDialog = true,
                    flagFailure = true,
                    failure = "IUpdateTableFront -> IFrontRepository.listAll -> IFrontRetrofitDatasource.listAll -> java.lang.NullPointerException",
                    currentProgress = 1f,
                    levelUpdate = null,
                ),
                list[1]
            )
            server.shutdown()
        }

    @Test
    fun check_return_failure_if_row_is_repeated() =
        runTest {
            val response = """
                {
                    "status": "success",
                    "data": [
                      {"id":1,"cd":1,"description":"Test1"},
                      {"id":1,"cd":1,"description":"Test1"}
                    ]
                }
            """
            val server = MockWebServer()
            server.start()
            server.enqueue(
                MockResponse().setBody(response)
            )
            BaseUrlModuleTest.url = server.url("/").toString()

            hiltRule.inject()

            initialRegister()

            val result = usecase(
                sizeAll = 16f,
                count = 1f
            )
            val list = result.toList()
            assertEquals(
                4,
                list.count()
            )
            assertEquals(
                UiStatusStateUpdate(
                    flagProgress = true,
                    levelUpdate = LevelUpdate.RECOVERY,
                    tableUpdate = "tb_front",
                    currentProgress = updatePercentage(1f, 1f, 16f)
                ),
                list[0]
            )
            assertEquals(
                UiStatusStateUpdate(
                    flagProgress = true,
                    levelUpdate = LevelUpdate.CLEAN,
                    tableUpdate = "tb_front",
                    currentProgress = updatePercentage(2f, 1f, 16f)
                ),
                list[1]
            )
            assertEquals(
                UiStatusStateUpdate(
                    flagProgress = true,
                    levelUpdate = LevelUpdate.SAVE,
                    tableUpdate = "tb_front",
                    currentProgress = updatePercentage(3f, 1f, 16f)
                ),
                list[2]
            )
            assertEquals(
                UiStatusStateUpdate(
                    errors = Errors.UPDATE,
                    flagDialog = true,
                    flagFailure = true,
                    failure = "IUpdateTableFront -> IFrontRepository.addAll -> IFrontRoomDatasource.addAll -> android.database.sqlite.SQLiteConstraintException: UNIQUE constraint failed: tb_front.id (code 1555 SQLITE_CONSTRAINT_PRIMARYKEY[1555])",
                    currentProgress = 1f,
                    levelUpdate = null,
                ),
                list[3]
            )
            server.shutdown()
        }

    @Test
    fun verify_return_data_if_success_usecase() =
        runTest {

            val response = """
                {
                    "status": "success",
                    "data": [
                      {"id":1,"cd":1,"description":"Test1"},
                      {"id":2,"cd":2,"description":"Test2"}
                    ]
                }
            """
            val mockWebServer = MockWebServer()
            mockWebServer.start()
            mockWebServer.enqueue(
                MockResponse().setBody(response)
            )
            BaseUrlModuleTest.url = mockWebServer.url("/").toString()

            hiltRule.inject()

            var pos = 0f

            initialRegister()

            val result = usecase(
                sizeAll = 16f,
                count = 1f
            )
            val list = result.toList()
            assertEquals(
                3,
                list.count()
            )
            assertEquals(
                UiStatusStateUpdate(
                    flagProgress = true,
                    levelUpdate = LevelUpdate.RECOVERY,
                    tableUpdate = "tb_front",
                    currentProgress = updatePercentage(++pos, 1f, 16f)
                ),
                list[0]
            )
            assertEquals(
                UiStatusStateUpdate(
                    flagProgress = true,
                    levelUpdate = LevelUpdate.CLEAN,
                    tableUpdate = "tb_front",
                    currentProgress = updatePercentage(++pos, 1f, 16f)
                ),
                list[1]
            )
            assertEquals(
                UiStatusStateUpdate(
                    flagProgress = true,
                    levelUpdate = LevelUpdate.SAVE,
                    tableUpdate = "tb_front",
                    currentProgress = updatePercentage(++pos, 1f, 16f)
                ),
                list[2]
            )
            val modelList = equipDao.all()
            assertEquals(
                modelList.size,
                2
            )
            val model1 = modelList[0]
            assertEquals(
                FrontRoomModel(
                    id = 1,
                    cd = 1,
                    description = "Test1"
                ),
                model1
            )
            val model2 = modelList[1]
            assertEquals(
                FrontRoomModel(
                    id = 2,
                    cd = 2,
                    description = "Test2"
                ),
                model2
            )
            mockWebServer.shutdown()
        }

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