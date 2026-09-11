package br.com.usinasantafe.cvf.external.room.datasource.stable

import br.com.usinasantafe.cvf.external.room.dao.stable.EquipDao
import br.com.usinasantafe.cvf.infra.datasource.room.stable.EquipRoomDatasource
import br.com.usinasantafe.cvf.infra.models.room.stable.EquipRoomModel
import br.com.usinasantafe.cvf.lib.TypeEquip
import br.com.usinasantafe.cvf.utils.EmptyResult
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import br.com.usinasantafe.cvf.utils.notZero
import br.com.usinasantafe.cvf.utils.result
import javax.inject.Inject

class IEquipRoomDatasource @Inject constructor(
    private val equipDao: EquipDao
): EquipRoomDatasource {

    override suspend fun addAll(list: List<EquipRoomModel>): EmptyResult =
        result(getClassAndMethod()) {
            equipDao.insertAll(list)
        }

    override suspend fun deleteAll(): EmptyResult =
        result(getClassAndMethod()) {
            equipDao.deleteAll()
        }

    override suspend fun deleteByNro(nro: Int): EmptyResult =
        result(getClassAndMethod()) {
            equipDao.deleteByNro(nro)
        }

    override suspend fun add(model: EquipRoomModel): EmptyResult =
        result(getClassAndMethod()) {
            if (!equipDao.hasByNro(model.nro)) equipDao.insert(model)
        }

    override suspend fun hasByNro(nro: Int): Result<Boolean> =
        result(getClassAndMethod()) {
            equipDao.hasByNro(nro)
        }

    override suspend fun getTypeByNro(nro: Int): Result<TypeEquip> =
        result(getClassAndMethod()) {
            equipDao.getTypeByNro(nro)
        }

    override suspend fun getById(id: Int): Result<EquipRoomModel> =
        result(getClassAndMethod()) {
            equipDao.getById(id)
        }

    override suspend fun getIdByNro(nro: Int): Result<Int> =
        result(getClassAndMethod()) {
            equipDao.getIdByNro(nro).notZero("id")
        }

    override suspend fun getCdClassOperByNro(nro: Int): Result<Int> =
        result(getClassAndMethod()) {
            equipDao.getCdClassOperByNro(nro).notZero("cdClassOper")
        }

}