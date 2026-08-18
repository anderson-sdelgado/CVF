package br.com.usinasantafe.cvf.domain.usecases.note

import br.com.usinasantafe.cvf.utils.call
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import javax.inject.Inject

interface SetDriver {
    suspend operator fun invoke(): Result<Unit>
}

class ISetDriver @Inject constructor(
): SetDriver {

    override suspend fun invoke(): Result<Unit> =
        call(getClassAndMethod()) {
            TODO("Not yet implemented")
        }

}