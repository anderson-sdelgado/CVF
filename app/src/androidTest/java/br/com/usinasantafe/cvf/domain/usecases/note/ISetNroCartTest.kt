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
class ISetNroCartTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var usecase: SetNroCart

    @Inject
    lateinit var cartSharedPreferencesDatasource: ICartSharedPreferencesDatasource

    @Inject
    lateinit var equipDao: EquipDao

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun check_return_failure_if_value_of_field_is_incorrect() =
        runTest {
            val result = usecase("de25", 2)
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "ISetNroCart -> stringToInt",
                result.exceptionOrNull()!!.message,
            )
            assertEquals(
                "java.lang.NumberFormatException: For input string: \"de25\"",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun check_return_failure_if_equip_room_not_have_data() =
        runTest {
            val result = usecase("230", 2)
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "ISetNroCart -> IEquipRepository.getIdByNro -> IEquipRoomDatasource.getIdByNro",
                result.exceptionOrNull()!!.message,
            )
            assertEquals(
                "java.lang.Exception: id is 0",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun check_alter_data_if_process_execute_successfully() =
        runTest {
            equipDao.insert(
                EquipRoomModel(
                    id = 12,
                    nro = 200,
                    cdOperClass = 25,
                    descOperClass = "OperClass1",
                    type = TypeEquip.CART
                )
            )
            cartSharedPreferencesDatasource.add(
                CartSharedPreferencesModel(
                    position = 1,
                    idCart = 10
                )
            )
            val result = usecase("200", 2)
            assertEquals(
                true,
                result.isSuccess
            )
            val list = cartSharedPreferencesDatasource.list().getOrThrow()
            assertEquals(
                2,
                list.size
            )
            val model1 = list[0]
            assertEquals(
                CartSharedPreferencesModel(
                    position = 1,
                    idCart = 10
                ),
                model1
            )
            val model2 = list[1]
            assertEquals(
                CartSharedPreferencesModel(
                    position = 2,
                    idCart = 12
                ),
                model2
            )
        }
}