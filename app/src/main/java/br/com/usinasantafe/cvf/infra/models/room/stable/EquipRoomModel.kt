package br.com.usinasantafe.cvf.infra.models.room.stable

import androidx.room.Entity
import androidx.room.PrimaryKey
import br.com.usinasantafe.cvf.domain.entities.stable.Equip
import br.com.usinasantafe.cvf.lib.TB_EQUIP
import br.com.usinasantafe.cvf.lib.TypeEquip

@Entity(tableName = TB_EQUIP)
data class EquipRoomModel(
    @PrimaryKey
    val id: Int,
    val nro: Int,
    val cdOperClass: Int,
    val descOperClass: String,
    val type: TypeEquip
)

fun EquipRoomModel.roomModelToEntity(): Equip {
    return with(this) {
        Equip(
            id = id,
            nro = nro,
            cdOperClass = cdOperClass,
            descOperClass = descOperClass,
            type = type
        )
    }
}

fun Equip.entityToRoomModel(): EquipRoomModel {
    return with(this) {
        EquipRoomModel(
            id = id,
            nro = nro,
            cdOperClass = cdOperClass,
            descOperClass = descOperClass,
            type = type
        )
    }
}

