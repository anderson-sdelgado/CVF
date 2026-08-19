package br.com.usinasantafe.cvf.domain.usecases.update

import br.com.usinasantafe.cvf.domain.repositories.stable.ColabRepository
import br.com.usinasantafe.cvf.domain.usecases.common.Token
import br.com.usinasantafe.cvf.lib.LevelUpdate
import br.com.usinasantafe.cvf.lib.TB_COLAB
import br.com.usinasantafe.cvf.utils.UiStatusStateUpdate
import br.com.usinasantafe.cvf.utils.emitProgress
import br.com.usinasantafe.cvf.utils.flowCallUpdate
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface UpdateTableColab {
    suspend operator fun invoke(
        sizeAll: Float,
        count: Float = 1f
    ): Flow<UiStatusStateUpdate>
}

class IUpdateTableColab @Inject constructor(
    private val token: Token,
    private val colabRepository: ColabRepository
): UpdateTableColab {

    override suspend fun invoke(
        sizeAll: Float,
        count: Float
    ): Flow<UiStatusStateUpdate> = flow {
        flowCallUpdate(getClassAndMethod()) {

            emitProgress(count, sizeAll, LevelUpdate.RECOVERY, TB_COLAB)
            val token = token().getOrThrow()
            val entityList = colabRepository.listAll(token).getOrThrow()

            emitProgress(count, sizeAll, LevelUpdate.CLEAN, TB_COLAB)
            colabRepository.deleteAll().getOrThrow()

            emitProgress(count, sizeAll, LevelUpdate.SAVE, TB_COLAB)
            colabRepository.addAll(entityList).getOrThrow()

        }
    }

}