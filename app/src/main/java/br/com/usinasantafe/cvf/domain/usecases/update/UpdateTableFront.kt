package br.com.usinasantafe.cvf.domain.usecases.update

import br.com.usinasantafe.cvf.domain.repositories.stable.FrontRepository
import br.com.usinasantafe.cvf.domain.usecases.common.Token
import br.com.usinasantafe.cvf.lib.LevelUpdate
import br.com.usinasantafe.cvf.lib.TB_FRONT
import br.com.usinasantafe.cvf.utils.UiStatusStateUpdate
import br.com.usinasantafe.cvf.utils.emitProgress
import br.com.usinasantafe.cvf.utils.flowCallUpdate
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface UpdateTableFront {
    suspend operator fun invoke(
        sizeAll: Float,
        count: Float = 1f
    ): Flow<UiStatusStateUpdate>
}

class IUpdateTableFront @Inject constructor(
    private val token: Token,
    private val frontRepository: FrontRepository
): UpdateTableFront {


    override suspend fun invoke(
        sizeAll: Float,
        count: Float
    ): Flow<UiStatusStateUpdate> = flow {
        flowCallUpdate(getClassAndMethod()) {

            emitProgress(count, sizeAll, LevelUpdate.RECOVERY, TB_FRONT)
            val token = token().getOrThrow()
            val entityList = frontRepository.listAll(token).getOrThrow()

            emitProgress(count, sizeAll, LevelUpdate.CLEAN, TB_FRONT)
            frontRepository.deleteAll().getOrThrow()

            emitProgress(count, sizeAll, LevelUpdate.SAVE, TB_FRONT)
            frontRepository.addAll(entityList).getOrThrow()

        }
    }

}