package br.com.usinasantafe.cvf.domain.usecases.manager

import br.com.usinasantafe.cvf.domain.repositories.variable.ManagerRepository
import br.com.usinasantafe.cvf.utils.call
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import javax.inject.Inject

interface QtdLimitCart {
    suspend operator fun invoke(): Result<Int>
}

class IQtdLimitCart @Inject constructor(
    private val managerRepository: ManagerRepository
): QtdLimitCart {

    override suspend fun invoke(): Result<Int> =
        call(getClassAndMethod()) {
            managerRepository.getQtdLimitCart().getOrThrow()
        }

}