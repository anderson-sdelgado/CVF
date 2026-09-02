package br.com.usinasantafe.cvf.domain.usecases.note

import br.com.usinasantafe.cvf.utils.call
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import javax.inject.Inject

interface GetNroCart {
    suspend operator fun invoke(): Result<String?>
}

class IGetNroCart @Inject constructor(
): GetNroCart {

    override suspend fun invoke(): Result<String?> =
        call(getClassAndMethod()) {
            TODO("Not yet implemented")
        }

}