package br.com.usinasantafe.cvf.domain.usecases.manager

import br.com.usinasantafe.cvf.domain.repositories.variable.ManagerRepository
import br.com.usinasantafe.cvf.utils.call
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import javax.inject.Inject

interface HasManager {
    suspend operator fun invoke(): Result<Boolean>
}

class IHasManager @Inject constructor(
    private val managerRepository: ManagerRepository
): HasManager {

    override suspend fun invoke(): Result<Boolean> =
        call(getClassAndMethod()) {
            managerRepository.has().getOrThrow()
        }

}