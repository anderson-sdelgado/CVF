package br.com.usinasantafe.cvf.presenter.view.splash

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.usinasantafe.cvf.R
import br.com.usinasantafe.cvf.presenter.theme.AlertDialogSimpleDesign
import br.com.usinasantafe.cvf.presenter.theme.CVFTheme
import br.com.usinasantafe.cvf.presenter.theme.MsgErrors
import br.com.usinasantafe.cvf.utils.UiStatusState

@Composable
fun SplashScreen(
    viewModel: SplashViewModel = hiltViewModel(),
    onNavConfig: () -> Unit,
) {
    CVFTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            LaunchedEffect(Unit) {
                viewModel.startApp()
            }

            SplashContent(
                onCloseDialog = viewModel::onCloseDialog,
                status = uiState.status,
                onNavConfig = onNavConfig,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )
        }
    }
}

@Composable
fun SplashContent(
    onCloseDialog: () -> Unit,
    status: UiStatusState,
    onNavConfig: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(
        label = "splash_fade"
    )

    val alpha = infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1100,
                delayMillis = 900
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "splash_alpha"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = stringResource(id = R.string.app_name),
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(250.dp)
                .alpha(alpha.value)
        )
    }

    if(status.flagDialog) {
        MsgErrors(status.errors, onCloseDialog, status.failure)
    }

    LaunchedEffect(status.flagAccess) {
        if(status.flagAccess) {
            onNavConfig()
        }
    }

}

@Preview(showBackground = true)
@Composable
fun SplashPagePreview() {
    CVFTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            SplashContent(
                onCloseDialog = {},
                status = UiStatusState(),
                onNavConfig = {},
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}