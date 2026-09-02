package br.com.usinasantafe.cvf.domain.repositories.stable

import br.com.usinasantafe.cvf.domain.entities.stable.Equip
import br.com.usinasantafe.cvf.lib.TypeEquip
import br.com.usinasantafe.cvf.utils.EmptyResult

interface EquipRepository {
    suspend fun addAll(list: List<Equip>): EmptyResult
    suspend fun deleteAll(): EmptyResult
    suspend fun listAll(token: String): Result<List<Equip>>
    suspend fun check(token: String, nro: Int, type: TypeEquip, pos: Int): Result<Boolean>
    suspend fun check(nro: Int, type: TypeEquip, pos: Int): Result<Boolean>
}