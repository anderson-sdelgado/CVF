package br.com.usinasantafe.cvf.domain.usecases.note

import br.com.usinasantafe.cvf.domain.repositories.stable.EquipRepository
import br.com.usinasantafe.cvf.domain.repositories.variable.NoteRepository
import br.com.usinasantafe.cvf.domain.usecases.common.Token
import br.com.usinasantafe.cvf.lib.TypeEquip
import br.com.usinasantafe.cvf.utils.CheckNetwork
import br.com.usinasantafe.cvf.utils.ERROR_STRING_TO_LONG
import br.com.usinasantafe.cvf.utils.NO_CONNECTION
import br.com.usinasantafe.cvf.utils.call
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import br.com.usinasantafe.cvf.utils.handleFailure
import br.com.usinasantafe.cvf.utils.tryCatch
import java.net.SocketTimeoutException
import javax.inject.Inject

interface CheckNroCart {
    suspend operator fun invoke(text: String): Result<Boolean>
}

class ICheckNroCart @Inject constructor(
    private val token: Token,
    private val checkNetwork: CheckNetwork,
    private val equipRepository: EquipRepository,
    private val noteRepository: NoteRepository
): CheckNroCart {

    override suspend fun invoke(text: String): Result<Boolean> =
        call(getClassAndMethod()) {
            val nro = tryCatch(ERROR_STRING_TO_LONG) { text.toInt() }
            val pos = noteRepository.posCart().getOrThrow()
            if(checkNetwork.isConnected()) {
                val token = token().getOrThrow()
                return@call equipRepository.check(token, nro, TypeEquip.CART, pos).fold(
                    onSuccess = { it },
                    onFailure = {
                        if(it.cause is SocketTimeoutException) {
                            handleFailure(it, getClassAndMethod())
                            return@fold equipRepository.check(nro, TypeEquip.CART, pos).getOrThrow()
                        }
                        throw it
                    }
                )
            }
            handleFailure(NO_CONNECTION, getClassAndMethod())
            equipRepository.check(nro, TypeEquip.CART, pos).getOrThrow()
        }

}