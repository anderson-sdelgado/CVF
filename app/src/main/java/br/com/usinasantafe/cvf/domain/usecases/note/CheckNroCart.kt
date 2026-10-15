package br.com.usinasantafe.cvf.domain.usecases.note

import br.com.usinasantafe.cvf.domain.repositories.stable.EquipRepository
import br.com.usinasantafe.cvf.domain.usecases.common.Token
import br.com.usinasantafe.cvf.lib.CheckCart
import br.com.usinasantafe.cvf.lib.CheckTruck
import br.com.usinasantafe.cvf.utils.CheckNetwork
import br.com.usinasantafe.cvf.utils.ERROR_STRING_TO_INT
import br.com.usinasantafe.cvf.utils.NO_CONNECTION
import br.com.usinasantafe.cvf.utils.call
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import br.com.usinasantafe.cvf.utils.handleFailure
import br.com.usinasantafe.cvf.utils.required
import br.com.usinasantafe.cvf.utils.tryCatch
import java.net.SocketTimeoutException
import javax.inject.Inject

interface CheckNroCart {
    suspend operator fun invoke(text: String, pos: Int): Result<CheckCart>
}

class ICheckNroCart @Inject constructor(
    private val token: Token,
    private val checkNetwork: CheckNetwork,
    private val equipRepository: EquipRepository
): CheckNroCart {

    override suspend fun invoke(text: String, pos: Int): Result<CheckCart> =
        call(getClassAndMethod()) {
            val nro = tryCatch(ERROR_STRING_TO_INT) { text.toInt() }
            if(checkNetwork.isConnected()) {
                val token = token().getOrThrow()
                return@call equipRepository.checkNroCart(token, nro, pos).fold(
                    onSuccess = { entity ->
                        if(entity.id == 0) return@call CheckCart.INVALID
                        val flagPreCEC = entity.flagPreCEC.required("flagPreCEC")
                        if(!flagPreCEC) return@call CheckCart.PRE_CEC_EXISTENT
                        val flagMilling = entity.flagMilling.required("flagMilling")
                        if(!flagMilling) return@call CheckCart.PENDING_WEIGHING
                        CheckCart.OK
                    },
                    onFailure = {
                        if(it.cause is SocketTimeoutException) {
                            handleFailure(it, getClassAndMethod())
                            val check = equipRepository.checkNroCart(nro, pos).getOrThrow()
                            return@fold if(check) CheckCart.OK else CheckCart.INVALID
                        }
                        throw it
                    }
                )
            }
            handleFailure(NO_CONNECTION, getClassAndMethod())
            val check = equipRepository.checkNroCart(nro, pos).getOrThrow()
            if(check) CheckCart.OK else CheckCart.INVALID
        }

}