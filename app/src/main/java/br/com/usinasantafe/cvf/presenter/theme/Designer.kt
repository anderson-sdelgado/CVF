package br.com.usinasantafe.cvf.presenter.theme

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import br.com.usinasantafe.cvf.R
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import br.com.usinasantafe.cvf.lib.Errors
import br.com.usinasantafe.cvf.lib.errors
import br.com.usinasantafe.cvf.lib.msg
import br.com.usinasantafe.cvf.utils.UiStatusStateUpdate


const val TAG_BUTTON_OK_ALERT_DIALOG_SIMPLE = "tag_button_ok_alert_dialog_simple"
const val TAG_BUTTON_YES_ALERT_DIALOG_CHECK = "tag_button_yes_alert_dialog_check"
const val TAG_BUTTON_NO_ALERT_DIALOG_CHECK = "tag_button_no_alert_dialog_check"
const val TAG_TOP_BAR_MENU = "tag_top_bar_menu"
const val TAG_TOP_BAR_DELETE = "tag_top_bar_delete"
const val TAG_TOP_BAR_MENU_ITEM_CONFIG = "tag_top_bar_menu_item_config"
const val TAG_TOP_BAR_MENU_ITEM_FRONT = "tag_top_bar_menu_item_front"
const val TAG_TOP_BAR_MENU_ITEM_RELEASE = "tag_top_bar_menu_item_release"
const val TAG_TOP_BAR_MENU_ITEM_CLOSE = "tag_top_bar_menu_item_close"

@Composable
fun ItemDefaultListDesign(
    id: Int = 0,
    text: String,
    font: Int = 22,
    padding: Int = 8,
    setActionItem: () -> Unit
) {
    return Text(
        textAlign = TextAlign.Left,
        text = text,
        fontSize = font.sp,
        modifier = Modifier
            .padding(vertical = padding.dp)
            .fillMaxWidth()
            .clickable {
                setActionItem()
            }
            .testTag("item_list_$id")
    )
}

@Composable
fun TitleDesign(
    text: String,
    font: Int = 30,
    padding: Int = 8
) {
    return Text(
        textAlign = TextAlign.Center,
        text = text,
        fontWeight = FontWeight.Bold,
        fontSize = font.sp,
        lineHeight = 32.sp,
        modifier = Modifier
            .padding(vertical = padding.dp)
            .fillMaxWidth()
    )
}

@Composable
fun AlertDialogSimpleDesign(
    text: String,
    onClickOk: () -> Unit
) {
    return AlertDialog(
        title = {
            Text(
                text = "ATENÇÃO",
                textAlign = TextAlign.Center
            )
        },
        text = {
            Text(
                text = text,
                modifier = Modifier.testTag("text_alert_dialog_simple")
            )
        },
        onDismissRequest = onClickOk,
        confirmButton = {
            Button(
                onClick = onClickOk,
                modifier = Modifier.testTag(TAG_BUTTON_OK_ALERT_DIALOG_SIMPLE)
            ) {
                Text("OK")
            }
        },
    )
}


@Composable
fun AlertDialogSimpleDesign(
    text: String,
    setCloseDialog: () -> Unit,
    setActionButtonOK: () -> Unit
) {
    return AlertDialog(
        title = {
            Text(
                text = "ATENÇÃO",
                textAlign = TextAlign.Center
            )
        },
        text = {
            Text(
                text = text,
                modifier = Modifier.testTag("text_alert_dialog_simple")
            )
        },
        onDismissRequest = setCloseDialog,
        confirmButton = {
            Button(
                onClick = setActionButtonOK,
                modifier = Modifier.testTag("button_ok_alert_dialog_simple")
            ) {
                Text("OK")
            }
        },
    )
}

@Composable
fun TextFieldPasswordDesign(
    value: String,
    onValueChange: (String) -> Unit,
    tag: String = ""
) {
    return OutlinedTextField(
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password
        ),
        textStyle = TextStyle(
            fontSize = 24.sp
        ),
        visualTransformation = PasswordVisualTransformation(),
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth().testTag(tag)
    )
}

