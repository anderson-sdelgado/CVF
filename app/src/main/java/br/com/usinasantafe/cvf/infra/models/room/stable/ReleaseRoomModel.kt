package br.com.usinasantafe.cvf.infra.models.room.stable

import androidx.room.Entity
import androidx.room.PrimaryKey
import br.com.usinasantafe.cvf.domain.entities.stable.Release
import br.com.usinasantafe.cvf.lib.TB_RELEASE

@Entity(tableName = TB_RELEASE)
data class ReleaseRoomModel(
    @PrimaryKey
    val id: Int,
    val nroOS: Int,
    val idPropAgr: Int,
    val descPropAgr: String,
    val idFront: Int,
)

fun ReleaseRoomModel.roomModelToEntity(): Release {
    return with(this) {
        Release(
            id = id,
            nroOS = nroOS,
            idPropAgr = idPropAgr,
            descPropAgr = descPropAgr,
            idFront = idFront
        )
    }
}

fun Release.entityToRoomModel(): ReleaseRoomModel {
    return with(this) {
        ReleaseRoomModel(
            id = id,
            nroOS = nroOS,
            idPropAgr = idPropAgr,
            descPropAgr = descPropAgr,
            idFront = idFront
        )
    }
}
