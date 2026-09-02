package br.com.usinasantafe.cvf.domain.usecases.note

import br.com.usinasantafe.cvf.utils.call
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import javax.inject.Inject

interface CheckNroCart {
    suspend operator fun invoke(text: String): Result<Boolean>
}

class ICheckNroCart @Inject constructor(
): CheckNroCart {

    override suspend fun invoke(text: String): Result<Boolean> =
        call(getClassAndMethod()) {
            TODO("Not yet implemented")
        }

}