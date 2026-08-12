package br.com.usinasantafe.cvf.domain.repositories.variable

import br.com.usinasantafe.cvf.utils.EmptyResult

interface ManagerRepository {
    suspend fun clean(): EmptyResult
    suspend fun has(): Result<Boolean>
    suspend fun getIdFront(): Result<Int?>
}