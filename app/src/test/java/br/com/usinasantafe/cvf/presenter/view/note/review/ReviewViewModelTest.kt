package br.com.usinasantafe.cvf.presenter.view.note.review

import br.com.usinasantafe.cvf.MainCoroutineRule
import br.com.usinasantafe.cvf.domain.usecases.note.DeleteNote
import br.com.usinasantafe.cvf.domain.usecases.note.FinishNote
import br.com.usinasantafe.cvf.domain.usecases.note.GetDescReview
import br.com.usinasantafe.cvf.lib.Errors
import br.com.usinasantafe.cvf.utils.resultFailure
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.atLeastOnce
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class ReviewViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val getDescReview = mock<GetDescReview>()
    private val finishNote = mock<FinishNote>()
    private val deleteNote = mock<DeleteNote>()
    private val viewModel = ReviewViewModel(
        getDescReview = getDescReview,
        finishNote = finishNote,
        deleteNote = deleteNote
    )

    @Test
    fun `recoverData - Check return failure if have error in GetDescReview`() =
        runTest {
            whenever(
                getDescReview()
            ).thenReturn(
                resultFailure(
                    context = "GetDescReview",
                    message = "-",
                    cause = Exception()
                )
            )
            viewModel.recoverData()
            assertEquals(
                true,
                viewModel.uiState.value.status.flagDialog
            )
            assertEquals(
                "ReviewViewModel.recoverData -> GetDescReview -> java.lang.Exception",
                viewModel.uiState.value.status.failure
            )
            assertEquals(
                Errors.EXCEPTION,
                viewModel.uiState.value.status.errors
            )
            assertEquals(
                true,
                viewModel.uiState.value.status.flagFailure
            )
        }

    @Test
    fun `recoverData - Check return correct if function execute successfully`() =
        runTest {
            whenever(
                getDescReview()
            ).thenReturn(
                Result.success("Test")
            )
            viewModel.recoverData()
            assertEquals(
                "Test",
                viewModel.uiState.value.text
            )
        }

    @Test
    fun `finish - Check return failure if have error in FinishNote`() =
        runTest {
            whenever(
                finishNote()
            ).thenReturn(
                resultFailure(
                    context = "FinishNote",
                    message = "-",
                    cause = Exception()
                )
            )
            viewModel.finish()
            assertEquals(
                true,
                viewModel.uiState.value.status.flagDialog
            )
            assertEquals(
                "ReviewViewModel.finish -> FinishNote -> java.lang.Exception",
                viewModel.uiState.value.status.failure
            )
            assertEquals(
                Errors.EXCEPTION,
                viewModel.uiState.value.status.errors
            )
            assertEquals(
                true,
                viewModel.uiState.value.status.flagFailure
            )
        }

    @Test
    fun `finish - Check return correct if function execute successfully`() =
        runTest {
            viewModel.finish()
            verify(finishNote, atLeastOnce()).invoke()
            assertEquals(
                true,
                viewModel.uiState.value.status.flagDialog
            )
            assertEquals(
                false,
                viewModel.uiState.value.status.flagFailure
            )
        }

    @Test
    fun `delete - Check return failure if have error in DeleteNote`() =
        runTest {
            whenever(
                deleteNote()
            ).thenReturn(
                resultFailure(
                    context = "DeleteNote",
                    message = "-",
                    cause = Exception()
                )
            )
            viewModel.delete()
            assertEquals(
                true,
                viewModel.uiState.value.status.flagDialog
            )
            assertEquals(
                "ReviewViewModel.delete -> DeleteNote -> java.lang.Exception",
                viewModel.uiState.value.status.failure
            )
            assertEquals(
                Errors.EXCEPTION,
                viewModel.uiState.value.status.errors
            )
            assertEquals(
                true,
                viewModel.uiState.value.status.flagFailure
            )
        }

    @Test
    fun `delete - Check return correct if function execute successfully`() =
        runTest {
            assertEquals(
                false,
                viewModel.uiState.value.status.flagAccess
            )
            viewModel.delete()
            verify(deleteNote, atLeastOnce()).invoke()
            assertEquals(
                true,
                viewModel.uiState.value.status.flagAccess
            )
        }

}