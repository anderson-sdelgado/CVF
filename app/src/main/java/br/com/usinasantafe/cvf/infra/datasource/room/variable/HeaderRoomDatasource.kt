package br.com.usinasantafe.cvf.infra.datasource.room.variable

import br.com.usinasantafe.cvf.infra.models.room.variable.HeaderRoomModel
import br.com.usinasantafe.cvf.lib.StatusSend
import br.com.usinasantafe.cvf.utils.EmptyResult

interface HeaderRoomDatasource {
    suspend fun hasByStatusSend(statusSend: StatusSend): Result<Boolean>
    suspend fun listByStatusSend(statusSend: StatusSend): Result<List<HeaderRoomModel>>
    suspend fun updateStatusSend(statusSend: StatusSend, list: List<HeaderRoomModel>): EmptyResult
    suspend fun save(model: HeaderRoomModel): Result<Int>
    suspend fun delete(id: Int): EmptyResult
}