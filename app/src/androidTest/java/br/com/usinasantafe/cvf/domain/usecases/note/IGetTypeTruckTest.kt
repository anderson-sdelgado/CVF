package br.com.usinasantafe.cvf.domain.usecases.note

import br.com.usinasantafe.cvf.external.room.dao.stable.EquipDao
import br.com.usinasantafe.cvf.external.sharedPreferences.IHeaderSharedPreferencesDatasource
import br.com.usinasantafe.cvf.infra.models.room.stable.EquipRoomModel
import br.com.usinasantafe.cvf.lib.TypeEquip
import br.com.usinasantafe.cvf.lib.TypeTruck
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import kotlin.test.assertEquals

@HiltAndroidTest
class IGetTypeTruckTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var usecase: GetTypeTruck

    @Inject
    lateinit var headerSharedPreferencesDatasource: IHeaderSharedPreferencesDatasource

    @Inject
    lateinit var equipDao: EquipDao

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun check_return_failure_if_idTruck_is_null() =
        runTest {
            val result = usecase()
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IGetTypeTruck -> idTruck is required",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.NullPointerException: idTruck is required",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun check_return_failure_if_equip_room_not_have_data() =
        runTest {
            headerSharedPreferencesDatasource.setIdTruck(100)
            val result = usecase()
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IGetTypeTruck -> IEquipRepository.getById -> IEquipRoomDatasource.getById",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.IllegalStateException: The query result was empty, but expected a single row to return a NON-NULL object of type 'br.com.usinasantafe.cvf.infra.models.room.stable.EquipRoomModel'.",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun check_return_TypeTruck_TRUCK_if_process_execute_successfully_and_cdOperClass_is_1() =
        runTest {
            headerSharedPreferencesDatasource.setIdTruck(100)
            equipDao.insert(
                EquipRoomModel(
                    id = 100,
                    nro = 256,
                    cdOperClass = 1,
                    descOperClass = "OperClass1",
                    type = TypeEquip.TRUCK
                )
            )
            val result = usecase()
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                TypeTruck.TRUCK,
                result.getOrNull()
            )
        }

    @Test
    fun check_return_TypeTruck_HAULAGE_TRUCK_if_process_execute_successfully_and_cdOperClass_is_different_1() =
        runTest {
            headerSharedPreferencesDatasource.setIdTruck(100)
            equipDao.insert(
                EquipRoomModel(
                    id = 100,
                    nro = 256,
                    cdOperClass = 8,
                    descOperClass = "OperClass1",
                    type = TypeEquip.TRUCK
                )
            )
            val result = usecase()
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                TypeTruck.HAULAGE_TRUCK,
                result.getOrNull()
            )
        }

}