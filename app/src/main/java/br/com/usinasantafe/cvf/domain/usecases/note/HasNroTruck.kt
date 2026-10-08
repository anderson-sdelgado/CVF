package br.com.usinasantafe.cvf.domain.usecases.note

import br.com.usinasantafe.cvf.domain.entities.stable.Front
import br.com.usinasantafe.cvf.domain.entities.stable.Release
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
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import br.com.usinasantafe.cvf.utils.handleFailure
import br.com.usinasantafe.cvf.utils.required
import br.com.usinasantafe.cvf.utils.tryCatch
import java.net.SocketTimeoutException
import javax.inject.Inject

interface HasNroTruck {
    suspend operator fun invoke(text: String): Result<CheckTruck>
}

class IHasNroTruck @Inject constructor(
    private val token: Token,
    private val checkNetwork: CheckNetwork,
    private val equipRepository: EquipRepository,
    private val managerRepository: ManagerRepository,
    private val frontRepository: FrontRepository,
    private val releaseRepository: ReleaseRepository,
    private val noteRepository: NoteRepository
): HasNroTruck {

    override suspend fun invoke(text: String): Result<CheckTruck> =
        call(getClassAndMethod()) {
            val nro = tryCatch(ERROR_STRING_TO_INT) { text.toInt() }
            if(checkNetwork.isConnected()) {
                val token = token().getOrThrow()
                return@call equipRepository.checkNroTruck(token, nro).fold(
                    onSuccess = { entity ->
                        if(entity.id == 0) return@call CheckTruck.INVALID
                        val dateHourExitMill = entity::dateHourExitMill.required()
                        val dateHourArrivalField = entity::dateHourArrivalField.required()
                        noteRepository.setDate(dateHourExitMill, dateHourArrivalField).getOrThrow()
                        if(entity.idFront == null) return@call CheckTruck.RELEASE_AND_FRONT_INVALID
                        val idFrontWS = entity::idFront.required()
                        val idReleaseWS = entity::idRelease.required()
                        val idFront = managerRepository.getIdFront().getOrThrow()
                        val idRelease = managerRepository.getIdRelease().getOrThrow()
                        if(idFront != idFrontWS || idRelease != idReleaseWS) return@call CheckTruck.RELEASE_AND_FRONT_INVALID
//                        managerRepository.update(idFrontWS, idReleaseWS).getOrThrow()
//                        val front = Front(
//                            id = idFrontWS,
//                            cd = entity::cdFront.required(),
//                            description = entity::descriptionFront.required()
//                        )
//                        frontRepository.add(front).getOrThrow()
//                        val release = Release(
//                            id = idReleaseWS,
//                            nroOS = entity::nroOSRelease.required(),
//                            idPropAgr = entity::idPropAgrRelease.required(),
//                            descPropAgr = entity::descPropAgrRelease.required(),
//                            idFront = idFrontWS
//                        )
//                        releaseRepository.add(release).getOrThrow()
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