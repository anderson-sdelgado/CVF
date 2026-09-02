package br.com.usinasantafe.cvf.domain.usecases.note

import br.com.usinasantafe.cvf.utils.call
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import javax.inject.Inject

interface PosCart {
    suspend operator fun invoke(): Result<Int>
}

class IPosCart @Inject constructor(
): PosCart {

    override suspend fun invoke(): Result<Int> =
        call(getClassAndMethod()) {
            TODO("Not yet implemented")
        }

}