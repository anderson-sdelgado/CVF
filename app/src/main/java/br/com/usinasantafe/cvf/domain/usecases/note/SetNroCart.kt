package br.com.usinasantafe.cvf.domain.usecases.note

import br.com.usinasantafe.cvf.utils.call
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import javax.inject.Inject

interface SetNroCart {
    suspend operator fun invoke(text: String): Result<Unit>
}

class ISetNroCart @Inject constructor(
): SetNroCart {

    override suspend fun invoke(text: String): Result<Unit> =
        call(getClassAndMethod()) {
            TODO("Not yet implemented")
        }

}