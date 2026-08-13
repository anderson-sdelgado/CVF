package br.com.usinasantafe.cvf.domain.usecases.manager

import br.com.usinasantafe.cvf.domain.entities.variable.Manager
import br.com.usinasantafe.cvf.domain.repositories.variable.ManagerRepository
import br.com.usinasantafe.cvf.domain.usecases.background.StartWorkManager
import br.com.usinasantafe.cvf.utils.EmptyResult
import br.com.usinasantafe.cvf.utils.call
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import javax.inject.Inject

interface SaveManager {
    suspend operator fun invoke(
        idFront: Int,
        idRelease: Int
    ): EmptyResult
}

class ISaveManager @Inject constructor(
    private val managerRepository: ManagerRepository,
    private val startWorkManager: StartWorkManager
): SaveManager {

    override suspend fun invoke(
        idFront: Int,
        idRelease: Int
    ): EmptyResult =
        call(getClassAndMethod()) {
            val entity = Manager(idRelease, idFront)
            managerRepository.save(entity).getOrThrow()
            startWorkManager()
        }

}