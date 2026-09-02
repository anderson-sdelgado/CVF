package br.com.usinasantafe.cvf.domain.usecases.note

import br.com.usinasantafe.cvf.lib.TypeTruck
import br.com.usinasantafe.cvf.utils.call
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import javax.inject.Inject

interface GetTypeTruck {
    suspend operator fun invoke(): Result<TypeTruck>
}

class IGetTypeTruck @Inject constructor(
): GetTypeTruck {

    override suspend fun invoke(): Result<TypeTruck> =
        call(getClassAndMethod()) {
            TODO("Not yet implemented")
        }

}