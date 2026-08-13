package br.com.usinasantafe.cvf.presenter.view.manager.release

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

@Composable
fun ReleaseScreen(
    viewModel: ReleaseViewModel = hiltViewModel(),
    onNavFront: () -> Unit,
    onNavColab: () -> Unit
) {
    CVFTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val list = viewModel.list

            LaunchedEffect(Unit) {
                viewModel.list()
            }

            ReleaseContent(
                list = list,
                onCheckChanged = viewModel::onCheckChanged,
                update = viewModel::update,
                onCloseDialog = viewModel::onCloseDialog,
                status = uiState.status,
                onNavFront = onNavFront,
                onNavColab = onNavColab,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Composable
fun ReleaseContent(
    list: List<ItemCheckBoxScreenModel>,
    onCheckChanged: (Int, Boolean) -> Unit,
    update: () -> Unit,
    onCloseDialog: () -> Unit,
    status: UiStatusStateUpdate,
    onNavFront: () -> Unit,
    onNavColab: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(16.dp)
    ) {
        TitleDesign(
            text = stringResource(
                id = R.string.text_release
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
                onClick = onNavFront,
                modifier = Modifier
                    .weight(1f)
            ) {
                TextButtonDesign(
                    text = stringResource(
                        id = R.string.text_pattern_return
                    ),
                    padding = 10
                )
            }
            Button(
                onClick = {},
                modifier = Modifier
                    .weight(1f)
            ) {
                TextButtonDesign(
                    text = stringResource(
                        id = R.string.text_pattern_next
                    ),
                    padding = 10
                )
            }
        }
        Spacer(modifier = Modifier.padding(vertical = 4.dp))
        ButtonMaxWidth(R.string.text_pattern_update, padding = 10, onClick = update)
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
            onNavColab()
        }
    }

}

@Preview(showBackground = true)
@Composable
fun ReleasePagePreview() {
    CVFTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            ReleaseContent(
                list = listOf(),
                onCheckChanged = { _, _ -> },
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
                onNavFront = {},
                onNavColab = {},
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ReleasePagePreviewWithData() {
    CVFTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            ReleaseContent(
                list = listOf(
                    ItemCheckBoxScreenModel(
                        id = 1,
                        desc = "LIBERAÇÃO: 1\n" +
                                "O.S.: 1\n" +
                                "PROPRIEDADE: ITAQUERE",
                        flag = false
                    ),
                    ItemCheckBoxScreenModel(
                        id = 2,
                        desc = "LIBERAÇÃO: 2\n" +
                                "O.S.: 2\n" +
                                "PROPRIEDADE: RANCHO AZUL",
                        flag = false
                    ),
                    ItemCheckBoxScreenModel(
                        id = 3,
                        desc = "LIBERAÇÃO: 3\n" +
                                "O.S.: 3\n" +
                                "PROPRIEDADE: ITAQUERE",
                        flag = false
                    ),
                ),
                onCheckChanged = { _, _ -> },
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
                onNavFront = {},
                onNavColab = {},
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}