package br.com.usinasantafe.cvf.infra.models.sharedpreferences

import br.com.usinasantafe.cvf.domain.entities.variable.Manager
import br.com.usinasantafe.cvf.lib.StatusSend
import br.com.usinasantafe.cvf.utils.required
import java.util.Date

data class ManagerSharedPreferencesModel(
    var idRelease: Int? = null,
    var idFront: Int? = null,
    var qtdLimitCart: Int? = null,
    val dateHourCreate: Date = Date(),
    val dateHourUpdate: Date = Date(),
    var statusSend: StatusSend = StatusSend.STARTED
)

fun ManagerSharedPreferencesModel.sharedPreferencesModelToEntity(): Manager {
    return with(this) {
        Manager(
            idRelease = idRelease,
            idFront = idFront,
            qtdLimitCart = qtdLimitCart
        )
    }
}

fun Manager.entityToSharedPreferencesModel(): ManagerSharedPreferencesModel {
    return with(this) {
        ManagerSharedPreferencesModel(
            idRelease = ::idRelease.required(),
            idFront = ::idFront.required(),
            qtdLimitCart = ::qtdLimitCart.required()
        )
    }
}
