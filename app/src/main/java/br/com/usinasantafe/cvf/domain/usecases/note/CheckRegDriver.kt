package br.com.usinasantafe.cvf.domain.usecases.note

import br.com.usinasantafe.cvf.domain.repositories.stable.ColabRepository
import br.com.usinasantafe.cvf.domain.usecases.common.Token
import br.com.usinasantafe.cvf.utils.CheckNetwork
import br.com.usinasantafe.cvf.utils.ERROR_STRING_TO_LONG
import br.com.usinasantafe.cvf.utils.NO_CONNECTION
import br.com.usinasantafe.cvf.utils.call
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import br.com.usinasantafe.cvf.utils.handleFailure
import br.com.usinasantafe.cvf.utils.tryCatch
import java.io.IOException
import javax.inject.Inject

interface CheckRegDriver {
    suspend operator fun invoke(text: String): Result<Boolean>
}

class ICheckRegDriver @Inject constructor(
    private val token: Token,
    private val checkNetwork: CheckNetwork,
    private val colabRepository: ColabRepository
): CheckRegDriver {

    override suspend fun invoke(text: String): Result<Boolean> =
        call(getClassAndMethod()) {
            val reg = tryCatch(ERROR_STRING_TO_LONG) { text.toLong() }
            if(checkNetwork.isConnected()) {
                val token = token().getOrThrow()
                return@call colabRepository.check(token, reg).fold(
                    onSuccess = { it },
                    onFailure = {
                        if(it.cause is IOException) {
                            handleFailure(it, getClassAndMethod())
                            return@fold colabRepository.check(reg).getOrThrow()
                        }
                        throw it
                    }
                )
            }
            handleFailure(NO_CONNECTION, getClassAndMethod())
            colabRepository.check(reg).getOrThrow()
        }

}