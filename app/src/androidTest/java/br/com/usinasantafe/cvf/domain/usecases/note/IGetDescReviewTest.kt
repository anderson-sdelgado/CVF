package br.com.usinasantafe.cvf.domain.usecases.note

import br.com.usinasantafe.cvf.external.room.dao.stable.ColabDao
import br.com.usinasantafe.cvf.external.room.dao.stable.EquipDao
import br.com.usinasantafe.cvf.external.room.dao.stable.FrontDao
import br.com.usinasantafe.cvf.external.room.dao.stable.ReleaseDao
import br.com.usinasantafe.cvf.external.sharedPreferences.ICartSharedPreferencesDatasource
import br.com.usinasantafe.cvf.external.sharedPreferences.IHeaderSharedPreferencesDatasource
import br.com.usinasantafe.cvf.external.sharedPreferences.IManagerSharedPreferencesDatasource
import br.com.usinasantafe.cvf.infra.models.room.stable.ColabRoomModel
import br.com.usinasantafe.cvf.infra.models.room.stable.EquipRoomModel
import br.com.usinasantafe.cvf.infra.models.room.stable.FrontRoomModel
import br.com.usinasantafe.cvf.infra.models.room.stable.ReleaseRoomModel
import br.com.usinasantafe.cvf.infra.models.sharedpreferences.CartSharedPreferencesModel
import br.com.usinasantafe.cvf.infra.models.sharedpreferences.ManagerSharedPreferencesModel
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
class IGetDescReviewTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var usecase: GetDescReview

    @Inject
    lateinit var releaseDao: ReleaseDao

    @Inject
    lateinit var frontDao: FrontDao

    @Inject
    lateinit var managerSharedPreferencesDatasource: IManagerSharedPreferencesDatasource

    @Inject
    lateinit var headerSharedPreferencesDatasource: IHeaderSharedPreferencesDatasource

    @Inject
    lateinit var colabDao: ColabDao

    @Inject
    lateinit var equipDao: EquipDao

    @Inject
    lateinit var cartSharedPreferencesDatasource: ICartSharedPreferencesDatasource

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun check_return_failure_if_manager_shared_preferences_not_have_data() =
        runTest {
            val result = usecase()
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IGetDescReview -> IGetTitleMenu -> idRelease is required",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.NullPointerException: idRelease is required",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun check_return_failure_if_regDriver_in_note_shared_preferences_not_have_data() =
        runTest {
            insertData(1)
            val result = usecase()
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IGetDescReview -> regDriver is required",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.NullPointerException: regDriver is required",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun check_return_failure_if_colab_room_not_have_data() =
        runTest {
            insertData(2)
            val result = usecase()
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IGetDescReview -> IColabRepository.getNameByReg -> IColabRoomDatasource.getNameByReg",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.IllegalStateException: The query result was empty, but expected a single row to return a NON-NULL object of type 'kotlin.String'.",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun check_return_failure_if_idTruck_in_note_shared_preferences_not_have_data() =
        runTest {
            insertData(3)
            val result = usecase()
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IGetDescReview -> idTruck is required",
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
            insertData(4)
            val result = usecase()
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IGetDescReview -> IEquipRepository.getById -> IEquipRoomDatasource.getById",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.IllegalStateException: The query result was empty, but expected a single row to return a NON-NULL object of type 'br.com.usinasantafe.cvf.infra.models.room.stable.EquipRoomModel'.",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun check_return_correct_if_cart_list_is_empty() =
        runTest {
            insertData(5)
            val result = usecase()
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                "FRENTE: Front2\n" +
                        "LIBERAÇÃO: 1\n" +
                        "O.S.: 1\n" +
                        "PROPRIEDADE: Release1\n" +
                        "\n" +
                        "MOTORISTA: 19759 - Colab1\n" +
                        "CAMINHÃO: 200 - OperClass1\n",
                result.getOrNull()!!
            )
        }

    @Test
    fun check_return_failure_if_equip_room_not_have_data_to_cart() =
        runTest {
            insertData(6)
            val result = usecase()
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IGetDescReview -> IEquipRepository.getById -> IEquipRoomDatasource.getById",
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
            insertData(7)
            val result = usecase()
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                "FRENTE: Front2\n" +
                        "LIBERAÇÃO: 1\n" +
                        "O.S.: 1\n" +
                        "PROPRIEDADE: Release1\n" +
                        "\n" +
                        "MOTORISTA: 19759 - Colab1\n" +
                        "CAMINHÃO: 200 - OperClass1\n" +
                        "CARRETA 1: 300 - Cart1\n" +
                        "CARRETA 1: 400 - Cart2",
                result.getOrNull()!!
            )
        }

    private suspend fun insertData(level: Int){

        managerSharedPreferencesDatasource.save(
            ManagerSharedPreferencesModel(
                idRelease = 1,
                idFront = 2,
            )
        )
        releaseDao.insertAll(
            listOf(
                ReleaseRoomModel(
                    id = 1,
                    nroOS = 1,
                    idPropAgr = 1,
                    descPropAgr = "Release1",
                    idFront = 1
                ),
                ReleaseRoomModel(
                    id = 2,
                    nroOS = 2,
                    idPropAgr = 2,
                    descPropAgr = "Release2",
                    idFront = 1
                ),
                ReleaseRoomModel(
                    id = 3,
                    nroOS = 3,
                    idPropAgr = 3,
                    descPropAgr = "Release3",
                    idFront = 1
                ),
            )
        )
        frontDao.insertAll(
            listOf(
                FrontRoomModel(
                    id = 1,
                    cd = 1,
                    description = "Front1"
                ),
                FrontRoomModel(
                    id = 2,
                    cd = 2,
                    description = "Front2"
                ),
                FrontRoomModel(
                    id = 3,
                    cd = 3,
                    description = "Front3"
                ),
            )
        )

        if(level == 1) return

        headerSharedPreferencesDatasource.setRegDriver(19759)

        if(level == 2) return

        colabDao.insertAll(
            listOf(
                ColabRoomModel(
                    reg = 19759,
                    name = "Colab1"
                ),
            )
        )

        if(level == 3) return

        headerSharedPreferencesDatasource.setIdTruck(100)

        if(level == 4) return

        equipDao.insertAll(
            listOf(
                EquipRoomModel(
                    id = 100,
                    nro = 200,
                    cdOperClass = 25,
                    descOperClass = "OperClass1",
                    type = TypeEquip.TRUCK
                ),
            )
        )

        if(level == 5) return

        cartSharedPreferencesDatasource.add(
            CartSharedPreferencesModel(
                position = 1,
                idCart = 10
            )
        )

        cartSharedPreferencesDatasource.add(
            CartSharedPreferencesModel(
                position = 1,
                idCart = 20
            )
        )

        if(level == 6) return

        equipDao.insertAll(
            listOf(
                EquipRoomModel(
                    id = 10,
                    nro = 300,
                    cdOperClass = 35,
                    descOperClass = "Cart1",
                    type = TypeEquip.CART
                ),
                EquipRoomModel(
                    id = 20,
                    nro = 400,
                    cdOperClass = 45,
                    descOperClass = "Cart2",
                    type = TypeEquip.CART
                )
            )
        )

        if(level == 7) return

    }

}