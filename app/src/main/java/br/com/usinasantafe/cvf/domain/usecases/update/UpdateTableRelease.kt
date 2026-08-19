package br.com.usinasantafe.cvf.domain.usecases.update

import br.com.usinasantafe.cvf.domain.repositories.stable.ReleaseRepository
import br.com.usinasantafe.cvf.domain.usecases.common.Token
import br.com.usinasantafe.cvf.lib.LevelUpdate
import br.com.usinasantafe.cvf.lib.TB_RELEASE
import br.com.usinasantafe.cvf.utils.UiStatusStateUpdate
import br.com.usinasantafe.cvf.utils.emitProgress
import br.com.usinasantafe.cvf.utils.flowCallUpdate
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface UpdateTableRelease {
    suspend operator fun invoke(
        sizeAll: Float,
        count: Float = 1f
    ): Flow<UiStatusStateUpdate>
}

class IUpdateTableRelease @Inject constructor(
    private val token: Token,
    private val releaseRepository: ReleaseRepository
): UpdateTableRelease {

    override suspend fun invoke(
        sizeAll: Float,
        count: Float
    ): Flow<UiStatusStateUpdate> = flow {
        flowCallUpdate(getClassAndMethod()) {

            emitProgress(count, sizeAll, LevelUpdate.RECOVERY, TB_RELEASE)
            val token = token().getOrThrow()
            val entityList = releaseRepository.listAll(token).getOrThrow()

            emitProgress(count, sizeAll, LevelUpdate.CLEAN, TB_RELEASE)
            releaseRepository.deleteAll().getOrThrow()

            emitProgress(count, sizeAll, LevelUpdate.SAVE, TB_RELEASE)
            releaseRepository.addAll(entityList).getOrThrow()

        }
    }

}