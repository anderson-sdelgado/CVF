package br.com.usinasantafe.cvf.domain.usecases.note

import br.com.usinasantafe.cvf.utils.call
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import javax.inject.Inject

interface LimitQtdCart {
    suspend operator fun invoke(): Result<Int>
}

class ILimitQtdCart @Inject constructor(
): LimitQtdCart {

    override suspend fun invoke(): Result<Int> =
        call(getClassAndMethod()) {
            TODO("Not yet implemented")
        }

}