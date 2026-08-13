package br.com.usinasantafe.cvf.domain.usecases.manager

import br.com.usinasantafe.cvf.external.room.dao.stable.FrontDao
import br.com.usinasantafe.cvf.external.sharedPreferences.IManagerSharedPreferencesDatasource
import br.com.usinasantafe.cvf.infra.models.room.stable.FrontRoomModel
import br.com.usinasantafe.cvf.infra.models.sharedpreferences.ManagerSharedPreferencesModel
import br.com.usinasantafe.cvf.presenter.model.ItemCheckBoxScreenModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import kotlin.test.assertEquals

@HiltAndroidTest
class IListFrontTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var usecase: ListFront

    @Inject
    lateinit var frontDao: FrontDao

    @Inject
    lateinit var managerSharedPreferencesDatasource: IManagerSharedPreferencesDatasource

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun check_return_empty_list_if_function_execute_successfully_and_list_is_empty() =
        runTest {
            val result = usecase()
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                emptyList(),
                result.getOrNull()!!
            )
        }

    @Test
    fun check_return_list_without_check_if_function_execute_successfully_and_idFront_is_null() =
        runTest {
            frontDao.insertAll(
                listOf(
                    FrontRoomModel(
                        id = 1,
                        cd = 1,
                        description = "Test1"
                    ),
                    FrontRoomModel(
                        id = 3,
                        cd = 3,
                        description = "Test3"
                    ),
                    FrontRoomModel(
                        id = 2,
                        cd = 2,
                        description = "Test2"
                    ),
                )
            )
            val result = usecase()
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                listOf(
                    ItemCheckBoxScreenModel(
                        id = 1,
                        desc = "Test1",
                        flag = false
                    ),
                    ItemCheckBoxScreenModel(
                        id = 2,
                        desc = "Test2",
                        flag = false
                    ),
                    ItemCheckBoxScreenModel(
                        id = 3,
                        desc = "Test3",
                        flag = false
                    )
                ),
                result.getOrNull()!!
            )
        }

    @Test
    fun check_return_list_with_check_if_function_execute_successfully_and_idFront_is_not_null() =
        runTest {
            frontDao.insertAll(
                listOf(
                    FrontRoomModel(
                        id = 1,
                        cd = 1,
                        description = "Test1"
                    ),
                    FrontRoomModel(
                        id = 3,
                        cd = 3,
                        description = "Test3"
                    ),
                    FrontRoomModel(
                        id = 2,
                        cd = 2,
                        description = "Test2"
                    ),
                )
            )
            managerSharedPreferencesDatasource.save(
                ManagerSharedPreferencesModel(
                    idFront = 2,
                    idRelease = 1
                )
            )
            val result = usecase()
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                listOf(
                    ItemCheckBoxScreenModel(
                        id = 1,
                        desc = "Test1",
                        flag = false
                    ),
                    ItemCheckBoxScreenModel(
                        id = 2,
                        desc = "Test2",
                        flag = true
                    ),
                    ItemCheckBoxScreenModel(
                        id = 3,
                        desc = "Test3",
                        flag = false
                    )
                ),
                result.getOrNull()!!
            )
        }



}