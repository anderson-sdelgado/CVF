package br.com.usinasantafe.cvf.presenter.view.note.truck

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.usinasantafe.cvf.domain.usecases.manager.GetTitleMenu
import br.com.usinasantafe.cvf.domain.usecases.note.DeleteNote
import br.com.usinasantafe.cvf.domain.usecases.note.GetNroTruck
import br.com.usinasantafe.cvf.lib.OptionMenu
import br.com.usinasantafe.cvf.lib.TypeButton
import br.com.usinasantafe.cvf.utils.UiStateWithStatusUpdate
import br.com.usinasantafe.cvf.utils.UiStatusStateUpdate
import br.com.usinasantafe.cvf.utils.onFailureUpdate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TruckState(
    val flagMenu: Boolean = false,
    val optionMenu: OptionMenu = OptionMenu.DELETE,
    val descRelease: String = "",
    val text: String = "",
    override val status: UiStatusStateUpdate = UiStatusStateUpdate()
) : UiStateWithStatusUpdate<TruckState> {

    override fun copyWithStatus(status: UiStatusStateUpdate): TruckState =
        copy(status = status)

}

@HiltViewModel
class TruckViewModel @Inject constructor(
    private val getTitleMenu: GetTitleMenu,
    private val getNroTruck: GetNroTruck,
    private val deleteNote: DeleteNote,
) : ViewModel() {

    private val _uiState = MutableStateFlow(TruckState())
    val uiState = _uiState.asStateFlow()

    private val state get() = uiState.value

    private fun updateState(block: TruckState.() -> TruckState) {
        _uiState.update(block)
    }

    fun onCloseDialog() = updateState { copy(status = status.copy(flagDialog = false, flagFailure = false)) }

    fun recoverData() = viewModelScope.launch {
        data class RecoverDriver(
            val descRelease: String,
            val text: String
        )
        runCatching {
            val descRelease = getTitleMenu().getOrThrow()
            val text = getNroTruck().getOrThrow() ?: ""
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
            if (optionMenu == OptionMenu.DELETE) {
                deleteNote().getOrThrow()
            }
        }
            .onSuccess {
                updateState {
                    copy(optionMenu = optionMenu, flagMenu = true)
                }
            }
            .onFailureUpdate(::updateState)
    }

    fun onTextField(text: String, typeButton: TypeButton) {

    }

    fun set() = viewModelScope.launch {

    }

}