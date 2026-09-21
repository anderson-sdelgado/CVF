package br.com.usinasantafe.cvf.domain.usecases.background

import android.util.Log
import br.com.usinasantafe.cvf.domain.usecases.config.SetTokenFCM
import br.com.usinasantafe.cvf.domain.usecases.manager.UpdateManager
import br.com.usinasantafe.cvf.infra.datasource.room.stable.FrontRoomDatasource
import br.com.usinasantafe.cvf.infra.datasource.room.stable.ReleaseRoomDatasource
import br.com.usinasantafe.cvf.infra.datasource.sharedpreferences.ConfigSharedPreferencesDatasource
import br.com.usinasantafe.cvf.infra.datasource.sharedpreferences.ManagerSharedPreferencesDatasource
import br.com.usinasantafe.cvf.infra.models.room.stable.FrontRoomModel
import br.com.usinasantafe.cvf.infra.models.room.stable.ReleaseRoomModel
import br.com.usinasantafe.cvf.infra.models.sharedpreferences.ManagerSharedPreferencesModel
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import br.com.usinasantafe.cvf.utils.handleFailure
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CVFFirebaseMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var setTokenFCM: SetTokenFCM

    @Inject
    lateinit var updateManager: UpdateManager

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        scope.launch { setTokenFCM(token).getOrThrow() }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        if (remoteMessage.data.isNotEmpty()) {
            val managerJson = remoteMessage.data["manager"]!!
            val frontJson = remoteMessage.data["front"]!!
            val releaseJson = remoteMessage.data["release"]!!

            scope.launch {
                try {
                    updateManager(managerJson, frontJson, releaseJson).getOrThrow()
                } catch (e: Exception) {
                    handleFailure(e, getClassAndMethod())
                }
            }
        }
    }

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }
}
