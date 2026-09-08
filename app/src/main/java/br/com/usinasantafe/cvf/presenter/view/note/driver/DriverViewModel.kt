package br.com.usinasantafe.cvf.presenter.view.note.driver

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.usinasantafe.cvf.domain.usecases.manager.GetTitleMenu
import br.com.usinasantafe.cvf.domain.usecases.note.HasRegDriver
import br.com.usinasantafe.cvf.domain.usecases.note.DeleteNote
import br.com.usinasantafe.cvf.domain.usecases.note.GetRegDriver
import br.com.usinasantafe.cvf.domain.usecases.note.SetRegDriver
import br.com.usinasantafe.cvf.lib.Errors
import br.com.usinasantafe.cvf.lib.OptionMenu
import br.com.usinasantafe.cvf.lib.TypeButton
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

data class DriverState(
    val flagCheckDialog: Boolean = false,
    val flagMenu: Boolean = false,
    val optionMenu: OptionMenu = OptionMenu.DELETE,
    val descRelease: String = "",
    val text: String = "",
    override val status: UiStatusStateUpdate = UiStatusStateUpdate()
) : UiStateWithStatusUpdate<DriverState> {

    override fun copyWithStatus(status: UiStatusStateUpdate): DriverState =
        copy(status = status)

}

@HiltViewModel
class DriverViewModel @Inject constructor(
    private val getTitleMenu: GetTitleMenu,
    private val getRegDriver: GetRegDriver,
    private val deleteNote: DeleteNote,
    private val hasRegDriver: HasRegDriver,
    private val setRegDriver: SetRegDriver
) : ViewModel() {

    private val _uiState = MutableStateFlow(DriverState())
    val uiState = _uiState.asStateFlow()

    private val state get() = uiState.value

    private fun updateState(block: DriverState.() -> DriverState) {
        _uiState.update(block)
    }

    fun onCloseDialog() = updateState { copy(status = status.copy(flagDialog = false, flagFailure = false)) }

    fun onCheckDialog(flag: Boolean) = updateState { copy(flagCheckDialog = flag) }

    fun recoverData() = viewModelScope.launch {
        data class RecoverDriver(
            val descRelease: String,
            val text: String
        )
        runCatching {
            val descRelease = getTitleMenu().getOrThrow().required("descRelease")
            val text = getRegDriver().getOrThrow() ?: ""
            RecoverDriver(
                descRelease = descRelease,
                text = text
            )
        }
            .onSuccess {
                updateState { copy(descRelease = it.descRelease, text = it.text, flagMenu = false) }
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
            else -> {}
        }
    }

    fun set() = viewModelScope.launch {
        runCatching {
            if(state.text.isEmpty()) {
                updateState { withFailure( Errors.FIELD_EMPTY) }
                return@launch
            }
            updateState { copy(status = status.copy(flagProgress = true)) }
            val check = hasRegDriver(state.text).getOrThrow()
            if(!check) {
                updateState { withFailure(Errors.INVALID) }
                return@launch
            }
            setRegDriver(state.text).getOrThrow()
        }
            .onSuccessUpdateAccess(::updateState)
            .onFailureUpdate(::updateState)
    }

}