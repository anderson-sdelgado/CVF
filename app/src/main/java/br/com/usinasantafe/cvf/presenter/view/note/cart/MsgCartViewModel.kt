package br.com.usinasantafe.cvf.presenter.view.note.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.usinasantafe.cvf.utils.UiStateWithStatus
import br.com.usinasantafe.cvf.utils.UiStatusState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MsgCartState(
    override val status: UiStatusState = UiStatusState()
) : UiStateWithStatus<MsgCartState> {

    override fun copyWithStatus(status: UiStatusState): MsgCartState =
        copy(status = status)

}

@HiltViewModel
class MsgCartViewModel @Inject constructor(
) : ViewModel() {

    private val _uiState = MutableStateFlow(MsgCartState())
    val uiState = _uiState.asStateFlow()

    private val state get() = uiState.value

    private fun updateState(block: MsgCartState.() -> MsgCartState) {
        _uiState.update(block)
    }

    fun onCloseDialog() = updateState { copy(status = status.copy(flagDialog = false, flagFailure = false)) }



}