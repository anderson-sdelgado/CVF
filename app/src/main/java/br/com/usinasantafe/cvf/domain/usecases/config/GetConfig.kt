package br.com.usinasantafe.cvf.domain.usecases.config

import br.com.usinasantafe.cvf.domain.repositories.variable.ConfigRepository
import br.com.usinasantafe.cvf.presenter.model.ConfigScreenModel
import br.com.usinasantafe.cvf.presenter.model.toConfigModel
import br.com.usinasantafe.cvf.utils.call
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import javax.inject.Inject
import kotlin.text.get

interface GetConfig {
    suspend operator fun invoke(): Result<ConfigScreenModel?>
}

class IGetConfig @Inject constructor(
    private val configRepository: ConfigRepository
): GetConfig {

    override suspend fun invoke(): Result<ConfigScreenModel?> =
        call(getClassAndMethod()) {
            val hasConfig = configRepository.has().getOrThrow()
            if (!hasConfig) return@call null
            val config = configRepository.get().getOrThrow()
            config.toConfigModel()
        }

}