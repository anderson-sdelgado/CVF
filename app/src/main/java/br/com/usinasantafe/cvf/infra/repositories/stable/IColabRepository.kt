package br.com.usinasantafe.cvf.infra.repositories.stable

import br.com.usinasantafe.cvf.domain.entities.stable.Colab
import br.com.usinasantafe.cvf.domain.repositories.stable.ColabRepository
import br.com.usinasantafe.cvf.infra.datasource.retrofit.stable.ColabRetrofitDatasource
import br.com.usinasantafe.cvf.infra.datasource.room.stable.ColabRoomDatasource
import br.com.usinasantafe.cvf.infra.models.retrofit.stable.retrofitModelToEntity
import br.com.usinasantafe.cvf.infra.models.retrofit.stable.retrofitModelToRoomModel
import br.com.usinasantafe.cvf.infra.models.room.stable.entityToRoomModel
import br.com.usinasantafe.cvf.utils.EmptyResult
import br.com.usinasantafe.cvf.utils.call
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import javax.inject.Inject
import kotlin.collections.addAll

class IColabRepository @Inject constructor(
    private val colabRetrofitDatasource: ColabRetrofitDatasource,
    private val colabRoomDatasource: ColabRoomDatasource
): ColabRepository {

    override suspend fun addAll(list: List<Colab>): EmptyResult =
        call(getClassAndMethod()) {
            val roomModelList = list.map { it.entityToRoomModel() }
            colabRoomDatasource.addAll(roomModelList).getOrThrow()
        }

    override suspend fun deleteAll(): EmptyResult =
        call(getClassAndMethod()) {
            colabRoomDatasource.deleteAll().getOrThrow()
        }

    override suspend fun listAll(token: String): Result<List<Colab>> =
        call(getClassAndMethod()) {
            val modelList = colabRetrofitDatasource.listAll(token).getOrThrow()
            modelList.map { it.retrofitModelToEntity() }
        }

    override suspend fun check(token: String, reg: Long): Result<Boolean> =
        call(getClassAndMethod()) {
            val model = colabRetrofitDatasource.check(token, reg).getOrThrow()
            if(model.reg == 0L) {
                colabRoomDatasource.deleteByReg(reg).getOrThrow()
                return@call false
            }
            colabRoomDatasource.add(model.retrofitModelToRoomModel()).getOrThrow()
            return@call true
        }

    override suspend fun check(reg: Long): Result<Boolean> =
        call(getClassAndMethod()) {
            colabRoomDatasource.checkByReg(reg).getOrThrow()
        }

    override suspend fun getNameByReg(reg: Long): Result<String> =
        call(getClassAndMethod()) {
            colabRoomDatasource.getNameByReg(reg).getOrThrow()
        }

}