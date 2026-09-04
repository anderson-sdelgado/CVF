package br.com.usinasantafe.cvf.domain.usecases.note

import br.com.usinasantafe.cvf.lib.TypeTruck
import br.com.usinasantafe.cvf.utils.call
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import javax.inject.Inject

interface CheckInvertedCart {
    suspend operator fun invoke(text: String, pos: Int, typeTruck: TypeTruck): Result<Boolean>
}

class ICheckInvertedCart @Inject constructor(

): CheckInvertedCart {

    override suspend fun invoke(text: String, pos: Int, typeTruck: TypeTruck): Result<Boolean> =
        call(getClassAndMethod()) {
            TODO("Not yet implemented")
        }

}