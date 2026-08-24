package br.com.usinasantafe.cvf.presenter.view.configuration.password

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.usinasantafe.cvf.presenter.theme.CVFTheme
import br.com.usinasantafe.cvf.presenter.theme.TitleDesign

@Composable
fun PasswordScreen() {
    CVFTheme() {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            PasswordContent(
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Composable
fun PasswordContent(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(16.dp)
    ) {
        TitleDesign(text = "")
    }
}

@Preview(showBackground = true)
@Composable
fun PasswordPagePreview() {
    CVFTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            PasswordContent(
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}