package br.com.usinasantafe.cvf.domain.usecases.note

import br.com.usinasantafe.cvf.external.room.dao.stable.EquipDao
import br.com.usinasantafe.cvf.external.sharedPreferences.IHeaderSharedPreferencesDatasource
import br.com.usinasantafe.cvf.infra.models.room.stable.EquipRoomModel
import br.com.usinasantafe.cvf.infra.models.sharedpreferences.HeaderSharedPreferencesModel
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
class ISetNroTruckTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var usecase: SetNroTruck

    @Inject
    lateinit var equipDao: EquipDao

    @Inject
    lateinit var headerSharedPreferencesDatasource: IHeaderSharedPreferencesDatasource

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun check_return_failure_if_value_of_field_is_incorrect() =
        runTest {
            val result = usecase("de25")
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "ISetNroTruck -> stringToInt",
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
            val result = usecase("230")
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "ISetNroTruck -> IEquipRepository.getIdByNro -> IEquipRoomDatasource.getIdByNro",
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
                    id = 30,
                    nro = 230,
                    cdOperClass = 25,
                    descOperClass = "OperClass1",
                    type = TypeEquip.CART
                )
            )
            headerSharedPreferencesDatasource.save(
                HeaderSharedPreferencesModel(
                    idTruck = 12,
                )
            )
            val modelBefore = headerSharedPreferencesDatasource.get().getOrThrow()
            assertEquals(
                12,
                modelBefore.idTruck
            )
            val result = usecase("230")
            assertEquals(
                true,
                result.isSuccess
            )
            val modelAfter = headerSharedPreferencesDatasource.get().getOrThrow()
            assertEquals(
                30,
                modelAfter.idTruck
            )
        }

}