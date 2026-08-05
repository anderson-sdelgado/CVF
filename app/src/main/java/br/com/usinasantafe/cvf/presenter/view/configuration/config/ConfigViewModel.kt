package br.com.usinasantafe.cvf.presenter.view.configuration.config

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.usinasantafe.cvf.domain.usecases.config.SetFinishUpdateAllTable
import br.com.usinasantafe.cvf.domain.usecases.config.UpdateConfig
import br.com.usinasantafe.cvf.domain.usecases.update.UpdateTableColab
import br.com.usinasantafe.cvf.domain.usecases.update.UpdateTableEquip
import br.com.usinasantafe.cvf.lib.Errors
import br.com.usinasantafe.cvf.utils.UiStateWithStatusUpdate
import br.com.usinasantafe.cvf.utils.UiStatusStateUpdate
import br.com.usinasantafe.cvf.utils.collectUpdateStep
import br.com.usinasantafe.cvf.utils.executeUpdateSteps
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import br.com.usinasantafe.cvf.utils.onFailureUpdate
import br.com.usinasantafe.cvf.utils.onSuccessUpdateFinish
import br.com.usinasantafe.cvf.utils.sizeUpdate
import br.com.usinasantafe.cvf.utils.withFailure
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ConfigState(
    val number: String = "",
    val password: String = "",
    val version: String = "",
    val flagAccess: Boolean = false,
    override val status: UiStatusStateUpdate = UiStatusStateUpdate()
) : UiStateWithStatusUpdate<ConfigState> {

    override fun copyWithStatus(status: UiStatusStateUpdate): ConfigState =
        copy(status = status)

}

@HiltViewModel
class ConfigViewModel @Inject constructor(
    private val updateConfig: UpdateConfig,
    private val setFinishUpdateAllTable: SetFinishUpdateAllTable,
    private val updateTableColab: UpdateTableColab,
    private val updateTableEquip: UpdateTableEquip
) : ViewModel() {

    private val _uiState = MutableStateFlow(ConfigState())
    val uiState = _uiState.asStateFlow()

    private val state get() = uiState.value

    private fun updateState(block: ConfigState.() -> ConfigState) {
        _uiState.update(block)
    }

    fun onCloseDialog() = updateState { copy(status = status.copy(flagDialog = false, flagFailure = false)) }

    fun onNumberChanged(v: String) = updateState { copy(number = v) }
    fun onPasswordChanged(v: String) = updateState { copy(password = v) }

    fun onVersionChanged(version: String) = updateState {
        copy(version = version)
    }

    private fun ConfigState.isValid() = number.isNotBlank() && password.isNotBlank()

    fun recoverData() = viewModelScope.launch {

    }

    fun onSaveAndUpdate() = viewModelScope.launch {
        if (!state.isValid()) {
            updateState { withFailure(getClassAndMethod(), Errors.FIELD_EMPTY) }
            return@launch
        }

        val ok = updateConfig(state.number, state.password, state.version, sizeAll = 3f, count = 1f)
            .collectUpdateStep(getClassAndMethod(), state.status, true) { status ->
                updateState { copyWithStatus(status) }
            }

        if (ok) {
            updateAllDatabase().collect { _uiState.value = it }
            if (!state.status.flagFailure) {
                setFinishUpdateAllTable()
                    .onSuccessUpdateFinish(::updateState)
                    .onFailureUpdate(getClassAndMethod(), ::updateState, Errors.UPDATE, true)
            }
        }
    }


    suspend fun updateAllDatabase(): Flow<ConfigState> {
        val tables = listOf(updateTableColab, updateTableEquip)
        val sizeAllTables = sizeUpdate(tables.size.toFloat())
        
        return executeUpdateSteps(
            steps = listUpdate(sizeAllTables),
            getState = { _uiState.value },
            getStatus = { it.status },
            copyStateWithStatus = { state, status -> state.copy(status = status) },
            classAndMethod = getClassAndMethod(),
            flagUpdateFinish = false,
            flagProgressOnFailure = true
        )
    }

    suspend fun listUpdate(sizeAll: Float) : List<Flow<UiStatusStateUpdate>> {
        var count = 1f
        return listOf(
            updateTableColab(sizeAll, count++),
            updateTableEquip(sizeAll, count++)
        )
    }
}