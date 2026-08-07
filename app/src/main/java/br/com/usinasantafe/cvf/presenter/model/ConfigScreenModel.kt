package br.com.usinasantafe.cvf.presenter.model

import br.com.usinasantafe.cvf.domain.entities.variable.Config
import br.com.usinasantafe.cvf.utils.required

data class ConfigScreenModel(
    val number: String,
    val password: String,
)

fun Config.toConfigModel(): ConfigScreenModel {
    return with(this){
        ConfigScreenModel(
            number = this::number.required().toString(),
            password = this::password.required(),
        )
    }
}
