package br.com.usinasantafe.cvf.domain.usecases.note

import br.com.usinasantafe.cvf.domain.repositories.variable.NoteRepository
import br.com.usinasantafe.cvf.utils.call
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import javax.inject.Inject

interface PosCart {
    suspend operator fun invoke(): Result<Int>
}

class IPosCart @Inject constructor(
    private val noteRepository: NoteRepository
): PosCart {

    override suspend fun invoke(): Result<Int> =
        call(getClassAndMethod()) {
            val list = noteRepository.cartList().getOrThrow()
            if(list.isEmpty()) return@call 1
            list.maxOf { it.position } + 1
        }

}