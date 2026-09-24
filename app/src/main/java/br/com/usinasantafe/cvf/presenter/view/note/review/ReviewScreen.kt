package br.com.usinasantafe.cvf.presenter.view.note.review

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.delay
import br.com.usinasantafe.cvf.R
import br.com.usinasantafe.cvf.presenter.theme.AlertDialogCheckDesign
import br.com.usinasantafe.cvf.presenter.theme.AlertDialogSimpleDesign
import br.com.usinasantafe.cvf.presenter.theme.ButtonMaxWidth
import br.com.usinasantafe.cvf.presenter.theme.CVFTheme
import br.com.usinasantafe.cvf.presenter.theme.MsgErrors
import br.com.usinasantafe.cvf.presenter.theme.TitleDesign
import br.com.usinasantafe.cvf.utils.UiStatusState
import kotlin.time.Duration.Companion.seconds

@Composable
fun ReviewScreen(
    viewModel: ReviewViewModel = hiltViewModel(),
    onNavDriver: () -> Unit
) {
    CVFTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            LaunchedEffect(Unit) {
                viewModel.recoverData()
            }

            ReviewContent(
                flagCheckDialog = uiState.flagCheckDialog,
                onCheckDialog = viewModel::onCheckDialog,
                delete = viewModel::delete,
                text = uiState.text,
                status = uiState.status,
                onCloseDialog = viewModel::onCloseDialog,
                onFlagAccess = viewModel::onFlagAccess,
                finish = viewModel::finish,
                onNavDriver = onNavDriver,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Composable
fun ReviewContent(
    flagCheckDialog: Boolean,
    onCheckDialog: (Boolean) -> Unit,
    delete: () -> Unit,
    text: String,
    status: UiStatusState,
    onCloseDialog: () -> Unit,
    onFlagAccess: () -> Unit,
    finish: () -> Unit,
    onNavDriver: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TitleDesign(
            text = stringResource(
                id = R.string.text_title_travel
            )
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = text,
                textAlign = TextAlign.Center,
                fontSize = 26.sp,
                lineHeight = 38.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
        ButtonMaxWidth(R.string.text_finish_travel, onClick = finish)
        Spacer(modifier = Modifier.padding(vertical = 2.dp))
        ButtonMaxWidth(R.string.text_clean_travel, flagDelete = true, onClick = { onCheckDialog(true) })
        BackHandler {}

        if(status.flagDialog) {
            if(status.flagFailure){
                MsgErrors(status.errors, onCloseDialog, status.failure)
            } else {
                AlertDialogSimpleDesign(stringResource(id = R.string.text_msg_finish_note), onFlagAccess)
            }
        }

        if(flagCheckDialog){
            AlertDialogCheckDesign(
                text = stringResource(id = R.string.text_msg_delete),
                onClickDismiss = { onCheckDialog(false) },
                onClickYes = delete
            )
        }

    }

    LaunchedEffect(status.flagAccess) {
        if (status.flagAccess) {
            onNavDriver()
        }
    }

    LaunchedEffect(status.flagDialog, status.flagFailure) {
        if (status.flagDialog && !status.flagFailure) {
            delay(10.seconds)
            onFlagAccess()
        }
    }

}

@Preview(showBackground = true)
@Composable
fun ReviewPagePreview() {
    CVFTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            ReviewContent(
                flagCheckDialog = false,
                onCheckDialog = {},
                delete = {},
                text = "Test",
                status = UiStatusState(),
                onCloseDialog = {},
                onFlagAccess = {},
                finish = {},
                onNavDriver = {},
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}