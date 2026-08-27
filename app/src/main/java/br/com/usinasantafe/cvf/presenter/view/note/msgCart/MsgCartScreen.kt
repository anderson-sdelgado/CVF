package br.com.usinasantafe.cvf.presenter.view.note.msgCart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.usinasantafe.cvf.R
import br.com.usinasantafe.cvf.lib.OptionMenu
import br.com.usinasantafe.cvf.presenter.theme.CVFTheme
import br.com.usinasantafe.cvf.presenter.theme.TextButtonDesign
import br.com.usinasantafe.cvf.presenter.theme.TitleDesign
import br.com.usinasantafe.cvf.presenter.theme.topBar

@Composable
fun MsgCartScreen(
    viewModel: MsgCartViewModel = hiltViewModel(),
) {
    CVFTheme {
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        LaunchedEffect(Unit) {
            viewModel.recoverData()
        }

        MsgCartContent(
            nroCart = uiState.nroCart,
            flagMenu = uiState.flagMenu,
            optionMenu = uiState.optionMenu,
            onOptionMenu = viewModel::onOptionMenu,
            descRelease = uiState.descRelease
        )
    }
}

@Composable
fun MsgCartContent(
    nroCart: Int,
    flagMenu: Boolean,
    optionMenu: OptionMenu,
    onOptionMenu: (OptionMenu) -> Unit,
    descRelease: String,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = topBar(
            title = descRelease,
            onOptionMenu = onOptionMenu
        ),
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.TopCenter)
            ) {
                TitleDesign(
                    text = stringResource(
                        id = R.string.text_title_msg
                    )
                )
            }
            Column(
                modifier = modifier
                    .align(Alignment.Center)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(
                        id = R.string.text_msg_cart, nroCart
                    ),
                    textAlign = TextAlign.Center,
                    fontSize = 28.sp,
                    lineHeight = 38.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Button(
                        onClick = {},
                        modifier = Modifier.weight(1f)
                    ) {
                        TextButtonDesign(
                            text = stringResource(
                                id = R.string.text_pattern_no
                            )
                        )
                    }
                    Button(
                        onClick = {},
                        modifier = Modifier.weight(1f),
                    ) {
                        TextButtonDesign(
                            text = stringResource(
                                id = R.string.text_pattern_yes
                            )
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MsgCartPagePreview() {
    CVFTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            MsgCartContent(
                nroCart = 2,
                flagMenu = false,
                optionMenu = OptionMenu.DELETE,
                onOptionMenu = {},
                descRelease = "LIBERAÇÃO: 3\nO.S.: 3\nPROPRIEDADE: Test3",
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}