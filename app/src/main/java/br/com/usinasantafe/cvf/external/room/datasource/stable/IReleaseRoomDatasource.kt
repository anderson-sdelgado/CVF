package br.com.usinasantafe.cvf.external.room.datasource.stable

import br.com.usinasantafe.cvf.external.room.dao.stable.ReleaseDao
import br.com.usinasantafe.cvf.infra.datasource.room.stable.ReleaseRoomDatasource
import br.com.usinasantafe.cvf.infra.models.room.stable.ReleaseRoomModel
import br.com.usinasantafe.cvf.utils.EmptyResult
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import br.com.usinasantafe.cvf.utils.result
import javax.inject.Inject

class IReleaseRoomDatasource @Inject constructor(
    private val releaseDao: ReleaseDao
): ReleaseRoomDatasource {

    override suspend fun addAll(list: List<ReleaseRoomModel>): EmptyResult =
        result(getClassAndMethod()) {
            releaseDao.insertAll(list)
        }

    override suspend fun deleteAll(): EmptyResult =
        result(getClassAndMethod()) {
            releaseDao.deleteAll()
        }

    override suspend fun listByIdFront(idFront: Int): Result<List<ReleaseRoomModel>> =
        result(getClassAndMethod()) {
            releaseDao.listByIdFront(idFront)
        }

}