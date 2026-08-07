package br.com.usinasantafe.cvf.infra.datasource.room.stable

import br.com.usinasantafe.cvf.infra.models.room.stable.ReleaseRoomModel
import br.com.usinasantafe.cvf.utils.EmptyResult

interface ReleaseRoomDatasource {
    suspend fun addAll(list: List<ReleaseRoomModel>): EmptyResult
    suspend fun deleteAll(): EmptyResult
}