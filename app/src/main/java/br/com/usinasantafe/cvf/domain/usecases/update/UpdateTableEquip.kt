package br.com.usinasantafe.cvf.domain.usecases.update

import br.com.usinasantafe.cvf.lib.LevelUpdate
import br.com.usinasantafe.cvf.lib.TB_EQUIP
import br.com.usinasantafe.cvf.utils.UiStatusStateUpdate
import br.com.usinasantafe.cvf.utils.emitProgress
import br.com.usinasantafe.cvf.utils.flowCall
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface UpdateTableEquip {
    suspend operator fun invoke(
        sizeAll: Float,
        count: Float = 1f
    ): Flow<UiStatusStateUpdate>
}

class IUpdateTableEquip @Inject constructor(
): UpdateTableEquip {

    override suspend fun invoke(
        sizeAll: Float,
        count: Float
    ): Flow<UiStatusStateUpdate> = flow {
        flowCall(getClassAndMethod()) {
            emitProgress(count, sizeAll, LevelUpdate.RECOVERY, TB_EQUIP)
            emitProgress(count, sizeAll, LevelUpdate.CLEAN, TB_EQUIP)
            emitProgress(count, sizeAll, LevelUpdate.SAVE, TB_EQUIP)
        }
    }

}