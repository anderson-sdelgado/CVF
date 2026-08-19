package br.com.usinasantafe.cvf.domain.usecases.common

import br.com.usinasantafe.cvf.domain.repositories.variable.ConfigRepository
import br.com.usinasantafe.cvf.lib.token
import br.com.usinasantafe.cvf.utils.call
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import br.com.usinasantafe.cvf.utils.required
import br.com.usinasantafe.cvf.utils.tryCatch
import javax.inject.Inject

interface Token {
    suspend operator fun invoke(): Result<String>
}

class IToken @Inject constructor(
    private val configRepository: ConfigRepository
): Token {

    override suspend fun invoke(): Result<String> =
        call(getClassAndMethod()) {
            val entity = configRepository.get().getOrThrow()
            tryCatch("token") {
                with(entity) {
                    token(
                        idServ = ::idServ.required(),
                        number = ::number.required(),
                        version = ::version.required(),
                    )
                }
            }
        }

}