package br.com.usinasantafe.cvf.infra.models.room.stable

import androidx.room.Entity
import androidx.room.PrimaryKey
import br.com.usinasantafe.cvf.domain.entities.stable.Colab
import br.com.usinasantafe.cvf.lib.TB_COLAB

@Entity(tableName = TB_COLAB)
data class ColabRoomModel(
    @PrimaryKey
    val reg: Long,
    val name: String
)

fun ColabRoomModel.roomModelToEntity(): Colab {
    return with(this){
        Colab(
            reg = reg,
            name = name,
        )
    }
}

fun Colab.entityToRoomModel(): ColabRoomModel {
    return with(this){
        ColabRoomModel(
            reg = reg,
            name = name,
        )
    }
}