package br.com.usinasantafe.cvf.domain.usecases.manager

import br.com.usinasantafe.cvf.domain.repositories.variable.ManagerRepository
import br.com.usinasantafe.cvf.lib.StatusSend
import br.com.usinasantafe.cvf.utils.call
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import javax.inject.Inject

interface CheckStatusManager {
    suspend operator fun invoke(): Result<Boolean>
}

class ICheckStatusManager @Inject constructor(
    private val managerRepository: ManagerRepository
): CheckStatusManager {

    override suspend fun invoke(): Result<Boolean> =
        call(getClassAndMethod()) {
            managerRepository.getStatusSend().getOrThrow() != StatusSend.STARTED
        }

}