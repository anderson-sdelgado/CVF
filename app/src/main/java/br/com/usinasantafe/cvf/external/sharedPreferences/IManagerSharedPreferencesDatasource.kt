package br.com.usinasantafe.cvf.external.sharedPreferences

import android.content.SharedPreferences
import androidx.core.content.edit
import br.com.usinasantafe.cvf.infra.datasource.sharedpreferences.ManagerSharedPreferencesDatasource
import br.com.usinasantafe.cvf.infra.models.sharedpreferences.ManagerSharedPreferencesModel
import br.com.usinasantafe.cvf.lib.BASE_SHARED_PREFERENCES_TABLE_MANAGER
import br.com.usinasantafe.cvf.lib.StatusSend
import br.com.usinasantafe.cvf.utils.EmptyResult
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import br.com.usinasantafe.cvf.utils.required
import br.com.usinasantafe.cvf.utils.result
import com.google.gson.Gson
import javax.inject.Inject

class IManagerSharedPreferencesDatasource @Inject constructor(
    private val sharedPreferences: SharedPreferences
): ManagerSharedPreferencesDatasource {

    suspend fun save(model: ManagerSharedPreferencesModel): EmptyResult =
        result(getClassAndMethod()) {
            sharedPreferences.edit {
                putString(
                    BASE_SHARED_PREFERENCES_TABLE_MANAGER,
                    Gson().toJson(model)
                )
            }
        }

    override suspend fun hasSend(): Result<Boolean> =
        result(getClassAndMethod()) {
            if(!has().getOrThrow()) return@result false
            val model = get().getOrThrow()
            model.statusSend == StatusSend.SEND
        }

    override suspend fun clean(): EmptyResult =
        result(getClassAndMethod()) {
            sharedPreferences.edit {
                remove(BASE_SHARED_PREFERENCES_TABLE_MANAGER)
            }
        }

    override suspend fun has(): Result<Boolean> =
        result(getClassAndMethod()) {
            val data = sharedPreferences.getString(
                BASE_SHARED_PREFERENCES_TABLE_MANAGER,
                null
            )
            !data.isNullOrEmpty()
        }

    override suspend fun getIdFront(): Result<Int?> =
        result(getClassAndMethod()) {
            if (!has().getOrThrow()) return@result null
            get().getOrThrow().idFront
        }

    override suspend fun getIdRelease(): Result<Int?> =
        result(getClassAndMethod()) {
            if (!has().getOrThrow()) return@result null
            get().getOrThrow().idRelease
        }

    override suspend fun setIdFront(id: Int): EmptyResult =
        result(getClassAndMethod()) {
            val model = get().getOrThrow()
            model.idFront = id
            model.statusSend = StatusSend.STARTED
            save(model).getOrThrow()
        }

    override suspend fun setIdRelease(id: Int): EmptyResult =
        result(getClassAndMethod()) {
            val model = get().getOrThrow()
            model.idRelease = id
            model.statusSend = StatusSend.SEND
            save(model).getOrThrow()
        }

    override suspend fun get(): Result<ManagerSharedPreferencesModel> =
        result(getClassAndMethod()) {
            val data = sharedPreferences.getString(
                BASE_SHARED_PREFERENCES_TABLE_MANAGER,
                null
            )
            if(data.isNullOrEmpty()) return@result ManagerSharedPreferencesModel()
            val model = Gson().fromJson(
                data,
                ManagerSharedPreferencesModel::class.java
            )
            model
        }

    override suspend fun setStatusSend(statusSend: StatusSend): EmptyResult =
        result(getClassAndMethod()) {
            val model = get().getOrThrow()
            model.statusSend = statusSend
            save(model).getOrThrow()
        }

    override suspend fun getStatusSend(): Result<StatusSend> =
        result(getClassAndMethod()) {
            val model = get().getOrThrow()
            model::statusSend.required()
        }

}