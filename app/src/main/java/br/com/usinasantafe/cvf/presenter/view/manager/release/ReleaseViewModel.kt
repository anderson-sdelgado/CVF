package br.com.usinasantafe.cvf.presenter.view.manager.release

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.usinasantafe.cvf.domain.usecases.manager.ListRelease
import br.com.usinasantafe.cvf.domain.usecases.manager.SetRelease
import br.com.usinasantafe.cvf.domain.usecases.update.UpdateTableRelease
import br.com.usinasantafe.cvf.lib.Errors
import br.com.usinasantafe.cvf.lib.LevelUpdate
import br.com.usinasantafe.cvf.lib.Option
import br.com.usinasantafe.cvf.lib.OptionReturn
import br.com.usinasantafe.cvf.presenter.model.ItemCheckBoxScreenModel
import br.com.usinasantafe.cvf.presenter.navigation.Args.OPTION_ARG
import br.com.usinasantafe.cvf.utils.CheckNetwork
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

data class ReleaseState(
    val option: Option = Option.INSERT,
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
    private val setRelease: SetRelease,
    private val checkNetwork: CheckNetwork,
) : ViewModel() {

    private val option: Int = savedStateHandle[OPTION_ARG]!!

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
                option = Option.entries[this@ReleaseViewModel.option],
            )
        }
    }

    fun start() {
        if(checkNetwork.isConnected()) update(false) else list()
    }

    fun list() = viewModelScope.launch {
        runCatching {
            listRelease().getOrThrow()
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

    fun save() = viewModelScope.launch {
        runCatching {
            val id = list.find { it.flag }?.id
            if (id == null) {
                updateState { withFailure(Errors.NOT_SELECTION) }
                return@launch
            }
            setRelease(id).getOrThrow()
        }
            .onSuccessUpdateAccess(::updateState)
            .onFailureUpdate(::updateState)
    }

    fun update(flagDialog: Boolean = true) = viewModelScope.launch {
        updateAllDatabase(flagDialog).collect { stateUpdate -> _uiState.value = stateUpdate }
        if (_uiState.value.status.levelUpdate == LevelUpdate.FINISH_UPDATE_COMPLETED) { list() }
    }

    suspend fun updateAllDatabase(flagDialog: Boolean = true): Flow<ReleaseState> =
        executeUpdateSteps(
            steps = listOf(updateTableRelease(sizeUpdate())),
            getState = { _uiState.value },
            getStatus = { it.status },
            copyStateWithStatus = { state, status -> state.copy(status = status) },
            classAndMethod = getClassAndMethod(),
            flagDialog = flagDialog
        )

}