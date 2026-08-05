package br.com.usinasantafe.cvf.presenter.view.configuration.config

import br.com.usinasantafe.cvf.MainCoroutineRule
import br.com.usinasantafe.cvf.domain.usecases.config.SetFinishUpdateAllTable
import br.com.usinasantafe.cvf.domain.usecases.config.UpdateConfig
import br.com.usinasantafe.cvf.domain.usecases.update.UpdateTableColab
import br.com.usinasantafe.cvf.domain.usecases.update.UpdateTableEquip
import br.com.usinasantafe.cvf.lib.Errors
import br.com.usinasantafe.cvf.lib.LevelUpdate
import br.com.usinasantafe.cvf.utils.UiStatusStateUpdate
import br.com.usinasantafe.cvf.utils.percentage
import br.com.usinasantafe.cvf.utils.resultFailure
import br.com.usinasantafe.cvf.utils.sizeUpdate
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.atLeastOnce
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class ConfigViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val updateConfig = mock<UpdateConfig>()
    private val setFinishUpdateAllTable = mock<SetFinishUpdateAllTable>()
    private val updateTableColab = mock<UpdateTableColab>()
    private val updateTableEquip = mock<UpdateTableEquip>()

    private val viewModel = ConfigViewModel(
        updateConfig = updateConfig,
        setFinishUpdateAllTable = setFinishUpdateAllTable,
        updateTableColab = updateTableColab,
        updateTableEquip = updateTableEquip
    )

    private val qtdTable = 2f

    @Test
    fun `onSaveAndUpdate - Check return failure if number, password or version is empty`() =
        runTest {
            viewModel.onSaveAndUpdate()
            val uiState = viewModel.uiState.value
            assertEquals(true, uiState.status.flagDialog)
            assertEquals(true, uiState.status.flagFailure)
            assertEquals(Errors.FIELD_EMPTY, uiState.status.errors)
        }

    @Test
    fun `onSaveAndUpdate - Check return failure if have error in UpdateConfig`() =
        runTest {
            whenever(
                updateConfig(
                    "16997417840",
                    "12345",
                    "1.00",
                    3f,
                    1f
                )
            ).thenReturn(
                flowOf(
                    UiStatusStateUpdate(
                        flagProgress = true,
                        levelUpdate = LevelUpdate.GET_TOKEN,
                        currentProgress = 0.33f
                    ),
                    UiStatusStateUpdate(
                        errors = Errors.UPDATE,
                        flagDialog = true,
                        flagFailure = true,
                        failure = "UpdateConfig -> java.lang.NullPointerException"
                    )
                )
            )

            val result = updateConfig("16997417840", "12345", "1.00", 3f, 1f).toList()

            assertEquals(result.count(), 2)
            assertEquals(
                result[0],
            UiStatusStateUpdate(
                flagProgress = true,
                levelUpdate = LevelUpdate.GET_TOKEN,
                currentProgress = 0.33f
                )
            )
            assertEquals(
                result[1],
            UiStatusStateUpdate(
                    errors = Errors.UPDATE,
                    flagDialog = true,
                    flagFailure = true,
                    failure = "UpdateConfig -> java.lang.NullPointerException"
                )
            )

            viewModel.onNumberChanged("16997417840")
            viewModel.onPasswordChanged("12345")
            viewModel.onVersionChanged("1.00")
            viewModel.onSaveAndUpdate()
            val configState = viewModel.uiState.value
            assertEquals(
                configState,
                ConfigState(
                    number = "16997417840",
                    password = "12345",
                    version = "1.00",
                    status = UiStatusStateUpdate(
                        errors = Errors.UPDATE,
                        flagFailure = true,
                        flagDialog = true,
                        flagProgress = true,
                        currentProgress = 1f,
                        failure = "ConfigViewModel.onSaveAndUpdate -> UpdateConfig -> java.lang.NullPointerException",
                    )
                )
            )
        }

    @Test
    fun `onSaveAndUpdate - Check return failure if have error in UpdateColab`() =
        runTest {
            val qtdBefore = 0f
            val sizeAllTables = sizeUpdate(qtdTable)

            whenever(
                updateConfig(
                    "16997417840",
                    "12345",
                    "1.00",
                    3f,
                    1f
                )
            ).thenReturn(
                flowOf(
                    UiStatusStateUpdate(
                        flagProgress = true,
                        levelUpdate = LevelUpdate.GET_TOKEN,
                        currentProgress = 0.33f
                    )
                )
            )

            whenever(
                updateTableColab(
                    sizeAll = sizeAllTables,
                    count = 1f
                )
            ).thenReturn(
                flowOf(
                    UiStatusStateUpdate(
                        flagProgress = true,
                        levelUpdate = LevelUpdate.RECOVERY,
                        tableUpdate = "tb_colab",
                        currentProgress = percentage(((qtdBefore * 3) + 1), qtdTable)
                    ),
                    UiStatusStateUpdate(
                        errors = Errors.UPDATE,
                        flagDialog = true,
                        flagFailure = true,
                        failure = "CleanColab -> java.lang.NullPointerException",
                    )
                )
            )

            val result = viewModel.updateAllDatabase().toList()
            assertEquals(
                result.count(),
                ((qtdBefore * 3) + 2).toInt()
            )
            assertEquals(
                result[(qtdBefore * 3).toInt()],
                ConfigState(
                    status = UiStatusStateUpdate(
                        flagProgress = true,
                        levelUpdate = LevelUpdate.RECOVERY,
                        tableUpdate = "tb_colab",
                        currentProgress = percentage(((qtdBefore * 3) + 1), qtdTable)
                    )
                )
            )
            assertEquals(
                result[((qtdBefore * 3) + 1).toInt()],
                ConfigState(
                    status = UiStatusStateUpdate(
                        errors = Errors.UPDATE,
                        flagDialog = true,
                        flagFailure = true,
                        flagProgress = true,
                        currentProgress = 1f,
                        failure = "ConfigViewModel.updateAllDatabase -> CleanColab -> java.lang.NullPointerException",
                    )
                )
            )

            viewModel.onNumberChanged("16997417840")
            viewModel.onPasswordChanged("12345")
            viewModel.onVersionChanged("1.00")
            viewModel.onSaveAndUpdate()
            val configState = viewModel.uiState.value
            assertEquals(
                configState,
                ConfigState(
                    number = "16997417840",
                    password = "12345",
                    version = "1.00",
                    status = UiStatusStateUpdate(
                        errors = Errors.UPDATE,
                        flagFailure = true,
                        flagDialog = true,
                        flagProgress = true,
                        currentProgress = 1f,
                        failure = "ConfigViewModel.onSaveAndUpdate -> ConfigViewModel.updateAllDatabase -> CleanColab -> java.lang.NullPointerException",
                    )
                )
            )
        }

    @Test
    fun `onSaveAndUpdate - Check return failure if have error in SetFinishUpdateAllTable`() =
        runTest {

            whenever(updateTableColab(any(), any())).thenReturn(flowOf())
            whenever(updateTableEquip(any(), any())).thenReturn(flowOf())

            whenever(
                updateConfig(any(), any(), any(), any(), any())
            ).thenReturn(
                flowOf(
                    UiStatusStateUpdate(
                        flagProgress = true,
                        levelUpdate = LevelUpdate.GET_TOKEN,
                        currentProgress = 0.33f
                    )
                )
            )

            whenever(
                setFinishUpdateAllTable()
            ).thenReturn(
                resultFailure(
                    "ISetFinishUpdateAllTable",
                    "-",
                    Exception()
                )
            )

            viewModel.onNumberChanged("16997417840")
            viewModel.onPasswordChanged("12345")
            viewModel.onVersionChanged("1.00")
            
            viewModel.onSaveAndUpdate()

            val configState = viewModel.uiState.value
            assertEquals(
                configState,
                ConfigState(
                    number = "16997417840",
                    password = "12345",
                    version = "1.00",
                    status = UiStatusStateUpdate(
                        errors = Errors.UPDATE,
                        flagFailure = true,
                        flagDialog = true,
                        flagProgress = true,
                        currentProgress = 1f,
                        failure = "ConfigViewModel.onSaveAndUpdate -> ISetFinishUpdateAllTable -> java.lang.Exception",
                    )
                )
            )
        }

    @Test
    fun `onSaveAndUpdate - Check return correct if function execute successfully`() =
        runTest {

            whenever(updateTableColab(any(), any())).thenReturn(flowOf())
            whenever(updateTableEquip(any(), any())).thenReturn(flowOf())

            whenever(
                updateConfig(any(), any(), any(), any(), any())
            ).thenReturn(
                flowOf(
                    UiStatusStateUpdate(
                        flagProgress = true,
                        levelUpdate = LevelUpdate.GET_TOKEN,
                        currentProgress = 0.33f
                    )
                )
            )

            viewModel.onNumberChanged("16997417840")
            viewModel.onPasswordChanged("12345")
            viewModel.onVersionChanged("1.00")

            viewModel.onSaveAndUpdate()

            val configState = viewModel.uiState.value
            assertEquals(
                configState,
                ConfigState(
                    number = "16997417840",
                    password = "12345",
                    version = "1.00",
                    status = UiStatusStateUpdate(
                        tableUpdate = "",
                        flagDialog = true,
                        flagProgress = false,
                        flagFailure = false,
                        levelUpdate = LevelUpdate.FINISH_UPDATE_COMPLETED,
                        currentProgress = 1f
                    )
                )
            )
            verify(setFinishUpdateAllTable, atLeastOnce()).invoke()
        }
}