package br.com.usinasantafe.cvf.domain.usecases.note

import br.com.usinasantafe.cvf.domain.repositories.stable.EquipRepository
import br.com.usinasantafe.cvf.domain.repositories.variable.NoteRepository
import br.com.usinasantafe.cvf.utils.ERROR_STRING_TO_INT
import br.com.usinasantafe.cvf.utils.call
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import br.com.usinasantafe.cvf.utils.tryCatch
import javax.inject.Inject

interface CheckRepeatedCart {
    suspend operator fun invoke(text: String): Result<Boolean>
}

class ICheckRepeatedCart @Inject constructor(
    private val noteRepository: NoteRepository,
    private val equipRepository: EquipRepository
): CheckRepeatedCart {

    override suspend fun invoke(text: String): Result<Boolean> =
        call(getClassAndMethod()) {
            val nro = tryCatch(ERROR_STRING_TO_INT) { text.toInt() }
            val id = equipRepository.getIdByNro(nro).getOrThrow()
            val list = noteRepository.cartList().getOrThrow()
            list.any { it.idCart == id }
        }

}