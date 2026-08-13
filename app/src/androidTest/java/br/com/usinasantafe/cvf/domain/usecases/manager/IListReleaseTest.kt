package br.com.usinasantafe.cvf.domain.usecases.manager

import br.com.usinasantafe.cvf.external.room.dao.stable.ReleaseDao
import br.com.usinasantafe.cvf.external.sharedPreferences.IManagerSharedPreferencesDatasource
import br.com.usinasantafe.cvf.infra.models.room.stable.ReleaseRoomModel
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
class IListReleaseTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var usecase: ListRelease

    @Inject
    lateinit var releaseDao: ReleaseDao

    @Inject
    lateinit var managerSharedPreferencesDatasource: IManagerSharedPreferencesDatasource

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun check_return_empty_list_if_function_execute_successfully_and_list_is_empty() =
        runTest {
            val result = usecase(1)
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
    fun check_return_empty_list_if_function_execute_successfully_and_idFront_is_non_existent() =
        runTest {
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
            val result = usecase(2)
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
    fun check_return_list_without_check_if_function_execute_successfully_and_idRelease_is_null() =
        runTest {
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
            val result = usecase(1)
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                listOf(
                    ItemCheckBoxScreenModel(
                        id = 1,
                        desc = "LIBERAÇÃO: 1\n O.S.: 1\n PROPRIEDADE: Test1",
                        flag = false
                    ),
                    ItemCheckBoxScreenModel(
                        id = 2,
                        desc = "LIBERAÇÃO: 2\n O.S.: 2\n PROPRIEDADE: Test2",
                        flag = false
                    ),
                    ItemCheckBoxScreenModel(
                        id = 3,
                        desc = "LIBERAÇÃO: 3\n O.S.: 3\n PROPRIEDADE: Test3",
                        flag = false
                    )
                ),
                result.getOrNull()!!
            )
        }

    @Test
    fun check_return_list_with_check_if_function_execute_successfully_and_idRelease_is_not_null() =
        runTest {
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
            managerSharedPreferencesDatasource.save(
                ManagerSharedPreferencesModel(
                    idFront = 1,
                    idRelease = 2
                )
            )
            val result = usecase(1)
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                listOf(
                    ItemCheckBoxScreenModel(
                        id = 1,
                        desc = "LIBERAÇÃO: 1\n O.S.: 1\n PROPRIEDADE: Test1",
                        flag = false
                    ),
                    ItemCheckBoxScreenModel(
                        id = 2,
                        desc = "LIBERAÇÃO: 2\n O.S.: 2\n PROPRIEDADE: Test2",
                        flag = true
                    ),
                    ItemCheckBoxScreenModel(
                        id = 3,
                        desc = "LIBERAÇÃO: 3\n O.S.: 3\n PROPRIEDADE: Test3",
                        flag = false
                    )
                ),
                result.getOrNull()!!
            )
        }

}