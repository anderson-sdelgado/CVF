package br.com.usinasantafe.cvf.domain.usecases.note

import br.com.usinasantafe.cvf.utils.call
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import javax.inject.Inject

interface GetNroTruck {
    suspend operator fun invoke(): Result<String?>
}

class IGetNroTruck @Inject constructor(
): GetNroTruck {

    override suspend fun invoke(): Result<String?> =
        call(getClassAndMethod()) {
            TODO("Not yet implemented")
        }

}