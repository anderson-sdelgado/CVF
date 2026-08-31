package br.com.usinasantafe.cvf.domain.usecases.manager

import br.com.usinasantafe.cvf.external.room.dao.stable.FrontDao
import br.com.usinasantafe.cvf.external.room.dao.stable.ReleaseDao
import br.com.usinasantafe.cvf.external.sharedPreferences.IManagerSharedPreferencesDatasource
import br.com.usinasantafe.cvf.infra.models.room.stable.FrontRoomModel
import br.com.usinasantafe.cvf.infra.models.room.stable.ReleaseRoomModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import kotlin.test.assertEquals

@HiltAndroidTest
class IGetTitleMenuTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var usecase: GetTitleMenu

    @Inject
    lateinit var releaseDao: ReleaseDao

    @Inject
    lateinit var frontDao: FrontDao

    @Inject
    lateinit var managerSharedPreferencesDatasource: IManagerSharedPreferencesDatasource

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun check_return_failure_if_not_have_idRelease_in_manager() =
        runTest {
            val result = usecase()
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IGetTitleMenu -> idRelease is required",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.NullPointerException: idRelease is required",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun check_return_failure_if_not_have_idFront_in_manager() =
        runTest {
            managerSharedPreferencesDatasource.setIdRelease(1)
            val result = usecase()
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IGetTitleMenu -> idFront is required",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.NullPointerException: idFront is required",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun check_return_failure_if_not_have_data_in_release() =
        runTest {
            managerSharedPreferencesDatasource.setIdRelease(1)
            managerSharedPreferencesDatasource.setIdFront(2)
            val result = usecase()
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IGetTitleMenu -> IReleaseRepository.getById -> IReleaseRoomDatasource.getById",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.IllegalStateException: The query result was empty, but expected a single row to return a NON-NULL object of type 'br.com.usinasantafe.cvf.infra.models.room.stable.ReleaseRoomModel'.",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun check_return_failure_if_not_have_data_in_front() =
        runTest {
            managerSharedPreferencesDatasource.setIdRelease(1)
            managerSharedPreferencesDatasource.setIdFront(2)
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
                result.isFailure
            )
            assertEquals(
                "IGetTitleMenu -> IFrontRepository.getById -> IFrontRoomDatasource.getById",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.IllegalStateException: The query result was empty, but expected a single row to return a NON-NULL object of type 'br.com.usinasantafe.cvf.infra.models.room.stable.FrontRoomModel'.",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun check_return_correct_if_have_data() =
        runTest {
            managerSharedPreferencesDatasource.setIdRelease(1)
            managerSharedPreferencesDatasource.setIdFront(2)
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
            frontDao.insertAll(
                listOf(
                    FrontRoomModel(
                        id = 1,
                        cd = 1,
                        description = "Test1"
                    ),
                    FrontRoomModel(
                        id = 2,
                        cd = 2,
                        description = "Test2"
                    ),
                    FrontRoomModel(
                        id = 3,
                        cd = 3,
                        description = "Test3"
                    ),
                )
            )
            val result = usecase()
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                "FRENTE: Test2\nLIBERAÇÃO: 1\nO.S.: 1\nPROPRIEDADE: Test1",
                result.getOrNull()!!
            )
        }

}