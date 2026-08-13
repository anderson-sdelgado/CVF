package br.com.usinasantafe.cvf.infra.models.sharedpreferences

import br.com.usinasantafe.cvf.domain.entities.variable.Manager
import br.com.usinasantafe.cvf.lib.StatusSend
import br.com.usinasantafe.cvf.utils.required
import java.util.Date

data class ManagerSharedPreferencesModel(
    val idRelease: Int,
    val idFront: Int,
    val dateHourCreate: Date = Date(),
    val dateHourUpdate: Date = Date(),
    val stateSend: StatusSend = StatusSend.SEND
)

fun ManagerSharedPreferencesModel.sharedPreferencesModelToEntity(): Manager {
    return with(this) {
        Manager(
            idRelease = idRelease,
            idFront = idFront,
        )
    }
}

fun Manager.entityToSharedPreferencesModel(): ManagerSharedPreferencesModel {
    return with(this) {
        ManagerSharedPreferencesModel(
            idRelease = ::idRelease.required(),
            idFront = ::idFront.required(),
        )
    }
}
