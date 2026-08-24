package br.com.usinasantafe.cvf.presenter.view.note.driver

import br.com.usinasantafe.cvf.MainCoroutineRule
import br.com.usinasantafe.cvf.domain.usecases.manager.GetDescRelease
import br.com.usinasantafe.cvf.domain.usecases.note.CheckRegDriver
import br.com.usinasantafe.cvf.domain.usecases.note.DeleteNote
import br.com.usinasantafe.cvf.domain.usecases.note.GetRegDriver
import br.com.usinasantafe.cvf.domain.usecases.note.SetRegDriver
import br.com.usinasantafe.cvf.lib.Errors
import br.com.usinasantafe.cvf.lib.OptionMenu
import br.com.usinasantafe.cvf.lib.TypeButton
import br.com.usinasantafe.cvf.utils.resultFailure
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.atLeastOnce
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class DriverViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val getDescRelease = mock<GetDescRelease>()
    private val getRegDriver = mock<GetRegDriver>()
    private val deleteNote = mock<DeleteNote>()
    private val checkRegDriver = mock<CheckRegDriver>()
    private val setRegDriver = mock<SetRegDriver>()
    private val viewModel = DriverViewModel(
        getDescRelease = getDescRelease,
        getRegDriver = getRegDriver,
        deleteNote = deleteNote,
        checkRegDriver = checkRegDriver,
        setRegDriver = setRegDriver
    )

    @Test
    fun `onOptionMenu - Check return failure if have error in DeleteNote`() =
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
            viewModel.onOptionMenu(OptionMenu.DELETE)
            verify(deleteNote, atLeastOnce()).invoke()
            assertEquals(
                true,
                viewModel.uiState.value.status.flagDialog
            )
            assertEquals(
                "DriverViewModel.onOptionMenu -> DeleteNote -> java.lang.Exception",
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
    fun `onOptionMenu - Check return correct if function execute successfully and OptionMenu is DELETE`() =
        runTest {
            viewModel.onOptionMenu(OptionMenu.DELETE)
            verify(deleteNote, atLeastOnce()).invoke()
            assertEquals(
                OptionMenu.DELETE,
                viewModel.uiState.value.optionMenu
            )
            assertEquals(
                true,
                viewModel.uiState.value.flagMenu
            )
        }

    @Test
    fun `onOptionMenu - Check return correct if function execute successfully and OptionMenu is CONFIG`() =
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
        }

    @Test
    fun `recoverData - Check return failure if function not executed`() =
        runTest {
            viewModel.recoverData()
            assertEquals(
                true,
                viewModel.uiState.value.status.flagDialog
            )
            assertEquals(
                "DriverViewModel.recoverData -> Parameter specified as non-null is null: method br.com.usinasantafe.cvf.presenter.view.note.driver.DriverViewModel\$recoverData\$1\$RecoverDriver.<init>, parameter descRelease -> null",
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
                getDescRelease()
            ).thenReturn(
                resultFailure(
                    context = "GetDescRelease",
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
                "DriverViewModel.recoverData -> GetDescRelease -> java.lang.Exception",
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
                getDescRelease()
            ).thenReturn(
                Result.success("Test")
            )
            whenever(
                getRegDriver()
            ).thenReturn(
                resultFailure(
                    context = "GetRegDriver",
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
                "DriverViewModel.recoverData -> GetRegDriver -> java.lang.Exception",
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
            viewModel.onOptionMenu(OptionMenu.DELETE)
            verify(deleteNote, atLeastOnce()).invoke()
            assertEquals(
                OptionMenu.DELETE,
                viewModel.uiState.value.optionMenu
            )
            assertEquals(
                true,
                viewModel.uiState.value.flagMenu
            )
            whenever(
                getDescRelease()
            ).thenReturn(
                Result.success("Test")
            )
            whenever(
                getRegDriver()
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
            assertEquals(
                false,
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
    fun `set - Check return failure if have error in CheckRegDriver`() =
        runTest {
            whenever(
                checkRegDriver("19759")
            ).thenReturn(
                resultFailure(
                    context = "CheckRegDriver",
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
                "DriverViewModel.onTextField -> DriverViewModel.set -> CheckRegDriver -> java.lang.Exception",
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
                checkRegDriver("19759")
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
                checkRegDriver("19759")
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
                "DriverViewModel.onTextField -> DriverViewModel.set -> SetRegDriver -> java.lang.Exception",
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
                checkRegDriver("19759")
            ).thenReturn(
                Result.success(true)
            )
            whenever(
                checkRegDriver("19759")
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