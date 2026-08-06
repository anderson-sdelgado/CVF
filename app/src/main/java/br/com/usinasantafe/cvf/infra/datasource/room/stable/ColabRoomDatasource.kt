package br.com.usinasantafe.cvf.infra.datasource.room.stable

import br.com.usinasantafe.cvf.infra.models.room.stable.ColabRoomModel
import br.com.usinasantafe.cvf.utils.EmptyResult

interface ColabRoomDatasource {
    suspend fun addAll(list: List<ColabRoomModel>): EmptyResult
    suspend fun deleteAll(): EmptyResult
}