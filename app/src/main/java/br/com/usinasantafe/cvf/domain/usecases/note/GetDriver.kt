package br.com.usinasantafe.cvf.domain.usecases.note

import br.com.usinasantafe.cvf.utils.call
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import javax.inject.Inject

interface GetDriver {
    suspend operator fun invoke(): Result<Unit>
}

class IGetDriver @Inject constructor(
): GetDriver {

    override suspend fun invoke(): Result<Unit> =
        call(getClassAndMethod()) {
            TODO("Not yet implemented")
        }

}