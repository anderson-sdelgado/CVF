package br.com.usinasantafe.cvf.domain.usecases.note

import br.com.usinasantafe.cvf.domain.repositories.stable.EquipRepository
import br.com.usinasantafe.cvf.lib.TypeTruck
import br.com.usinasantafe.cvf.utils.ERROR_STRING_TO_INT
import br.com.usinasantafe.cvf.utils.call
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import br.com.usinasantafe.cvf.utils.tryCatch
import javax.inject.Inject

interface CheckInvertedCart {
    suspend operator fun invoke(text: String, pos: Int, typeTruck: TypeTruck): Result<Boolean>
}

class ICheckInvertedCart @Inject constructor(
    private val equipRepository: EquipRepository
): CheckInvertedCart {

    override suspend fun invoke(text: String, pos: Int, typeTruck: TypeTruck): Result<Boolean> =
        call(getClassAndMethod()) {
            val nro = tryCatch(ERROR_STRING_TO_INT) { text.toInt() }
            val cdClassOper = equipRepository.getCdClassOperByNro(nro).getOrThrow()
            if(typeTruck == TypeTruck.TRUCK) return@call cdClassOper == 21
            if(cdClassOper == 21) return@call pos != 1
            pos == 1
        }

}