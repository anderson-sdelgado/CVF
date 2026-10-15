package br.com.usinasantafe.cvf.presenter.view.note.driver

import br.com.usinasantafe.cvf.MainDispatcherRule
import br.com.usinasantafe.cvf.domain.usecases.config.GetStatusSend
import br.com.usinasantafe.cvf.domain.usecases.manager.GetTitleMenu
import br.com.usinasantafe.cvf.domain.usecases.note.DeleteNote
import br.com.usinasantafe.cvf.domain.usecases.note.GetRegDriver
import br.com.usinasantafe.cvf.domain.usecases.note.HasRegColab
import br.com.usinasantafe.cvf.domain.usecases.note.SetRegDriver
import br.com.usinasantafe.cvf.lib.Errors
import br.com.usinasantafe.cvf.lib.OptionMenu
import br.com.usinasantafe.cvf.lib.StatusSend
import br.com.usinasantafe.cvf.lib.TypeButton
import br.com.usinasantafe.cvf.utils.resultFailure
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.atLeastOnce
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class DriverViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getTitleMenu = mock<GetTitleMenu>()
    private val getRegDriver = mock<GetRegDriver>()
    private val deleteNote = mock<DeleteNote>()
    private val hasRegColab = mock<HasRegColab>()
    private val setRegDriver = mock<SetRegDriver>()
    private val getStatusSend = mock<GetStatusSend>()

    private fun createdViewModel(): DriverViewModel {
        whenever(
            getTitleMenu()
        ).thenReturn(
            flowOf("Test")
        )
        whenever(
            getStatusSend()
        ).thenReturn(
            flowOf(StatusSend.SEND)
        )
        return DriverViewModel(
            getTitleMenu = getTitleMenu,
            getRegDriver = getRegDriver,
            deleteNote = deleteNote,
            hasRegColab = hasRegColab,
            setRegDriver = setRegDriver,
            getStatusSend = getStatusSend
        )
    }

    @Test
    fun `recoverData - Check return failure if function not executed`() =
        runTest {
            whenever(
                getRegDriver()
            ).thenReturn(
                resultFailure(
                    context = "GetRegDriver",
                    message = "-",
                    cause = Exception()
                )
            )
            val viewModel = createdViewModel()
            viewModel.recoverData()
            assertEquals(
                true,
                viewModel.uiState.value.status.flagDialog
            )
            assertEquals(
                "DriverViewModel.recoverData -> DriverViewModel.updateState -> GetRegDriver -> java.lang.Exception",
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
    fun `recoverData - Check return failure if have error in GetDescRelease`() =
        runTest {
            whenever(
                getTitleMenu()
            ).thenReturn(
                flow { throw Exception("GetDescRelease") }
            )
            whenever(
                getStatusSend()
            ).thenReturn(
                flowOf(StatusSend.SEND)
            )
            val viewModel = DriverViewModel(
                getTitleMenu = getTitleMenu,
                getRegDriver = getRegDriver,
                deleteNote = deleteNote,
                hasRegColab = hasRegColab,
                setRegDriver = setRegDriver,
                getStatusSend = getStatusSend
            )
            assertEquals(
                true,
                viewModel.uiState.value.status.flagDialog
            )
            assertEquals(
                "DriverViewModel.<init> -> DriverViewModel.updateState -> DriverViewModel.observeTitle -> EXCEPTION",
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
    fun `recoverData - Check return failure if have error in GetRegDriver`() =
        runTest {
            whenever(
                getRegDriver()
            ).thenReturn(
                resultFailure(
                    context = "GetRegDriver",
                    message = "-",
                    cause = Exception()
                )
            )
            val viewModel = createdViewModel()
            viewModel.recoverData()
            assertEquals(
                true,
                viewModel.uiState.value.status.flagDialog
            )
            assertEquals(
                "DriverViewModel.recoverData -> DriverViewModel.updateState -> GetRegDriver -> java.lang.Exception",
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
    fun `recoverData - Check return correct if function execute successfully`() =
        runTest {
            whenever(
                getRegDriver()
            ).thenReturn(
                Result.success("Test2")
            )
            val viewModel = createdViewModel()
            viewModel.recoverData()
            assertEquals(
                "Test",
                viewModel.uiState.value.descRelease
            )
            assertEquals(
                "Test2",
                viewModel.uiState.value.text
            )
            assertEquals(
                false,
                viewModel.uiState.value.flagMenu
            )
        }

    @Test
    fun `onOptionMenu - Check altered if option is different from DELETE `() =
        runTest {
            val viewModel = createdViewModel()
            viewModel.onOptionMenu(OptionMenu.CONFIG)
            assertEquals(
                OptionMenu.CONFIG,
                viewModel.uiState.value.optionMenu
            )
            assertEquals(
                true,
                viewModel.uiState.value.flagMenu
            )
            assertEquals(
                false,
                viewModel.uiState.value.flagCheckDialog
            )
        }

    @Test
    fun `onOptionMenu - Check altered if option is equal from DELETE `() =
        runTest {
            val viewModel = createdViewModel()
            viewModel.onOptionMenu(OptionMenu.DELETE)
            assertEquals(
                OptionMenu.DELETE,
                viewModel.uiState.value.optionMenu
            )
            assertEquals(
                false,
                viewModel.uiState.value.flagMenu
            )
            assertEquals(
                true,
                viewModel.uiState.value.flagCheckDialog
            )
        }

    @Test
    fun `delete - Check return failure if have error in DeleteNote`() =
        runTest {
            whenever(
                deleteNote()
            ).thenReturn(
                resultFailure(
                    context = "DeleteNote",
                    message = "-",
                    cause = Exception()
                )
            )
            val viewModel = createdViewModel()
            viewModel.delete()
            assertEquals(
                true,
                viewModel.uiState.value.status.flagDialog
            )
            assertEquals(
                "DriverViewModel.delete -> DriverViewModel.updateState -> DeleteNote -> java.lang.Exception",
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
            assertEquals(
                false,
                viewModel.uiState.value.flagMenu
            )
        }

    @Test
    fun `delete - Check return correct if function execute successfully`() =
        runTest {
            val viewModel = createdViewModel()
            viewModel.delete()
            verify(deleteNote, atLeastOnce()).invoke()
            assertEquals(
                true,
                viewModel.uiState.value.flagMenu
            )
        }

    @Test
    fun `onTextField - Check remover char`() {
        val viewModel = createdViewModel()
        viewModel.onTextField(
            "1",
            TypeButton.NUMERIC
        )
        viewModel.onTextField(
            "9",
            TypeButton.NUMERIC
        )
        viewModel.onTextField(
            "7",
            TypeButton.NUMERIC
        )
        viewModel.onTextField(
            "5",
            TypeButton.NUMERIC
        )
        viewModel.onTextField(
            "9",
            TypeButton.NUMERIC
        )
        viewModel.onTextField(
            "APAGAR",
            TypeButton.CLEAN
        )
        viewModel.onTextField(
            "APAGAR",
            TypeButton.CLEAN
        )
        viewModel.onTextField(
            "APAGAR",
            TypeButton.CLEAN
        )
        viewModel.onTextField(
            "1",
            TypeButton.NUMERIC
        )
        assertEquals(
            viewModel.uiState.value.text,
            "191"
        )
    }

    @Test
    fun `set - Check msg of empty field`() {
        val viewModel = createdViewModel()
        viewModel.onTextField(
            "OK",
            TypeButton.OK
        )
        assertEquals(
            viewModel.uiState.value.status.flagDialog,
            true
        )
        assertEquals(
            viewModel.uiState.value.status.errors,
            Errors.FIELD_EMPTY
        )
    }

    @Test
    fun `set - Check return failure if have error in CheckRegDriver`() =
        runTest {
            whenever(
                hasRegColab("19759")
            ).thenReturn(
                resultFailure(
                    context = "CheckRegDriver",
                    message = "-",
                    cause = Exception()
                )
            )
            val viewModel = createdViewModel()
            viewModel.onTextField(
                "19759",
                TypeButton.NUMERIC
            )
            viewModel.onTextField(
                "OK",
                TypeButton.OK
            )
            assertEquals(
                true,
                viewModel.uiState.value.status.flagDialog
            )
            assertEquals(
                "DriverViewModel.onTextField -> DriverViewModel.set -> DriverViewModel.updateState -> CheckRegDriver -> java.lang.Exception",
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
            assertEquals(
                false,
                viewModel.uiState.value.status.flagProgress
            )
        }

    @Test
    fun `set - Check msg if reg driver is invalid`() =
        runTest {
            whenever(
                hasRegColab("19759")
            ).thenReturn(
                Result.success(false)
            )
            val viewModel = createdViewModel()
            viewModel.onTextField(
                "19759",
                TypeButton.NUMERIC
            )
            viewModel.onTextField(
                "OK",
                TypeButton.OK
            )
            assertEquals(
                true,
                viewModel.uiState.value.status.flagDialog
            )
            assertEquals(
                "DriverViewModel.onTextField -> DriverViewModel.updateState -> DriverViewModel.set -> INVALID",
                viewModel.uiState.value.status.failure
            )
            assertEquals(
                Errors.INVALID,
                viewModel.uiState.value.status.errors
            )
            assertEquals(
                true,
                viewModel.uiState.value.status.flagFailure
            )
            assertEquals(
                false,
                viewModel.uiState.value.status.flagProgress
            )
        }

    @Test
    fun `set - Check return failure if have error in SetRegDriver`() =
        runTest {
            whenever(
                hasRegColab("19759")
            ).thenReturn(
                Result.success(true)
            )
            whenever(
                setRegDriver("19759")
            ).thenReturn(
                resultFailure(
                    context = "SetRegDriver",
                    message = "-",
                    cause = Exception()
                )
            )
            val viewModel = createdViewModel()
            viewModel.onTextField(
                "19759",
                TypeButton.NUMERIC
            )
            viewModel.onTextField(
                "OK",
                TypeButton.OK
            )
            assertEquals(
                true,
                viewModel.uiState.value.status.flagDialog
            )
            assertEquals(
                "DriverViewModel.onTextField -> DriverViewModel.set -> DriverViewModel.updateState -> SetRegDriver -> java.lang.Exception",
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
            assertEquals(
                false,
                viewModel.uiState.value.status.flagProgress
            )
        }

    @Test
    fun `set - Check return correct if function execute successfully`() =
        runTest {
            whenever(
                hasRegColab("19759")
            ).thenReturn(
                Result.success(true)
            )
            val viewModel = createdViewModel()
            viewModel.onTextField(
                "19759",
                TypeButton.NUMERIC
            )
            viewModel.onTextField(
                "OK",
                TypeButton.OK
            )
            viewModel.set()
            verify(setRegDriver, atLeastOnce()).invoke("19759")
            assertEquals(
                true,
                viewModel.uiState.value.status.flagAccess
            )
            assertEquals(
                false,
                viewModel.uiState.value.status.flagProgress
            )
        }

}
