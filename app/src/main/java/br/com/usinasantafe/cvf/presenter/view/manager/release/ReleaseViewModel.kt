package br.com.usinasantafe.cvf.presenter.view.manager.release

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.usinasantafe.cvf.domain.usecases.update.UpdateTableRelease
import br.com.usinasantafe.cvf.lib.LevelUpdate
import br.com.usinasantafe.cvf.presenter.model.ItemCheckBoxScreenModel
import br.com.usinasantafe.cvf.presenter.view.manager.front.FrontState
import br.com.usinasantafe.cvf.utils.UiStateWithStatusUpdate
import br.com.usinasantafe.cvf.utils.UiStatusStateUpdate
import br.com.usinasantafe.cvf.utils.executeUpdateSteps
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import br.com.usinasantafe.cvf.utils.sizeUpdate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ReleaseState(
    val flagAccess: Boolean = false,
    override val status: UiStatusStateUpdate = UiStatusStateUpdate()
) : UiStateWithStatusUpdate<ReleaseState> {

    override fun copyWithStatus(status: UiStatusStateUpdate): ReleaseState =
        copy(status = status)

}

@HiltViewModel
class ReleaseViewModel @Inject constructor(
    private val updateTableRelease: UpdateTableRelease
) : ViewModel() {

    val list = mutableStateListOf<ItemCheckBoxScreenModel>()

    private val _uiState = MutableStateFlow(ReleaseState())
    val uiState = _uiState.asStateFlow()

    private val state get() = uiState.value

    private fun updateState(block: ReleaseState.() -> ReleaseState) {
        _uiState.update(block)
    }

    fun onCloseDialog() = updateState { copy(status = status.copy(flagDialog = false, flagFailure = false)) }

    fun list() = viewModelScope.launch {

    }

    fun onCheckChanged(id: Int, checked: Boolean) {
        for (i in list.indices) {
            val item = list[i]
            if (item.id == id) {
                list[i] = item.copy(flag = checked)
            } else if (checked && item.flag) {
                list[i] = item.copy(flag = false)
            }
        }
    }

    fun update() = viewModelScope.launch {
        updateAllDatabase().collect { stateUpdate -> _uiState.value = stateUpdate }
        if (_uiState.value.status.levelUpdate == LevelUpdate.FINISH_UPDATE_COMPLETED) { list() }
    }

    suspend fun updateAllDatabase(): Flow<ReleaseState> =
        executeUpdateSteps(
            steps = listOf(updateTableRelease(sizeUpdate())),
            getState = { _uiState.value },
            getStatus = { it.status },
            copyStateWithStatus = { state, status -> state.copy(status = status) },
            classAndMethod = getClassAndMethod(),
        )



}