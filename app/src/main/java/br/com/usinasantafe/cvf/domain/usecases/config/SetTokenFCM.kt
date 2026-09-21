package br.com.usinasantafe.cvf.domain.usecases.config

import br.com.usinasantafe.cvf.domain.repositories.variable.ConfigRepository
import br.com.usinasantafe.cvf.domain.usecases.background.StartWorkManager
import br.com.usinasantafe.cvf.utils.EmptyResult
import br.com.usinasantafe.cvf.utils.call
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import javax.inject.Inject

interface SetTokenFCM {
    suspend operator fun invoke(token: String): EmptyResult
}

class ISetTokenFCM @Inject constructor(
    private val configRepository: ConfigRepository,
    private val startWorkManager: StartWorkManager
): SetTokenFCM {

    override suspend fun invoke(token: String): EmptyResult =
        call(getClassAndMethod()) {
            val check = configRepository.setTokenFCM(token).getOrThrow()
            if(check) startWorkManager()
        }

}
