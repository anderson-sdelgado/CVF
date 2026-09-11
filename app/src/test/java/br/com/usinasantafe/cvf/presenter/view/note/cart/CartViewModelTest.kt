package br.com.usinasantafe.cvf.presenter.view.note.cart

import androidx.lifecycle.SavedStateHandle
import br.com.usinasantafe.cvf.MainCoroutineRule
import br.com.usinasantafe.cvf.domain.usecases.manager.GetTitleMenu
import br.com.usinasantafe.cvf.domain.usecases.note.HasNroEquip
import br.com.usinasantafe.cvf.domain.usecases.note.DeleteNote
import br.com.usinasantafe.cvf.domain.usecases.note.GetNroCart
import br.com.usinasantafe.cvf.domain.usecases.note.GetTypeTruck
import br.com.usinasantafe.cvf.domain.usecases.manager.QtdLimitCart
import br.com.usinasantafe.cvf.domain.usecases.note.CheckInvertedCart
import br.com.usinasantafe.cvf.domain.usecases.note.CheckRepeatedCart
import br.com.usinasantafe.cvf.domain.usecases.note.PosCart
import br.com.usinasantafe.cvf.domain.usecases.note.SetNroCart
import br.com.usinasantafe.cvf.lib.Errors
import br.com.usinasantafe.cvf.lib.FlowCart
import br.com.usinasantafe.cvf.lib.OptionMenu
import br.com.usinasantafe.cvf.lib.TypeButton
import br.com.usinasantafe.cvf.lib.TypeTruck
import br.com.usinasantafe.cvf.presenter.navigation.Args
import br.com.usinasantafe.cvf.utils.resultFailure
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
    private val hasNroEquip = mock<HasNroEquip>()
    private val setNroCart = mock<SetNroCart>()
    private val getTypeTruck = mock<GetTypeTruck>()
    private val qtdLimitCart = mock<QtdLimitCart>()
    private val checkRepeatedCart = mock<CheckRepeatedCart>()
    private val checkInvertedCart = mock<CheckInvertedCart>()

    private  fun createdViewModel(
        flowCart: FlowCart = FlowCart.NORMAL
    ) = CartViewModel(
        savedStateHandle = SavedStateHandle(
            mapOf(
                Args.FLOW_CART_ARG to flowCart.ordinal
            )
        ),
        getTitleMenu = getTitleMenu,
        deleteNote = deleteNote,
        posCart = posCart,
        getNroCart = getNroCart,
        hasNroEquip = hasNroEquip,
        setNroCart = setNroCart,
        getTypeTruck = getTypeTruck,
        qtdLimitCart = qtdLimitCart,
        checkRepeatedCart = checkRepeatedCart,
        checkInvertedCart = checkInvertedCart
    )

    @Test
    fun `recoverData - Check return failure if have error in PosCart and flowCart is FlowCart RETURN`() =
        runTest {
            whenever(
                posCart()
            ).thenReturn(
                resultFailure(
                    context = "PosCart",
                    message = "-",
                    cause = Exception()
                )
            )
            val viewModel = createdViewModel(FlowCart.RETURN)
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
    fun `recoverData - Check return correct if function execute successfully and flowCart is FlowCart RETURN`() =
        runTest {
            whenever(
                posCart()
            ).thenReturn(
                Result.success(2)
            )
            val viewModel = createdViewModel(FlowCart.RETURN)
            viewModel.recoverData()
            verify(getNroCart, atLeastOnce()).invoke(2)
        }

    @Test
    fun `recoverData - Check return correct if function execute successfully and flowCart is FlowCart NORMAL`() =
        runTest {
            val viewModel = createdViewModel()
            viewModel.recoverData()
            verify(getNroCart, atLeastOnce()).invoke(1)
            verify(posCart, never()).invoke()
        }

    @Test
    fun `get - Check return failure if function not executed`() =
        runTest {
            val viewModel = createdViewModel()
            viewModel.get(2)
            assertEquals(
                true,
                viewModel.uiState.value.status.flagDialog
            )
            assertEquals(
                "CartViewModel.get -> CartViewModel.updateState -> descRelease is required -> null",
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
    fun `get - Check return failure if have error in GetNroCart`() =
        runTest {
            whenever(
                getNroCart(3)
            ).thenReturn(
                resultFailure(
                    context = "GetNroCart",
                    message = "-",
                    cause = Exception()
                )
            )
            val viewModel = createdViewModel()
            viewModel.get(3)
            assertEquals(
                true,
                viewModel.uiState.value.status.flagDialog
            )
            assertEquals(
                "CartViewModel.get -> CartViewModel.updateState -> GetNroCart -> java.lang.Exception",
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
    fun `get - Check return failure if have error in GetTitleMenu`() =
        runTest {
            whenever(
                getNroCart(3)
            ).thenReturn(
                Result.success("Test1")
            )
            whenever(
                getTitleMenu()
            ).thenReturn(
                resultFailure(
                    context = "GetTitleMenu",
                    message = "-",
                    cause = Exception()
                )
            )
            val viewModel = createdViewModel()
            viewModel.get(3)
            assertEquals(
                true,
                viewModel.uiState.value.status.flagDialog
            )
            assertEquals(
                "CartViewModel.get -> CartViewModel.updateState -> GetTitleMenu -> java.lang.Exception",
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
                getNroCart(1)
            ).thenReturn(
                Result.success("Test1")
            )
            val viewModel = createdViewModel()
            viewModel.get(1)
            assertEquals(
                "Test",
                viewModel.uiState.value.descRelease
            )
            assertEquals(
                "Test1",
                viewModel.uiState.value.text
            )
            assertEquals(
                1,
                viewModel.uiState.value.pos
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
            val viewModel = createdViewModel()
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
            val viewModel = createdViewModel()
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
            val viewModel = createdViewModel()
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
            val viewModel = createdViewModel(FlowCart.RETURN)
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
            val viewModel = createdViewModel(FlowCart.RETURN)
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
    fun `set - Check return failure if have error in CheckRepeatedCart`() =
        runTest {
            wheneverRecoverData(2, "100")
            val viewModel = createdViewModel(FlowCart.RETURN)
            viewModel.recoverData()
            whenever(
                getTypeTruck()
            ).thenReturn(
                Result.success(TypeTruck.HAULAGE_TRUCK)
            )
            whenever(
                checkRepeatedCart("100")
            ).thenReturn(
                resultFailure(
                    context = "CheckRepeatedCart",
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
                "CartViewModel.set -> CartViewModel.updateState -> CheckRepeatedCart -> java.lang.Exception",
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
    fun `set - Check return failure if CheckRepeatedCart return true`() =
        runTest {
            wheneverRecoverData(2, "100")
            val viewModel = createdViewModel(FlowCart.RETURN)
            viewModel.recoverData()
            whenever(
                getTypeTruck()
            ).thenReturn(
                Result.success(TypeTruck.HAULAGE_TRUCK)
            )
            whenever(
                checkRepeatedCart("100")
            ).thenReturn(
                Result.success(true)
            )
            viewModel.set()
            assertEquals(
                true,
                viewModel.uiState.value.status.flagDialog
            )
            assertEquals(
                "CartViewModel.updateState -> CartViewModel.set -> CART_REPEATED",
                viewModel.uiState.value.status.failure
            )
            assertEquals(
                Errors.CART_REPEATED,
                viewModel.uiState.value.status.errors
            )
            assertEquals(
                true,
                viewModel.uiState.value.status.flagFailure
            )
        }

    @Test
    fun `set - Check return failure if text is not empty and have error in HasNroCart`() =
        runTest {
            wheneverRecoverData(2, "100")
            val viewModel = createdViewModel(FlowCart.RETURN)
            viewModel.recoverData()
            whenever(
                getTypeTruck()
            ).thenReturn(
                Result.success(TypeTruck.HAULAGE_TRUCK)
            )
            whenever(
                checkRepeatedCart("100")
            ).thenReturn(
                Result.success(false)
            )
            whenever(
                hasNroEquip("100", 2)
            ).thenReturn(
                resultFailure(
                    context = "HasNroCart",
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
                "CartViewModel.set -> CartViewModel.updateState -> HasNroCart -> java.lang.Exception",
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
    fun `set - Check return failure invalid if text is not empty and HasNroCart return false`() =
        runTest {
            wheneverRecoverData(1, "100")
            val viewModel = createdViewModel()
            viewModel.recoverData()
            whenever(
                getTypeTruck()
            ).thenReturn(
                Result.success(TypeTruck.HAULAGE_TRUCK)
            )
            whenever(
                checkRepeatedCart("100")
            ).thenReturn(
                Result.success(false)
            )
            whenever(
                hasNroEquip("100", 1)
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
    fun `set - Check return failure if have error in CheckInvertedCart`() =
        runTest {
            wheneverRecoverData(1, "100")
            val viewModel = createdViewModel()
            viewModel.recoverData()
            whenever(
                getTypeTruck()
            ).thenReturn(
                Result.success(TypeTruck.HAULAGE_TRUCK)
            )
            whenever(
                checkRepeatedCart("100")
            ).thenReturn(
                Result.success(false)
            )
            whenever(
                hasNroEquip("100", 1)
            ).thenReturn(
                Result.success(true)
            )
            whenever(
                checkInvertedCart("100", 1, TypeTruck.HAULAGE_TRUCK)
            ).thenReturn(
                resultFailure(
                    context = "CheckInvertedCart",
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
                "CartViewModel.set -> CartViewModel.updateState -> CheckInvertedCart -> java.lang.Exception",
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
    fun `set - Check return failure if CheckInvertedCart return true`() =
        runTest {
            wheneverRecoverData(1, "100")
            val viewModel = createdViewModel()
            viewModel.recoverData()
            whenever(
                getTypeTruck()
            ).thenReturn(
                Result.success(TypeTruck.HAULAGE_TRUCK)
            )
            whenever(
                checkRepeatedCart("100")
            ).thenReturn(
                Result.success(false)
            )
            whenever(
                hasNroEquip("100", 1)
            ).thenReturn(
                Result.success(true)
            )
            whenever(
                checkInvertedCart("100", 1, TypeTruck.HAULAGE_TRUCK)
            ).thenReturn(
                Result.success(true)
            )
            viewModel.set()
            assertEquals(
                true,
                viewModel.uiState.value.status.flagDialog
            )
            assertEquals(
                "CartViewModel.updateState -> CartViewModel.set -> INVERTED_CART",
                viewModel.uiState.value.status.failure
            )
            assertEquals(
                Errors.INVERTED_CART,
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
            val viewModel = createdViewModel(FlowCart.RETURN)
            viewModel.recoverData()
            whenever(
                getTypeTruck()
            ).thenReturn(
                Result.success(TypeTruck.HAULAGE_TRUCK)
            )
            whenever(
                checkRepeatedCart("100")
            ).thenReturn(
                Result.success(false)
            )
            whenever(
                hasNroEquip("100", 2)
            ).thenReturn(
                Result.success(true)
            )
            whenever(
                checkInvertedCart("100", 2, TypeTruck.HAULAGE_TRUCK)
            ).thenReturn(
                Result.success(false)
            )
            whenever(
                setNroCart("100", 2)
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
            val viewModel = createdViewModel(FlowCart.RETURN)
            viewModel.recoverData()
            whenever(
                getTypeTruck()
            ).thenReturn(
                Result.success(TypeTruck.HAULAGE_TRUCK)
            )
            whenever(
                checkRepeatedCart("100")
            ).thenReturn(
                Result.success(false)
            )
            whenever(
                hasNroEquip("100", 2)
            ).thenReturn(
                Result.success(true)
            )
            whenever(
                checkInvertedCart("100", 2, TypeTruck.HAULAGE_TRUCK)
            ).thenReturn(
                Result.success(false)
            )
            whenever(
                qtdLimitCart()
            ).thenReturn(
                resultFailure(
                    context = "LimitQtdCart",
                    message = "-",
                    cause = Exception()
                )
            )
            viewModel.set()
            verify(setNroCart, atLeastOnce()).invoke("100", 2)
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
            val viewModel = createdViewModel(FlowCart.RETURN)
            viewModel.recoverData()
            whenever(
                getTypeTruck()
            ).thenReturn(
                Result.success(TypeTruck.HAULAGE_TRUCK)
            )
            whenever(
                checkRepeatedCart("100")
            ).thenReturn(
                Result.success(false)
            )
            whenever(
                hasNroEquip("100", 3)
            ).thenReturn(
                Result.success(true)
            )
            whenever(
                checkInvertedCart("100", 3, TypeTruck.HAULAGE_TRUCK)
            ).thenReturn(
                Result.success(false)
            )
            whenever(
                qtdLimitCart()
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
            val viewModel = createdViewModel(FlowCart.RETURN)
            viewModel.recoverData()
            whenever(
                getTypeTruck()
            ).thenReturn(
                Result.success(TypeTruck.HAULAGE_TRUCK)
            )
            whenever(
                checkRepeatedCart("100")
            ).thenReturn(
                Result.success(false)
            )
            whenever(
                hasNroEquip("100", 2)
            ).thenReturn(
                Result.success(true)
            )
            whenever(
                checkInvertedCart("100", 2, TypeTruck.HAULAGE_TRUCK)
            ).thenReturn(
                Result.success(false)
            )
            whenever(
                qtdLimitCart()
            ).thenReturn(
                Result.success(3)
            )
            assertEquals(
                false,
                viewModel.uiState.value.status.flagAccess
            )
            viewModel.set()
            verify(getTitleMenu, times(2)).invoke()
            verify(getNroCart, atLeastOnce()).invoke(2)
            verify(getNroCart, atLeastOnce()).invoke(3)
            verify(posCart, atLeastOnce()).invoke()
            assertEquals(
                false,
                viewModel.uiState.value.status.flagAccess
            )
        }

    //////////////////////////////////////////////////////////////////////////////////

    suspend fun wheneverRecoverData(pos: Int = 1, text: String? = null) {
        whenever(
            getTitleMenu()
        ).thenReturn(
            Result.success("Test")
        )
        whenever(
            getNroCart(pos)
        ).thenReturn(
            Result.success(text)
        )
        whenever(
            posCart()
        ).thenReturn(
            Result.success(pos)
        )
    }

}