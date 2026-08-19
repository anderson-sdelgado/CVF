package br.com.usinasantafe.cvf.domain.usecases.background

import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.jvm.java

interface StartWorkManager {
    suspend operator fun invoke()
}

class IStartWorkManager @Inject constructor(
    private val workManager: WorkManager
): StartWorkManager {

    override suspend fun invoke() {
        val constraints = Constraints
            .Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val workRequest = OneTimeWorkRequest
            .Builder(ProcessWorkManager::class.java)
            .setConstraints(constraints)
            .setBackoffCriteria(
                BackoffPolicy.LINEAR,
                2, TimeUnit.MINUTES
            )
            .build()
        workManager.enqueueUniqueWork("WORK-MANAGER-CVF", ExistingWorkPolicy.REPLACE, workRequest)
    }

}