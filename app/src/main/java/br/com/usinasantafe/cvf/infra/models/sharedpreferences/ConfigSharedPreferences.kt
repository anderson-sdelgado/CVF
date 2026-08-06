package br.com.usinasantafe.cvf.infra.models.sharedpreferences

import br.com.usinasantafe.cvf.domain.entities.variable.Config
import br.com.usinasantafe.cvf.lib.StatusSend
import br.com.usinasantafe.cvf.utils.required

data class ConfigSharedPreferencesModel(
    var number: Long? = null,
    var password: String? = null,
    var idServ: Int? = null,
    var version: String? = null,
    var statusSend: StatusSend = StatusSend.STARTED,
    var flagUpdate: Boolean = false,
)

fun ConfigSharedPreferencesModel.sharedPreferencesModelToEntity(): Config {
    return with(this) {
        Config(
            number = ::number.required(),
            password = ::password.required(),
            idServ = idServ,
            version = version,
            statusSend = statusSend,
            flagUpdate = flagUpdate,
        )
    }
}

fun Config.entityToSharedPreferencesModel(): ConfigSharedPreferencesModel {
    return with(this) {
        ConfigSharedPreferencesModel(
            number = ::number.required(),
            password = ::password.required(),
            idServ = ::idServ.required(),
            version = ::version.required(),
            statusSend = statusSend,
            flagUpdate = flagUpdate,
        )
    }
}