package br.com.usinasantafe.cvf.presenter.view.note.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import br.com.usinasantafe.cvf.presenter.view.addTextField
import br.com.usinasantafe.cvf.presenter.view.clearTextField
import br.com.usinasantafe.cvf.utils.UiStateWithStatusUpdate
import br.com.usinasantafe.cvf.utils.UiStatusStateUpdate
import br.com.usinasantafe.cvf.utils.onFailureUpdate
import br.com.usinasantafe.cvf.utils.onSuccessUpdateAccess
import br.com.usinasantafe.cvf.utils.required
import br.com.usinasantafe.cvf.utils.withFailure
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CartState(
    val flagCheckDialog: Boolean = false,
    val posCart: Int = 0,
    val flagMenu: Boolean = false,
    val optionMenu: OptionMenu = OptionMenu.DELETE,
    val descRelease: String = "",
    val text: String = "",
    val flagReturn: Boolean = false,
    val flagFinish: Boolean = false,
    override val status: UiStatusStateUpdate = UiStatusStateUpdate()
) : UiStateWithStatusUpdate<CartState> {

    override fun copyWithStatus(status: UiStatusStateUpdate): CartState =
        copy(status = status)

}

@HiltViewModel
class CartViewModel @Inject constructor(
    private val getTitleMenu: GetTitleMenu,
    private val deleteNote: DeleteNote,
    private val posCart: PosCart,
    private val getNroCart: GetNroCart,
    private val checkNroCart: CheckNroCart,
    private val setNroCart: SetNroCart,
    private val getTypeTruck: GetTypeTruck,
    private val limitQtdCart: LimitQtdCart
) : ViewModel() {

    private val _uiState = MutableStateFlow(CartState())
    val uiState = _uiState.asStateFlow()

    private val state get() = uiState.value

    private fun updateState(block: CartState.() -> CartState) {
        _uiState.update(block)
    }

    fun onCloseDialog() = updateState { copy(status = status.copy(flagDialog = false, flagFailure = false)) }

    fun onCheckDialog(flag: Boolean) = updateState { copy(flagCheckDialog = flag) }

    fun recoverData() = viewModelScope.launch {
        data class RecoverDriver(
            val descRelease: String,
            val text: String,
            val posCart: Int
        )
        runCatching {
            val descRelease = getTitleMenu().getOrThrow().required("descRelease")
            val text = getNroCart().getOrThrow() ?: ""
            val posCart = posCart().getOrThrow().required("posCart")
            RecoverDriver(
                descRelease = descRelease,
                text = text,
                posCart = posCart
            )
        }
            .onSuccess {
                updateState { copy(descRelease = it.descRelease, text = it.text, posCart = it.posCart, flagMenu = false) }
            }
            .onFailureUpdate(::updateState)
    }

    fun onOptionMenu(optionMenu: OptionMenu) = viewModelScope.launch {
        runCatching {
            optionMenu == OptionMenu.DELETE
        }
            .onSuccess { check ->
                updateState {
                    copy(optionMenu = optionMenu, flagMenu = !check, flagCheckDialog = check)
                }
            }
            .onFailureUpdate(::updateState)
    }

    fun delete() = viewModelScope.launch {
        runCatching {
            deleteNote().getOrThrow()
        }
            .onSuccess {
                updateState { copy(flagMenu = true) }
            }
            .onFailureUpdate(::updateState)
    }

    fun onTextField(text: String, typeButton: TypeButton) {
        when (typeButton) {
            TypeButton.NUMERIC -> updateState { copy(text = addTextField(this.text, text)) }
            TypeButton.CLEAN -> updateState { copy(text = clearTextField(this.text)) }
            TypeButton.OK -> set()
            TypeButton.CANCEL -> updateState { copy(flagReturn = true) }
        }
    }

    fun set() = viewModelScope.launch {
        runCatching {
            val typeTruck = getTypeTruck().getOrThrow()
            if((state.text.isEmpty()) && (typeTruck == TypeTruck.HAULAGE_TRUCK) && (state.posCart == 1)) {
                updateState { withFailure(Errors.FIELD_EMPTY) }
                return@launch
            }
            if(state.text.isEmpty()) return@runCatching true
            updateState { copy(status = status.copy(flagProgress = true)) }
            val check = checkNroCart(state.text).getOrThrow()
            if (!check) {
                updateState { withFailure(Errors.INVALID) }
                return@launch
            }
            setNroCart(state.text).getOrThrow()
            val finish = limitQtdCart().getOrThrow()
            if(finish == state.posCart) return@runCatching true
            false
        }
            .onSuccess {
                updateState { copy(status = status.copy(flagAccess = it)) }
                if(!it) recoverData()
            }
            .onFailureUpdate(::updateState)
    }
}