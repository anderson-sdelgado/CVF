package br.com.usinasantafe.cvf.domain.usecases.manager

import br.com.usinasantafe.cvf.domain.entities.stable.Front
import br.com.usinasantafe.cvf.domain.entities.stable.Release
import br.com.usinasantafe.cvf.domain.repositories.stable.FrontRepository
import br.com.usinasantafe.cvf.domain.repositories.stable.ReleaseRepository
import br.com.usinasantafe.cvf.domain.repositories.variable.ManagerRepository
import br.com.usinasantafe.cvf.infra.datasource.room.stable.FrontRoomDatasource
import br.com.usinasantafe.cvf.infra.datasource.room.stable.ReleaseRoomDatasource
import br.com.usinasantafe.cvf.infra.models.room.stable.ReleaseRoomModel
import br.com.usinasantafe.cvf.infra.models.sharedpreferences.ManagerSharedPreferencesModel
import br.com.usinasantafe.cvf.utils.call
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import com.google.gson.Gson
import javax.inject.Inject
import kotlin.collections.get

interface UpdateManager {
    suspend operator fun invoke(managerJson: String, frontJson: String, releaseJson: String): Result<Unit>
}

class IUpdateManager @Inject constructor(
    private val managerRepository: ManagerRepository,
    private val frontRepository: FrontRepository,
    private val releaseRepository: ReleaseRepository,
): UpdateManager {

    override suspend fun invoke(managerJson: String, frontJson: String, releaseJson: String): Result<Unit> =
        call(getClassAndMethod()) {
            val gson = Gson()

            val frontEntity = gson.fromJson(frontJson, Front::class.java)
            frontRepository.add(frontEntity).getOrThrow()

            val releaseEntity = gson.fromJson(releaseJson, Release::class.java)
            releaseRepository.add(releaseEntity).getOrThrow()

            val managerMap = gson.fromJson(managerJson, Map::class.java)
            val idFront = managerMap["idFront"]?.toString()?.toDoubleOrNull()?.toInt() ?: 0
            val idRelease = managerMap["idRelease"]?.toString()?.toDoubleOrNull()?.toInt() ?: 0
            val qtdLimitCart = managerMap["qtdLimitCart"]?.toString()?.toDoubleOrNull()?.toInt() ?: 0
            managerRepository.update(idFront, idRelease, qtdLimitCart).getOrThrow()

        }

}