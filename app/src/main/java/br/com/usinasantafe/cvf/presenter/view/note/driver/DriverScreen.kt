package br.com.usinasantafe.cvf.presenter.view.note.driver

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.usinasantafe.cvf.R
import br.com.usinasantafe.cvf.lib.Errors
import br.com.usinasantafe.cvf.lib.OptionMenu
import br.com.usinasantafe.cvf.lib.TypeButton
import br.com.usinasantafe.cvf.presenter.theme.AlertDialogCheckDesign
import br.com.usinasantafe.cvf.presenter.theme.AlertDialogProgressIndeterminateDesign
import br.com.usinasantafe.cvf.presenter.theme.CVFTheme
import br.com.usinasantafe.cvf.presenter.theme.MsgUpdate
import br.com.usinasantafe.cvf.presenter.theme.TextFieldDesign
import br.com.usinasantafe.cvf.presenter.theme.TitleDesign
import br.com.usinasantafe.cvf.presenter.theme.topBar
import br.com.usinasantafe.cvf.presenter.theme.ButtonsGenericNumeric
import br.com.usinasantafe.cvf.utils.UiStatusStateUpdate

@Composable
fun DriverScreen(
    viewModel: DriverViewModel = hiltViewModel(),
    onNavPassword: (OptionMenu) -> Unit,
    onNavTruck: () -> Unit
) {
    CVFTheme {

        RequestNotificationPermission()

        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        LaunchedEffect(Unit) {
            viewModel.recoverData()
        }

        DriverContent(
            flagCheckDialog = uiState.flagCheckDialog,
            onCheckDialog = viewModel::onCheckDialog,
            delete = viewModel::delete,
            recoverData = viewModel::recoverData,
            flagMenu = uiState.flagMenu,
            optionMenu = uiState.optionMenu,
            onOptionMenu = viewModel::onOptionMenu,
            descRelease = uiState.descRelease,
            text = uiState.text,
            onTextField = viewModel::onTextField,
            onCloseDialog = viewModel::onCloseDialog,
            status = uiState.status,
            onNavPassword = onNavPassword,
            onNavTruck = onNavTruck,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DriverContent(
    flagCheckDialog: Boolean,
    onCheckDialog: (Boolean) -> Unit,
    delete: () -> Unit,
    recoverData: () -> Unit,
    flagMenu: Boolean,
    optionMenu: OptionMenu,
    onOptionMenu: (OptionMenu) -> Unit,
    descRelease: String,
    text: String,
    onTextField: (String, TypeButton) -> Unit,
    onCloseDialog: () -> Unit,
    status: UiStatusStateUpdate,
    onNavPassword: (OptionMenu) -> Unit,
    onNavTruck: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = topBar(
            title = descRelease,
            onOptionMenu = onOptionMenu
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
                flagReturn = false
            )
            BackHandler {}

            if (status.flagDialog) {
                MsgUpdate(status = status, onClickOk = onCloseDialog, value = stringResource(id = R.string.text_driver))
            }

            if (status.flagProgress) {
                AlertDialogProgressIndeterminateDesign(
                    stringResource(
                        id = R.string.text_msg_check_data, stringResource(R.string.text_driver)
                    )
                )
            }

            if(flagCheckDialog){
                AlertDialogCheckDesign(
                    text = stringResource(id = R.string.text_msg_delete),
                    onClickDismiss = { onCheckDialog(false) },
                    onClickYes = delete
                )
            }

        }

    }

    LaunchedEffect(status.flagAccess) {
        if(status.flagAccess) {
            onNavTruck()
        }
    }

    LaunchedEffect(flagMenu) {
        if(flagMenu) {
            when(optionMenu){
                OptionMenu.DELETE -> recoverData()
                else -> onNavPassword(optionMenu)
            }
        }
    }

}

@Composable
fun RequestNotificationPermission() {
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        // Opcional: trate o resultado (granted/denied) aqui
    }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permissionStatus = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            )
            if (permissionStatus != PackageManager.PERMISSION_GRANTED) {
                launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DriverPagePreview() {
    CVFTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            DriverContent(
                flagCheckDialog = false,
                onCheckDialog = {},
                delete = {},
                recoverData = {},
                flagMenu = false,
                optionMenu = OptionMenu.DELETE,
                onOptionMenu = {},
                descRelease = "LIBERAÇÃO: 3\nO.S.: 3\nPROPRIEDADE: Test3",
                text = "",
                onTextField = { _, _ -> },
                onCloseDialog = {},
                status = UiStatusStateUpdate(),
                onNavPassword = {},
                onNavTruck = {},
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DriverPagePreviewProgress() {
    CVFTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            DriverContent(
                flagCheckDialog = false,
                onCheckDialog = {},
                delete = {},
                recoverData = {},
                flagMenu = false,
                optionMenu = OptionMenu.DELETE,
                onOptionMenu = {},
                descRelease = "LIBERAÇÃO: 3\nO.S.: 3\nPROPRIEDADE: Test3",
                text = "",
                onTextField = { _, _ -> },
                onCloseDialog = {},
                status = UiStatusStateUpdate(
                    flagProgress = true
                ),
                onNavPassword = {},
                onNavTruck = {},
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DriverPagePreviewFailure() {
    CVFTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            DriverContent(
                flagCheckDialog = false,
                onCheckDialog = {},
                delete = {},
                recoverData = {},
                flagMenu = false,
                optionMenu = OptionMenu.DELETE,
                onOptionMenu = {},
                descRelease = "LIBERAÇÃO: 3\nO.S.: 3\nPROPRIEDADE: Test3",
                text = "",
                onTextField = { _, _ -> },
                onCloseDialog = {},
                status = UiStatusStateUpdate(
                    flagDialog = true,
                    flagFailure = true,
                    failure = "Failure",
                    errors = Errors.EXCEPTION
                ),
                onNavPassword = {},
                onNavTruck = {},
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DriverPagePreviewCheck() {
    CVFTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            DriverContent(
                flagCheckDialog = true,
                onCheckDialog = {},
                delete = {},
                recoverData = {},
                flagMenu = false,
                optionMenu = OptionMenu.DELETE,
                onOptionMenu = {},
                descRelease = "LIBERAÇÃO: 3\nO.S.: 3\nPROPRIEDADE: Test3",
                text = "",
                onTextField = { _, _ -> },
                onCloseDialog = {},
                status = UiStatusStateUpdate(),
                onNavPassword = {},
                onNavTruck = {},
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}