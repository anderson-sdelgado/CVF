package br.com.usinasantafe.cvf.presenter.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import br.com.usinasantafe.cvf.R
import br.com.usinasantafe.cvf.lib.TypeButton
import br.com.usinasantafe.cvf.presenter.theme.ButtonNumericDesign
import br.com.usinasantafe.cvf.presenter.theme.TextButtonCleanDesign
import br.com.usinasantafe.cvf.presenter.theme.TextButtonNumericDesign
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale
import kotlin.math.pow

fun addTextField(text: String, char: String): String {
    return text + char
}

fun clearTextField(text: String): String {

    val reduced = text.dropLast(1)
    val maskChars = listOf('(', '.', '-', ')', ' ')

    var finalResult = reduced
    while (finalResult.isNotEmpty() && finalResult.last() in maskChars) {
        finalResult = finalResult.dropLast(1)
    }

    return finalResult

}

@Composable
fun ButtonsGenericNumeric(
    onTextField: (
        text: String,
        typeButton: TypeButton,
    ) -> Unit,
    flagUpdate: Boolean = true,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        val number1 = stringResource(id = R.string.text_number_1)
        val number2 = stringResource(id = R.string.text_number_2)
        val number3 = stringResource(id = R.string.text_number_3)
        val number4 = stringResource(id = R.string.text_number_4)
        val number5 = stringResource(id = R.string.text_number_5)
        val number6 = stringResource(id = R.string.text_number_6)
        val number7 = stringResource(id = R.string.text_number_7)
        val number8 = stringResource(id = R.string.text_number_8)
        val number9 = stringResource(id = R.string.text_number_9)
        val number0 = stringResource(id = R.string.text_number_0)
        val clean = stringResource(id = R.string.text_pattern_clean)
        val ok = stringResource(id = R.string.text_pattern_ok)
        val update = stringResource(id = R.string.text_pattern_update)
        Row(
            modifier = Modifier
                .weight(1f),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {

            ButtonNumericDesign(
                {
                    TextButtonNumericDesign(
                        text = number1
                    )
                },
                {
                    onTextField(
                        number1,
                        TypeButton.NUMERIC
                    )
                },
                Modifier.weight(1f),
                tag = number1
            )
            ButtonNumericDesign(
                {
                    TextButtonNumericDesign(
                        text = number2
                    )
                },
                {
                    onTextField(
                        number2,
                        TypeButton.NUMERIC
                    )
                },
                Modifier.weight(1f),
                tag = number2
            )
            ButtonNumericDesign(
                {
                    TextButtonNumericDesign(
                        text = number3
                    )
                },
                {
                    onTextField(
                        number3,
                        TypeButton.NUMERIC
                    )
                },
                Modifier.weight(1f),
                tag = number3
            )
        }
        Row(
            modifier = Modifier
                .weight(1f),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            ButtonNumericDesign(
                {
                    TextButtonNumericDesign(
                        text = number4
                    )
                },
                {
                    onTextField(
                        number4,
                        TypeButton.NUMERIC
                    )
                },
                Modifier.weight(1f),
                tag = number4
            )
            ButtonNumericDesign(
                {
                    TextButtonNumericDesign(
                        text = number5
                    )
                },
                {
                    onTextField(
                        number5,
                        TypeButton.NUMERIC
                    )
                },
                Modifier.weight(1f),
                tag = number5
            )
            ButtonNumericDesign(
                {
                    TextButtonNumericDesign(
                        text = number6
                    )
                },
                {
                    onTextField(
                        number6,
                        TypeButton.NUMERIC
                    )
                },
                Modifier.weight(1f),
                tag = number6
            )
        }
        Row(
            modifier = Modifier
                .weight(1f),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            ButtonNumericDesign(
                {
                    TextButtonNumericDesign(
                        text = number7
                    )
                },
                {
                    onTextField(
                        number7,
                        TypeButton.NUMERIC
                    )
                },
                Modifier.weight(1f),
                tag = number7
            )
            ButtonNumericDesign(
                {
                    TextButtonNumericDesign(
                        text = number8
                    )
                },
                {
                    onTextField(
                        number8,
                        TypeButton.NUMERIC
                    )
                },
                Modifier.weight(1f),
                tag = number8
            )
            ButtonNumericDesign(
                {
                    TextButtonNumericDesign(
                        text = number9
                    )
                },
                {
                    onTextField(
                        number9,
                        TypeButton.NUMERIC
                    )
                },
                Modifier.weight(1f),
                tag = number9
            )
        }
        Row(
            modifier = Modifier
                .weight(1f),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            ButtonNumericDesign(
                text = {
                    TextButtonCleanDesign(
                        text = clean
                    )
                },
                {
                    onTextField(
                        clean,
                        TypeButton.CLEAN
                    )
                },
                modifier = Modifier.weight(1f),
                tag = "CLEAN"
            )
            ButtonNumericDesign(
                {
                    TextButtonNumericDesign(
                        text = number0
                    )
                },
                {
                    onTextField(
                        number0,
                        TypeButton.NUMERIC
                    )
                },
                Modifier.weight(1f),
                tag = number0
            )
            ButtonNumericDesign(
                {
                    TextButtonNumericDesign(
                        text = ok
                    )
                },
                {
                    onTextField(
                        ok,
                        TypeButton.OK
                    )
                },
                Modifier.weight(1f),
                tag = "OK"
            )
        }
        if(flagUpdate){
            Row(
                modifier = Modifier
                    .weight(1f),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                ButtonNumericDesign(
                    {
                        TextButtonNumericDesign(
                            text = update
                        )
                    },
                    {
                        onTextField(
                            update,
                            TypeButton.UPDATE
                        )
                    },
                    Modifier.weight(1f),
                    tag = "UPDATE"
                )
            }
        }
    }
}
