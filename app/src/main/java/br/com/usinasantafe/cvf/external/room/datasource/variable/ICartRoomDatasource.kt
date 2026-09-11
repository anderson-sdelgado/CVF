package br.com.usinasantafe.cvf.external.room.datasource.variable

import br.com.usinasantafe.cvf.external.room.dao.variable.CartDao
import br.com.usinasantafe.cvf.infra.datasource.room.variable.CartRoomDatasource
import br.com.usinasantafe.cvf.infra.models.room.variable.CartRoomModel
import br.com.usinasantafe.cvf.utils.EmptyResult
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import br.com.usinasantafe.cvf.utils.notZero
import br.com.usinasantafe.cvf.utils.result
import javax.inject.Inject

class ICartRoomDatasource @Inject constructor(
    private val cartDao: CartDao
): CartRoomDatasource {

    override suspend fun listByIdHeader(idHeader: Int): Result<List<CartRoomModel>> =
        result(getClassAndMethod()) {
            cartDao.listByIdHeader(idHeader)
        }

    override suspend fun save(model: CartRoomModel): Result<Int> =
        result(getClassAndMethod()) {
            cartDao.insert(model).toInt().notZero("id")
        }

    override suspend fun deleteByIdHeader(idHeader: Int): EmptyResult =
        result(getClassAndMethod()) {
            cartDao.deleteByIdHeader(idHeader)
        }

}