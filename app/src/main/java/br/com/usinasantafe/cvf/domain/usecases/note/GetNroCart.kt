package br.com.usinasantafe.cvf.domain.usecases.note

import br.com.usinasantafe.cvf.domain.repositories.stable.EquipRepository
import br.com.usinasantafe.cvf.domain.repositories.variable.NoteRepository
import br.com.usinasantafe.cvf.utils.call
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import javax.inject.Inject

interface GetNroCart {
    suspend operator fun invoke(pos: Int): Result<String?>
}

class IGetNroCart @Inject constructor(
    private val noteRepository: NoteRepository,
    private val equipRepository: EquipRepository
): GetNroCart {

    override suspend fun invoke(pos: Int): Result<String?> =
        call(getClassAndMethod()) {
            val list = noteRepository.cartList().getOrThrow()
            val cart = list.find { it.position == pos } ?: return@call null
            val equip = equipRepository.getById(cart.idCart).getOrThrow()
            equip.nro.toString()
        }

}