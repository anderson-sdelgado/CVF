package br.com.usinasantafe.cvf.domain.usecases.manager

import br.com.usinasantafe.cvf.domain.repositories.variable.ManagerRepository
import br.com.usinasantafe.cvf.domain.usecases.background.StartWorkManager
import br.com.usinasantafe.cvf.utils.EmptyResult
import br.com.usinasantafe.cvf.utils.call
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import javax.inject.Inject

interface SetIdRelease {
    suspend operator fun invoke(
        idRelease: Int
    ): EmptyResult
}

class ISetIdRelease @Inject constructor(
    private val managerRepository: ManagerRepository,
    private val startWorkManager: StartWorkManager
): SetIdRelease {

    override suspend fun invoke(
        idRelease: Int
    ): EmptyResult =
        call(getClassAndMethod()) {
            managerRepository.setIdRelease(idRelease).getOrThrow()
            startWorkManager()
        }

}