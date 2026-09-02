package br.com.usinasantafe.cvf.presenter.view.note.cart

import br.com.usinasantafe.cvf.MainCoroutineRule
import br.com.usinasantafe.cvf.domain.usecases.manager.GetTitleMenu
import br.com.usinasantafe.cvf.domain.usecases.note.CheckNroCart
import br.com.usinasantafe.cvf.domain.usecases.note.DeleteNote
import br.com.usinasantafe.cvf.domain.usecases.note.GetNroCart
import br.com.usinasantafe.cvf.domain.usecases.note.GetTypeTruck
import br.com.usinasantafe.cvf.domain.usecases.note.LimitQtdCart
import br.com.usinasantafe.cvf.domain.usecases.note.PosCart
import br.com.usinasantafe.cvf.domain.usecases.note.SetNroCart
import br.com.usinasantafe.cvf.lib.Errors
import br.com.usinasantafe.cvf.lib.OptionMenu
import br.com.usinasantafe.cvf.lib.TypeButton
import br.com.usinasantafe.cvf.lib.TypeTruck
import br.com.usinasantafe.cvf.utils.resultFailure
import com.ibm.icu.impl.duration.Period.at
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.atLeastOnce
import org.mockito.kotlin.never
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class CartViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val getTitleMenu = mock<GetTitleMenu>()
    private val deleteNote = mock<DeleteNote>()
    private val posCart = mock<PosCart>()
    private val getNroCart = mock<GetNroCart>()
    private val checkNroCart = mock<CheckNroCart>()
    private val setNroCart = mock<SetNroCart>()
    private val getTypeTruck = mock<GetTypeTruck>()
    private val limitQtdCart = mock<LimitQtdCart>()

    private val viewModel = CartViewModel(
        getTitleMenu = getTitleMenu,
        deleteNote = deleteNote,
        posCart = posCart,
        getNroCart = getNroCart,
        checkNroCart = checkNroCart,
        setNroCart = setNroCart,
        getTypeTruck = getTypeTruck,
        limitQtdCart = limitQtdCart
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
                "CartViewModel.recoverData -> CartViewModel.updateState -> descRelease is required -> null",
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
    fun `recoverData - Check return failure if have error in GetTitleMenu`() =
        runTest {
            whenever(
                getTitleMenu()
            ).thenReturn(
                resultFailure(
                    context = "GetTitleMenu",
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
                "CartViewModel.recoverData -> CartViewModel.updateState -> GetTitleMenu -> java.lang.Exception",
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
    fun `recoverData - Check return failure if have error in GetNroCart`() =
        runTest {
            whenever(
                getTitleMenu()
            ).thenReturn(
                Result.success("Test")
            )
            whenever(
                getNroCart()
            ).thenReturn(
                resultFailure(
                    context = "GetNroCart",
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
                "CartViewModel.recoverData -> CartViewModel.updateState -> GetNroCart -> java.lang.Exception",
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
    fun `recoverData - Check return failure if have error in PosCart`() =
        runTest {
            whenever(
                getTitleMenu()
            ).thenReturn(
                Result.success("Test")
            )
            whenever(
                getNroCart()
            ).thenReturn(
                Result.success("Test1")
            )
            whenever(
                posCart()
            ).thenReturn(
                resultFailure(
                    context = "PosCart",
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
                "CartViewModel.recoverData -> CartViewModel.updateState -> PosCart -> java.lang.Exception",
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
                Result.success("Test")
            )
            whenever(
                getNroCart()
            ).thenReturn(
                Result.success("Test1")
            )
            whenever(
                posCart()
            ).thenReturn(
                Result.success(2)
            )
            viewModel.recoverData()
            assertEquals(
                "Test",
                viewModel.uiState.value.descRelease
            )
            assertEquals(
                "Test1",
                viewModel.uiState.value.text
            )
            assertEquals(
                2,
                viewModel.uiState.value.posCart
            )
            assertEquals(
                false,
                viewModel.uiState.value.flagMenu
            )
        }

    @Test
    fun `onOptionMenu - Check altered if option is different from DELETE `() =
        runTest {
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
            viewModel.delete()
            assertEquals(
                true,
                viewModel.uiState.value.status.flagDialog
            )
            assertEquals(
                "CartViewModel.delete -> CartViewModel.updateState -> DeleteNote -> java.lang.Exception",
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
    fun `set - Check return failure if have error in GetTypeTruck`() =
        runTest {
            whenever(
                getTypeTruck()
            ).thenReturn(
                resultFailure(
                    context = "GetTypeTruck",
                    message = "-",
                    cause = Exception()
                )
            )
            viewModel.set()
            assertEquals(
                true,
                viewModel.uiState.value.status.flagDialog
            )
            assertEquals(
                "CartViewModel.set -> CartViewModel.updateState -> GetTypeTruck -> java.lang.Exception",
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
    fun `set - Check return failure field empty if text is empty, typeTruck is TypeTruck HAULAGE_TRUCK and posCart is 1`() =
        runTest {
            wheneverRecoverData()
            viewModel.recoverData()
            whenever(
                getTypeTruck()
            ).thenReturn(
                Result.success(TypeTruck.HAULAGE_TRUCK)
            )
            viewModel.set()
            assertEquals(
                true,
                viewModel.uiState.value.status.flagDialog
            )
            assertEquals(
                "CartViewModel.updateState -> CartViewModel.set -> FIELD_EMPTY",
                viewModel.uiState.value.status.failure
            )
            assertEquals(
                Errors.FIELD_EMPTY,
                viewModel.uiState.value.status.errors
            )
            assertEquals(
                true,
                viewModel.uiState.value.status.flagFailure
            )
        }

    @Test
    fun `set - Check return true if text is empty, typeTruck is TypeTruck TRUCK and posCart is 1`() =
        runTest {
            wheneverRecoverData()
            viewModel.recoverData()
            whenever(
                getTypeTruck()
            ).thenReturn(
                Result.success(TypeTruck.TRUCK)
            )
            assertEquals(
                false,
                viewModel.uiState.value.status.flagAccess
            )
            viewModel.set()
            assertEquals(
                true,
                viewModel.uiState.value.status.flagAccess
            )
        }

    @Test
    fun `set - Check return true if text is empty, typeTruck is TypeTruck TRUCK and posCart is different of 1`() =
        runTest {
            wheneverRecoverData(5)
            viewModel.recoverData()
            whenever(
                getTypeTruck()
            ).thenReturn(
                Result.success(TypeTruck.TRUCK)
            )
            assertEquals(
                false,
                viewModel.uiState.value.status.flagAccess
            )
            viewModel.set()
            assertEquals(
                true,
                viewModel.uiState.value.status.flagAccess
            )
        }

    @Test
    fun `set - Check return true if text is empty, typeTruck is TypeTruck HAULAGE_TRUCK and posCart is different of 1`() =
        runTest {
            wheneverRecoverData(5)
            viewModel.recoverData()
            whenever(
                getTypeTruck()
            ).thenReturn(
                Result.success(TypeTruck.HAULAGE_TRUCK)
            )
            assertEquals(
                false,
                viewModel.uiState.value.status.flagAccess
            )
            viewModel.set()
            assertEquals(
                true,
                viewModel.uiState.value.status.flagAccess
            )
        }

    @Test
    fun `set - Check return failure if text is not empty and have error in CheckNroCart`() =
        runTest {
            wheneverRecoverData(2, "100")
            viewModel.recoverData()
            whenever(
                getTypeTruck()
            ).thenReturn(
                Result.success(TypeTruck.HAULAGE_TRUCK)
            )
            whenever(
                checkNroCart("100")
            ).thenReturn(
                resultFailure(
                    context = "CheckNroCart",
                    message = "-",
                    cause = Exception()
                )
            )
            viewModel.set()
            assertEquals(
                true,
                viewModel.uiState.value.status.flagDialog
            )
            assertEquals(
                "CartViewModel.set -> CartViewModel.updateState -> CheckNroCart -> java.lang.Exception",
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
    fun `set - Check return failure invalid if text is not empty and CheckNroCart return false`() =
        runTest {
            wheneverRecoverData(2, "100")
            viewModel.recoverData()
            whenever(
                getTypeTruck()
            ).thenReturn(
                Result.success(TypeTruck.HAULAGE_TRUCK)
            )
            whenever(
                checkNroCart("100")
            ).thenReturn(
                Result.success(false)
            )
            viewModel.set()
            assertEquals(
                true,
                viewModel.uiState.value.status.flagDialog
            )
            assertEquals(
                "CartViewModel.updateState -> CartViewModel.set -> INVALID",
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
        }

    @Test
    fun `set - Check return failure if have error in SetNroCart`() =
        runTest {
            wheneverRecoverData(2, "100")
            viewModel.recoverData()
            whenever(
                getTypeTruck()
            ).thenReturn(
                Result.success(TypeTruck.HAULAGE_TRUCK)
            )
            whenever(
                checkNroCart("100")
            ).thenReturn(
                Result.success(true)
            )
            whenever(
                setNroCart("100")
            ).thenReturn(
                resultFailure(
                    context = "SetNroCart",
                    message = "-",
                    cause = Exception()
                )
            )
            viewModel.set()
            assertEquals(
                true,
                viewModel.uiState.value.status.flagDialog
            )
            assertEquals(
                "CartViewModel.set -> CartViewModel.updateState -> SetNroCart -> java.lang.Exception",
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
    fun `set - Check return failure if have error in LimitQtdCart`() =
        runTest {
            wheneverRecoverData(2, "100")
            viewModel.recoverData()
            whenever(
                getTypeTruck()
            ).thenReturn(
                Result.success(TypeTruck.HAULAGE_TRUCK)
            )
            whenever(
                checkNroCart("100")
            ).thenReturn(
                Result.success(true)
            )
            whenever(
                limitQtdCart()
            ).thenReturn(
                resultFailure(
                    context = "LimitQtdCart",
                    message = "-",
                    cause = Exception()
                )
            )
            viewModel.set()
            verify(setNroCart, atLeastOnce()).invoke("100")
            assertEquals(
                true,
                viewModel.uiState.value.status.flagDialog
            )
            assertEquals(
                "CartViewModel.set -> CartViewModel.updateState -> LimitQtdCart -> java.lang.Exception",
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
    fun `set - Check return true if LimitQtdCart is equal of posCart`() =
        runTest {
            wheneverRecoverData(3, "100")
            viewModel.recoverData()
            whenever(
                getTypeTruck()
            ).thenReturn(
                Result.success(TypeTruck.HAULAGE_TRUCK)
            )
            whenever(
                checkNroCart("100")
            ).thenReturn(
                Result.success(true)
            )
            whenever(
                limitQtdCart()
            ).thenReturn(
                Result.success(3)
            )
            assertEquals(
                false,
                viewModel.uiState.value.status.flagAccess
            )
            viewModel.set()
            assertEquals(
                true,
                viewModel.uiState.value.status.flagAccess
            )
        }

    @Test
    fun `set - Check return false if LimitQtdCart is different of posCart`() =
        runTest {
            wheneverRecoverData(2, "100")
            viewModel.recoverData()
            whenever(
                getTypeTruck()
            ).thenReturn(
                Result.success(TypeTruck.HAULAGE_TRUCK)
            )
            whenever(
                checkNroCart("100")
            ).thenReturn(
                Result.success(true)
            )
            whenever(
                limitQtdCart()
            ).thenReturn(
                Result.success(3)
            )
            assertEquals(
                false,
                viewModel.uiState.value.status.flagAccess
            )
            viewModel.set()
            verify(getTitleMenu, times(2)).invoke()
            verify(getNroCart, times(2)).invoke()
            verify(posCart, times(2)).invoke()
            assertEquals(
                false,
                viewModel.uiState.value.status.flagAccess
            )
        }

    //////////////////////////////////////////////////////////////////////////////////

    suspend fun wheneverRecoverData(posCart: Int = 1, text: String? = null) {
        whenever(
            getTitleMenu()
        ).thenReturn(
            Result.success("Test")
        )
        whenever(
            getNroCart()
        ).thenReturn(
            Result.success(text)
        )
        whenever(
            posCart()
        ).thenReturn(
            Result.success(posCart)
        )
    }

}