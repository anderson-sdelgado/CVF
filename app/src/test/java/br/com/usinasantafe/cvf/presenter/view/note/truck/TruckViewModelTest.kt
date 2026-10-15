package br.com.usinasantafe.cvf.presenter.view.note.truck

import br.com.usinasantafe.cvf.MainDispatcherRule
import br.com.usinasantafe.cvf.domain.usecases.config.GetStatusSend
import br.com.usinasantafe.cvf.domain.usecases.manager.GetTitleMenu
import br.com.usinasantafe.cvf.domain.usecases.note.DeleteNote
import br.com.usinasantafe.cvf.domain.usecases.note.GetNroTruck
import br.com.usinasantafe.cvf.domain.usecases.note.CheckNroTruck
import br.com.usinasantafe.cvf.domain.usecases.note.SetNroTruck
import br.com.usinasantafe.cvf.lib.CheckTruck
import br.com.usinasantafe.cvf.lib.Errors
import br.com.usinasantafe.cvf.lib.OptionMenu
import br.com.usinasantafe.cvf.lib.TypeButton
import br.com.usinasantafe.cvf.lib.StatusSend
import br.com.usinasantafe.cvf.utils.resultFailure
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.atLeastOnce
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class TruckViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getTitleMenu = mock<GetTitleMenu>()
    private val deleteNote = Mockito.mock<DeleteNote>()
    private val getNroTruck = mock<GetNroTruck>()
    private val checkNroTruck = mock<CheckNroTruck>()
    private val setNroTruck = mock<SetNroTruck>()
    private val getStatusSend = mock<GetStatusSend>()

    private fun createdViewModel(): TruckViewModel {
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
        return TruckViewModel(
            getTitleMenu = getTitleMenu,
            deleteNote = deleteNote,
            getNroTruck = getNroTruck,
            checkNroTruck = checkNroTruck,
            setNroTruck = setNroTruck,
            getStatusSend = getStatusSend
        )
    }

    @Test
    fun `recoverData - Check return failure if function not executed`() =
        runTest {
            whenever(
                getNroTruck()
            ).thenReturn(
                resultFailure(
                    context = "GetNroTruck",
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
                "TruckViewModel.recoverData -> TruckViewModel.updateState -> GetNroTruck -> java.lang.Exception",
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
            val viewModel = TruckViewModel(
                getTitleMenu = getTitleMenu,
                deleteNote = deleteNote,
                getNroTruck = getNroTruck,
                checkNroTruck = checkNroTruck,
                setNroTruck = setNroTruck,
                getStatusSend = getStatusSend
            )
            assertEquals(
                true,
                viewModel.uiState.value.status.flagDialog
            )
            assertEquals(
                "TruckViewModel.<init> -> TruckViewModel.updateState -> TruckViewModel.observeTitle -> EXCEPTION",
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
    fun `recoverData - Check return failure if have error in GetNroTruck`() =
        runTest {
            whenever(
                getNroTruck()
            ).thenReturn(
                resultFailure(
                    context = "GetNroTruck",
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
                "TruckViewModel.recoverData -> TruckViewModel.updateState -> GetNroTruck -> java.lang.Exception",
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
                getNroTruck()
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
        }

    @Test
    fun `onOptionMenu - Check altered if option is different from DELETE `() =
        runTest {
            val viewModel = createdViewModel()
            viewModel.onOptionMenu(OptionMenu.CONFIG)
            verify(deleteNote, never()).invoke()
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
            verify(deleteNote, never()).invoke()
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
                "TruckViewModel.delete -> TruckViewModel.updateState -> DeleteNote -> java.lang.Exception",
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
            "191",
            viewModel.uiState.value.text
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
            true,
            viewModel.uiState.value.status.flagDialog
        )
        assertEquals(
            Errors.FIELD_EMPTY,
            viewModel.uiState.value.status.errors
        )
    }

    @Test
    fun `set - Check return failure if have error in CheckNroTruck`() =
        runTest {
            whenever(
                checkNroTruck("19759")
            ).thenReturn(
                resultFailure(
                    context = "CheckNroTruck",
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
                "TruckViewModel.onTextField -> TruckViewModel.set -> TruckViewModel.updateState -> CheckNroTruck -> java.lang.Exception",
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
    fun `set - Check msg if nro truck is invalid`() =
        runTest {
            whenever(
                checkNroTruck("19759")
            ).thenReturn(
                Result.success(CheckTruck.INVALID)
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
                "TruckViewModel.onTextField -> TruckViewModel.updateState -> TruckViewModel.set -> INVALID",
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
    fun `set - Check msg if release and front is invalid`() =
        runTest {
            whenever(
                checkNroTruck("19759")
            ).thenReturn(
                Result.success(CheckTruck.RELEASE_AND_FRONT_INVALID)
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
                "TruckViewModel.onTextField -> TruckViewModel.updateState -> TruckViewModel.set -> RELEASE_AND_FRONT_INVALID",
                viewModel.uiState.value.status.failure
            )
            assertEquals(
                Errors.RELEASE_AND_FRONT_INVALID,
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
    fun `set - Check msg if a pre cec already exists`() =
        runTest {
            whenever(
                checkNroTruck("19759")
            ).thenReturn(
                Result.success(CheckTruck.PRE_CEC_EXISTENT)
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
                "TruckViewModel.onTextField -> TruckViewModel.updateState -> TruckViewModel.set -> PRE_CEC_EXISTENT",
                viewModel.uiState.value.status.failure
            )
            assertEquals(
                Errors.PRE_CEC_EXISTENT,
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
    fun `set - Check return failure if have error in SetNroTruck`() =
        runTest {
            whenever(
                checkNroTruck("19759")
            ).thenReturn(
                Result.success(CheckTruck.OK)
            )
            whenever(
                setNroTruck("19759")
            ).thenReturn(
                resultFailure(
                    context = "SetNroTruck",
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
                "TruckViewModel.onTextField -> TruckViewModel.set -> TruckViewModel.updateState -> SetNroTruck -> java.lang.Exception",
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
                checkNroTruck("19759")
            ).thenReturn(
                Result.success(CheckTruck.OK)
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
            verify(setNroTruck, atLeastOnce()).invoke("19759")
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