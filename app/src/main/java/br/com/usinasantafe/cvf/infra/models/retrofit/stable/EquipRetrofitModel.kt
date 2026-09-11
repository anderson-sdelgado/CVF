package br.com.usinasantafe.cvf.infra.models.retrofit.stable

import br.com.usinasantafe.cvf.domain.entities.stable.Equip
import br.com.usinasantafe.cvf.infra.models.room.stable.EquipRoomModel
import br.com.usinasantafe.cvf.lib.TypeEquip

data class EquipRetrofitInput(
    val status: String,
    val failure: String?,
    val data: List<EquipRetrofitModel>?
)

data class EquipRetrofitCheck(
    val status: String,
    val failure: String?,
    val data: EquipRetrofitModel?
)

data class EquipRetrofitModel(
    val id: Int,
    val nro: Int,
    val cdOperClass: Int,
    val descOperClass: String,
    val type: Int
)

fun EquipRetrofitModel.retrofitModelToEntity(): Equip {
    return with(this) {
        Equip(
            id = id,
            nro = nro,
            cdOperClass = cdOperClass,
            descOperClass = descOperClass,
            type = if(type == 1) TypeEquip.TRUCK else TypeEquip.CART
        )
    }
}

fun EquipRetrofitModel.retrofitModelToRoomModel(): EquipRoomModel {
    return with(this) {
        EquipRoomModel(
            id = id,
            nro = nro,
            cdOperClass = cdOperClass,
            descOperClass = descOperClass,
            type = if(type == 1) TypeEquip.TRUCK else TypeEquip.CART
        )
    }
}
