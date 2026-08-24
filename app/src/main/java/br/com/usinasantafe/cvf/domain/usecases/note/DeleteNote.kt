package br.com.usinasantafe.cvf.domain.usecases.note

import br.com.usinasantafe.cvf.domain.repositories.variable.NoteRepository
import br.com.usinasantafe.cvf.utils.EmptyResult
import br.com.usinasantafe.cvf.utils.call
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import javax.inject.Inject

interface DeleteNote {
    suspend operator fun invoke(): EmptyResult
}

class IDeleteNote @Inject constructor(
    private val noteRepository: NoteRepository
): DeleteNote {

    override suspend fun invoke(): EmptyResult =
        call(getClassAndMethod()) {
            noteRepository.deleteNote().getOrThrow()
        }

}