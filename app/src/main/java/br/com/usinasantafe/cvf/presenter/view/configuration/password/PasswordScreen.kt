package br.com.usinasantafe.cvf.presenter.view.configuration.password

import android.annotation.SuppressLint
import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.usinasantafe.cvf.R
import br.com.usinasantafe.cvf.lib.OptionMenu
import br.com.usinasantafe.cvf.lib.OptionReturn
import br.com.usinasantafe.cvf.presenter.theme.CVFTheme
import br.com.usinasantafe.cvf.presenter.theme.MsgErrors
import br.com.usinasantafe.cvf.presenter.theme.TextButtonDesign
import br.com.usinasantafe.cvf.presenter.theme.TextFieldPasswordDesign
import br.com.usinasantafe.cvf.presenter.theme.TitleDesign
import br.com.usinasantafe.cvf.utils.UiStatusState

const val TAG_PASSWORD_TEXT_FIELD_SCREEN = "tag_password_text_field_screen"

@Composable
fun PasswordScreen(
    viewModel: PasswordViewModel = hiltViewModel(),
    onNavConfig: () -> Unit,
    onNavFront: () -> Unit,
    onNavRelease: () -> Unit,
    onNavNote: () -> Unit,
) {
    CVFTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            PasswordContent(
                optionMenu = uiState.optionMenu,
                password = uiState.password,
                onPasswordChanged =  viewModel::onPasswordChanged,
                onCheckAccess = viewModel::onCheckAccess,
                onCloseDialog = viewModel::onCloseDialog,
                status = uiState.status,
                onNavConfig = onNavConfig,
                onNavFront = onNavFront,
                onNavRelease = onNavRelease,
                onNavNote = onNavNote,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@SuppressLint("ContextCastToActivity")
@Composable
fun PasswordContent(
    optionMenu: OptionMenu,
    password: String,
    onPasswordChanged: (String) -> Unit,
    onCheckAccess: () -> Unit,
    onCloseDialog: () -> Unit,
    status: UiStatusState,
    onNavConfig: () -> Unit,
    onNavFront: () -> Unit,
    onNavRelease: () -> Unit,
    onNavNote: () -> Unit,
    modifier: Modifier = Modifier
) {
    val activity = (LocalContext.current as? Activity)
    Column(
        modifier = modifier
            .padding(16.dp)
    ) {
        TitleDesign(
            text = stringResource(
                id = R.string.text_password
            )
        )
        TextFieldPasswordDesign(
            value = password,
            onValueChange = onPasswordChanged,
            tag = TAG_PASSWORD_TEXT_FIELD_SCREEN
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.Center,
        )  {
            Button(
                onClick = onCheckAccess,
                modifier = Modifier.weight(1f),
            ) {
                TextButtonDesign(
                    text = stringResource(id = R.string.text_pattern_ok)
                )
            }
            Button(
                onClick = onNavNote,
                modifier = Modifier.weight(1f)
            ) {
                TextButtonDesign(
                    text = stringResource(id = R.string.text_pattern_cancel)
                )
            }
        }
        BackHandler {}

        if(status.flagDialog) {
            MsgErrors(status.errors, onCloseDialog, status.failure)
        }

    }

    LaunchedEffect(status.flagAccess) {
        if(status.flagAccess) {
            when(optionMenu) {
                OptionMenu.CONFIG -> onNavConfig()
                OptionMenu.FRONT -> onNavFront()
                OptionMenu.RELEASE -> onNavRelease()
                else -> activity?.finish()
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun PasswordPagePreview() {
    CVFTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            PasswordContent(
                optionMenu = OptionMenu.DELETE,
                password = "",
                onPasswordChanged = {},
                onCheckAccess = {},
                onCloseDialog = {},
                status = UiStatusState(),
                onNavConfig = {},
                onNavFront = {},
                onNavRelease = {},
                onNavNote = {},
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}