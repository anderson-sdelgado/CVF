package br.com.usinasantafe.cvf.infra.datasource.room.stable

import br.com.usinasantafe.cvf.infra.models.room.stable.FrontRoomModel
import br.com.usinasantafe.cvf.utils.EmptyResult

interface FrontRoomDatasource {
    suspend fun addAll(list: List<FrontRoomModel>): EmptyResult
    suspend fun deleteAll(): EmptyResult
    suspend fun listAll(): Result<List<FrontRoomModel>>
}