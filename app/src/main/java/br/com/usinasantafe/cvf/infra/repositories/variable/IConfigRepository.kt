package br.com.usinasantafe.cvf.infra.repositories.variable

import br.com.usinasantafe.cvf.domain.entities.stable.Config
import br.com.usinasantafe.cvf.domain.repositories.variable.ConfigRepository
import br.com.usinasantafe.cvf.utils.EmptyResult
import javax.inject.Inject

class IConfigRepository @Inject constructor(

): ConfigRepository {

    override suspend fun send(entity: Config): Result<Config> {
        TODO("Not yet implemented")
    }

    override suspend fun save(entity: Config): EmptyResult {
        TODO("Not yet implemented")
    }

}