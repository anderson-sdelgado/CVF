package br.com.usinasantafe.cvf.domain.repositories.stable

import br.com.usinasantafe.cvf.domain.entities.stable.Colab
import br.com.usinasantafe.cvf.utils.EmptyResult

interface ColabRepository {
    suspend fun addAll(list: List<Colab>): EmptyResult
    suspend fun deleteAll(): EmptyResult
    suspend fun listAll(token: String): Result<List<Colab>>
}