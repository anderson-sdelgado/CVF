package br.com.usinasantafe.cvf.domain.usecases.manager

import android.content.SharedPreferences
import br.com.usinasantafe.cvf.external.room.dao.DatabaseRoom
import br.com.usinasantafe.cvf.external.room.dao.stable.FrontDao
import br.com.usinasantafe.cvf.external.room.dao.stable.ReleaseDao
import br.com.usinasantafe.cvf.external.sharedPreferences.IManagerSharedPreferencesDatasource
import br.com.usinasantafe.cvf.infra.models.room.stable.FrontRoomModel
import br.com.usinasantafe.cvf.infra.models.room.stable.ReleaseRoomModel
import br.com.usinasantafe.cvf.infra.models.sharedpreferences.ManagerSharedPreferencesModel
import br.com.usinasantafe.cvf.lib.StatusSend
import br.com.usinasantafe.cvf.utils.resultFailure
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.Date
import javax.inject.Inject
import kotlin.test.assertEquals

@HiltAndroidTest
class IUpdateManagerTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var usecase: UpdateManager

    @Inject
    lateinit var frontDao: FrontDao

    @Inject
    lateinit var releaseDao: ReleaseDao

    @Inject
    lateinit var managerSharedPreferencesDatasource: IManagerSharedPreferencesDatasource

    @Inject
    lateinit var db: DatabaseRoom

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    @Before
    fun setUp() {
        hiltRule.inject()
        db.clearAllTables()
        sharedPreferences.edit().clear().commit()
    }

    private val managerJson = """
        {
            "idFront": 10,
            "idRelease": 7,
            "qtdLimitCart": 3
        }
    """.trimIndent()

    private val frontJson = """
        {
            "id": 10,
            "cd": 12,
            "description": "Test1"
        }
    """.trimIndent()

    private val releaseJson = """
        {
            "id": 7,
            "nroOS": 1528,
            "idPropAgr": 1,
            "descPropAgr": "Test1",
            "idFront": 10
        }
    """.trimIndent()

    @Test
    fun check_return_correct_and_verify_data_if_json_is_correct() =
        runTest {
            val result = usecase(managerJson, frontJson, releaseJson)
            assertEquals(
                true,
                result.isSuccess
            )

            val frontList = frontDao.all()
            assertEquals(1, frontList.size)
            val front = frontList.first()
            assertEquals(10, front.id)
            assertEquals(12, front.cd)
            assertEquals("Test1", front.description)

            val releaseList = releaseDao.all()
            assertEquals(1, releaseList.size)
            val release = releaseList.first()
            assertEquals(7, release.id)
            assertEquals(1528, release.nroOS)
            assertEquals(10, release.idFront)

            val managerModel = managerSharedPreferencesDatasource.get().getOrThrow()
            assertEquals(10, managerModel.idFront)
            assertEquals(7, managerModel.idRelease)
            assertEquals(3, managerModel.qtdLimitCart)
            assertEquals(StatusSend.STARTED, managerModel.statusSend)
        }

    @Test
    fun check_not_insert_data_if_data_is_existent() =
        runTest {
            frontDao.insert(FrontRoomModel(10, 50, "Original Front"))
            releaseDao.insert(ReleaseRoomModel(7, 9999, 1, "Original Release", 10))

            val result = usecase(managerJson, frontJson, releaseJson)
            assertEquals(true, result.isSuccess)

            val front = frontDao.getById(10)
            assertEquals("Original Front", front.description)
            assertEquals(50, front.cd)

            val release = releaseDao.getById(7)
            assertEquals(9999, release.nroOS)
            assertEquals("Original Release", release.descPropAgr)
        }

    @Test
    fun check_insert_data_multiple_times_with_different_ids() =
        runTest {
            usecase(managerJson, frontJson, releaseJson)

            val managerJson2 = """
                {
                    "idFront": 11,
                    "idRelease": 8,
                    "qtdLimitCart": 4
                }
            """.trimIndent()

            val frontJson2 = """
                {
                    "id": 11,
                    "cd": 13,
                    "description": "Test2"
                }
            """.trimIndent()

            val releaseJson2 = """
                {
                    "id": 8,
                    "nroOS": 1529,
                    "idPropAgr": 2,
                    "descPropAgr": "Test2",
                    "idFront": 11
                }
            """.trimIndent()

            val result = usecase(managerJson2, frontJson2, releaseJson2)
            assertEquals(true, result.isSuccess)

            assertEquals(2, frontDao.all().size)
            assertEquals(2, releaseDao.all().size)

            val managerModel = managerSharedPreferencesDatasource.get().getOrThrow()
            assertEquals(11, managerModel.idFront)
            assertEquals(8, managerModel.idRelease)
            assertEquals(4, managerModel.qtdLimitCart)
        }

    @Test
    fun check_update_manager_data_and_preserve_other_fields() =
        runTest {
            val initialDate = Date(1000)
            managerSharedPreferencesDatasource.save(
                ManagerSharedPreferencesModel(
                    idFront = 1,
                    idRelease = 1,
                    qtdLimitCart = 1,
                    dateHourCreate = initialDate,
                    statusSend = StatusSend.SENT
                )
            )

            val result = usecase(managerJson, frontJson, releaseJson)
            assertEquals(true, result.isSuccess)

            val managerModel = managerSharedPreferencesDatasource.get().getOrThrow()
            assertEquals(10, managerModel.idFront)
            assertEquals(7, managerModel.idRelease)
            assertEquals(3, managerModel.qtdLimitCart)
            assertEquals(initialDate.time, managerModel.dateHourCreate.time)
            assertEquals(StatusSend.SENT, managerModel.statusSend)
        }

    @Test
    fun check_success_if_manager_json_has_numbers_as_strings() =
        runTest {
            val jsonWithStrings = """
                {
                    "idFront": "20",
                    "idRelease": "15",
                    "qtdLimitCart": "2"
                }
            """.trimIndent()

            val result = usecase(jsonWithStrings, frontJson, releaseJson)
            assertEquals(true, result.isSuccess)

            val managerModel = managerSharedPreferencesDatasource.get().getOrThrow()
            assertEquals(20, managerModel.idFront)
            assertEquals(15, managerModel.idRelease)
            assertEquals(2, managerModel.qtdLimitCart)
        }

    @Test
    fun check_success_and_use_default_if_manager_json_is_missing_fields() =
        runTest {
            val incompleteJson = """
                {
                    "idFront": "10"
                }
            """.trimIndent()

            val result = usecase(incompleteJson, frontJson, releaseJson)
            assertEquals(true, result.isSuccess)

            val managerModel = managerSharedPreferencesDatasource.get().getOrThrow()
            assertEquals(10, managerModel.idFront)
            assertEquals(0, managerModel.idRelease)
            assertEquals(0, managerModel.qtdLimitCart)
        }

    @Test
    fun check_return_failure_if_manager_json_is_malformed() =
        runTest {
            val result = usecase("malformed", frontJson, releaseJson)
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                true,
                result.exceptionOrNull()!!.message!!.contains("IUpdateManager")
            )
        }

    @Test
    fun check_return_failure_if_front_json_is_malformed() =
        runTest {
            val result = usecase(managerJson, "malformed", releaseJson)
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                true,
                result.exceptionOrNull()!!.message!!.contains("IUpdateManager")
            )
        }

    @Test
    fun check_return_failure_if_release_json_is_malformed() =
        runTest {
            val result = usecase(managerJson, frontJson, "malformed")
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                true,
                result.exceptionOrNull()!!.message!!.contains("IUpdateManager")
            )
        }

}
