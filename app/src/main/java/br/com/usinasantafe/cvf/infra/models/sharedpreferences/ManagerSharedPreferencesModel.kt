package br.com.usinasantafe.cvf.infra.models.sharedpreferences

import br.com.usinasantafe.cvf.domain.entities.variable.Manager
import br.com.usinasantafe.cvf.utils.required

data class ManagerSharedPreferencesModel(
    val idRelease: Int,
    val idFront: Int,
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
