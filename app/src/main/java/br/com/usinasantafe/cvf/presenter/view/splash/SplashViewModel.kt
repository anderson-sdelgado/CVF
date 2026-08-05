package br.com.usinasantafe.cvf.presenter.view.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.usinasantafe.cvf.utils.UiStateWithStatus
import br.com.usinasantafe.cvf.utils.UiStatusState
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import br.com.usinasantafe.cvf.utils.onFailureState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.copy

data class SplashState(
    override val status: UiStatusState = UiStatusState()
) : UiStateWithStatus<SplashState> {

    override fun copyWithStatus(status: UiStatusState): SplashState =
        copy(status = status)

}

@HiltViewModel
class SplashViewModel @Inject constructor(
) : ViewModel() {

    private val _uiState = MutableStateFlow(SplashState())
    val uiState = _uiState.asStateFlow()

    private val state get() = uiState.value

    private fun updateState(block: SplashState.() -> SplashState) {
        _uiState.update(block)
    }

    fun onCloseDialog() = updateState { copy(status = status.copy(flagDialog = false, flagFailure = false)) }

    fun startApp() = viewModelScope.launch {
        runCatching {
            true
        }
            .onSuccess { updateState { copy(status = status.copy(flagAccess = it)) } }
            .onFailureState(getClassAndMethod(), ::updateState)
    }


}