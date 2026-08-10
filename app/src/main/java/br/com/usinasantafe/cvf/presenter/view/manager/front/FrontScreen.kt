package br.com.usinasantafe.cvf.presenter.view.manager.front

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import br.com.usinasantafe.cvf.presenter.model.ItemCheckBoxScreenModel
import br.com.usinasantafe.cvf.presenter.theme.CVFTheme
import br.com.usinasantafe.cvf.presenter.theme.CheckboxDefault
import br.com.usinasantafe.cvf.presenter.theme.TitleDesign

@Composable
fun FrontScreen(
    viewModel: FrontViewModel = hiltViewModel()
) {
    CVFTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val list = viewModel.list

            LaunchedEffect(Unit) {
                viewModel.list()
            }

            FrontContent(
                list = list,
                onCheckChange = viewModel::onCheckChange,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Composable
fun FrontContent(
    list: List<ItemCheckBoxScreenModel>,
    onCheckChange: (Int, Boolean) -> Unit,
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
                    onChecked = { onCheckChange(item.id, it) }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FrontPagePreview() {
    CVFTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            FrontContent(
                list = listOf(),
                onCheckChange = { _, _ -> },
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
                onCheckChange = { _, _ -> },
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}