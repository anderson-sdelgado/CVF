package br.com.usinasantafe.cvf.presenter.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import br.com.usinasantafe.cvf.R
import br.com.usinasantafe.cvf.lib.TypeButton

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
//
//@Composable
//fun ButtonsGenericNumeric(
//    onTextField: (
//        text: String,
//        typeButton: TypeButton,
//    ) -> Unit,
//    flagReturn: Boolean = true,
//) {
//    Column(
//        verticalArrangement = Arrangement.spacedBy(4.dp)
//    ) {
//        val number1 = stringResource(id = R.string.text_number_1)
//        val number2 = stringResource(id = R.string.text_number_2)
//        val number3 = stringResource(id = R.string.text_number_3)
//        val number4 = stringResource(id = R.string.text_number_4)
//        val number5 = stringResource(id = R.string.text_number_5)
//        val number6 = stringResource(id = R.string.text_number_6)
//        val number7 = stringResource(id = R.string.text_number_7)
//        val number8 = stringResource(id = R.string.text_number_8)
//        val number9 = stringResource(id = R.string.text_number_9)
//        val number0 = stringResource(id = R.string.text_number_0)
//        val clean = stringResource(id = R.string.text_pattern_clean)
//        val ok = stringResource(id = R.string.text_pattern_ok)
//        val update = stringResource(id = R.string.text_pattern_update)
//        Row(
//            modifier = Modifier
//                .weight(1f),
//            horizontalArrangement = Arrangement.spacedBy(4.dp)
//        ) {
//
//            ButtonNumericDesign(
//                {
//                    TextButtonNumericDesign(
//                        text = number1
//                    )
//                },
//                {
//                    onTextField(
//                        number1,
//                        TypeButton.NUMERIC
//                    )
//                },
//                Modifier.weight(1f),
//                tag = number1
//            )
//            ButtonNumericDesign(
//                {
//                    TextButtonNumericDesign(
//                        text = number2
//                    )
//                },
//                {
//                    onTextField(
//                        number2,
//                        TypeButton.NUMERIC
//                    )
//                },
//                Modifier.weight(1f),
//                tag = number2
//            )
//            ButtonNumericDesign(
//                {
//                    TextButtonNumericDesign(
//                        text = number3
//                    )
//                },
//                {
//                    onTextField(
//                        number3,
//                        TypeButton.NUMERIC
//                    )
//                },
//                Modifier.weight(1f),
//                tag = number3
//            )
//        }
//        Row(
//            modifier = Modifier
//                .weight(1f),
//            horizontalArrangement = Arrangement.spacedBy(4.dp)
//        ) {
//            ButtonNumericDesign(
//                {
//                    TextButtonNumericDesign(
//                        text = number4
//                    )
//                },
//                {
//                    onTextField(
//                        number4,
//                        TypeButton.NUMERIC
//                    )
//                },
//                Modifier.weight(1f),
//                tag = number4
//            )
//            ButtonNumericDesign(
//                {
//                    TextButtonNumericDesign(
//                        text = number5
//                    )
//                },
//                {
//                    onTextField(
//                        number5,
//                        TypeButton.NUMERIC
//                    )
//                },
//                Modifier.weight(1f),
//                tag = number5
//            )
//            ButtonNumericDesign(
//                {
//                    TextButtonNumericDesign(
//                        text = number6
//                    )
//                },
//                {
//                    onTextField(
//                        number6,
//                        TypeButton.NUMERIC
//                    )
//                },
//                Modifier.weight(1f),
//                tag = number6
//            )
//        }
//        Row(
//            modifier = Modifier
//                .weight(1f),
//            horizontalArrangement = Arrangement.spacedBy(4.dp)
//        ) {
//            ButtonNumericDesign(
//                {
//                    TextButtonNumericDesign(
//                        text = number7
//                    )
//                },
//                {
//                    onTextField(
//                        number7,
//                        TypeButton.NUMERIC
//                    )
//                },
//                Modifier.weight(1f),
//                tag = number7
//            )
//            ButtonNumericDesign(
//                {
//                    TextButtonNumericDesign(
//                        text = number8
//                    )
//                },
//                {
//                    onTextField(
//                        number8,
//                        TypeButton.NUMERIC
//                    )
//                },
//                Modifier.weight(1f),
//                tag = number8
//            )
//            ButtonNumericDesign(
//                {
//                    TextButtonNumericDesign(
//                        text = number9
//                    )
//                },
//                {
//                    onTextField(
//                        number9,
//                        TypeButton.NUMERIC
//                    )
//                },
//                Modifier.weight(1f),
//                tag = number9
//            )
//        }
//        Row(
//            modifier = Modifier
//                .weight(1f),
//            horizontalArrangement = Arrangement.spacedBy(4.dp)
//        ) {
//            ButtonNumericDesign(
//                text = {
//                    TextButtonCleanDesign(
//                        text = clean
//                    )
//                },
//                {
//                    onTextField(
//                        clean,
//                        TypeButton.CLEAN
//                    )
//                },
//                modifier = Modifier.weight(1f),
//                tag = "CLEAN"
//            )
//            ButtonNumericDesign(
//                {
//                    TextButtonNumericDesign(
//                        text = number0
//                    )
//                },
//                {
//                    onTextField(
//                        number0,
//                        TypeButton.NUMERIC
//                    )
//                },
//                Modifier.weight(1f),
//                tag = number0
//            )
//        }
//        Row(
//            modifier = Modifier
//                .weight(1f),
//            horizontalArrangement = Arrangement.spacedBy(4.dp)
//        ) {
//            if(flagReturn) {
//                ButtonNumericDesign(
//                    {
//                        TextButtonNumericDesign(
//                            text = stringResource(R.string.text_pattern_return)
//                        )
//                    },
//                    {
//                        onTextField(
//                            ok,
//                            TypeButton.CANCEL
//                        )
//                    },
//                    Modifier.weight(1f),
//                    tag = "CANCEL"
//                )
//            }
//            ButtonNumericDesign(
//                {
//                    TextButtonNumericDesign(
//                        text = stringResource(R.string.text_pattern_next)
//                    )
//                },
//                {
//                    onTextField(
//                        ok,
//                        TypeButton.OK
//                    )
//                },
//                Modifier.weight(1f),
//                tag = "OK"
//            )
//        }
//    }
//}

