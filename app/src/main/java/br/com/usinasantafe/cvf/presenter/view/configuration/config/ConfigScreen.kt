package br.com.usinasantafe.cvf.presenter.view.configuration.config

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.usinasantafe.cvf.BuildConfig
import br.com.usinasantafe.cvf.R
import br.com.usinasantafe.cvf.lib.Errors
import br.com.usinasantafe.cvf.lib.LevelUpdate
import br.com.usinasantafe.cvf.lib.msg
import br.com.usinasantafe.cvf.presenter.theme.ButtonMaxWidth
import br.com.usinasantafe.cvf.presenter.theme.CVFTheme
import br.com.usinasantafe.cvf.presenter.theme.MsgUpdate
import br.com.usinasantafe.cvf.presenter.theme.TextFieldConfigDesign
import br.com.usinasantafe.cvf.presenter.theme.TextFieldPasswordDesign
import br.com.usinasantafe.cvf.presenter.theme.TitleDesign
import br.com.usinasantafe.cvf.utils.UiStatusStateUpdate

const val TAG_NUMBER_TEXT_FIELD_CONFIG_SCREEN = "tag_number_text_field_config_screen"
const val TAG_PASSWORD_TEXT_FIELD_CONFIG_SCREEN = "tag_password_text_field_config_screen"

@Composable
fun ConfigScreen(
    viewModel: ConfigViewModel = hiltViewModel(),
    onNavFront: () -> Unit,
    onNavNote: () -> Unit
) {
    CVFTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            LaunchedEffect(Unit) {
                viewModel.recoverData()
                viewModel.onVersionChanged(BuildConfig.VERSION_NAME)
            }

            ConfigContent(
                number = uiState.number,
                onNumberChanged = viewModel::onNumberChanged,
                password = uiState.password,
                onPasswordChanged = viewModel::onPasswordChanged,
                onSaveAndUpdate = viewModel::onSaveAndUpdate,
                onCloseDialog = viewModel::onCloseDialog,
                status = uiState.status,
                flagReturn = uiState.flagReturn,
                onNavFront = onNavFront,
                onNavNote = onNavNote,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Composable
fun ConfigContent(
    number: String,
    onNumberChanged: (String) -> Unit,
    password: String,
    onPasswordChanged: (String) -> Unit,
    onSaveAndUpdate: () -> Unit,
    onCloseDialog: () -> Unit,
    status: UiStatusStateUpdate,
    flagReturn: Boolean,
    onNavFront: () -> Unit,
    onNavNote: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(16.dp)
    ) {
        TitleDesign(
            font = 32,
            text = stringResource(
                id = R.string.text_title_config
            )
        )
        TitleDesign(
            text = stringResource(
                id = R.string.text_number
            )
        )
        TextFieldConfigDesign(
            value = number,
            onValueChange = onNumberChanged,
            tag = TAG_NUMBER_TEXT_FIELD_CONFIG_SCREEN
        )
        Spacer(modifier = Modifier.padding(vertical = 8.dp))
        TitleDesign(
            text = stringResource(id = R.string.text_password)
        )
        TextFieldPasswordDesign(
            value = password,
            onValueChange = onPasswordChanged,
            tag = TAG_PASSWORD_TEXT_FIELD_CONFIG_SCREEN
        )
        Spacer(modifier = Modifier.padding(vertical = 8.dp))
        ButtonMaxWidth(id = R.string.text_pattern_save, onClick = onSaveAndUpdate)
        Spacer(modifier = Modifier.padding(vertical = 8.dp))

        if(flagReturn) ButtonMaxWidth(id = R.string.text_pattern_return, onClick = onNavNote)

        if (status.flagProgress) {
            Spacer(modifier = Modifier.padding(vertical = 16.dp))
            LinearProgressIndicator(
                progress = { status.currentProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp),
            )
            Spacer(modifier = Modifier.padding(vertical = 4.dp))
            val msgProgress = msg(status.levelUpdate, status.failure, status.tableUpdate)
            Text(
                text = msgProgress,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
        BackHandler {}

        if (status.flagDialog) {
            MsgUpdate(status = status, onClickOk = onCloseDialog)
        }
    }

    LaunchedEffect(status.flagAccess) {
        if(status.flagAccess) {
            onNavFront()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ConfigPagePreview() {
    CVFTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            ConfigContent(
                number = "",
                onNumberChanged = {},
                password = "",
                onPasswordChanged = {},
                onSaveAndUpdate = {},
                onCloseDialog = {},
                status = UiStatusStateUpdate(),
                flagReturn = false,
                onNavFront = {},
                onNavNote = {},
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ConfigPagePreviewWithData() {
    CVFTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            ConfigContent(
                number = "16997417840",
                onNumberChanged = {},
                password = "12345",
                onPasswordChanged = {},
                onSaveAndUpdate = {},
                onCloseDialog = {},
                status = UiStatusStateUpdate(
                    flagAccess = false,
                    flagProgress = false,
                    currentProgress = 0.0f,
                    levelUpdate = null,
                    tableUpdate = "",
                    flagDialog = false,
                    flagFailure = false,
                    errors = Errors.FIELD_EMPTY,
                    failure = "",
                ),
                flagReturn = true,
                onNavFront = {},
                onNavNote = {},
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ConfigPagePreviewShowProgress() {
    CVFTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            ConfigContent(
                number = "16997417840",
                onNumberChanged = {},
                password = "12345",
                onPasswordChanged = {},
                onSaveAndUpdate = {},
                onCloseDialog = {},
                status = UiStatusStateUpdate(
                    flagProgress = true,
                    currentProgress = 0.2f,
                    levelUpdate = LevelUpdate.RECOVERY,
                    tableUpdate = "tb_colab",
                    flagDialog = false,
                    flagFailure = false,
                    errors = Errors.FIELD_EMPTY,
                    failure = "",
                ),
                flagReturn = false,
                onNavFront = {},
                onNavNote = {},
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ConfigPagePreviewShowMsgFieldEmpty() {
    CVFTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            ConfigContent(
                number = "",
                onNumberChanged = {},
                password = "",
                onPasswordChanged = {},
                onSaveAndUpdate = {},
                onCloseDialog = {},
                status = UiStatusStateUpdate(
                    flagProgress = false,
                    currentProgress = 0.0f,
                    levelUpdate = null,
                    tableUpdate = "",
                    flagDialog = true,
                    flagFailure = true,
                    errors = Errors.FIELD_EMPTY,
                    failure = "",
                ),
                flagReturn = false,
                onNavFront = {},
                onNavNote = {},
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ConfigPagePreviewShowMsgSuccess() {
    CVFTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            ConfigContent(
                number = "16997417840",
                onNumberChanged = {},
                password = "12345",
                onPasswordChanged = {},
                onSaveAndUpdate = {},
                onCloseDialog = {},
                status = UiStatusStateUpdate(
                    flagProgress = false,
                    currentProgress = 0.0f,
                    levelUpdate = LevelUpdate.FINISH_UPDATE_COMPLETED,
                    tableUpdate = "",
                    flagDialog = true,
                    flagFailure = false,
                    errors = Errors.FIELD_EMPTY,
                    failure = "",
                ),
                flagReturn = false,
                onNavFront = {},
                onNavNote = {},
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}