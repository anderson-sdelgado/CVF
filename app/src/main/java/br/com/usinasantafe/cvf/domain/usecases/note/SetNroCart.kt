package br.com.usinasantafe.cvf.domain.usecases.note

import br.com.usinasantafe.cvf.domain.entities.variable.Cart
import br.com.usinasantafe.cvf.domain.repositories.stable.EquipRepository
import br.com.usinasantafe.cvf.domain.repositories.variable.NoteRepository
import br.com.usinasantafe.cvf.utils.ERROR_STRING_TO_INT
import br.com.usinasantafe.cvf.utils.call
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import br.com.usinasantafe.cvf.utils.tryCatch
import javax.inject.Inject

interface SetNroCart {
    suspend operator fun invoke(text: String, pos: Int): Result<Unit>
}

class ISetNroCart @Inject constructor(
    private val noteRepository: NoteRepository,
    private val equipRepository: EquipRepository
): SetNroCart {

    override suspend fun invoke(text: String, pos: Int): Result<Unit> =
        call(getClassAndMethod()) {
            val nro = tryCatch(ERROR_STRING_TO_INT) { text.toInt() }
            val idCart = equipRepository.getIdByNro(nro).getOrThrow()
            val entity = Cart(position = pos, idCart = idCart)
            noteRepository.setCart(entity).getOrThrow()
        }

}