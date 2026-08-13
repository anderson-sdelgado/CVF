package br.com.usinasantafe.cvf.presenter.view.manager.front

import br.com.usinasantafe.cvf.MainCoroutineRule
import br.com.usinasantafe.cvf.domain.usecases.manager.ListFront
import br.com.usinasantafe.cvf.domain.usecases.update.UpdateTableFront
import br.com.usinasantafe.cvf.lib.Errors
import br.com.usinasantafe.cvf.lib.LevelUpdate
import br.com.usinasantafe.cvf.presenter.model.ItemCheckBoxScreenModel
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
class FrontViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val listFront = mock<ListFront>()
    private val updateTableFront = mock<UpdateTableFront>()

    private val viewModel = FrontViewModel(
        listFront = listFront,
        updateTableFront = updateTableFront
    )

    @Test
    fun `list - Check return failure if have error in ListFront`() =
        runTest {
            whenever(
                listFront()
            ).thenReturn(
                resultFailure(
                    context = "ListFront",
                    message = "-",
                    cause = Exception()
                )
            )
            viewModel.list()
            assertEquals(
                true,
                viewModel.uiState.value.status.flagDialog
            )
            assertEquals(
                "FrontViewModel.list -> ListFront -> java.lang.Exception",
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
                listFront()
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
    fun `update - Check return failure if have error in CleanFront`() =
        runTest {
            whenever(
                updateTableFront(
                    count = 1f,
                    sizeAll = 4f
                )
            ).thenReturn(
                flowOf(
                    UiStatusStateUpdate(
                        flagProgress = true,
                        levelUpdate = LevelUpdate.RECOVERY,
                        tableUpdate = "tb_front",
                        currentProgress = percentage(1f, 4f)
                    ),
                    UiStatusStateUpdate(
                        errors = Errors.UPDATE,
                        flagDialog = true,
                        flagFailure = true,
                        failure = "CleanFront -> java.lang.NullPointerException",
                        currentProgress = 1f,
                    )
                )
            )
            val result = viewModel.updateAllDatabase().toList()
            assertEquals(result.count(), 2)
            assertEquals(
                FrontState(
                    status = UiStatusStateUpdate(
                        flagProgress = true,
                        levelUpdate = LevelUpdate.RECOVERY,
                        tableUpdate = "tb_front",
                        currentProgress = percentage(1f, 4f)
                    )
                ),
                result[0]
            )
            assertEquals(
                FrontState(
                    status = UiStatusStateUpdate(
                        errors = Errors.UPDATE,
                        flagDialog = true,
                        flagFailure = true,
                        failure = "FrontViewModel.updateAllDatabase -> CleanFront -> java.lang.NullPointerException",
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
                "FrontViewModel.update -> FrontViewModel.updateAllDatabase -> CleanFront -> java.lang.NullPointerException",
                viewModel.uiState.value.status.failure
            )
        }

    @Test
    fun `update - Check return correct if function execute successfully`() =
        runTest {
            whenever(
                listFront()
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
                updateTableFront(
                    count = 1f,
                    sizeAll = 4f
                )
            ).thenReturn(
                flowOf(
                    UiStatusStateUpdate(
                        flagProgress = true,
                        levelUpdate = LevelUpdate.RECOVERY,
                        tableUpdate = "tb_front",
                        currentProgress = percentage(1f, 4f)
                    ),
                    UiStatusStateUpdate(
                        flagProgress = true,
                        levelUpdate = LevelUpdate.CLEAN,
                        tableUpdate = "tb_front",
                        currentProgress = percentage(2f, 4f)
                    ),
                    UiStatusStateUpdate(
                        flagProgress = true,
                        levelUpdate = LevelUpdate.SAVE,
                        tableUpdate = "tb_front",
                        currentProgress = percentage(3f, 4f)
                    ),
                )
            )
            val result = viewModel.updateAllDatabase().toList()
            assertEquals(result.count(), 4)
            assertEquals(
                FrontState(
                    status = UiStatusStateUpdate(
                        flagProgress = true,
                        levelUpdate = LevelUpdate.RECOVERY,
                        tableUpdate = "tb_front",
                        currentProgress = percentage(1f, 4f)
                    )
                ),
                result[0]
            )
            assertEquals(
                FrontState(
                    status = UiStatusStateUpdate(
                        flagProgress = true,
                        levelUpdate = LevelUpdate.CLEAN,
                        tableUpdate = "tb_front",
                        currentProgress = percentage(2f, 4f),
                    )
                ),
                result[1]
            )
            assertEquals(
                FrontState(
                    status = UiStatusStateUpdate(
                        flagProgress = true,
                        levelUpdate = LevelUpdate.SAVE,
                        tableUpdate = "tb_front",
                        currentProgress = percentage(3f, 4f),
                    )
                ),
                result[2]
            )
            assertEquals(
                FrontState(
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
    fun `check - Check return failure if not selection any item`() =
        runTest {
            viewModel.check()
            assertEquals(
                true,
                viewModel.uiState.value.status.flagDialog
            )
            assertEquals(
                "FrontViewModel.updateState -> FrontViewModel.check -> NOT_SELECTION",
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
            assertEquals(
                false,
                viewModel.uiState.value.status.flagAccess
            )
        }

    @Test
    fun `check - Check idSelection and access if selection any item`() =
        runTest {
            whenever(
                listFront()
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
                            flag = false
                        ),
                        ItemCheckBoxScreenModel(
                            id = 3,
                            desc = "Test3",
                            flag = false
                        )
                    )
                )
            )
            viewModel.list()
            viewModel.onCheckChanged(
                id = 3,
                checked = true
            )
            viewModel.check()
            assertEquals(
                3,
                viewModel.uiState.value.idSelection
            )
            assertEquals(
                true,
                viewModel.uiState.value.status.flagAccess
            )
            assertEquals(
                false,
                viewModel.uiState.value.status.flagFailure
            )
        }

}