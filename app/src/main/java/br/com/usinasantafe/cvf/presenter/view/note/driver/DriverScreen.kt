package br.com.usinasantafe.cvf.presenter.view.note.driver

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.usinasantafe.cvf.R
import br.com.usinasantafe.cvf.lib.TypeButton
import br.com.usinasantafe.cvf.presenter.theme.CVFTheme
import br.com.usinasantafe.cvf.presenter.theme.MsgUpdate
import br.com.usinasantafe.cvf.presenter.theme.Progress
import br.com.usinasantafe.cvf.presenter.theme.TextFieldDesign
import br.com.usinasantafe.cvf.presenter.theme.TitleDesign
import br.com.usinasantafe.cvf.presenter.theme.topBar
import br.com.usinasantafe.cvf.presenter.view.ButtonsGenericNumeric
import br.com.usinasantafe.cvf.utils.UiStatusStateUpdate

@Composable
fun DriverScreen(
    viewModel: DriverViewModel = hiltViewModel()
) {
    CVFTheme {
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        LaunchedEffect(Unit) {
        }

        DriverContent(
            text = uiState.text,
            onTextField = viewModel::onTextField,
            onCloseDialog = viewModel::onCloseDialog,
            status = uiState.status,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DriverContent(
    text: String,
    onTextField: (String, TypeButton) -> Unit,
    onCloseDialog: () -> Unit,
    status: UiStatusStateUpdate,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = topBar(
            stringResource(
                id = R.string.text_item_release,
                "1235456",
                "125426",
                "RANCHO AZUL"
            )
        ),
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(8.dp)
        ) {
            TitleDesign(
                text = stringResource(
                    id = R.string.text_driver
                )
            )
            TextFieldDesign(
                value = text
            )
            Spacer(modifier = Modifier.padding(vertical = 8.dp))
            ButtonsGenericNumeric(
                onTextField = onTextField,
                flagUpdate = false
            )
            BackHandler {}

            if (status.flagDialog) {
                MsgUpdate(status = status, onClickOk = onCloseDialog, value = stringResource(id = R.string.text_driver))
            }

            if (status.flagProgress) {
                Progress(status)
            }
        }
    }

    LaunchedEffect(status.flagAccess) {
        if(status.flagAccess) {
//            onNavMenu()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DriverPagePreview() {
    CVFTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            DriverContent(
                text = "",
                onTextField = { _, _ -> },
                onCloseDialog = {},
                status = UiStatusStateUpdate(),
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}