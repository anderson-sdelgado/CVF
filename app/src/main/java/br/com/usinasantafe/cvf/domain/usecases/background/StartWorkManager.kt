package br.com.usinasantafe.cvf.domain.usecases.background

import br.com.usinasantafe.cvf.utils.call
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import javax.inject.Inject

interface StartWorkManager {
    suspend operator fun invoke(): Result<Unit>
}

class IStartWorkManager @Inject constructor(
): StartWorkManager {

    override suspend fun invoke(): Result<Unit> =
        call(getClassAndMethod()) {
            TODO("Not yet implemented")
        }

}