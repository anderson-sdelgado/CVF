package br.com.usinasantafe.cvf.infra.repositories.stable

import br.com.usinasantafe.cvf.domain.entities.stable.Equip
import br.com.usinasantafe.cvf.domain.repositories.stable.EquipRepository
import br.com.usinasantafe.cvf.infra.datasource.retrofit.stable.EquipRetrofitDatasource
import br.com.usinasantafe.cvf.infra.datasource.room.stable.EquipRoomDatasource
import br.com.usinasantafe.cvf.infra.models.retrofit.stable.retrofitModelToEntity
import br.com.usinasantafe.cvf.infra.models.retrofit.stable.retrofitModelToRoomModel
import br.com.usinasantafe.cvf.infra.models.room.stable.entityToRoomModel
import br.com.usinasantafe.cvf.infra.models.room.stable.roomModelToEntity
import br.com.usinasantafe.cvf.lib.TypeEquip
import br.com.usinasantafe.cvf.utils.EmptyResult
import br.com.usinasantafe.cvf.utils.call
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import javax.inject.Inject

class IEquipRepository @Inject constructor(
    private val equipRetrofitDatasource: EquipRetrofitDatasource,
    private val equipRoomDatasource: EquipRoomDatasource
): EquipRepository {

    override suspend fun addAll(list: List<Equip>): EmptyResult =
        call(getClassAndMethod()) {
            val roomModelList = list.map { it.entityToRoomModel() }
            equipRoomDatasource.addAll(roomModelList).getOrThrow()
        }

    override suspend fun deleteAll(): EmptyResult =
        call(getClassAndMethod()) {
            equipRoomDatasource.deleteAll().getOrThrow()
        }

    override suspend fun listAll(token: String): Result<List<Equip>> =
        call(getClassAndMethod()) {
            val modelList = equipRetrofitDatasource.listAll(token).getOrThrow()
            modelList.map { it.retrofitModelToEntity() }
        }

    override suspend fun check(token: String, nro: Int, pos: Int): Result<Boolean> =
        call(getClassAndMethod()) {
            val modelRetrofit = equipRetrofitDatasource.checkByNro(token, nro).getOrThrow()
            if(modelRetrofit.nro == 0) {
                equipRoomDatasource.deleteByNro(nro).getOrThrow()
                return@call false
            }
            val modelRoom = modelRetrofit.retrofitModelToRoomModel()
            equipRoomDatasource.add(modelRetrofit.retrofitModelToRoomModel()).getOrThrow()
            if((modelRoom.type == TypeEquip.TRUCK) && (pos == 0)) return@call true
            if((modelRoom.type == TypeEquip.CART) && (pos > 0)) return@call true
            false
        }

    override suspend fun check(nro: Int, pos: Int): Result<Boolean> =
        call(getClassAndMethod()) {
            if(!equipRoomDatasource.hasByNro(nro).getOrThrow()) return@call false
            val type = equipRoomDatasource.getTypeByNro(nro).getOrThrow()
            if((type == TypeEquip.TRUCK) && (pos == 0)) return@call true
            if((type == TypeEquip.CART) && (pos > 0)) return@call true
            false
        }

    override suspend fun getById(id: Int): Result<Equip> =
        call(getClassAndMethod()) {
            equipRoomDatasource.getById(id).getOrThrow().roomModelToEntity()
        }

    override suspend fun getIdByNro(nro: Int): Result<Int> =
        call(getClassAndMethod()) {
            equipRoomDatasource.getIdByNro(nro).getOrThrow()
        }

    override suspend fun getCdClassOperByNro(nro: Int): Result<Int> =
        call(getClassAndMethod()) {
            equipRoomDatasource.getCdClassOperByNro(nro).getOrThrow()
        }

}