package br.com.usinasantafe.cvf.domain.repositories.variable

import br.com.usinasantafe.cvf.domain.entities.stable.Config
import br.com.usinasantafe.cvf.utils.EmptyResult

interface ConfigRepository {
    suspend fun send(entity: Config): Result<Config>
    suspend fun save(entity: Config): EmptyResult
}