package br.com.usinasantafe.cvf.presenter.view.note.truck

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
import br.com.usinasantafe.cvf.R
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
import kotlin.Boolean

@Composable
fun TruckScreen(
    viewModel: TruckViewModel = hiltViewModel(),
    onNavPassword: (OptionMenu) -> Unit,
    onNavDriver: () -> Unit,
    onNavCart: () -> Unit,
) {
    CVFTheme {
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        LaunchedEffect(Unit) {
            viewModel.recoverData()
        }

        TruckContent(
            flagCheckDialog = uiState.flagCheckDialog,
            onCheckDialog = viewModel::onCheckDialog,
            delete = viewModel::delete,
            flagMenu = uiState.flagMenu,
            optionMenu = uiState.optionMenu,
            onOptionMenu = viewModel::onOptionMenu,
            descRelease = uiState.descRelease,
            text = uiState.text,
            flagReturn = uiState.flagReturn,
            onTextField = viewModel::onTextField,
            onCloseDialog = viewModel::onCloseDialog,
            status = uiState.status,
            onNavPassword = onNavPassword,
            onNavDriver = onNavDriver,
            onNavCart = onNavCart
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TruckContent(
    flagCheckDialog: Boolean,
    onCheckDialog: (Boolean) -> Unit,
    delete: () -> Unit,
    flagMenu: Boolean,
    optionMenu: OptionMenu,
    onOptionMenu: (OptionMenu) -> Unit,
    descRelease: String,
    text: String,
    flagReturn: Boolean,
    onTextField: (String, TypeButton) -> Unit,
    onCloseDialog: () -> Unit,
    status: UiStatusStateUpdate,
    onNavPassword: (OptionMenu) -> Unit,
    onNavDriver: () -> Unit,
    onNavCart: () -> Unit,
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
                    id = R.string.text_truck
                )
            )
            TextFieldDesign(
                value = text
            )
            Spacer(modifier = Modifier.padding(vertical = 8.dp))
            ButtonsGenericNumeric(
                onTextField = onTextField,
                flagReturn = true
            )
            BackHandler {}

            if (status.flagDialog) {
                MsgUpdate(status = status, onClickOk = onCloseDialog, value = stringResource(id = R.string.text_truck))
            }

            if (status.flagProgress) {
                AlertDialogProgressIndeterminateDesign(
                    stringResource(
                        id = R.string.text_msg_check_data, stringResource(R.string.text_truck)
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
            onNavCart()
        }
    }

    LaunchedEffect(flagMenu) {
        if(flagMenu) {
            when(optionMenu){
                OptionMenu.DELETE -> onNavDriver()
                else -> onNavPassword(optionMenu)
            }
        }
    }

    LaunchedEffect(flagReturn) {
        if (flagReturn) {
            onNavDriver()
        }
    }

}

@Preview(showBackground = true)
@Composable
fun TruckPagePreview() {
    CVFTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            TruckContent(
                flagCheckDialog = false,
                onCheckDialog = {},
                delete = {},
                flagMenu = false,
                optionMenu = OptionMenu.DELETE,
                onOptionMenu = {},
                descRelease = "LIBERAÇÃO: 3\nO.S.: 3\nPROPRIEDADE: Test3",
                text = "",
                flagReturn = false,
                onTextField = { _, _ -> },
                onCloseDialog = {},
                status = UiStatusStateUpdate(),
                onNavPassword = {},
                onNavDriver = {},
                onNavCart = {},
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}