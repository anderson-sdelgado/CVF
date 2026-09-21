package br.com.usinasantafe.cvf.presenter.view.note.cart

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
fun CartScreen(
    viewModel: CartViewModel = hiltViewModel(),
    onNavReview: () -> Unit,
    onNavDriver: () -> Unit,
    onNavPassword: (OptionMenu) -> Unit,
) {
    CVFTheme {
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        LaunchedEffect(Unit) {
            viewModel.recoverData()
        }

        CartContent(
            flagCheckDialog = uiState.flagCheckDialog,
            onCheckDialog = viewModel::onCheckDialog,
            delete = viewModel::delete,
            posCart = uiState.pos,
            flagMenu = uiState.flagMenu,
            optionMenu = uiState.optionMenu,
            onOptionMenu = viewModel::onOptionMenu,
            descRelease = uiState.descRelease,
            text = uiState.text,
            flagReturn = uiState.flagReturn,
            onTextField = viewModel::onTextField,
            onCloseDialog = viewModel::onCloseDialog,
            status = uiState.status,
            onNavReview = onNavReview,
            onNavDriver = onNavDriver,
            onNavPassword = onNavPassword
        )
    }
}

@Composable
fun CartContent(
    flagCheckDialog: Boolean,
    onCheckDialog: (Boolean) -> Unit,
    delete: () -> Unit,
    posCart: Int,
    flagMenu: Boolean,
    optionMenu: OptionMenu,
    onOptionMenu: (OptionMenu) -> Unit,
    descRelease: String,
    text: String,
    flagReturn: Boolean,
    onTextField: (String, TypeButton) -> Unit,
    onCloseDialog: () -> Unit,
    status: UiStatusStateUpdate,
    onNavReview: () -> Unit,
    onNavDriver: () -> Unit,
    onNavPassword: (OptionMenu) -> Unit,
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
                    id = R.string.text_cart, posCart
                )
            )
            TextFieldDesign(
                value = text
            )
            Spacer(modifier = Modifier.padding(vertical = 8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                ButtonsGenericNumeric(
                    onTextField = onTextField,
                    flagReturn = true
                )
            }
            BackHandler {}

            if (status.flagDialog) {
                MsgUpdate(
                    status = status,
                    onClickOk = onCloseDialog,
                    value =
                        if (status.errors != Errors.CART_REPEATED) {
                            stringResource(
                                id = R.string.text_cart, posCart
                            )
                        } else {
                            text
                        }
                )
            }

            if (status.flagProgress) {
                AlertDialogProgressIndeterminateDesign(
                    stringResource(
                        id = R.string.text_msg_check_data, stringResource(id = R.string.text_truck)
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
            onNavReview()
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
fun CartPagePreview() {
    CVFTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            CartContent(
                flagCheckDialog = false,
                onCheckDialog = {},
                delete = {},
                posCart = 2,
                flagMenu = false,
                optionMenu = OptionMenu.DELETE,
                onOptionMenu = {},
                descRelease = "LIBERAÇÃO: 3\nO.S.: 3\nPROPRIEDADE: Test3",
                text = "",
                flagReturn = false,
                onTextField = { _, _ -> },
                onCloseDialog = {},
                status = UiStatusStateUpdate(),
                onNavReview = {},
                onNavDriver = {},
                onNavPassword = {},
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}