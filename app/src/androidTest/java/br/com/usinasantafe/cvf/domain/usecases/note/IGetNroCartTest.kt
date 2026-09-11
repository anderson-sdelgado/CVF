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
class IGetNroCartTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var usecase: GetNroCart

    @Inject
    lateinit var cartSharedPreferencesDatasource: ICartSharedPreferencesDatasource

    @Inject
    lateinit var equipDao: EquipDao

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun check_return_null_if_not_have_data() =
        runTest {
            val result = usecase(1)
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                null,
                result.getOrNull()
            )
        }

    @Test
    fun check_return_failure_if_equip_room_not_have_data() =
        runTest {
            cartSharedPreferencesDatasource.add(
                CartSharedPreferencesModel(
                    position = 1,
                    idCart = 10
                )
            )
            val result = usecase(1)
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IGetNroCart -> IEquipRepository.getById -> IEquipRoomDatasource.getById",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.IllegalStateException: The query result was empty, but expected a single row to return a NON-NULL object of type 'br.com.usinasantafe.cvf.infra.models.room.stable.EquipRoomModel'.",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun check_return_correct_if_process_execute_successfully() =
        runTest {
            cartSharedPreferencesDatasource.add(
                CartSharedPreferencesModel(
                    position = 1,
                    idCart = 10
                )
            )
            equipDao.insertAll(
                listOf(
                    EquipRoomModel(
                        id = 10,
                        nro = 200,
                        cdOperClass = 25,
                        descOperClass = "OperClass1",
                        type = TypeEquip.CART
                    ),
                )
            )
            val result = usecase(1)
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                "200",
                result.getOrNull()
            )
        }

}