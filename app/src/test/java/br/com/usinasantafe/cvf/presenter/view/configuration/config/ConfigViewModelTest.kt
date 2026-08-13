package br.com.usinasantafe.cvf.presenter.view.configuration.config

import br.com.usinasantafe.cvf.MainCoroutineRule
import br.com.usinasantafe.cvf.domain.usecases.config.GetConfig
import br.com.usinasantafe.cvf.domain.usecases.config.SetFinishUpdateAllTable
import br.com.usinasantafe.cvf.domain.usecases.config.UpdateConfig
import br.com.usinasantafe.cvf.domain.usecases.manager.HasManager
import br.com.usinasantafe.cvf.domain.usecases.update.UpdateTableColab
import br.com.usinasantafe.cvf.domain.usecases.update.UpdateTableEquip
import br.com.usinasantafe.cvf.domain.usecases.update.UpdateTableFront
import br.com.usinasantafe.cvf.domain.usecases.update.UpdateTableRelease
import br.com.usinasantafe.cvf.lib.Errors
import br.com.usinasantafe.cvf.lib.LevelUpdate
import br.com.usinasantafe.cvf.presenter.model.ConfigScreenModel
import br.com.usinasantafe.cvf.utils.UiStatusStateUpdate
import br.com.usinasantafe.cvf.utils.percentage
import br.com.usinasantafe.cvf.utils.resultFailure
import br.com.usinasantafe.cvf.utils.sizeUpdate
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
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
    private val updateTableFront = mock<UpdateTableFront>()
    private val updateTableRelease = mock<UpdateTableRelease>()
    private val hasManager = mock<HasManager>()
    private val getConfig = mock<GetConfig>()
    private var tableList = mutableListOf<String>()
    private val viewModel = ConfigViewModel(
        getConfig = getConfig,
        updateConfig = updateConfig,
        setFinishUpdateAllTable = setFinishUpdateAllTable,
        updateTableColab = updateTableColab,
        updateTableEquip = updateTableEquip,
        updateTableFront = updateTableFront,
        updateTableRelease = updateTableRelease,
        hasManager = hasManager
    )

    private val qtdTable = 4f

    @Test
    fun `onSaveAndUpdate - Check return failure if number, password or version is empty`() =
        runTest {
            viewModel.onSaveAndUpdate()
            val uiState = viewModel.uiState.value
            assertEquals(
                true,
                uiState.status.flagDialog
            )
            assertEquals(
                true,
                uiState.status.flagFailure
            )
            assertEquals(
                Errors.FIELD_EMPTY,
                uiState.status.errors
            )
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
            assertEquals(
                2,
                result.count()
            )
            assertEquals(
                UiStatusStateUpdate(
                    flagProgress = true,
                    levelUpdate = LevelUpdate.GET_TOKEN,
                    currentProgress = 0.33f
                ),
                result[0]
            )
            assertEquals(
                UiStatusStateUpdate(
                    errors = Errors.UPDATE,
                    flagDialog = true,
                    flagFailure = true,
                    failure = "UpdateConfig -> java.lang.NullPointerException"
                ),
                result[1]
            )
            viewModel.onNumberChanged("16997417840")
            viewModel.onPasswordChanged("12345")
            viewModel.onVersionChanged("1.00")
            viewModel.onSaveAndUpdate()
            val configState = viewModel.uiState.value
            assertEquals(
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
                ),
                configState
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
                ((qtdBefore * 3) + 2).toInt(),
                result.count()
            )
            assertEquals(
                ConfigState(
                    status = UiStatusStateUpdate(
                        flagProgress = true,
                        levelUpdate = LevelUpdate.RECOVERY,
                        tableUpdate = "tb_colab",
                        currentProgress = percentage(((qtdBefore * 3) + 1), qtdTable)
                    )
                ),
                result[(qtdBefore * 3).toInt()]
            )
            assertEquals(
                ConfigState(
                    status = UiStatusStateUpdate(
                        errors = Errors.UPDATE,
                        flagDialog = true,
                        flagFailure = true,
                        flagProgress = true,
                        currentProgress = 1f,
                        failure = "ConfigViewModel.updateAllDatabase -> CleanColab -> java.lang.NullPointerException",
                    )
                ),
                result[((qtdBefore * 3) + 1).toInt()]
            )
            viewModel.onNumberChanged("16997417840")
            viewModel.onPasswordChanged("12345")
            viewModel.onVersionChanged("1.00")
            viewModel.onSaveAndUpdate()
            val configState = viewModel.uiState.value
            assertEquals(
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
                ),
                configState
            )
        }

    @Test
    fun `update - Check return failure if have error in UpdateTableEquip`() =
        runTest {
            val qtdBefore = 1f
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
            wheneverSuccess(qtdBefore)
            whenever(
                updateTableEquip(
                    sizeAll = sizeUpdate(qtdTable),
                    count = (qtdBefore + 1)
                )
            ).thenReturn(
                flowOf(
                    UiStatusStateUpdate(
                        flagProgress = true,
                        levelUpdate = LevelUpdate.RECOVERY,
                        tableUpdate = "tb_equip",
                        currentProgress = percentage(((qtdBefore * 3) + 1), qtdTable)
                    ),
                    UiStatusStateUpdate(
                        errors = Errors.UPDATE,
                        flagDialog = true,
                        flagFailure = true,
                        failure = "CleanEquip -> java.lang.NullPointerException",
                    )
                )
            )
            val result = viewModel.updateAllDatabase().toList()
            assertEquals(
                ((qtdBefore * 3) + 2).toInt(),
                result.count()
            )
            checkResultUpdate(qtdBefore, result)
            assertEquals(
                ConfigState(
                    status = UiStatusStateUpdate(
                        flagProgress = true,
                        levelUpdate = LevelUpdate.RECOVERY,
                        tableUpdate = "tb_equip",
                        currentProgress = percentage(((qtdBefore * 3) + 1), qtdTable)
                    )
                ),
                result[(qtdBefore * 3).toInt()]
            )
            assertEquals(
                ConfigState(
                    status = UiStatusStateUpdate(
                        errors = Errors.UPDATE,
                        flagDialog = true,
                        flagFailure = true,
                        flagProgress = true,
                        currentProgress = 1f,
                        failure = "ConfigViewModel.updateAllDatabase -> CleanEquip -> java.lang.NullPointerException",
                    )
                ),
                result[((qtdBefore * 3) + 1).toInt()]
            )
            viewModel.onNumberChanged("16997417840")
            viewModel.onPasswordChanged("12345")
            viewModel.onVersionChanged("1.00")
            viewModel.onSaveAndUpdate()
            val configState = viewModel.uiState.value
            assertEquals(
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
                        failure = "ConfigViewModel.onSaveAndUpdate -> ConfigViewModel.updateAllDatabase -> CleanEquip -> java.lang.NullPointerException",
                    )
                ),
                configState
            )
        }

    @Test
    fun `update - Check return failure if have error in UpdateTableFront`() =
        runTest {
            val qtdBefore = 2f
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
            wheneverSuccess(qtdBefore)
            whenever(
                updateTableFront(
                    sizeAll = sizeUpdate(qtdTable),
                    count = (qtdBefore + 1)
                )
            ).thenReturn(
                flowOf(
                    UiStatusStateUpdate(
                        flagProgress = true,
                        levelUpdate = LevelUpdate.RECOVERY,
                        tableUpdate = "tb_front",
                        currentProgress = percentage(((qtdBefore * 3) + 1), qtdTable)
                    ),
                    UiStatusStateUpdate(
                        errors = Errors.UPDATE,
                        flagDialog = true,
                        flagFailure = true,
                        failure = "CleanFront -> java.lang.NullPointerException",
                    )
                )
            )
            val result = viewModel.updateAllDatabase().toList()
            assertEquals(
                ((qtdBefore * 3) + 2).toInt(),
                result.count()
            )
            checkResultUpdate(qtdBefore, result)
            assertEquals(
                ConfigState(
                    status = UiStatusStateUpdate(
                        flagProgress = true,
                        levelUpdate = LevelUpdate.RECOVERY,
                        tableUpdate = "tb_front",
                        currentProgress = percentage(((qtdBefore * 3) + 1), qtdTable)
                    )
                ),
                result[(qtdBefore * 3).toInt()]
            )
            assertEquals(
                ConfigState(
                    status = UiStatusStateUpdate(
                        errors = Errors.UPDATE,
                        flagDialog = true,
                        flagFailure = true,
                        flagProgress = true,
                        currentProgress = 1f,
                        failure = "ConfigViewModel.updateAllDatabase -> CleanFront -> java.lang.NullPointerException",
                    )
                ),
                result[((qtdBefore * 3) + 1).toInt()]
            )
            viewModel.onNumberChanged("16997417840")
            viewModel.onPasswordChanged("12345")
            viewModel.onVersionChanged("1.00")
            viewModel.onSaveAndUpdate()
            val configState = viewModel.uiState.value
            assertEquals(
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
                        failure = "ConfigViewModel.onSaveAndUpdate -> ConfigViewModel.updateAllDatabase -> CleanFront -> java.lang.NullPointerException",
                    )
                ),
                configState
            )
        }

    @Test
    fun `update - Check return failure if have error in UpdateTableRelease`() =
        runTest {
            val qtdBefore = 3f
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
            wheneverSuccess(qtdBefore)
            whenever(
                updateTableRelease(
                    sizeAll = sizeUpdate(qtdTable),
                    count = (qtdBefore + 1)
                )
            ).thenReturn(
                flowOf(
                    UiStatusStateUpdate(
                        flagProgress = true,
                        levelUpdate = LevelUpdate.RECOVERY,
                        tableUpdate = "tb_release",
                        currentProgress = percentage(((qtdBefore * 3) + 1), qtdTable)
                    ),
                    UiStatusStateUpdate(
                        errors = Errors.UPDATE,
                        flagDialog = true,
                        flagFailure = true,
                        failure = "CleanRelease -> java.lang.NullPointerException",
                    )
                )
            )
            val result = viewModel.updateAllDatabase().toList()
            assertEquals(
                ((qtdBefore * 3) + 2).toInt(),
                result.count()
            )
            checkResultUpdate(qtdBefore, result)
            assertEquals(
                ConfigState(
                    status = UiStatusStateUpdate(
                        flagProgress = true,
                        levelUpdate = LevelUpdate.RECOVERY,
                        tableUpdate = "tb_release",
                        currentProgress = percentage(((qtdBefore * 3) + 1), qtdTable)
                    )
                ),
                result[(qtdBefore * 3).toInt()]
            )
            assertEquals(
                ConfigState(
                    status = UiStatusStateUpdate(
                        errors = Errors.UPDATE,
                        flagDialog = true,
                        flagFailure = true,
                        flagProgress = true,
                        currentProgress = 1f,
                        failure = "ConfigViewModel.updateAllDatabase -> CleanRelease -> java.lang.NullPointerException",
                    )
                ),
                result[((qtdBefore * 3) + 1).toInt()]
            )
            viewModel.onNumberChanged("16997417840")
            viewModel.onPasswordChanged("12345")
            viewModel.onVersionChanged("1.00")
            viewModel.onSaveAndUpdate()
            val configState = viewModel.uiState.value
            assertEquals(
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
                        failure = "ConfigViewModel.onSaveAndUpdate -> ConfigViewModel.updateAllDatabase -> CleanRelease -> java.lang.NullPointerException",
                    )
                ),
                configState
            )
        }

    @Test
    fun `onSaveAndUpdate - Check return failure if have error in SetFinishUpdateAllTable`() =
        runTest {
            val qtdBefore = 4f
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
            wheneverSuccess(qtdBefore)
            whenever(
                setFinishUpdateAllTable()
            ).thenReturn(
                resultFailure(
                    "ISetFinishUpdateAllTable",
                    "-",
                    Exception()
                )
            )
            val result = viewModel.updateAllDatabase().toList()
            assertEquals(
                (qtdBefore * 3).toInt(),
                result.count()
            )
            checkResultUpdate(qtdBefore, result)
            viewModel.onNumberChanged("16997417840")
            viewModel.onPasswordChanged("12345")
            viewModel.onVersionChanged("1.00")
            viewModel.onSaveAndUpdate()
            val configState = viewModel.uiState.value
            assertEquals(
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
                ),
                configState
            )
        }

    @Test
    fun `onSaveAndUpdate - Check return correct if function execute successfully`() =
        runTest {
            val qtdBefore = 4f
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
            wheneverSuccess(qtdBefore)
            val result = viewModel.updateAllDatabase().toList()
            assertEquals(
                (qtdBefore * 3).toInt(),
                result.count()
            )
            checkResultUpdate(qtdBefore, result)
            viewModel.onNumberChanged("16997417840")
            viewModel.onPasswordChanged("12345")
            viewModel.onVersionChanged("1.00")
            viewModel.onSaveAndUpdate()
            val configState = viewModel.uiState.value
            assertEquals(
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
                ),
                configState
            )
            verify(setFinishUpdateAllTable, atLeastOnce()).invoke()
        }

    @Test
    fun `recoverData - Check return failure if have error in GetConfig`() =
        runTest {
            whenever(
                getConfig()
            ).thenReturn(
                resultFailure(
                    context = "GetConfig",
                    message = "-",
                    cause = Exception()
                )
            )
            viewModel.recoverData()
            assertEquals(
                true,
                viewModel.uiState.value.status.flagDialog
            )
            assertEquals(
                "ConfigViewModel.recoverData -> GetConfig -> java.lang.Exception",
                viewModel.uiState.value.status.failure
            )
            assertEquals(
                true,
                viewModel.uiState.value.status.flagFailure
            )
            assertEquals(
                Errors.EXCEPTION,
                viewModel.uiState.value.status.errors
            )
        }

    @Test
    fun `recoverData - Check return failure if have error in HasManager`() =
        runTest {
            whenever(
                getConfig()
            ).thenReturn(
                Result.success(null)
            )
            whenever(
                hasManager()
            ).thenReturn(
                resultFailure(
                    context = "HasManager",
                    message = "-",
                    cause = Exception()
                )
            )
            viewModel.recoverData()
            assertEquals(
                true,
                viewModel.uiState.value.status.flagDialog
            )
            assertEquals(
                "ConfigViewModel.recoverData -> HasManager -> java.lang.Exception",
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
    fun `recoverData - Check return null if GetConfig execute successfully and return null`() =
        runTest {
            whenever(
                getConfig()
            ).thenReturn(
                Result.success(null)
            )
            whenever(
                hasManager()
            ).thenReturn(
                Result.success(false)
            )
            viewModel.recoverData()
            assertEquals(
                "",
                viewModel.uiState.value.number
            )
            assertEquals(
                "",
                viewModel.uiState.value.password
            )
            assertEquals(
                false,
                viewModel.uiState.value.flagReturn
            )
        }

    @Test
    fun `recoverData - Check return correct if GetConfig execute successfully`() =
        runTest {
            whenever(
                getConfig()
            ).thenReturn(
                Result.success(
                    ConfigScreenModel(
                        number = "16997417840",
                        password = "12345"
                    )
                )
            )
            whenever(
                hasManager()
            ).thenReturn(
                Result.success(true)
            )
            viewModel.recoverData()
            assertEquals(
                "16997417840",
                viewModel.uiState.value.number
            )
            assertEquals(
                "12345",
                viewModel.uiState.value.password
            )
            assertEquals(
                true,
                viewModel.uiState.value.flagReturn
            )
        }

    ///////////////////////////////////////////////////////////////////////////////////////

    private fun wheneverSuccess(posTable: Float) =
        runTest {
            var contUpdate = 0f
            var contWhenever = 0f
            val sizeAll = sizeUpdate(qtdTable)
            tableList = mutableListOf(
                "tb_colab", "tb_equip", "tb_front", "tb_release",
            )
            val updateFunctions = mutableListOf<
                    suspend (Float, Float) -> Flow<UiStatusStateUpdate>
                    >(
                { sizeAll, count -> updateTableColab(sizeAll, count) },
                { sizeAll, count -> updateTableEquip(sizeAll, count) },
                { sizeAll, count -> updateTableFront(sizeAll, count) },
                { sizeAll, count -> updateTableRelease(sizeAll, count) },
            )
            for (func in updateFunctions) {
                whenever(
                    func(
                        sizeAll,
                        ++contUpdate
                    )
                ).thenReturn(
                    flowOf(
                        UiStatusStateUpdate(
                            flagProgress = true,
                            levelUpdate = LevelUpdate.RECOVERY,
                            tableUpdate = tableList[contUpdate.toInt() - 1],
                            currentProgress = percentage(++contWhenever, sizeAll)
                        ),
                        UiStatusStateUpdate(
                            flagProgress = true,
                            levelUpdate = LevelUpdate.CLEAN,
                            tableUpdate = tableList[contUpdate.toInt() - 1],
                            currentProgress = percentage(++contWhenever, sizeAll)
                        ),
                        UiStatusStateUpdate(
                            flagProgress = true,
                            levelUpdate = LevelUpdate.SAVE,
                            tableUpdate = tableList[contUpdate.toInt() - 1],
                            currentProgress = percentage(++contWhenever, sizeAll)
                        ),
                    )
                )
                if(posTable == contUpdate) break
            }
        }

    private fun checkResultUpdate(posTable: Float, result: List<ConfigState>) =
        runTest {
            val sizeAll = sizeUpdate(qtdTable)
            var contUpdate = 0f
            var count = 0
            for(table in tableList) {
                ++count
                assertEquals(
                    ConfigState(
                        status = UiStatusStateUpdate(
                            flagProgress = true,
                            levelUpdate = LevelUpdate.RECOVERY,
                            tableUpdate = table,
                            currentProgress = percentage(count.toFloat(), sizeAll)
                        )
                    ),
                    result[count - 1]
                )
                ++count
                assertEquals(
                    ConfigState(
                        status = UiStatusStateUpdate(
                            flagProgress = true,
                            levelUpdate = LevelUpdate.CLEAN,
                            tableUpdate = table,
                            currentProgress = percentage(count.toFloat(), sizeAll)
                        )
                    ),
                    result[count - 1]
                )
                ++count
                assertEquals(
                    ConfigState(
                        status = UiStatusStateUpdate(
                            flagProgress = true,
                            levelUpdate = LevelUpdate.SAVE,
                            tableUpdate = table,
                            currentProgress = percentage(count.toFloat(), sizeAll)
                        )
                    ),
                    result[count - 1]
                )
                ++contUpdate
                if(posTable == contUpdate) break
            }
        }

}