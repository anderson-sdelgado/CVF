package br.com.usinasantafe.cvf.infra.models.retrofit.variable

import br.com.usinasantafe.cvf.infra.models.sharedpreferences.ManagerSharedPreferencesModel
import br.com.usinasantafe.cvf.utils.required

data class ManagerRetrofitModelOutput(
    val idRelease: Int,
    val idFront: Int,
    val idServ: Int
)

data class ManagerRetrofitModelInput(
    val status: String,
    val idServ: Int?,
    val failure: String?
)

fun ManagerSharedPreferencesModel.sharedPreferencesModelToRetrofitModel(
    idServ: Int
): ManagerRetrofitModelOutput {
    return ManagerRetrofitModelOutput(
        idRelease = ::idRelease.required(),
        idFront = ::idFront.required(),
        idServ = idServ,
    )
}