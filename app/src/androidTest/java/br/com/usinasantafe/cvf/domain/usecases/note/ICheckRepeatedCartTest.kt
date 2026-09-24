package br.com.usinasantafe.cvf.domain.usecases.note

import br.com.usinasantafe.cvf.external.room.dao.stable.EquipDao
import br.com.usinasantafe.cvf.external.sharedPreferences.ICartSharedPreferencesDatasource
import br.com.usinasantafe.cvf.infra.models.room.stable.EquipRoomModel
import br.com.usinasantafe.cvf.infra.models.sharedpreferences.CartSharedPreferencesModel
import br.com.usinasantafe.cvf.lib.TypeEquip
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import kotlin.test.assertEquals

@HiltAndroidTest
class ICheckRepeatedCartTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var usecase: CheckRepeatedCart

    @Inject
    lateinit var equipDao: EquipDao

    @Inject
    lateinit var cartSharedPreferencesDatasource: ICartSharedPreferencesDatasource

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun check_return_failure_if_value_of_field_is_incorrect() =
        runTest {
            val result = usecase("de25", 1)
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "ICheckRepeatedCart -> stringToInt",
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
            val result = usecase("158", 1)
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "ICheckRepeatedCart -> IEquipRepository.getIdByNro -> IEquipRoomDatasource.getIdByNro",
                result.exceptionOrNull()!!.message,
            )
            assertEquals(
                "java.lang.Exception: id is 0",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun check_return_false_if_equip_room_have_value_fielded_and_cart_list_have_not_cart_fielded() =
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
            cartSharedPreferencesDatasource.add(
                CartSharedPreferencesModel(
                    position = 1,
                    idCart = 1
                )
            )
            val result = usecase("123", 1)
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
    fun check_return_true_if_equip_room_have_value_fielded_and_cart_list_have_cart_fielded() =
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
            cartSharedPreferencesDatasource.add(
                CartSharedPreferencesModel(
                    position = 1,
                    idCart = 1
                )
            )
            cartSharedPreferencesDatasource.add(
                CartSharedPreferencesModel(
                    position = 2,
                    idCart = 10
                )
            )
            val result = usecase("123", 1)
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                true,
                result.getOrNull()!!
            )
        }

}