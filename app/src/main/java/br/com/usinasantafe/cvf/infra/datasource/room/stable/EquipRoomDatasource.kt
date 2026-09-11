package br.com.usinasantafe.cvf.infra.datasource.room.stable

import br.com.usinasantafe.cvf.infra.models.room.stable.EquipRoomModel
import br.com.usinasantafe.cvf.lib.TypeEquip
import br.com.usinasantafe.cvf.utils.EmptyResult

interface EquipRoomDatasource {
    suspend fun addAll(list: List<EquipRoomModel>): EmptyResult
    suspend fun deleteAll(): EmptyResult
    suspend fun deleteByNro(nro: Int): EmptyResult
    suspend fun add(model: EquipRoomModel): EmptyResult
    suspend fun hasByNro(nro: Int): Result<Boolean>
    suspend fun getTypeByNro(nro: Int): Result<TypeEquip>
    suspend fun getById(id: Int): Result<EquipRoomModel>
    suspend fun getIdByNro(nro: Int): Result<Int>
    suspend fun getCdClassOperByNro(nro: Int): Result<Int>
}