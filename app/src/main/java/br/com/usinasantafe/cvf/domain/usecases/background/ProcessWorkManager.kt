package br.com.usinasantafe.cvf.domain.usecases.background

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import br.com.usinasantafe.cvf.domain.usecases.config.GetConfig
import br.com.usinasantafe.cvf.domain.usecases.config.SetStatusSend
import br.com.usinasantafe.cvf.domain.usecases.manager.HasSendManager
import br.com.usinasantafe.cvf.domain.usecases.manager.SendManager
import br.com.usinasantafe.cvf.domain.usecases.note.HasSendNote
import br.com.usinasantafe.cvf.domain.usecases.note.SendNote
import br.com.usinasantafe.cvf.lib.StatusSend
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import br.com.usinasantafe.cvf.utils.handleFailure
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class ProcessWorkManager @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val getConfig: GetConfig,
    private val hasSendManager: HasSendManager,
    private val setStatusSend: SetStatusSend,
    private val sendManager: SendManager,
    private val hasSendNote: HasSendNote,
    private val sendNote: SendNote,
): CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {

        getConfig().getOrElse {
            handleFailure(it, getClassAndMethod())
            return Result.success()
        } ?: return Result.success()

        sendStep(hasSendManager::invoke, sendManager::invoke)?.let { return it }
        sendStep(hasSendNote::invoke, sendNote::invoke)?.let { return it }

        return Result.success()
    }

    private suspend fun sendStep(
        check: suspend () -> kotlin.Result<Boolean>,
        send: suspend () -> kotlin.Result<Unit>
    ): Result? {
        val hasData = check().getOrElse {
            handleFailure(it, getClassAndMethod())
            return Result.success()
        }

        if (hasData) {
            setStatusSend(StatusSend.SEND).onFailure { handleFailure(it, getClassAndMethod()) }
            send().onFailure {
                handleFailure(it, getClassAndMethod())
                return Result.retry()
            }
            setStatusSend(StatusSend.SENT).onFailure { handleFailure(it, getClassAndMethod()) }
        }
        return null
    }

}