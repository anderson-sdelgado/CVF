package br.com.usinasantafe.cvf.domain.repositories.variable

import br.com.usinasantafe.cvf.domain.entities.variable.Manager
import br.com.usinasantafe.cvf.utils.EmptyResult

interface ManagerRepository {
    suspend fun clean(): EmptyResult
    suspend fun has(): Result<Boolean>
    suspend fun getIdFront(): Result<Int?>
    suspend fun getIdRelease(): Result<Int?>
    suspend fun save(entity: Manager): EmptyResult
    suspend fun hasSend(): Result<Boolean>
    suspend fun send(token: String, idServ: Int): EmptyResult
}