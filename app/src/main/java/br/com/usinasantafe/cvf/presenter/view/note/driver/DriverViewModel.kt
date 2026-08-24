package br.com.usinasantafe.cvf.presenter.view.note.driver

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.usinasantafe.cvf.domain.usecases.manager.GetDescRelease
import br.com.usinasantafe.cvf.domain.usecases.note.GetRegDriver
import br.com.usinasantafe.cvf.domain.usecases.update.UpdateTableColab
import br.com.usinasantafe.cvf.lib.TypeButton
import br.com.usinasantafe.cvf.presenter.view.addTextField
import br.com.usinasantafe.cvf.presenter.view.clearTextField
import br.com.usinasantafe.cvf.utils.UiStateWithStatusUpdate
import br.com.usinasantafe.cvf.utils.UiStatusStateUpdate
import br.com.usinasantafe.cvf.utils.executeUpdateSteps
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import br.com.usinasantafe.cvf.utils.onFailureUpdate
import br.com.usinasantafe.cvf.utils.sizeUpdate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DriverState(
    val descRelease: String = "",
    val text: String = "",
    override val status: UiStatusStateUpdate = UiStatusStateUpdate()
) : UiStateWithStatusUpdate<DriverState> {

    override fun copyWithStatus(status: UiStatusStateUpdate): DriverState =
        copy(status = status)

}

@HiltViewModel
class DriverViewModel @Inject constructor(
    private val getDescRelease: GetDescRelease,
    private val getRegDriver: GetRegDriver
) : ViewModel() {

    private val _uiState = MutableStateFlow(DriverState())
    val uiState = _uiState.asStateFlow()

    private val state get() = uiState.value

    private fun updateState(block: DriverState.() -> DriverState) {
        _uiState.update(block)
    }

    fun onCloseDialog() = updateState { copy(status = status.copy(flagDialog = false, flagFailure = false)) }

    fun recoverData() = viewModelScope.launch {

        data class RecoverDriver(
            val descRelease: String,
            val text: String
        )

        runCatching {
            val descRelease = getDescRelease().getOrThrow()
            val text = getRegDriver().getOrThrow() ?: ""
            RecoverDriver(
                descRelease = descRelease,
                text = text
            )
        }
            .onSuccess {
                updateState { copy(descRelease = it.descRelease, text = it.text) }
            }
            .onFailureUpdate(getClassAndMethod(), ::updateState)
    }

    fun onTextField(text: String, typeButton: TypeButton) {
        when (typeButton) {
            TypeButton.NUMERIC -> updateState { copy(text = addTextField(this.text, text)) }
            TypeButton.CLEAN -> updateState { copy(text = clearTextField(this.text)) }
            TypeButton.OK -> {}
            TypeButton.UPDATE -> {}
        }
    }

    fun set() {}

}