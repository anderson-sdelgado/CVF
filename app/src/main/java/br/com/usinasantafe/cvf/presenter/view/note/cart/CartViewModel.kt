package br.com.usinasantafe.cvf.presenter.view.note.cart

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.usinasantafe.cvf.domain.usecases.manager.GetTitleMenu
import br.com.usinasantafe.cvf.domain.usecases.note.HasNroCart
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
import br.com.usinasantafe.cvf.presenter.navigation.Args.FLOW_CART_ARG
import br.com.usinasantafe.cvf.presenter.view.addTextField
import br.com.usinasantafe.cvf.presenter.view.clearTextField
import br.com.usinasantafe.cvf.utils.UiStateWithStatusUpdate
import br.com.usinasantafe.cvf.utils.UiStatusStateUpdate
import br.com.usinasantafe.cvf.utils.onFailureUpdate
import br.com.usinasantafe.cvf.utils.required
import br.com.usinasantafe.cvf.utils.withFailure
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CartState(
    val flowCart: FlowCart = FlowCart.NORMAL,
    val flagCheckDialog: Boolean = false,
    val pos: Int = 0,
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
    savedStateHandle: SavedStateHandle,
    private val getTitleMenu: GetTitleMenu,
    private val deleteNote: DeleteNote,
    private val posCart: PosCart,
    private val getNroCart: GetNroCart,
    private val hasNroCart: HasNroCart,
    private val setNroCart: SetNroCart,
    private val getTypeTruck: GetTypeTruck,
    private val qtdLimitCart: QtdLimitCart,
    private val checkRepeatedCart: CheckRepeatedCart,
    private val checkInvertedCart: CheckInvertedCart
) : ViewModel() {

    private val flowCart: Int = savedStateHandle[FLOW_CART_ARG]!!

    private val _uiState = MutableStateFlow(CartState())
    val uiState = _uiState.asStateFlow()

    private val state get() = uiState.value

    private fun updateState(block: CartState.() -> CartState) {
        _uiState.update(block)
    }

    fun onCloseDialog() = updateState { copy(status = status.copy(flagDialog = false, flagFailure = false)) }

    fun onCheckDialog(flag: Boolean) = updateState { copy(flagCheckDialog = flag) }

    init {
        updateState {
            copy(
                flowCart = FlowCart.entries[this@CartViewModel.flowCart]
            )
        }
    }

    fun recoverData() = viewModelScope.launch {
        runCatching {
            if (state.flowCart == FlowCart.RETURN) posCart().getOrThrow() else 1
        }
            .onSuccess { get(it) }
            .onFailureUpdate(::updateState)
    }

    fun get(pos: Int) = viewModelScope.launch {
        data class RecoverDriver(
            val descRelease: String,
            val text: String,
            val pos: Int
        )
        runCatching {
            val text = getNroCart(pos).getOrThrow() ?: ""
            val descRelease = getTitleMenu().getOrThrow().required("descRelease")
            RecoverDriver(
                descRelease = descRelease,
                text = text,
                pos = pos
            )
        }
            .onSuccess {
                updateState { copy(descRelease = it.descRelease, text = it.text, pos = it.pos, flagMenu = false) }
            }
            .onFailureUpdate(::updateState)
    }

    fun ret() = viewModelScope.launch {
        runCatching {
            if(state.pos == 1) {
                updateState { copy(flagReturn = true) }
                return@launch
            }
        }
            .onSuccess { get(pos = state.pos - 1) }
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
            TypeButton.CANCEL -> ret()
        }
    }

    fun set() = viewModelScope.launch {
        runCatching {
            val typeTruck = getTypeTruck().getOrThrow()
            if((state.text.isEmpty()) && (typeTruck == TypeTruck.HAULAGE_TRUCK) && (state.pos == 1)) {
                updateState { withFailure(Errors.FIELD_EMPTY) }
                return@launch
            }
            if(state.text.isEmpty()) return@runCatching true
            if(checkRepeatedCart(state.text).getOrThrow()) {
                updateState { withFailure(Errors.CART_REPEATED) }
                return@launch
            }
            updateState { copy(status = status.copy(flagProgress = true)) }
            if (!hasNroCart(state.text, state.pos).getOrThrow()) {
                updateState { withFailure(Errors.INVALID) }
                return@launch
            }
            if(checkInvertedCart(state.text, state.pos, typeTruck).getOrThrow()){
                updateState { withFailure(Errors.INVERTED_CART) }
                return@launch
            }
            setNroCart(state.text, state.pos).getOrThrow()
            val finish = qtdLimitCart().getOrThrow()
            if(finish == state.pos) return@runCatching true
            false
        }
            .onSuccess {
                updateState { copy(status = status.copy(flagAccess = it)) }
                if(!it) get(pos = state.pos + 1)
            }
            .onFailureUpdate(::updateState)
    }
}