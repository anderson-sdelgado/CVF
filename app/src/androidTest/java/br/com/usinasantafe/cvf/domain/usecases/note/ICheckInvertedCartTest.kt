package br.com.usinasantafe.cvf.domain.usecases.note

import br.com.usinasantafe.cvf.external.room.dao.stable.EquipDao
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
class ICheckInvertedCartTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var usecase: CheckInvertedCart

    @Inject
    lateinit var equipDao: EquipDao

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun check_return_failure_if_value_of_field_is_incorrect() =
        runTest {
            val result = usecase("de25", 2, TypeTruck.TRUCK)
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "ICheckInvertedCart -> stringToInt",
                result.exceptionOrNull()!!.message,
            )
            assertEquals(
                "java.lang.NumberFormatException: For input string: \"de25\"",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun check_return_failure_if_equip_room_have_not_value_fielded() =
        runTest {
            equipDao.insertAll(
                listOf(
                    EquipRoomModel(
                        id = 10,
                        nro = 123,
                        cdOperClass = 13,
                        descOperClass = "Test",
                        type = TypeEquip.TRUCK
                    )
                )
            )
            val result = usecase("158", 1, TypeTruck.TRUCK)
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "ICheckInvertedCart -> IEquipRepository.getCdClassOperByNro -> IEquipRoomDatasource.getCdClassOperByNro",
                result.exceptionOrNull()!!.message,
            )
            assertEquals(
                "java.lang.Exception: cdClassOper is 0",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun check_return_true_if_typeTruck_is_TypeTruck_TRUCK_and_cdClassOper_is_21() =
        runTest {
            equipDao.insertAll(
                listOf(
                    EquipRoomModel(
                        id = 10,
                        nro = 123,
                        cdOperClass = 21,
                        descOperClass = "Test",
                        type = TypeEquip.TRUCK
                    )
                )
            )
            val result = usecase("123", 1, TypeTruck.TRUCK)
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                true,
                result.getOrNull()!!
            )
        }

    @Test
    fun check_return_false_if_typeTruck_is_TypeTruck_TRUCK_and_cdClassOper_is_not_21() =
        runTest {
            equipDao.insertAll(
                listOf(
                    EquipRoomModel(
                        id = 10,
                        nro = 123,
                        cdOperClass = 5,
                        descOperClass = "Test",
                        type = TypeEquip.TRUCK
                    )
                )
            )
            val result = usecase("123", 1, TypeTruck.TRUCK)
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                false,
                result.getOrNull()!!
            )
        }

    @Test
    fun check_return_true_if_typeTruck_is_TypeTruck_HAULAGE_TRUCK_and_cdClassOper_is_21_and_pos_is_greater_than_1() =
        runTest {
            equipDao.insertAll(
                listOf(
                    EquipRoomModel(
                        id = 10,
                        nro = 123,
                        cdOperClass = 21,
                        descOperClass = "Test",
                        type = TypeEquip.TRUCK
                    )
                )
            )
            val result = usecase("123", 3, TypeTruck.HAULAGE_TRUCK)
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                true,
                result.getOrNull()!!
            )
        }

    @Test
    fun check_return_false_if_typeTruck_is_TypeTruck_HAULAGE_TRUCK_and_cdClassOper_is_21_and_pos_is_1() =
        runTest {
            equipDao.insertAll(
                listOf(
                    EquipRoomModel(
                        id = 10,
                        nro = 123,
                        cdOperClass = 21,
                        descOperClass = "Test",
                        type = TypeEquip.TRUCK
                    )
                )
            )
            val result = usecase("123", 1, TypeTruck.HAULAGE_TRUCK)
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                false,
                result.getOrNull()!!
            )
        }

    @Test
    fun check_return_true_if_typeTruck_is_TypeTruck_HAULAGE_TRUCK_and_cdClassOper_is_different_21_and_pos_is_1() =
        runTest {
            equipDao.insertAll(
                listOf(
                    EquipRoomModel(
                        id = 10,
                        nro = 123,
                        cdOperClass = 5,
                        descOperClass = "Test",
                        type = TypeEquip.TRUCK
                    )
                )
            )
            val result = usecase("123", 1, TypeTruck.HAULAGE_TRUCK)
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                true,
                result.getOrNull()!!
            )
        }

    @Test
    fun check_return_true_if_typeTruck_is_TypeTruck_HAULAGE_TRUCK_and_cdClassOper_is_different_21_and_pos_is_greater_than_1() =
        runTest {
            equipDao.insertAll(
                listOf(
                    EquipRoomModel(
                        id = 10,
                        nro = 123,
                        cdOperClass = 5,
                        descOperClass = "Test",
                        type = TypeEquip.TRUCK
                    )
                )
            )
            val result = usecase("123", 3, TypeTruck.HAULAGE_TRUCK)
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                false,
                result.getOrNull()!!
            )
        }
}