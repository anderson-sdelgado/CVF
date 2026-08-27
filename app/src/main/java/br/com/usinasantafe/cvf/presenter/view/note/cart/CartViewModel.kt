package br.com.usinasantafe.cvf.presenter.view.note.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.usinasantafe.cvf.lib.OptionMenu
import br.com.usinasantafe.cvf.lib.TypeButton
import br.com.usinasantafe.cvf.utils.UiStateWithStatusUpdate
import br.com.usinasantafe.cvf.utils.UiStatusStateUpdate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CartState(
    val nroCart: Int = 0,
    val flagMenu: Boolean = false,
    val optionMenu: OptionMenu = OptionMenu.DELETE,
    val descRelease: String = "",
    val text: String = "",
    override val status: UiStatusStateUpdate = UiStatusStateUpdate()
) : UiStateWithStatusUpdate<CartState> {

    override fun copyWithStatus(status: UiStatusStateUpdate): CartState =
        copy(status = status)

}

@HiltViewModel
class CartViewModel @Inject constructor(
) : ViewModel() {

    private val _uiState = MutableStateFlow(CartState())
    val uiState = _uiState.asStateFlow()

    private val state get() = uiState.value

    private fun updateState(block: CartState.() -> CartState) {
        _uiState.update(block)
    }

    fun onCloseDialog() = updateState { copy(status = status.copy(flagDialog = false, flagFailure = false)) }

    fun recoverData() = viewModelScope.launch {

    }

    fun onOptionMenu(optionMenu: OptionMenu) = viewModelScope.launch {

    }

    fun onTextField(text: String, typeButton: TypeButton) {

    }

    fun set() = viewModelScope.launch {

    }
}