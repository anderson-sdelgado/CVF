package br.com.usinasantafe.cvf.domain.repositories.variable

import br.com.usinasantafe.cvf.domain.entities.variable.Config
import br.com.usinasantafe.cvf.utils.EmptyResult

interface ConfigRepository {
    suspend fun get(): Result<Config>
    suspend fun send(entity: Config): Result<Config>
    suspend fun save(entity: Config): EmptyResult
    suspend fun has(): Result<Boolean>
    suspend fun setFlagUpdate(): EmptyResult
}