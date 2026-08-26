package br.com.usinasantafe.cvf.presenter.view.configuration.password

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.usinasantafe.cvf.domain.usecases.config.CheckPassword
import br.com.usinasantafe.cvf.lib.Errors
import br.com.usinasantafe.cvf.lib.OptionMenu
import br.com.usinasantafe.cvf.presenter.navigation.Args.OPTION_MENU_ARG
import br.com.usinasantafe.cvf.utils.UiStateWithStatus
import br.com.usinasantafe.cvf.utils.UiStatusState
import br.com.usinasantafe.cvf.utils.onFailureState
import br.com.usinasantafe.cvf.utils.onSuccessStateAccess
import br.com.usinasantafe.cvf.utils.withFailure
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PasswordState(
    val optionMenu: OptionMenu = OptionMenu.DELETE,
    val password: String = "",
    override val status: UiStatusState = UiStatusState()
) : UiStateWithStatus<PasswordState> {

    override fun copyWithStatus(status: UiStatusState): PasswordState =
        copy(status = status)

}

@HiltViewModel
class PasswordViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val checkPassword: CheckPassword,
) : ViewModel() {

    private val flowMenu: Int = savedStateHandle[OPTION_MENU_ARG]!!

    private val _uiState = MutableStateFlow(PasswordState())
    val uiState = _uiState.asStateFlow()

    private val state get() = uiState.value

    private fun updateState(block: PasswordState.() -> PasswordState) {
        _uiState.update(block)
    }

    fun onCloseDialog() = updateState { copy(status = status.copy(flagDialog = false, flagFailure = false)) }

    fun onPasswordChanged(password: String) = updateState { copy(password = password) }

    fun onCheckAccess() = viewModelScope.launch {
        runCatching {
            val check = checkPassword(state.password).getOrThrow()
            if (!check) {
                updateState { withFailure(Errors.PASSWORD_INVALID) }
                return@launch
            }
        }
            .onSuccessStateAccess(::updateState)
            .onFailureState(::updateState)

    }

}