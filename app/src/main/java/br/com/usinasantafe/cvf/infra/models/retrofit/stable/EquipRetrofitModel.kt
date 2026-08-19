package br.com.usinasantafe.cvf.infra.models.retrofit.stable

import br.com.usinasantafe.cvf.domain.entities.stable.Equip

data class EquipRetrofitInput(
    val status: String,
    val failure: String?,
    val data: List<EquipRetrofitModel>?
)

data class EquipRetrofitModel(
    val id: Int,
    val nro: Int,
    val cdOperClass: Int,
    val description: String,
)

fun EquipRetrofitModel.retrofitModelToEntity(): Equip {
    return with(this) {
        Equip(
            id = id,
            nro = nro,
            cdOperClass = cdOperClass,
            description = description
        )
    }
}
