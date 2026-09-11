package br.com.usinasantafe.cvf.external.room.datasource.variable

import br.com.usinasantafe.cvf.external.room.dao.variable.HeaderDao
import br.com.usinasantafe.cvf.infra.datasource.room.variable.HeaderRoomDatasource
import br.com.usinasantafe.cvf.infra.models.room.variable.HeaderRoomModel
import br.com.usinasantafe.cvf.lib.StatusSend
import br.com.usinasantafe.cvf.utils.EmptyResult
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import br.com.usinasantafe.cvf.utils.notZero
import br.com.usinasantafe.cvf.utils.required
import br.com.usinasantafe.cvf.utils.result
import javax.inject.Inject

class IHeaderRoomDatasource @Inject constructor(
    private val headerDao: HeaderDao
): HeaderRoomDatasource {

    override suspend fun hasByStatusSend(statusSend: StatusSend): Result<Boolean> =
        result(getClassAndMethod()) {
            headerDao.hasByStatusSend(statusSend)
        }

    override suspend fun listByStatusSend(statusSend: StatusSend): Result<List<HeaderRoomModel>> =
        result(getClassAndMethod()) {
            headerDao.listByStatusSend(statusSend)
        }

    override suspend fun updateStatusSend(statusSend: StatusSend, list: List<HeaderRoomModel>): EmptyResult =
        result(getClassAndMethod()) {
            list.forEach {
                headerDao.updateStatusSend(statusSend, it::id.required(), it::idServ.required())
            }
        }

    override suspend fun save(model: HeaderRoomModel): Result<Int> =
        result(getClassAndMethod()) {
            headerDao.insert(model).toInt().notZero("id")
        }

    override suspend fun delete(id: Int): EmptyResult =
        result(getClassAndMethod()) {
            headerDao.deleteById(id)
        }

}