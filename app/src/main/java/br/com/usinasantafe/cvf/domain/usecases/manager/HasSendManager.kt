package br.com.usinasantafe.cvf.domain.usecases.manager

import br.com.usinasantafe.cvf.domain.repositories.variable.ManagerRepository
import br.com.usinasantafe.cvf.utils.call
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import javax.inject.Inject

interface HasSendManager {
    suspend operator fun invoke(): Result<Boolean>
}

class IHasSendManager @Inject constructor(
    private val managerRepository: ManagerRepository
): HasSendManager {

    override suspend fun invoke(): Result<Boolean> =
        call(getClassAndMethod()) {
            managerRepository.hasSend().getOrThrow()
        }

}