package br.com.usinasantafe.cvf.domain.usecases.note

import br.com.usinasantafe.cvf.domain.repositories.variable.ConfigRepository
import br.com.usinasantafe.cvf.domain.repositories.variable.NoteRepository
import br.com.usinasantafe.cvf.domain.usecases.common.Token
import br.com.usinasantafe.cvf.utils.call
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import br.com.usinasantafe.cvf.utils.required
import javax.inject.Inject

interface SendNote {
    suspend operator fun invoke(): Result<Unit>
}

class ISendNote @Inject constructor(
    private val token: Token,
    private val configRepository: ConfigRepository,
    private val noteRepository: NoteRepository
): SendNote {

    override suspend fun invoke(): Result<Unit> =
        call(getClassAndMethod()) {
            val token = token().getOrThrow()
            val entity = configRepository.get().getOrThrow()
            noteRepository.send(token, entity::idServ.required()).getOrThrow()
        }

}