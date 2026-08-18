package br.com.usinasantafe.cvf.presenter.view.manager.front

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.usinasantafe.cvf.domain.usecases.manager.ListFront
import br.com.usinasantafe.cvf.domain.usecases.update.UpdateTableFront
import br.com.usinasantafe.cvf.lib.Errors
import br.com.usinasantafe.cvf.lib.LevelUpdate
import br.com.usinasantafe.cvf.presenter.model.ItemCheckBoxScreenModel
import br.com.usinasantafe.cvf.presenter.navigation.Args.ID_FRONT_ARG
import br.com.usinasantafe.cvf.utils.UiStateWithStatusUpdate
import br.com.usinasantafe.cvf.utils.UiStatusStateUpdate
import br.com.usinasantafe.cvf.utils.executeUpdateSteps
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import br.com.usinasantafe.cvf.utils.onFailureUpdate
import br.com.usinasantafe.cvf.utils.sizeUpdate
import br.com.usinasantafe.cvf.utils.withFailure
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FrontState(
    val idSelection: Int? = null,
    override val status: UiStatusStateUpdate = UiStatusStateUpdate()
) : UiStateWithStatusUpdate<FrontState> {

    override fun copyWithStatus(status: UiStatusStateUpdate): FrontState =
        copy(status = status)

}

@HiltViewModel
class FrontViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val listFront: ListFront,
    private val updateTableFront: UpdateTableFront
) : ViewModel() {

    private val idFront: Int = savedStateHandle[ID_FRONT_ARG]!!

    val list = mutableStateListOf<ItemCheckBoxScreenModel>()

    private val _uiState = MutableStateFlow(FrontState())
    val uiState = _uiState.asStateFlow()

    private val state get() = uiState.value

    private fun updateState(block: FrontState.() -> FrontState) {
        _uiState.update(block)
    }

    fun onCloseDialog() = updateState { copy(status = status.copy(flagDialog = false, flagFailure = false)) }

    init {
        updateState {
            copy(
                idSelection = if(this@FrontViewModel.idFront == 0) null else this@FrontViewModel.idFront,
            )
        }
    }

    fun list() = viewModelScope.launch {
        runCatching {
            listFront().getOrThrow()
        }
            .onSuccess {
                list.clear()
                list.addAll(it)
                state.idSelection?.let { id -> onCheckChanged(id, true) }
            }
            .onFailureUpdate(getClassAndMethod(), ::updateState)
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

    fun check() = viewModelScope.launch {
        val id = list.find { it.flag }?.id
        if (id == null) {
            updateState { withFailure(getClassAndMethod(), Errors.NOT_SELECTION) }
            return@launch
        }
        updateState { copy(idSelection = id, status = status.copy(flagAccess = true)) }
    }

    fun update() = viewModelScope.launch {
        updateAllDatabase().collect { stateUpdate -> _uiState.value = stateUpdate }
        if (_uiState.value.status.levelUpdate == LevelUpdate.FINISH_UPDATE_COMPLETED) { list() }
    }

    suspend fun updateAllDatabase(): Flow<FrontState> =
        executeUpdateSteps(
            steps = listOf(updateTableFront(sizeUpdate())),
            getState = { _uiState.value },
            getStatus = { it.status },
            copyStateWithStatus = { state, status -> state.copy(status = status) },
            classAndMethod = getClassAndMethod(),
        )


}