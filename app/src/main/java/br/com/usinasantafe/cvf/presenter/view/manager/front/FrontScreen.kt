package br.com.usinasantafe.cvf.presenter.view.manager.front

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
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
import br.com.usinasantafe.cvf.presenter.model.ItemCheckBoxScreenModel
import br.com.usinasantafe.cvf.presenter.theme.ButtonMaxWidth
import br.com.usinasantafe.cvf.presenter.theme.CVFTheme
import br.com.usinasantafe.cvf.presenter.theme.CheckboxDefault
import br.com.usinasantafe.cvf.presenter.theme.MsgUpdate
import br.com.usinasantafe.cvf.presenter.theme.Progress
import br.com.usinasantafe.cvf.presenter.theme.TextButtonDesign
import br.com.usinasantafe.cvf.presenter.theme.TitleDesign
import br.com.usinasantafe.cvf.utils.UiStatusStateUpdate
import br.com.usinasantafe.cvf.utils.required

@Composable
fun FrontScreen(
    viewModel: FrontViewModel = hiltViewModel(),
    onNavRelease: (Int) -> Unit,
    onNavConfig: () -> Unit
) {
    CVFTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val list = viewModel.list

            LaunchedEffect(Unit) {
                viewModel.list()
            }

            FrontContent(
                idSelection = uiState.idSelection,
                list = list,
                onCheckChanged = viewModel::onCheckChanged,
                check = viewModel::check,
                update = viewModel::update,
                onCloseDialog = viewModel::onCloseDialog,
                status = uiState.status,
                onNavRelease = onNavRelease,
                onNavConfig = onNavConfig,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Composable
fun FrontContent(
    idSelection: Int?,
    list: List<ItemCheckBoxScreenModel>,
    onCheckChanged: (Int, Boolean) -> Unit,
    check: () -> Unit,
    update: () -> Unit,
    onCloseDialog: () -> Unit,
    status: UiStatusStateUpdate,
    onNavRelease: (Int) -> Unit,
    onNavConfig: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(16.dp)
    ) {
        TitleDesign(
            text = stringResource(
                id = R.string.text_front
            )
        )
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(list, key = { it.id }) { item ->
                CheckboxDefault(
                    id = item.id,
                    text = item.desc,
                    checked = item.flag,
                    onChecked = { onCheckChanged(item.id, it) }
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        )  {
            Button(
                onClick = onNavConfig,
                modifier = Modifier
                    .weight(1f)
            ) {
                TextButtonDesign(
                    text = stringResource(
                        id = R.string.text_pattern_return
                    ),
                )
            }
            Button(
                onClick = check,
                modifier = Modifier
                    .weight(1f)
            ) {
                TextButtonDesign(
                    text = stringResource(
                        id = R.string.text_pattern_next
                    ),
                )
            }
        }
        Spacer(modifier = Modifier.padding(vertical = 4.dp))
        ButtonMaxWidth(R.string.text_pattern_update, onClick = update)
        BackHandler {}

        if (status.flagDialog) {
            MsgUpdate(status = status, onClickOk = onCloseDialog, value = stringResource(id = R.string.text_front))
        }

        if (status.flagProgress) {
            Progress(status)
        }

    }

    LaunchedEffect(status.flagAccess) {
        if (status.flagAccess) {
            onNavRelease(idSelection.required("idSelection"))
        }
    }


}

@Preview(showBackground = true)
@Composable
fun FrontPagePreview() {
    CVFTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            FrontContent(
                idSelection = null,
                list = listOf(),
                onCheckChanged = { _, _ -> },
                check = {},
                update = {},
                onCloseDialog = {},
                status = UiStatusStateUpdate(
                    flagAccess = false,
                    flagDialog = false,
                    flagFailure = false,
                    errors = Errors.FIELD_EMPTY,
                    failure = "",
                    flagProgress = false,
                    levelUpdate = null,
                    tableUpdate = "",
                    currentProgress = 0f,
                ),
                onNavRelease = {},
                onNavConfig = {},
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FrontPagePreviewWithData() {
    CVFTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            FrontContent(
                idSelection = null,
                list = listOf(
                    ItemCheckBoxScreenModel(
                        id = 1,
                        desc = "FRENTE 1",
                        flag = false
                    ),
                    ItemCheckBoxScreenModel(
                        id = 2,
                        desc = "FRENTE 2",
                        flag = false
                    ),
                    ItemCheckBoxScreenModel(
                        id = 3,
                        desc = "FRENTE 3",
                        flag = false
                    ),
                ),
                onCheckChanged = { _, _ -> },
                check = {},
                update = {},
                onCloseDialog = {},
                status = UiStatusStateUpdate(
                    flagAccess = false,
                    flagDialog = false,
                    flagFailure = false,
                    errors = Errors.FIELD_EMPTY,
                    failure = "",
                    flagProgress = false,
                    levelUpdate = null,
                    tableUpdate = "",
                    currentProgress = 0f,
                ),
                onNavRelease = {},
                onNavConfig = {},
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}