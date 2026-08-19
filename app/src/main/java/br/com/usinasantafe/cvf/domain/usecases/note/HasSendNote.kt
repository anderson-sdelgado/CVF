package br.com.usinasantafe.cvf.domain.usecases.note

import br.com.usinasantafe.cvf.domain.repositories.variable.NoteRepository
import br.com.usinasantafe.cvf.utils.call
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import javax.inject.Inject

interface HasSendNote {
    suspend operator fun invoke(): Result<Boolean>
}

class IHasSendNote @Inject constructor(
    private val noteRepository: NoteRepository
): HasSendNote {

    override suspend fun invoke(): Result<Boolean> =
        call(getClassAndMethod()) {
            noteRepository.hasSend().getOrThrow()
        }

}