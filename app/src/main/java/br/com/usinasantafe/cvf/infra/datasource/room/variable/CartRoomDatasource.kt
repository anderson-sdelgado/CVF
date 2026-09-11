package br.com.usinasantafe.cvf.infra.datasource.room.variable

import br.com.usinasantafe.cvf.infra.models.room.variable.CartRoomModel
import br.com.usinasantafe.cvf.utils.EmptyResult

interface CartRoomDatasource {
    suspend fun listByIdHeader(idHeader: Int): Result<List<CartRoomModel>>
    suspend fun save(model: CartRoomModel): Result<Int>
    suspend fun deleteByIdHeader(idHeader: Int): EmptyResult
}