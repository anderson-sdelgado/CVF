package br.com.usinasantafe.cvf.presenter.view.manager.front

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.usinasantafe.cvf.domain.usecases.manager.CheckStatusManager
import br.com.usinasantafe.cvf.domain.usecases.manager.ListFront
import br.com.usinasantafe.cvf.domain.usecases.manager.SetIdFront
import br.com.usinasantafe.cvf.domain.usecases.update.UpdateTableFront
import br.com.usinasantafe.cvf.lib.Errors
import br.com.usinasantafe.cvf.lib.LevelUpdate
import br.com.usinasantafe.cvf.lib.Option
import br.com.usinasantafe.cvf.lib.OptionMenu
import br.com.usinasantafe.cvf.presenter.model.ItemCheckBoxScreenModel
import br.com.usinasantafe.cvf.presenter.navigation.Args.OPTION_ARG
import br.com.usinasantafe.cvf.presenter.navigation.Args.OPTION_MENU_ARG
import br.com.usinasantafe.cvf.utils.UiStateWithStatusUpdate
import br.com.usinasantafe.cvf.utils.UiStatusStateUpdate
import br.com.usinasantafe.cvf.utils.executeUpdateSteps
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

data class FrontState(
    val checkReturn: Boolean = false,
    val option: Option = Option.INSERT,
    val optionMenu: OptionMenu = OptionMenu.CONFIG,
    override val status: UiStatusStateUpdate = UiStatusStateUpdate()
) : UiStateWithStatusUpdate<FrontState> {

    override fun copyWithStatus(status: UiStatusStateUpdate): FrontState =
        copy(status = status)

}

@HiltViewModel
class FrontViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val listFront: ListFront,
    private val setIdFront: SetIdFront,
    private val updateTableFront: UpdateTableFront,
    private val checkStatusManager: CheckStatusManager
) : ViewModel() {

    private val option: Int = savedStateHandle[OPTION_ARG]!!
    private val optionMenu: Int = savedStateHandle[OPTION_MENU_ARG]!!

    val list = mutableStateListOf<ItemCheckBoxScreenModel>()

    private val _uiState = MutableStateFlow(FrontState())
    val uiState = _uiState.asStateFlow()

    private fun updateState(block: FrontState.() -> FrontState) {
        _uiState.update(block)
    }

    fun onCloseDialog() = updateState { copy(status = status.copy(flagDialog = false, flagFailure = false)) }

    init {
        updateState {
            copy(
                option = Option.entries[this@FrontViewModel.option],
                optionMenu = OptionMenu.entries[this@FrontViewModel.optionMenu]
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
            }
            .onFailureUpdate(::updateState)
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

    fun set() = viewModelScope.launch {
        runCatching {
            val id = list.find { it.flag }?.id
            if (id == null) {
                updateState { withFailure(Errors.NOT_SELECTION) }
                return@launch
            }
            setIdFront(id).getOrThrow()
        }
            .onSuccessUpdateAccess(::updateState)
            .onFailureUpdate(::updateState)
    }

    fun check() = viewModelScope.launch {
        runCatching {
            val check = checkStatusManager().getOrThrow()
            if (!check) {
                updateState { withFailure(Errors.RETURN_INVALID_FRONT) }
                return@launch
            }
        }
            .onSuccess {
                updateState { copy(checkReturn = true) }
            }
            .onFailureUpdate(::updateState)
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
        )


}