package br.com.usinasantafe.cvf.infra.models.room.variable

import androidx.room.Entity
import androidx.room.PrimaryKey
import br.com.usinasantafe.cvf.infra.models.sharedpreferences.HeaderSharedPreferencesModel
import br.com.usinasantafe.cvf.lib.StatusSend
import br.com.usinasantafe.cvf.lib.TB_HEADER
import br.com.usinasantafe.cvf.utils.required
import java.util.Date

@Entity(tableName = TB_HEADER)
data class HeaderRoomModel(
    @PrimaryKey
    var id: Int? = null,
    val regDriver: Long,
    val idTruck: Int,
    val nroTruck: Int,
    var idServ: Int? = null,
    var statusSend: StatusSend = StatusSend.SEND,
    var dateHourExitMill: Date = Date(),
    var dateHourArrivalField: Date = Date(),
    val dateHour: Date = Date()
)

fun HeaderSharedPreferencesModel.sharedPreferencesModelToRoomModel(): HeaderRoomModel {
    return with(this){
        HeaderRoomModel(
            regDriver = ::regDriver.required(),
            idTruck = ::idTruck.required(),
            nroTruck = ::nroTruck.required(),
            dateHourExitMill = ::dateHourExitMill.required(),
            dateHourArrivalField = ::dateHourArrivalField.required()
        )
    }
}
