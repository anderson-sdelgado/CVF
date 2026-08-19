package br.com.usinasantafe.cvf.domain.usecases.note

import br.com.usinasantafe.cvf.domain.repositories.variable.NoteRepository
import br.com.usinasantafe.cvf.utils.call
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import javax.inject.Inject

interface GetRegDriver {
    suspend operator fun invoke(): Result<Long>
}

class IGetRegDriver @Inject constructor(
    private val noteRepository: NoteRepository
): GetRegDriver {

    override suspend fun invoke(): Result<Long> =
        call(getClassAndMethod()) {
            noteRepository.getRegDriver().getOrThrow()
        }

}