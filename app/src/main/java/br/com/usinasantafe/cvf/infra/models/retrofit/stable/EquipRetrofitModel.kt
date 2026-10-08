package br.com.usinasantafe.cvf.infra.models.retrofit.stable

import br.com.usinasantafe.cvf.domain.entities.stable.Equip
import br.com.usinasantafe.cvf.infra.models.room.stable.EquipRoomModel
import br.com.usinasantafe.cvf.lib.TypeEquip
import br.com.usinasantafe.cvf.utils.stringToDate

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
    val idOperClass: Int,
    val type: Int,
    val idFront: Int? = null,
    val cdFront: Int? = null,
    val descriptionFront: String? = null,
    val idRelease: Int? = null,
    val nroOSRelease: Int? = null,
    val idPropAgrRelease: Int? = null,
    val descPropAgrRelease: String? = null,
    val dateHourExitMill: String? = null,
    val dateHourArrivalField: String? = null,
)

fun EquipRetrofitModel.retrofitModelToEntity(): Equip {
    return with(this) {
        Equip(
            id = id,
            nro = nro,
            idOperClass = idOperClass,
            type = if(type == 1) TypeEquip.TRUCK else TypeEquip.CART,
            idFront = idFront,
            cdFront = cdFront,
            descriptionFront = descriptionFront,
            idRelease = idRelease,
            nroOSRelease = nroOSRelease,
            idPropAgrRelease = idPropAgrRelease,
            descPropAgrRelease = descPropAgrRelease,
            dateHourExitMill = dateHourExitMill?.stringToDate(),
            dateHourArrivalField = dateHourArrivalField?.stringToDate()
        )
    }
}

fun EquipRetrofitModel.retrofitModelToRoomModel(): EquipRoomModel {
    return with(this) {
        EquipRoomModel(
            id = id,
            nro = nro,
            idOperClass = idOperClass,
            type = if(type == 1) TypeEquip.TRUCK else TypeEquip.CART
        )
    }
}
