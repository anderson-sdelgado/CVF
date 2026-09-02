package br.com.usinasantafe.cvf.presenter.view.note.review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.usinasantafe.cvf.domain.usecases.note.DeleteNote
import br.com.usinasantafe.cvf.domain.usecases.note.GetDescReview
import br.com.usinasantafe.cvf.domain.usecases.note.FinishNote
import br.com.usinasantafe.cvf.utils.UiStateWithStatus
import br.com.usinasantafe.cvf.utils.UiStatusState
import br.com.usinasantafe.cvf.utils.onFailureState
import br.com.usinasantafe.cvf.utils.onFailureUpdate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ReviewState(
    val flagCheckDialog: Boolean = false,
    val text: String = "",
    override val status: UiStatusState = UiStatusState()
) : UiStateWithStatus<ReviewState> {

    override fun copyWithStatus(status: UiStatusState): ReviewState =
        copy(status = status)

}

@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val getDescReview: GetDescReview,
    private val finishNote: FinishNote,
    private val deleteNote: DeleteNote,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReviewState())
    val uiState = _uiState.asStateFlow()

    private fun updateState(block: ReviewState.() -> ReviewState) {
        _uiState.update(block)
    }
    
    fun onCloseDialog() = updateState { copy(status = status.copy(flagDialog = false, flagFailure = false)) }

    fun onFlagAccess() = updateState { copy(status = status.copy(flagAccess = true, flagDialog = false)) }

    fun onCheckDialog(flag: Boolean) = updateState { copy(flagCheckDialog = flag) }

    fun recoverData() = viewModelScope.launch {
        runCatching {
            getDescReview().getOrThrow()
        }
            .onSuccess { updateState { copy( text = it ) } }
            .onFailureState(::updateState)
    }

    fun finish()  = viewModelScope.launch {
        runCatching {
            finishNote().getOrThrow()
        }
            .onSuccess { updateState { copy(status = status.copy(flagDialog = true, flagFailure = false)) } }
            .onFailureState(::updateState)
    }

    fun delete() = viewModelScope.launch {
        runCatching {
            deleteNote().getOrThrow()
        }
            .onSuccess { updateState { copy(status = status.copy(flagAccess = true)) } }
            .onFailureState(::updateState)
    }


}