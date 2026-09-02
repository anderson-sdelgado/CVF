package br.com.usinasantafe.cvf.domain.usecases.note

import br.com.usinasantafe.cvf.utils.call
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import javax.inject.Inject

interface GetDescReview {
    suspend operator fun invoke(): Result<String>
}

class IGetDescReview @Inject constructor(
): GetDescReview {

    override suspend fun invoke(): Result<String> =
        call(getClassAndMethod()) {
            TODO("Not yet implemented")
        }

}