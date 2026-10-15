package br.com.usinasantafe.cvf.domain.usecases.note

import br.com.usinasantafe.cvf.domain.repositories.stable.EquipRepository
import br.com.usinasantafe.cvf.domain.repositories.stable.FrontRepository
import br.com.usinasantafe.cvf.domain.repositories.stable.ReleaseRepository
import br.com.usinasantafe.cvf.domain.repositories.variable.ManagerRepository
import br.com.usinasantafe.cvf.domain.repositories.variable.NoteRepository
import br.com.usinasantafe.cvf.domain.usecases.common.Token
import br.com.usinasantafe.cvf.lib.CheckTruck
import br.com.usinasantafe.cvf.utils.CheckNetwork
import br.com.usinasantafe.cvf.utils.ERROR_STRING_TO_INT
import br.com.usinasantafe.cvf.utils.NO_CONNECTION
import br.com.usinasantafe.cvf.utils.call
import br.com.usinasantafe.cvf.utils.dateFiveTeenMinutesAgo
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import br.com.usinasantafe.cvf.utils.handleFailure
import br.com.usinasantafe.cvf.utils.required
import br.com.usinasantafe.cvf.utils.tryCatch
import java.net.SocketTimeoutException
import java.util.Date
import javax.inject.Inject

interface CheckNroTruck {
    suspend operator fun invoke(text: String): Result<CheckTruck>
}

class ICheckNroTruck @Inject constructor(
    private val token: Token,
    private val checkNetwork: CheckNetwork,
    private val equipRepository: EquipRepository,
    private val managerRepository: ManagerRepository,
    private val noteRepository: NoteRepository
): CheckNroTruck {

    override suspend fun invoke(text: String): Result<CheckTruck> =
        call(getClassAndMethod()) {
            val nro = tryCatch(ERROR_STRING_TO_INT) { text.toInt() }
            if(checkNetwork.isConnected()) {
                val token = token().getOrThrow()
                val idFront = managerRepository.getIdFront().getOrThrow().required("idFront")
                return@call equipRepository.checkNroTruck(token, nro, idFront).fold(
                    onSuccess = { entity ->
                        if(entity.id == 0) return@call CheckTruck.INVALID
                        val flagPreCEC = entity::flagPreCEC.required()
                        if(flagPreCEC) return@call CheckTruck.PRE_CEC_EXISTENT
                        if((entity.idFront == null) || (entity.idRelease == null)) return@call CheckTruck.RELEASE_AND_FRONT_INVALID
                        val idRelease = managerRepository.getIdRelease().getOrThrow().required("idRelease")
                        if((idFront != entity.idFront) || (idRelease != entity.idRelease)) return@call CheckTruck.RELEASE_AND_FRONT_INVALID
                        val dateHourExitMill = entity::dateHourExitMill.required()
                        val dateHourArrivalField = entity::dateHourArrivalField.required()
                        noteRepository.setDate(dateHourExitMill, dateHourArrivalField).getOrThrow()
                        CheckTruck.OK
                    },
                    onFailure = {
                        if(it.cause is SocketTimeoutException) {
                            handleFailure(it, getClassAndMethod())
                            val check = equipRepository.checkNroTruck(nro).getOrThrow()
                            return@fold if(check) CheckTruck.OK else CheckTruck.INVALID
                        }
                        throw it
                    }
                )
            }
            handleFailure(NO_CONNECTION, getClassAndMethod())
            val check = equipRepository.checkNroTruck(nro).getOrThrow()
            if(check) CheckTruck.OK else CheckTruck.INVALID
        }

}