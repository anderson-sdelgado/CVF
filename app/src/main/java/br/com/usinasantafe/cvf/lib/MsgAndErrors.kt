package br.com.usinasantafe.cvf.lib

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import br.com.usinasantafe.cvf.R

@Composable
fun msg(levelUpdate: LevelUpdate?, failure: String, tableUpdate: String): String {
    return when(levelUpdate){
        LevelUpdate.RECOVERY -> stringResource(id = R.string.text_msg_recovery, tableUpdate)
        LevelUpdate.CLEAN -> stringResource(id = R.string.text_msg_clean, tableUpdate)
        LevelUpdate.SAVE -> stringResource(id = R.string.text_msg_save, tableUpdate)
        LevelUpdate.GET_TOKEN -> stringResource(id = R.string.text_msg_get_token)
        LevelUpdate.SAVE_TOKEN -> stringResource(id = R.string.text_msg_save_token)
        LevelUpdate.FINISH_UPDATE_INITIAL -> stringResource(id = R.string.text_msg_finish_update_initial)
        LevelUpdate.FINISH_UPDATE_COMPLETED -> stringResource(id = R.string.text_msg_finish_update_completed)
        else -> failure
    }
}

@Composable
fun errors(errors: Errors, failure: String, value: String = ""): String {
    return when (errors) {
        Errors.FIELD_EMPTY -> {
            if(!value.isEmpty()) return stringResource(
                id = R.string.text_field_empty,
                value
            )
            stringResource(id = R.string.text_field_empty_config)
        }
        Errors.UPDATE -> stringResource(
            id = R.string.text_update_failure,
            failure
        )
        Errors.TOKEN -> stringResource(
            id = R.string.text_token_failure,
            failure
        )
        Errors.INVALID -> {
            stringResource(
                id = R.string.text_input_data_invalid,
                value
            )
        }
        Errors.NOT_SELECTION -> {
            stringResource(
                id = R.string.text_not_selection,
                value
            )
        }
        else -> stringResource(
            id = R.string.text_failure,
            failure
        )
    }
}