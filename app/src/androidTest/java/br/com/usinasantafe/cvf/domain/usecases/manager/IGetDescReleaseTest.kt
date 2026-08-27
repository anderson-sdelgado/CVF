package br.com.usinasantafe.cvf.domain.usecases.manager

import br.com.usinasantafe.cvf.external.room.dao.stable.ReleaseDao
import br.com.usinasantafe.cvf.external.sharedPreferences.IManagerSharedPreferencesDatasource
import br.com.usinasantafe.cvf.infra.models.room.stable.ReleaseRoomModel
import br.com.usinasantafe.cvf.infra.models.sharedpreferences.ManagerSharedPreferencesModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import kotlin.test.assertEquals

@HiltAndroidTest
class IGetDescReleaseTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var usecase: GetDescRelease

    @Inject
    lateinit var releaseDao: ReleaseDao

    @Inject
    lateinit var managerSharedPreferencesDatasource: IManagerSharedPreferencesDatasource

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun check_return_failure_if_not_have_data_in_manager() =
        runTest {
            val result = usecase()
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IGetDescRelease -> idRelease is required",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.NullPointerException: idRelease is required",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun check_return_failure_if_not_have_data_in_release() =
        runTest {
            managerSharedPreferencesDatasource.setIdRelease(1)
            val result = usecase()
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IGetDescRelease -> IReleaseRepository.getById -> IReleaseRoomDatasource.getById",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.IllegalStateException: The query result was empty, but expected a single row to return a NON-NULL object of type 'br.com.usinasantafe.cvf.infra.models.room.stable.ReleaseRoomModel'.",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun check_return_correct_if_function_execute_successfully() =
        runTest {
            managerSharedPreferencesDatasource.setIdRelease(1)
            releaseDao.insertAll(
                listOf(
                    ReleaseRoomModel(
                        id = 1,
                        nroOS = 1,
                        idPropAgr = 1,
                        descPropAgr = "Test1",
                        idFront = 1
                    ),
                    ReleaseRoomModel(
                        id = 2,
                        nroOS = 2,
                        idPropAgr = 2,
                        descPropAgr = "Test2",
                        idFront = 1
                    ),
                    ReleaseRoomModel(
                        id = 3,
                        nroOS = 3,
                        idPropAgr = 3,
                        descPropAgr = "Test3",
                        idFront = 1
                    ),
                )
            )
            val result = usecase()
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                "LIBERAÇÃO: 2\nO.S.: 2\nPROPRIEDADE: Test2",
                result.getOrNull()!!
            )
        }

}