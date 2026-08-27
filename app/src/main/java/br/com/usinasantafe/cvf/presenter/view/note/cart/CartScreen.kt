package br.com.usinasantafe.cvf.presenter.view.note.cart

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import br.com.usinasantafe.cvf.presenter.theme.AlertDialogProgressIndeterminateDesign
import br.com.usinasantafe.cvf.presenter.theme.CVFTheme
import br.com.usinasantafe.cvf.presenter.theme.MsgUpdate
import br.com.usinasantafe.cvf.presenter.theme.TextFieldDesign
import br.com.usinasantafe.cvf.presenter.theme.TitleDesign
import br.com.usinasantafe.cvf.presenter.theme.topBar
import br.com.usinasantafe.cvf.presenter.view.ButtonsGenericNumeric
import br.com.usinasantafe.cvf.utils.UiStatusStateUpdate

@Composable
fun CartScreen(
    viewModel: CartViewModel = hiltViewModel(),
) {
    CVFTheme {
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        LaunchedEffect(Unit) {
            viewModel.recoverData()
        }

        CartContent(
            nroCart = uiState.nroCart,
            flagMenu = uiState.flagMenu,
            optionMenu = uiState.optionMenu,
            onOptionMenu = viewModel::onOptionMenu,
            descRelease = uiState.descRelease,
            text = uiState.text,
            onTextField = viewModel::onTextField,
            onCloseDialog = viewModel::onCloseDialog,
            status = uiState.status,
        )
    }
}

@Composable
fun CartContent(
    nroCart: Int,
    flagMenu: Boolean,
    optionMenu: OptionMenu,
    onOptionMenu: (OptionMenu) -> Unit,
    descRelease: String,
    text: String,
    onTextField: (String, TypeButton) -> Unit,
    onCloseDialog: () -> Unit,
    status: UiStatusStateUpdate,
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
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            TitleDesign(
                text = stringResource(
                    id = R.string.text_cart, nroCart
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
                MsgUpdate(
                    status = status,
                    onClickOk = onCloseDialog,
                    value = stringResource(
                        id = R.string.text_cart, nroCart
                    )
                )
            }

            if (status.flagProgress) {
                AlertDialogProgressIndeterminateDesign(
                    stringResource(
                        id = R.string.text_msg_check_data, R.string.text_truck
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CartPagePreview() {
    CVFTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            CartContent(
                nroCart = 2,
                flagMenu = false,
                optionMenu = OptionMenu.DELETE,
                onOptionMenu = {},
                descRelease = "LIBERAÇÃO: 3\nO.S.: 3\nPROPRIEDADE: Test3",
                text = "",
                onTextField = { _, _ -> },
                onCloseDialog = {},
                status = UiStatusStateUpdate(),
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}