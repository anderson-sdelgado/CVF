package br.com.usinasantafe.cvf.presenter.view.manager.release

import androidx.lifecycle.SavedStateHandle
import br.com.usinasantafe.cvf.MainCoroutineRule
import br.com.usinasantafe.cvf.domain.usecases.manager.ListRelease
import br.com.usinasantafe.cvf.domain.usecases.manager.SaveManager
import br.com.usinasantafe.cvf.domain.usecases.update.UpdateTableRelease
import br.com.usinasantafe.cvf.lib.Errors
import br.com.usinasantafe.cvf.lib.LevelUpdate
import br.com.usinasantafe.cvf.presenter.model.ItemCheckBoxScreenModel
import br.com.usinasantafe.cvf.presenter.navigation.Args
import br.com.usinasantafe.cvf.presenter.view.manager.front.FrontState
import br.com.usinasantafe.cvf.utils.CheckNetwork
import br.com.usinasantafe.cvf.utils.UiStatusStateUpdate
import br.com.usinasantafe.cvf.utils.percentage
import br.com.usinasantafe.cvf.utils.resultFailure
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class ReleaseViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val updateTableRelease = mock<UpdateTableRelease>()
    private val listRelease = mock<ListRelease>()
    private val saveManager = mock<SaveManager>()
    private val checkNetwork = mock<CheckNetwork>()

    private fun createdViewModel(
        idFront: Int = 1
    ) = ReleaseViewModel(
        savedStateHandle = SavedStateHandle(
            mapOf(
                Args.ID_FRONT_ARG to idFront
            )
        ),
        updateTableRelease = updateTableRelease,
        listRelease = listRelease,
        saveManager = saveManager,
        checkNetwork = checkNetwork
    )

    @Test
    fun `list - Check return failure if have error in ListRelease`() =
        runTest {
            whenever(
                listRelease(2)
            ).thenReturn(
                resultFailure(
                    context = "ListRelease",
                    message = "-",
                    cause = Exception()
                )
            )
            val viewModel = createdViewModel(2)
            viewModel.list()
            assertEquals(
                true,
                viewModel.uiState.value.status.flagDialog
            )
            assertEquals(
                "ReleaseViewModel.list -> ListRelease -> java.lang.Exception",
                viewModel.uiState.value.status.failure
            )
            assertEquals(
                Errors.EXCEPTION,
                viewModel.uiState.value.status.errors
            )
            assertEquals(
                true,
                viewModel.uiState.value.status.flagFailure
            )
        }

    @Test
    fun `list - Check return correct if function execute successfully`() =
        runTest {
            whenever(
                listRelease(2)
            ).thenReturn(
                Result.success(
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
                    )
                )
            )
            val viewModel = createdViewModel(2)
            viewModel.list()
            val list = viewModel.list.toList()
            assertEquals(
                3,
                list.size
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
                list
            )
        }

    @Test
    fun `update - Check return failure if have error in CleanRelease`() =
        runTest {
            whenever(
                updateTableRelease(
                    count = 1f,
                    sizeAll = 4f
                )
            ).thenReturn(
                flowOf(
                    UiStatusStateUpdate(
                        flagProgress = true,
                        levelUpdate = LevelUpdate.RECOVERY,
                        tableUpdate = "tb_release",
                        currentProgress = percentage(1f, 4f)
                    ),
                    UiStatusStateUpdate(
                        errors = Errors.UPDATE,
                        flagDialog = true,
                        flagFailure = true,
                        failure = "CleanRelease -> java.lang.NullPointerException",
                        currentProgress = 1f,
                    )
                )
            )
            val viewModel = createdViewModel()
            val result = viewModel.updateAllDatabase().toList()
            assertEquals(result.count(), 2)
            assertEquals(
                ReleaseState(
                    idFront = 1,
                    status = UiStatusStateUpdate(
                        flagProgress = true,
                        levelUpdate = LevelUpdate.RECOVERY,
                        tableUpdate = "tb_release",
                        currentProgress = percentage(1f, 4f)
                    )
                ),
                result[0]
            )
            assertEquals(
                ReleaseState(
                    idFront = 1,
                    status = UiStatusStateUpdate(
                        errors = Errors.UPDATE,
                        flagDialog = true,
                        flagFailure = true,
                        failure = "ReleaseViewModel.updateAllDatabase -> CleanRelease -> java.lang.NullPointerException",
                        currentProgress = 1f,
                    )
                ),
                result[1]
            )
            viewModel.update()
            assertEquals(
                true,
                viewModel.uiState.value.status.flagDialog
            )
            assertEquals(
                "ReleaseViewModel.update -> ReleaseViewModel.updateAllDatabase -> CleanRelease -> java.lang.NullPointerException",
                viewModel.uiState.value.status.failure
            )
        }

    @Test
    fun `update - Check return correct if function execute successfully`() =
        runTest {
            whenever(
                listRelease(1)
            ).thenReturn(
                Result.success(
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
                    )
                )
            )
            whenever(
                updateTableRelease(
                    count = 1f,
                    sizeAll = 4f
                )
            ).thenReturn(
                flowOf(
                    UiStatusStateUpdate(
                        flagProgress = true,
                        levelUpdate = LevelUpdate.RECOVERY,
                        tableUpdate = "tb_release",
                        currentProgress = percentage(1f, 4f)
                    ),
                    UiStatusStateUpdate(
                        flagProgress = true,
                        levelUpdate = LevelUpdate.CLEAN,
                        tableUpdate = "tb_release",
                        currentProgress = percentage(2f, 4f)
                    ),
                    UiStatusStateUpdate(
                        flagProgress = true,
                        levelUpdate = LevelUpdate.SAVE,
                        tableUpdate = "tb_release",
                        currentProgress = percentage(3f, 4f)
                    ),
                )
            )
            val viewModel = createdViewModel()
            val result = viewModel.updateAllDatabase().toList()
            assertEquals(result.count(), 4)
            assertEquals(
                ReleaseState(
                    idFront = 1,
                    status = UiStatusStateUpdate(
                        flagProgress = true,
                        levelUpdate = LevelUpdate.RECOVERY,
                        tableUpdate = "tb_release",
                        currentProgress = percentage(1f, 4f)
                    )
                ),
                result[0]
            )
            assertEquals(
                ReleaseState(
                    idFront = 1,
                    status = UiStatusStateUpdate(
                        flagProgress = true,
                        levelUpdate = LevelUpdate.CLEAN,
                        tableUpdate = "tb_release",
                        currentProgress = percentage(2f, 4f),
                    )
                ),
                result[1]
            )
            assertEquals(
                ReleaseState(
                    idFront = 1,
                    status = UiStatusStateUpdate(
                        flagProgress = true,
                        levelUpdate = LevelUpdate.SAVE,
                        tableUpdate = "tb_release",
                        currentProgress = percentage(3f, 4f),
                    )
                ),
                result[2]
            )
            assertEquals(
                ReleaseState(
                    idFront = 1,
                    status = UiStatusStateUpdate(
                        flagDialog = true,
                        flagProgress = false,
                        flagFailure = false,
                        levelUpdate = LevelUpdate.FINISH_UPDATE_COMPLETED,
                        currentProgress = 1f,
                    )
                ),
                result[3]
            )
            viewModel.update()
            assertEquals(
                viewModel.uiState.value.status.flagDialog,
                true
            )
            val list = viewModel.list.toList()
            assertEquals(
                3,
                list.size
            )
            assertEquals(
                list,
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
                )
            )
        }

    @Test
    fun `save - Check return failure if not selection any item`() =
        runTest {
            val viewModel = createdViewModel()
            viewModel.save()
            assertEquals(
                true,
                viewModel.uiState.value.status.flagDialog
            )
            assertEquals(
                "ReleaseViewModel.updateState -> ReleaseViewModel.save -> NOT_SELECTION",
                viewModel.uiState.value.status.failure
            )
            assertEquals(
                Errors.NOT_SELECTION,
                viewModel.uiState.value.status.errors
            )
            assertEquals(
                true,
                viewModel.uiState.value.status.flagFailure
            )
        }

    @Test
    fun `save - Check return failure if have error in SaveManager`() =
        runTest {
            whenever(
                listRelease(2)
            ).thenReturn(
                Result.success(
                    listOf(
                        ItemCheckBoxScreenModel(
                            id = 5,
                            desc = "Test1",
                            flag = false
                        ),
                        ItemCheckBoxScreenModel(
                            id = 20,
                            desc = "Test2",
                            flag = false
                        ),
                        ItemCheckBoxScreenModel(
                            id = 10,
                            desc = "Test3",
                            flag = false
                        )
                    )
                )
            )
            whenever(
                saveManager(2, 10)
            ).thenReturn(
                resultFailure(
                    context = "SaveManager",
                    message = "-",
                    cause = Exception()
                )
            )
            val viewModel = createdViewModel(2)
            viewModel.list()
            viewModel.onCheckChanged(
                id = 10,
                checked = true
            )
            viewModel.save()
            assertEquals(
                true,
                viewModel.uiState.value.status.flagDialog
            )
            assertEquals(
                "ReleaseViewModel.save -> SaveManager -> java.lang.Exception",
                viewModel.uiState.value.status.failure
            )
            assertEquals(
                Errors.EXCEPTION,
                viewModel.uiState.value.status.errors
            )
            assertEquals(
                true,
                viewModel.uiState.value.status.flagFailure
            )
        }

    @Test
    fun `save - Check return correct if function execute successfully`() =
        runTest {
            whenever(
                listRelease(2)
            ).thenReturn(
                Result.success(
                    listOf(
                        ItemCheckBoxScreenModel(
                            id = 5,
                            desc = "Test1",
                            flag = false
                        ),
                        ItemCheckBoxScreenModel(
                            id = 20,
                            desc = "Test2",
                            flag = false
                        ),
                        ItemCheckBoxScreenModel(
                            id = 10,
                            desc = "Test3",
                            flag = false
                        )
                    )
                )
            )
            val viewModel = createdViewModel(2)
            viewModel.list()
            viewModel.onCheckChanged(
                id = 10,
                checked = true
            )
            viewModel.save()
            assertEquals(
                true,
                viewModel.uiState.value.status.flagAccess
            )
            assertEquals(
                false,
                viewModel.uiState.value.status.flagFailure
            )
            assertEquals(
                false,
                viewModel.uiState.value.status.flagDialog
            )
        }


}