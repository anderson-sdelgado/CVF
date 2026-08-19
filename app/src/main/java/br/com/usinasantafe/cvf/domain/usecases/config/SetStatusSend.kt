package br.com.usinasantafe.cvf.domain.usecases.config

import br.com.usinasantafe.cvf.domain.repositories.variable.ConfigRepository
import br.com.usinasantafe.cvf.lib.StatusSend
import br.com.usinasantafe.cvf.utils.EmptyResult
import br.com.usinasantafe.cvf.utils.call
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import javax.inject.Inject

interface SetStatusSend {
    suspend operator fun invoke(statusSend: StatusSend): EmptyResult
}

class ISetStatusSend @Inject constructor(
    private val configRepository: ConfigRepository
): SetStatusSend {

    override suspend fun invoke(statusSend: StatusSend): EmptyResult =
        call(getClassAndMethod()) {
            configRepository.setStatusSend(statusSend).getOrThrow()
        }

}