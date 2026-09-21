package br.com.usinasantafe.cvf.external.sharedPreferences

import android.content.SharedPreferences
import androidx.core.content.edit
import br.com.usinasantafe.cvf.infra.datasource.sharedpreferences.ConfigSharedPreferencesDatasource
import br.com.usinasantafe.cvf.infra.models.sharedpreferences.ConfigSharedPreferencesModel
import br.com.usinasantafe.cvf.infra.models.sharedpreferences.sharedPreferencesModelToEntity
import br.com.usinasantafe.cvf.lib.BASE_SHARED_PREFERENCES_TABLE_CONFIG
import br.com.usinasantafe.cvf.lib.StatusSend
import br.com.usinasantafe.cvf.utils.EmptyResult
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import br.com.usinasantafe.cvf.utils.required
import br.com.usinasantafe.cvf.utils.result
import com.google.gson.Gson
import javax.inject.Inject

class IConfigSharedPreferencesDatasource @Inject constructor(
    private val sharedPreferences: SharedPreferences
): ConfigSharedPreferencesDatasource {

    override suspend fun save(model: ConfigSharedPreferencesModel): EmptyResult =
        result(getClassAndMethod()) {
            sharedPreferences.edit {
                putString(
                    BASE_SHARED_PREFERENCES_TABLE_CONFIG,
                    Gson().toJson(model)
                )
            }
        }

    override suspend fun get(): Result<ConfigSharedPreferencesModel> =
        result(getClassAndMethod()) {
            val data = sharedPreferences.getString(
                BASE_SHARED_PREFERENCES_TABLE_CONFIG,
                null
            )
            if(data.isNullOrEmpty()) return@result ConfigSharedPreferencesModel()
            val model = Gson().fromJson(
                data,
                ConfigSharedPreferencesModel::class.java
            )
            model
        }

    override suspend fun has(): Result<Boolean> =
        result(getClassAndMethod()) {
            val result = sharedPreferences.getString(
                BASE_SHARED_PREFERENCES_TABLE_CONFIG,
                null
            )
            result != null
        }

    override suspend fun setFlagUpdate(): EmptyResult =
        result(getClassAndMethod()) {
            val model = get().getOrThrow()
            model.flagUpdate = true
            model.statusSend = StatusSend.SENT
            save(model).getOrThrow()
        }

    override suspend fun setStatusSend(statusSend: StatusSend): EmptyResult =
        result(getClassAndMethod()) {
            val model = get().getOrThrow()
            model.statusSend = statusSend
            save(model).getOrThrow()
        }

    override suspend fun getPassword(): Result<String> =
        result(getClassAndMethod()) {
            val model = get().getOrThrow()
            model::password.required()
        }

    override suspend fun getFlagUpdate(): Result<Boolean> =
        result(getClassAndMethod()) {
            val model = get().getOrThrow()
            model.flagUpdate
        }

    override suspend fun setTokenFCM(token: String): Result<Boolean> =
        result(getClassAndMethod()) {
            val has = has().getOrThrow()
            val model = get().getOrThrow()
            val check = (has && (model.tokenFCM != token) && (model.statusSend == StatusSend.SENT))
            model.tokenFCM = token
            if(check) model.statusSend = StatusSend.SEND
            save(model).getOrThrow()
            check
        }

    override suspend fun getTokenFCM(): Result<String> =
        result(getClassAndMethod()) {
            val model = get().getOrThrow()
            model::tokenFCM.required()
        }

}