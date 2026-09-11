package br.com.usinasantafe.cvf.presenter.view.splash

import br.com.usinasantafe.cvf.MainCoroutineRule
import br.com.usinasantafe.cvf.domain.usecases.common.StartApp
import br.com.usinasantafe.cvf.lib.Errors
import br.com.usinasantafe.cvf.lib.FlowApp
import br.com.usinasantafe.cvf.utils.resultFailure
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class SplashViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val startApp = mock<StartApp>()
    private val viewModel = SplashViewModel(
        startApp = startApp
    )

    @Test
    fun `start - Check return failure if have error in StartApp`() =
        runTest {
            whenever(
                startApp()
            ).thenReturn(
                resultFailure(
                    context = "StartApp",
                    message = "-",
                    cause = Exception()
                )
            )
            viewModel.start()
            assertEquals(
                true,
                viewModel.uiState.value.status.flagDialog
            )
            assertEquals(
                "SplashViewModel.start -> StartApp -> java.lang.Exception",
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
    fun `start - Check return correct if function execute successfully`() =
        runTest {
            whenever(
                startApp()
            ).thenReturn(
                Result.success(FlowApp.FRONT)
            )
            viewModel.start()
            assertEquals(
                true,
                viewModel.uiState.value.status.flagAccess
            )
            assertEquals(
                FlowApp.FRONT,
                viewModel.uiState.value.flowApp
            )
        }

}