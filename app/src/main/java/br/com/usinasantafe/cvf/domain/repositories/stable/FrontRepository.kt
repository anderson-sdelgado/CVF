package br.com.usinasantafe.cvf.domain.repositories.stable

import br.com.usinasantafe.cvf.domain.entities.stable.Front
import br.com.usinasantafe.cvf.utils.EmptyResult

interface FrontRepository {
    suspend fun addAll(list: List<Front>): EmptyResult
    suspend fun deleteAll(): EmptyResult
    suspend fun listAll(token: String): Result<List<Front>>
    suspend fun listAll(): Result<List<Front>>
}