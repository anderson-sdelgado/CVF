package br.com.usinasantafe.cvf.domain.repositories.variable

import br.com.usinasantafe.cvf.domain.entities.variable.Manager
import br.com.usinasantafe.cvf.lib.StatusSend
import br.com.usinasantafe.cvf.utils.EmptyResult
import kotlinx.coroutines.flow.Flow

interface ManagerRepository {
    suspend fun clean(): EmptyResult
    suspend fun has(): Result<Boolean>
    suspend fun getIdFront(): Result<Int?>
    suspend fun getIdRelease(): Result<Int?>
    suspend fun setIdFront(id: Int): EmptyResult
    suspend fun setIdRelease(id: Int): EmptyResult
    suspend fun hasSend(): Result<Boolean>
    suspend fun send(token: String, idServ: Int): EmptyResult
    suspend fun getStatusSend(): Result<StatusSend>
    suspend fun getQtdLimitCart(): Result<Int>
    suspend fun update(idFront: Int, idRelease: Int, qtdLimitCart: Int): EmptyResult
    fun observe(): Flow<Manager>
}
