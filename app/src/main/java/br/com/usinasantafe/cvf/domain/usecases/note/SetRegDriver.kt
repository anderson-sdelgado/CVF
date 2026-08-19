package br.com.usinasantafe.cvf.domain.usecases.note

import br.com.usinasantafe.cvf.domain.repositories.variable.NoteRepository
import br.com.usinasantafe.cvf.utils.ERROR_STRING_TO_LONG
import br.com.usinasantafe.cvf.utils.call
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import br.com.usinasantafe.cvf.utils.tryCatch
import javax.inject.Inject

interface SetRegDriver {
    suspend operator fun invoke(text: String): Result<Unit>
}

class ISetRegDriver @Inject constructor(
    private val noteRepository: NoteRepository
): SetRegDriver {

    override suspend fun invoke(text: String): Result<Unit> =
        call(getClassAndMethod()) {
            val reg = tryCatch(ERROR_STRING_TO_LONG) { text.toLong() }
            noteRepository.setRegDriver(reg).getOrThrow()
        }

}