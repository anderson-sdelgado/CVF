package br.com.usinasantafe.cvf.infra.repositories.stable

import br.com.usinasantafe.cvf.domain.entities.stable.Release
import br.com.usinasantafe.cvf.domain.repositories.stable.ReleaseRepository
import br.com.usinasantafe.cvf.infra.datasource.retrofit.stable.ReleaseRetrofitDatasource
import br.com.usinasantafe.cvf.infra.datasource.room.stable.ReleaseRoomDatasource
import br.com.usinasantafe.cvf.infra.models.retrofit.stable.retrofitModelToEntity
import br.com.usinasantafe.cvf.infra.models.room.stable.entityToRoomModel
import br.com.usinasantafe.cvf.infra.models.room.stable.roomModelToEntity
import br.com.usinasantafe.cvf.utils.EmptyResult
import br.com.usinasantafe.cvf.utils.call
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import javax.inject.Inject
import kotlin.getOrThrow

class IReleaseRepository @Inject constructor(
    private val releaseRetrofitDatasource: ReleaseRetrofitDatasource,
    private val releaseRoomDatasource: ReleaseRoomDatasource
): ReleaseRepository {

    override suspend fun addAll(list: List<Release>): EmptyResult =
        call(getClassAndMethod()) {
            val roomModelList = list.map { it.entityToRoomModel() }
            releaseRoomDatasource.addAll(roomModelList).getOrThrow()
        }

    override suspend fun deleteAll(): EmptyResult =
        call(getClassAndMethod()) {
            releaseRoomDatasource.deleteAll().getOrThrow()
        }

    override suspend fun listAll(token: String): Result<List<Release>> =
        call(getClassAndMethod()) {
            val modelList = releaseRetrofitDatasource.listAll(token).getOrThrow()
            modelList.map { it.retrofitModelToEntity() }
        }

    override suspend fun listByIdFront(idFront: Int): Result<List<Release>> =
        call(getClassAndMethod()) {
            val roomModelList = releaseRoomDatasource.listByIdFront(idFront).getOrThrow()
            roomModelList.map { it.roomModelToEntity() }
        }

}