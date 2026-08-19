package br.com.usinasantafe.cvf.domain.usecases.manager

import br.com.usinasantafe.cvf.domain.repositories.variable.ConfigRepository
import br.com.usinasantafe.cvf.domain.repositories.variable.ManagerRepository
import br.com.usinasantafe.cvf.domain.usecases.common.Token
import br.com.usinasantafe.cvf.utils.call
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import br.com.usinasantafe.cvf.utils.required
import javax.inject.Inject

interface SendManager {
    suspend operator fun invoke(): Result<Unit>
}

class ISendManager @Inject constructor(
    private val token: Token,
    private val configRepository: ConfigRepository,
    private val managerRepository: ManagerRepository
): SendManager {

    override suspend fun invoke(): Result<Unit> =
        call(getClassAndMethod()) {
            val token = token().getOrThrow()
            val entity = configRepository.get().getOrThrow()
            managerRepository.send(token, entity::idServ.required()).getOrThrow()
        }

}