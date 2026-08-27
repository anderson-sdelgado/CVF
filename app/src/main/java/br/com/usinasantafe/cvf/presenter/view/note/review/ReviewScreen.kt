package br.com.usinasantafe.cvf.presenter.view.note.review

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.usinasantafe.cvf.R
import br.com.usinasantafe.cvf.presenter.theme.ButtonMaxWidth
import br.com.usinasantafe.cvf.presenter.theme.CVFTheme
import br.com.usinasantafe.cvf.presenter.theme.TextButtonDesign
import br.com.usinasantafe.cvf.presenter.theme.TitleDesign

@Composable
fun ReviewScreen() {
    CVFTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            ReviewContent(
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Composable
fun ReviewContent(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TitleDesign(
            text = stringResource(
                id = R.string.text_title_travel
            )
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(
                    id = R.string.text_title_msg
                ),
                textAlign = TextAlign.Center,
                fontSize = 28.sp,
                lineHeight = 38.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
        ButtonMaxWidth(R.string.text_finish_travel, onClick = {})
        Spacer(modifier = Modifier.padding(vertical = 2.dp))
        ButtonMaxWidth(R.string.text_clean_travel, flagDelete = true, onClick = {})
    }
}

@Preview(showBackground = true)
@Composable
fun ReviewPagePreview() {
    CVFTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            ReviewContent(
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}