@Composable
fun TextButtonDesign(
    text: String,
    flagMax: Boolean = false
) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxWidth()
    ) {
        val fontSize = if(flagMax) {
            (16f * (maxWidth.value / 180f))
                .coerceIn(12f, 22f)
        } else {
            (24f * (maxWidth.value / 180f))
                .coerceIn(18f, 28f)
        }
        Text(
            textAlign = TextAlign.Center,
            text = text,
            fontWeight = FontWeight.Bold,
            fontSize = fontSize.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
        )
    }
}

@Composable
fun TextFieldConfigDesign(
    value: String,
    onValueChange: (String) -> Unit,
    tag: String = ""
) {
    return OutlinedTextField(
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.None,
            autoCorrectEnabled = true,
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next
        ),
        textStyle = TextStyle(
            textAlign = TextAlign.Right,
            fontSize = 24.sp
        ),
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .testTag(tag)
    )
}

@Composable
fun AlertDialogProgressDesign(
    currentProgress: Float,
    msgProgress: String,
) {
    return Dialog(
        onDismissRequest = {}
    ) {
        Card {
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Text(
                    text = "ATENÇÃO",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Spacer(modifier = Modifier.padding(vertical = 4.dp))
                LinearProgressIndicator(
                    progress = { currentProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(30.dp),
                )
                Spacer(modifier = Modifier.padding(vertical = 4.dp))
                Text(
                    text = msgProgress,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun MsgUpdate(status : UiStatusStateUpdate, onClickOk: () -> Unit, value: String = ""){
    val text =
        if (status.flagFailure) {
            errors(status.errors, status.failure, value)
        } else {
            msg(status.levelUpdate, status.failure, status.tableUpdate)
        }
    AlertDialogSimpleDesign(
        text = text,
        onClickOk = onClickOk,
    )
}

@Composable
fun Progress(status : UiStatusStateUpdate){
    val msgProgress = msg(status.levelUpdate, status.failure, status.tableUpdate)
    AlertDialogProgressDesign(
        currentProgress = status.currentProgress,
        msgProgress = msgProgress
    )
}

@Composable
fun TextFieldDesign(
    value: String,
    tag: String = ""
) {
    return OutlinedTextField(
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.None,
            autoCorrectEnabled = true,
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Previous
        ),
        textStyle = TextStyle(
            textAlign = TextAlign.Right,
            fontSize = 28.sp,
        ),
        readOnly = true,
        value = value,
        onValueChange = {},
        modifier = Modifier
            .fillMaxWidth()
            .testTag(tag)
    )
}

@Composable
fun ButtonNumericDesign(
    text: @Composable () -> Unit,
    setActionButton: () -> Unit,
    modifier: Modifier,
    tag: String = "",
) {
    return ElevatedButton(
        onClick = {
            setActionButton()
        },
        modifier = modifier
            .fillMaxHeight()
            .testTag("button_$tag")
        ,
        shape = RoundedCornerShape(10.dp)
    ) {
        text()
    }
}

@Composable
fun ButtonMaxWidth(
    id: Int,
    flagDelete: Boolean = false,
    onClick: () -> Unit
) {

    var containerColor: Color = MaterialTheme.colorScheme.primary
    var contentColor: Color = MaterialTheme.colorScheme.onPrimary
    if(flagDelete) {
        containerColor = MaterialTheme.colorScheme.error
        contentColor = MaterialTheme.colorScheme.onError
    }

    return Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        )
    ) {
        TextButtonDesign(
            text = stringResource(id = id),
            flagMax = true
        )
    }
}

@Composable
fun TextButtonNumericDesign(
    text: String,
) {
    return Text(
        textAlign = TextAlign.Center,
        text = text,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        modifier = Modifier
            .fillMaxWidth()
    )
}

@Composable
fun TextButtonCleanDesign(
    text: String
) {
    return Text(
        textAlign = TextAlign.Center,
        text = text,
        fontWeight = FontWeight.Bold,
        fontSize = 17.sp,
        modifier = Modifier
            .fillMaxWidth()
    )
}

@Composable
fun AlertDialogCheckDesign(
    text: String,
    onClickDismiss: () -> Unit,
    onClickYes: () -> Unit
) {
    return AlertDialog(
        title = {
            Text(
                text = "ATENÇÃO",
                textAlign = TextAlign.Center
            )
        },
        text = {
            Text(
                text = text,
                modifier = Modifier.testTag("text_alert_dialog_check")
            )
        },
        onDismissRequest = onClickDismiss,
        confirmButton = {
            Button(
                onClick = onClickYes,
                modifier = Modifier.testTag(TAG_BUTTON_YES_ALERT_DIALOG_CHECK)
            ) {
                Text("SIM")
            }
        },
        dismissButton = {
            Button(
                onClick = onClickDismiss,
                modifier = Modifier.testTag(TAG_BUTTON_NO_ALERT_DIALOG_CHECK)
            ) {
                Text("NÃO")
            }
        }
    )
}

@Composable
fun CheckboxDefault(
    id: Int,
    text: String,
    font: Int = 22,
    paddingStart: Int = 10,
    checked: Boolean,
    onChecked: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable { onChecked(!checked) }
            .padding(
                start = paddingStart.dp,
                top = 10.dp,
                end = 10.dp,
                bottom = 10.dp,
            )
            .testTag("item_check_box_$id")
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = null,
            modifier = Modifier
                .padding(end = 10.dp)
        )

        BoxWithConstraints(
            modifier = Modifier.fillMaxWidth()
        ) {
            val fontSize =
                (14f * (maxWidth.value / 180f))
                    .coerceIn(10f, 20f)
            Text(
                text = text,
                fontSize = fontSize.sp,
            )
        }
    }
}

@Composable
fun MsgErrors(errors: Errors, onClickOk: () -> Unit, failure: String, value: String = ""){
    val text = errors(errors, failure, value)
    AlertDialogSimpleDesign(
        text = text,
        onClickOk = onClickOk,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun topBar(title: String): @Composable () -> Unit = {
    var menuExpanded by remember { mutableStateOf(false) }
    TopAppBar(
        title = {
            Text(
                text = title,
                style = TextStyle(
                    color = Color.White
                )
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Blue,
        ),
        actions = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {},
                    Modifier.testTag(TAG_TOP_BAR_DELETE)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(R.string.text_cancel_note),
                        tint = Color.White
                    )
                }
                Box {
                    IconButton(
                        onClick = {
                            menuExpanded = !menuExpanded
                        },
                        Modifier.testTag(TAG_TOP_BAR_MENU)
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = stringResource(R.string.text_menu),
                            tint = Color.White
                        )
                    }

                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = {
                            menuExpanded = false
                        }
                    ) {
                        DropdownMenuItem(
                            text = {
                                Text(stringResource(R.string.text_config))
                            },
                            onClick = {
                                menuExpanded = false
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Settings,
                                    contentDescription = null
                                )
                            },
                            modifier = Modifier.testTag(TAG_TOP_BAR_MENU_ITEM_CONFIG)
                        )
                        DropdownMenuItem(
                            text = {
                                Text(stringResource(R.string.text_front))
                            },
                            onClick = {
                                menuExpanded = false
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null
                                )
                            },
                            modifier = Modifier.testTag(TAG_TOP_BAR_MENU_ITEM_FRONT)
                        )
                        DropdownMenuItem(
                            text = {
                                Text(stringResource(R.string.text_release))
                            },
                            onClick = {
                                menuExpanded = false
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = null
                                )
                            },
                            modifier = Modifier.testTag(TAG_TOP_BAR_MENU_ITEM_RELEASE)
                        )
                        DropdownMenuItem(
                            text = {
                                Text(stringResource(R.string.text_close))
                            },
                            onClick = {
                                menuExpanded = false
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = null
                                )
                            },
                            modifier = Modifier.testTag(TAG_TOP_BAR_MENU_ITEM_CLOSE)
                        )
                    }
                }
            }
        }
    )
}

