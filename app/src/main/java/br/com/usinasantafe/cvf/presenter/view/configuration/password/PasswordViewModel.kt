package br.com.usinasantafe.cvf.presenter.view.configuration.password

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

data class PasswordState(
    override val status: UiStatusState = UiStatusState()
) : UiStateWithStatus<PasswordState> {

    override fun copyWithStatus(status: UiStatusState): PasswordState =
        copy(status = status)

}

@HiltViewModel
class PasswordViewModel @Inject constructor(
) : ViewModel() {

    private val _uiState = MutableStateFlow(PasswordState())
    val uiState = _uiState.asStateFlow()

    private val state get() = uiState.value

    private fun updateState(block: PasswordState.() -> PasswordState) {
        _uiState.update(block)
    }

    fun onCloseDialog() = updateState { copy(status = status.copy(flagDialog = false, flagFailure = false)) }



}