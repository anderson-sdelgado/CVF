package br.com.usinasantafe.cvf.domain.usecases.config

import br.com.usinasantafe.cvf.utils.call
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import javax.inject.Inject

interface SetFinishUpdateAllTable {
    suspend operator fun invoke(): Result<Unit>
}

class ISetFinishUpdateAllTable @Inject constructor(
): SetFinishUpdateAllTable {

    override suspend fun invoke(): Result<Unit> =
        call(getClassAndMethod()) {
            TODO("Not yet implemented")
        }

}