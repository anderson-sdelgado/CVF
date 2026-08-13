package br.com.usinasantafe.cvf.presenter.view.manager.release

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.usinasantafe.cvf.domain.usecases.manager.ListRelease
import br.com.usinasantafe.cvf.domain.usecases.manager.SaveManager
import br.com.usinasantafe.cvf.domain.usecases.update.UpdateTableRelease
import br.com.usinasantafe.cvf.lib.Errors
import br.com.usinasantafe.cvf.lib.LevelUpdate
import br.com.usinasantafe.cvf.presenter.model.ItemCheckBoxScreenModel
import br.com.usinasantafe.cvf.presenter.navigation.Args.ID_FRONT_ARG
import br.com.usinasantafe.cvf.presenter.view.manager.front.FrontState
import br.com.usinasantafe.cvf.utils.UiStateWithStatusUpdate
import br.com.usinasantafe.cvf.utils.UiStatusStateUpdate
import br.com.usinasantafe.cvf.utils.executeUpdateSteps
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import br.com.usinasantafe.cvf.utils.onFailureUpdate
import br.com.usinasantafe.cvf.utils.onSuccessUpdateAccess
import br.com.usinasantafe.cvf.utils.sizeUpdate
import br.com.usinasantafe.cvf.utils.withFailure
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.get

data class ReleaseState(
    val idFront: Int = 0,
    override val status: UiStatusStateUpdate = UiStatusStateUpdate()
) : UiStateWithStatusUpdate<ReleaseState> {

    override fun copyWithStatus(status: UiStatusStateUpdate): ReleaseState =
        copy(status = status)

}

@HiltViewModel
class ReleaseViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val updateTableRelease: UpdateTableRelease,
    private val listRelease: ListRelease,
    private val saveManager: SaveManager
) : ViewModel() {

    private val idFront: Int = savedStateHandle[ID_FRONT_ARG]!!

    val list = mutableStateListOf<ItemCheckBoxScreenModel>()

    private val _uiState = MutableStateFlow(ReleaseState())
    val uiState = _uiState.asStateFlow()

    private val state get() = uiState.value

    private fun updateState(block: ReleaseState.() -> ReleaseState) {
        _uiState.update(block)
    }

    fun onCloseDialog() = updateState { copy(status = status.copy(flagDialog = false, flagFailure = false)) }

    init {
        updateState {
            copy(
                idFront = this@ReleaseViewModel.idFront,
            )
        }
    }

    fun list() = viewModelScope.launch {
        runCatching {
            listRelease(state.idFront).getOrThrow()
        }
            .onSuccess {
                list.clear()
                list.addAll(it)
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

    fun save() = viewModelScope.launch {
        runCatching {
            val id = list.find { it.flag }?.id
            if (id == null) {
                updateState { withFailure(getClassAndMethod(), Errors.NOT_SELECTION) }
                return@launch
            }
            saveManager(state.idFront, id).getOrThrow()
        }
            .onSuccessUpdateAccess(::updateState)
            .onFailureUpdate(getClassAndMethod(), ::updateState)
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