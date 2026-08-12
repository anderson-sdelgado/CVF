package br.com.usinasantafe.cvf.domain.usecases.config

import br.com.usinasantafe.cvf.di.provider.BaseUrlModuleTest
import br.com.usinasantafe.cvf.external.sharedPreferences.IManagerSharedPreferencesDatasource
import br.com.usinasantafe.cvf.infra.datasource.sharedpreferences.ConfigSharedPreferencesDatasource
import br.com.usinasantafe.cvf.infra.models.sharedpreferences.ConfigSharedPreferencesModel
import br.com.usinasantafe.cvf.infra.models.sharedpreferences.ManagerSharedPreferencesModel
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
class IUpdateConfigTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var usecase: UpdateConfig

    @Inject
    lateinit var configSharedPreferencesDatasource: ConfigSharedPreferencesDatasource

    @Inject
    lateinit var managerSharedPreferencesDatasource: IManagerSharedPreferencesDatasource

    @Test
    fun check_return_failure_if_number_is_incorrect() =
        runTest {

            hiltRule.inject()

            val result = usecase(
                number = "dfasdfsda",
                password = "12345",
                version = "1.00",
                sizeAll = 3f,
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
                    levelUpdate = LevelUpdate.GET_TOKEN,
                    currentProgress = updatePercentage(1f, 1f, 3f)
                ),
                list[0]
            )
            assertEquals(
                UiStatusStateUpdate(
                    errors = Errors.TOKEN,
                    flagDialog = true,
                    flagFailure = true,
                    failure = "IUpdateConfig -> toLong -> java.lang.NumberFormatException: For input string: \"dfasdfsda\"",
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

            val result = usecase(
                number = "16997417840",
                password = "12345",
                version = "1.00",
                sizeAll = 3f,
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
                    levelUpdate = LevelUpdate.GET_TOKEN,
                    currentProgress = updatePercentage(1f, 1f, 3f)
                ),
                list[0]
            )
            assertEquals(
                UiStatusStateUpdate(
                    errors = Errors.TOKEN,
                    flagDialog = true,
                    flagFailure = true,
                    failure = "IUpdateConfig -> IConfigRepository.send -> IConfigRetrofitDatasource.recoverToken -> java.net.ConnectException: Failed to connect to localhost/127.0.0.1:8080",
                    currentProgress = 1f,
                    levelUpdate = null,
                ),
                list[1]
            )
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

            val result = usecase(
                number = "16997417840",
                password = "12345",
                version = "1.00",
                sizeAll = 3f,
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
                    levelUpdate = LevelUpdate.GET_TOKEN,
                    currentProgress = updatePercentage(1f, 1f, 3f)
                ),
                list[0]
            )
            assertEquals(
                UiStatusStateUpdate(
                    errors = Errors.TOKEN,
                    flagDialog = true,
                    flagFailure = true,
                    failure = "IUpdateConfig -> IConfigRepository.send -> IConfigRetrofitDatasource.recoverToken -> java.lang.NullPointerException",
                    currentProgress = 1f,
                    levelUpdate = null,
                ),
                list[1]
            )
        }

    @Test
    fun verify_return_data_if_success_usecase() =
        runTest {

            val response = """
                {
                    "idServ": 16
                }
            """
            val mockWebServer = MockWebServer()
            mockWebServer.start()
            mockWebServer.enqueue(
                MockResponse().setBody(response)
            )
            BaseUrlModuleTest.url = mockWebServer.url("/").toString()

            hiltRule.inject()

            managerSharedPreferencesDatasource.save(
                ManagerSharedPreferencesModel(
                    idFront = 1,
                    idRelease = 1
                )
            )

            val result = usecase(
                number = "16997417840",
                password = "12345",
                version = "1.00",
                sizeAll = 3f,
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
                    levelUpdate = LevelUpdate.GET_TOKEN,
                    currentProgress = updatePercentage(1f, 1f, 3f)
                ),
                list[0]
            )
            assertEquals(
                UiStatusStateUpdate(
                    flagProgress = true,
                    levelUpdate = LevelUpdate.SAVE_TOKEN,
                    currentProgress = updatePercentage(2f, 1f, 3f)
                ),
                list[1]
            )
            assertEquals(
                UiStatusStateUpdate(
                    flagProgress = true,
                    levelUpdate = LevelUpdate.FINISH_UPDATE_INITIAL,
                    currentProgress = updatePercentage(3f, 1f, 3f)
                ),
                list[2]
            )

            val configModel = configSharedPreferencesDatasource.get().getOrThrow()
            assertEquals(
                ConfigSharedPreferencesModel(
                    number = 16997417840,
                    password = "12345",
                    idServ = 16,
                    version = "1.00",
                ),
                configModel
            )
            val flagManager = managerSharedPreferencesDatasource.has().getOrThrow()
            assertEquals(
                false,
                flagManager
            )
        }

}