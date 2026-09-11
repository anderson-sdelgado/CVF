package br.com.usinasantafe.cvf.domain.usecases.note

import br.com.usinasantafe.cvf.domain.repositories.stable.EquipRepository
import br.com.usinasantafe.cvf.domain.repositories.variable.NoteRepository
import br.com.usinasantafe.cvf.lib.TypeTruck
import br.com.usinasantafe.cvf.utils.call
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import br.com.usinasantafe.cvf.utils.required
import javax.inject.Inject

interface GetTypeTruck {
    suspend operator fun invoke(): Result<TypeTruck>
}

class IGetTypeTruck @Inject constructor(
    private val noteRepository: NoteRepository,
    private val equipRepository: EquipRepository
): GetTypeTruck {

    override suspend fun invoke(): Result<TypeTruck> =
        call(getClassAndMethod()) {
            val idTruck = noteRepository.getIdTruck().getOrThrow()
            val equip = equipRepository.getById(idTruck.required("idTruck")).getOrThrow()
            if(equip.cdOperClass == 1) TypeTruck.TRUCK else TypeTruck.HAULAGE_TRUCK
        }

}