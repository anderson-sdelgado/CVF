package br.com.usinasantafe.cvf.domain.usecases.manager

import br.com.usinasantafe.cvf.domain.repositories.variable.ManagerRepository
import br.com.usinasantafe.cvf.utils.call
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import javax.inject.Inject

interface SetIdFront {
    suspend operator fun invoke(id: Int): Result<Unit>
}

class ISetIdFront @Inject constructor(
    private val managerRepository: ManagerRepository
): SetIdFront {

    override suspend fun invoke(id: Int): Result<Unit> =
        call(getClassAndMethod()) {
            managerRepository.setIdFront(id).getOrThrow()
        }

}