package br.com.usinasantafe.cvf.domain.usecases.note

import br.com.usinasantafe.cvf.domain.repositories.stable.EquipRepository
import br.com.usinasantafe.cvf.domain.repositories.variable.NoteRepository
import br.com.usinasantafe.cvf.utils.call
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import javax.inject.Inject

interface GetNroTruck {
    suspend operator fun invoke(): Result<String?>
}

class IGetNroTruck @Inject constructor(
    private val noteRepository: NoteRepository,
    private val equipRepository: EquipRepository
): GetNroTruck {

    override suspend fun invoke(): Result<String?> =
        call(getClassAndMethod()) {
            val idTruck = noteRepository.getIdTruck().getOrThrow() ?: return@call null
            val equip = equipRepository.getById(idTruck).getOrThrow()
            equip.nro.toString()
        }

}