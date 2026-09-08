package br.com.usinasantafe.cvf.domain.usecases.note

import br.com.usinasantafe.cvf.domain.repositories.stable.EquipRepository
import br.com.usinasantafe.cvf.domain.usecases.common.Token
import br.com.usinasantafe.cvf.utils.CheckNetwork
import br.com.usinasantafe.cvf.utils.ERROR_STRING_TO_INT
import br.com.usinasantafe.cvf.utils.NO_CONNECTION
import br.com.usinasantafe.cvf.utils.call
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import br.com.usinasantafe.cvf.utils.handleFailure
import br.com.usinasantafe.cvf.utils.tryCatch
import java.net.SocketTimeoutException
import javax.inject.Inject

interface HasNroTruck {
    suspend operator fun invoke(text: String): Result<Boolean>
}

class IHasNroTruck @Inject constructor(
    private val token: Token,
    private val checkNetwork: CheckNetwork,
    private val equipRepository: EquipRepository,
): HasNroTruck {

    override suspend fun invoke(text: String): Result<Boolean> =
        call(getClassAndMethod()) {
            val nro = tryCatch(ERROR_STRING_TO_INT) { text.toInt() }
            if(checkNetwork.isConnected()) {
                val token = token().getOrThrow()
                return@call equipRepository.check(token, nro).fold(
                    onSuccess = { it },
                    onFailure = {
                        if(it.cause is SocketTimeoutException) {
                            handleFailure(it, getClassAndMethod())
                            return@fold equipRepository.check(nro).getOrThrow()
                        }
                        throw it
                    }
                )
            }
            handleFailure(NO_CONNECTION, getClassAndMethod())
            equipRepository.check(nro).getOrThrow()
        }

}