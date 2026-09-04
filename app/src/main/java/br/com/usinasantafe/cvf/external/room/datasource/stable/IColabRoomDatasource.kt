package br.com.usinasantafe.cvf.external.room.datasource.stable

import br.com.usinasantafe.cvf.external.room.dao.stable.ColabDao
import br.com.usinasantafe.cvf.infra.datasource.room.stable.ColabRoomDatasource
import br.com.usinasantafe.cvf.infra.models.room.stable.ColabRoomModel
import br.com.usinasantafe.cvf.utils.EmptyResult
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import br.com.usinasantafe.cvf.utils.result
import javax.inject.Inject

class IColabRoomDatasource @Inject constructor(
    private val colabDao: ColabDao
): ColabRoomDatasource {

    override suspend fun addAll(list: List<ColabRoomModel>): EmptyResult =
        result(getClassAndMethod()) {
            colabDao.insertAll(list)
        }

    override suspend fun deleteAll(): EmptyResult =
        result(getClassAndMethod()) {
            colabDao.deleteAll()
        }

    override suspend fun deleteByReg(reg: Long): EmptyResult =
        result(getClassAndMethod()) {
            colabDao.deleteByReg(reg)
        }

    override suspend fun add(model: ColabRoomModel): EmptyResult =
        result(getClassAndMethod()) {
            if (!colabDao.checkByReg(model.reg)) colabDao.insert(model)
        }

    override suspend fun checkByReg(reg: Long): Result<Boolean> =
        result(getClassAndMethod()) {
            colabDao.checkByReg(reg)
        }

    override suspend fun getNameByReg(reg: Long): Result<String> =
        result(getClassAndMethod()) {
            colabDao.getNameByReg(reg)
        }

}