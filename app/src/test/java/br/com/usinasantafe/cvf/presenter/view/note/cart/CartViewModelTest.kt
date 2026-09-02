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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
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

}