@Composable
fun ButtonsGenericNumeric(
    onTextField: (
        text: String,
        typeButton: TypeButton,
    ) -> Unit,
    flagReturn: Boolean = true,
) {
    val number1 = stringResource(R.string.text_number_1)
    val number2 = stringResource(R.string.text_number_2)
    val number3 = stringResource(R.string.text_number_3)
    val number4 = stringResource(R.string.text_number_4)
    val number5 = stringResource(R.string.text_number_5)
    val number6 = stringResource(R.string.text_number_6)
    val number7 = stringResource(R.string.text_number_7)
    val number8 = stringResource(R.string.text_number_8)
    val number9 = stringResource(R.string.text_number_9)
    val number0 = stringResource(R.string.text_number_0)

    val clean = stringResource(R.string.text_pattern_clean)
    val ok = stringResource(R.string.text_pattern_ok)
    val returnText = stringResource(R.string.text_pattern_return)

    val buttonSpacing = 8.dp

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(buttonSpacing)
    ) {

        // ---------------------------------------------------------
        // 1 2 3
        // ---------------------------------------------------------

        NumericRow {
            NumericButton(
                text = number1,
                onClick = {
                    onTextField(number1, TypeButton.NUMERIC)
                },
                tag = number1
            )

            NumericButton(
                text = number2,
                onClick = {
                    onTextField(number2, TypeButton.NUMERIC)
                },
                tag = number2
            )

            NumericButton(
                text = number3,
                onClick = {
                    onTextField(number3, TypeButton.NUMERIC)
                },
                tag = number3
            )
        }

        // ---------------------------------------------------------
        // 4 5 6
        // ---------------------------------------------------------

        NumericRow {
            NumericButton(
                text = number4,
                onClick = {
                    onTextField(number4, TypeButton.NUMERIC)
                },
                tag = number4
            )

            NumericButton(
                text = number5,
                onClick = {
                    onTextField(number5, TypeButton.NUMERIC)
                },
                tag = number5
            )

            NumericButton(
                text = number6,
                onClick = {
                    onTextField(number6, TypeButton.NUMERIC)
                },
                tag = number6
            )
        }

        // ---------------------------------------------------------
        // 7 8 9
        // ---------------------------------------------------------

        NumericRow {
            NumericButton(
                text = number7,
                onClick = {
                    onTextField(number7, TypeButton.NUMERIC)
                },
                tag = number7
            )

            NumericButton(
                text = number8,
                onClick = {
                    onTextField(number8, TypeButton.NUMERIC)
                },
                tag = number8
            )

            NumericButton(
                text = number9,
                onClick = {
                    onTextField(number9, TypeButton.NUMERIC)
                },
                tag = number9
            )
        }

        // ---------------------------------------------------------
        // APAGAR 0
        // ---------------------------------------------------------

        NumericRow {

            ButtonNumericDesign(
                text = {
                    TextButtonCleanDesign(
                        text = clean
                    )
                },
                setActionButton = {
                    onTextField(
                        clean,
                        TypeButton.CLEAN
                    )
                },
                modifier = Modifier.weight(1f),
                tag = "CLEAN",
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer
            )

            NumericButton(
                text = number0,
                onClick = {
                    onTextField(number0, TypeButton.NUMERIC)
                },
                tag = number0
            )
        }

        // ---------------------------------------------------------
        // RETORNAR / AVANÇAR
        // ---------------------------------------------------------

        NumericRow {

            if (flagReturn) {

                ButtonNumericDesign(
                    text = {
                        TextButtonCleanDesign(
                            text = returnText
                        )
                    },
                    setActionButton = {
                        onTextField(
                            ok,
                            TypeButton.CANCEL
                        )
                    },
                    modifier = Modifier.weight(1f),
                    tag = "CANCEL",
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }

            ButtonNumericDesign(
                text = {
                    TextButtonCleanDesign(
                        text = stringResource(R.string.text_pattern_next)
                    )
                },
                setActionButton = {
                    onTextField(
                        ok,
                        TypeButton.OK
                    )
                },
                modifier = Modifier.weight(1f),
                tag = "OK",
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
private fun ColumnScope.NumericRow(
    content: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        content = content
    )
}

@Composable
private fun RowScope.NumericButton(
    text: String,
    onClick: () -> Unit,
    tag: String
) {
    ButtonNumericDesign(
        text = {
            TextButtonNumericDesign(
                text = text
            )
        },
        setActionButton = onClick,
        modifier = Modifier.weight(1f),
        tag = tag
    )
}