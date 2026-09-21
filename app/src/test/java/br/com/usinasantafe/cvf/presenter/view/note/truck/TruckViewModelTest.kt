package br.com.usinasantafe.cvf.presenter.view.note.truck

import br.com.usinasantafe.cvf.MainCoroutineRule
import br.com.usinasantafe.cvf.domain.usecases.manager.GetTitleMenu
import br.com.usinasantafe.cvf.domain.usecases.note.DeleteNote
import br.com.usinasantafe.cvf.domain.usecases.note.GetNroTruck
import br.com.usinasantafe.cvf.domain.usecases.note.HasNroEquip
import br.com.usinasantafe.cvf.domain.usecases.note.SetNroTruck
import br.com.usinasantafe.cvf.lib.Errors
import br.com.usinasantafe.cvf.lib.OptionMenu
import br.com.usinasantafe.cvf.lib.TypeButton
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
    val mainCoroutineRule = MainCoroutineRule()

    private val getTitleMenu = mock<GetTitleMenu>()
    private val deleteNote = Mockito.mock<DeleteNote>()
    private val getNroTruck = mock<GetNroTruck>()
    private val hasNroEquip = mock<HasNroEquip>()
    private val setNroTruck = mock<SetNroTruck>()
    private val viewModel = TruckViewModel(
        getTitleMenu = getTitleMenu,
        deleteNote = deleteNote,
        getNroTruck = getNroTruck,
        hasNroEquip = hasNroEquip,
        setNroTruck = setNroTruck
    )

    @Test
    fun `recoverData - Check return failure if function not executed`() =
        runTest {
            viewModel.recoverData()
            assertEquals(
                true,
                viewModel.uiState.value.status.flagDialog
            )
            assertEquals(
                "TruckViewModel.recoverData -> TruckViewModel.updateState -> Parameter specified as non-null is null: method br.com.usinasantafe.cvf.presenter.view.note.truck.TruckViewModel\$recoverData\$1\$RecoverDriver.<init>, parameter descRelease -> null",
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
            viewModel.recoverData()
            assertEquals(
                true,
                viewModel.uiState.value.status.flagDialog
            )
            assertEquals(
                "TruckViewModel.recoverData -> TruckViewModel.updateState -> GetDescRelease -> java.lang.Exception",
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
                getTitleMenu()
            ).thenReturn(
                flowOf("Test")
            )
            whenever(
                getNroTruck()
            ).thenReturn(
                resultFailure(
                    context = "GetNroTruck",
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
                getTitleMenu()
            ).thenReturn(
                flowOf("Test")
            )
            whenever(
                getNroTruck()
            ).thenReturn(
                Result.success("Test2")
            )
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
            viewModel.delete()
            verify(deleteNote, atLeastOnce()).invoke()
            assertEquals(
                true,
                viewModel.uiState.value.flagMenu
            )
        }

    @Test
    fun `onTextField - Check remover char`() {
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
    fun `set - Check return failure if have error in CheckNroTruck`() =
        runTest {
            whenever(
                hasNroEquip("19759")
            ).thenReturn(
                resultFailure(
                    context = "CheckNroTruck",
                    message = "-",
                    cause = Exception()
                )
            )
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
                hasNroEquip("19759")
            ).thenReturn(
                Result.success(false)
            )
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
    fun `set - Check return failure if have error in SetNroTruck`() =
        runTest {
            whenever(
                hasNroEquip("19759")
            ).thenReturn(
                Result.success(true)
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
                hasNroEquip("19759")
            ).thenReturn(
                Result.success(true)
            )
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