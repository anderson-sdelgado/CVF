package br.com.usinasantafe.cvf.domain.repositories.stable

import br.com.usinasantafe.cvf.domain.entities.stable.Equip
import br.com.usinasantafe.cvf.utils.EmptyResult

interface EquipRepository {
    suspend fun addAll(list: List<Equip>): EmptyResult
    suspend fun deleteAll(): EmptyResult
    suspend fun listAll(token: String): Result<List<Equip>>
    suspend fun checkNroCart(token: String, nro: Int, pos: Int): Result<Equip>
    suspend fun checkNroTruck(token: String, nro: Int, idFront: Int): Result<Equip>
    suspend fun checkNroCart(nro: Int, pos: Int): Result<Boolean>
    suspend fun checkNroTruck(nro: Int): Result<Boolean>
    suspend fun getById(id: Int): Result<Equip>
    suspend fun getIdByNro(nro: Int): Result<Int>
    suspend fun getCdClassOperByNro(nro: Int): Result<Int>
}