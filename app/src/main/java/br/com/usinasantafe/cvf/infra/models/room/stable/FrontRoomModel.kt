package br.com.usinasantafe.cvf.infra.models.room.stable

import androidx.room.Entity
import androidx.room.PrimaryKey
import br.com.usinasantafe.cvf.domain.entities.stable.Front
import br.com.usinasantafe.cvf.lib.TB_FRONT

@Entity(tableName = TB_FRONT)
data class FrontRoomModel(
    @PrimaryKey
    val id: Int,
    val cd: Int,
    val description: String,
)

fun FrontRoomModel.roomModelToEntity(): Front {
    return with(this) {
        Front(
            id = id,
            cd = cd,
            description = description
        )
    }
}

fun Front.entityToRoomModel(): FrontRoomModel {
    return with(this) {
        FrontRoomModel(
            id = id,
            cd = cd,
            description = description
        )
    }
}