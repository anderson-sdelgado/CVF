package br.com.usinasantafe.cvf.domain.usecases.config

import br.com.usinasantafe.cvf.domain.repositories.variable.ConfigRepository
import br.com.usinasantafe.cvf.utils.EmptyResult
import br.com.usinasantafe.cvf.utils.call
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import javax.inject.Inject

interface SetFinishUpdateAllTable {
    suspend operator fun invoke(): EmptyResult
}

class ISetFinishUpdateAllTable @Inject constructor(
    private val configRepository: ConfigRepository
): SetFinishUpdateAllTable {

    override suspend fun invoke(): EmptyResult =
        call(getClassAndMethod()) {
            configRepository.setFlagUpdate().getOrThrow()
        }

}