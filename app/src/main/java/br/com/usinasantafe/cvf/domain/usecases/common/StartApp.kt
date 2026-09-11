package br.com.usinasantafe.cvf.domain.usecases.common

import br.com.usinasantafe.cvf.domain.repositories.variable.ConfigRepository
import br.com.usinasantafe.cvf.domain.repositories.variable.ManagerRepository
import br.com.usinasantafe.cvf.domain.repositories.variable.NoteRepository
import br.com.usinasantafe.cvf.lib.FlowApp
import br.com.usinasantafe.cvf.lib.StatusSend
import br.com.usinasantafe.cvf.utils.call
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import javax.inject.Inject

interface StartApp {
    suspend operator fun invoke(): Result<FlowApp>
}

class IStartApp @Inject constructor(
    private val configRepository: ConfigRepository,
    private val managerRepository: ManagerRepository,
    private val noteRepository: NoteRepository
): StartApp {

    override suspend fun invoke(): Result<FlowApp> =
        call(getClassAndMethod()) {
            if(!configRepository.getFlagUpdate().getOrThrow()) return@call FlowApp.CONFIG
            if(!managerRepository.has().getOrThrow()) return@call FlowApp.FRONT
            if(managerRepository.getStatusSend().getOrThrow() == StatusSend.STARTED) return@call FlowApp.RELEASE
            noteRepository.clean().getOrThrow()
            FlowApp.NOTE
        }

}