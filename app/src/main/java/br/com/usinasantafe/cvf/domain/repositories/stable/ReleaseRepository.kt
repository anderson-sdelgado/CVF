package br.com.usinasantafe.cvf.domain.repositories.stable

import br.com.usinasantafe.cvf.domain.entities.stable.Release
import br.com.usinasantafe.cvf.utils.EmptyResult

interface ReleaseRepository {
    suspend fun addAll(list: List<Release>): EmptyResult
    suspend fun deleteAll(): EmptyResult
    suspend fun listAll(token: String): Result<List<Release>>
    suspend fun listByIdFront(idFront: Int): Result<List<Release>>
}