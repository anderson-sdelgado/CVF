package br.com.usinasantafe.cvf.domain.usecases.note

import br.com.usinasantafe.cvf.domain.repositories.variable.NoteRepository
import br.com.usinasantafe.cvf.domain.usecases.background.StartWorkManager
import br.com.usinasantafe.cvf.utils.call
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import javax.inject.Inject

interface FinishNote {
    suspend operator fun invoke(): Result<Unit>
}

class IFinishNote @Inject constructor(
    private val noteRepository: NoteRepository,
    private val startWorkManager: StartWorkManager
): FinishNote {

    override suspend fun invoke(): Result<Unit> =
        call(getClassAndMethod()) {
            noteRepository.finish().getOrThrow()
            startWorkManager()
        }

}