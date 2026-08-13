package br.com.usinasantafe.cvf.external.sharedPreferences

import android.content.SharedPreferences
import androidx.core.content.edit
import br.com.usinasantafe.cvf.infra.datasource.sharedpreferences.ManagerSharedPreferencesDatasource
import br.com.usinasantafe.cvf.infra.models.sharedpreferences.ManagerSharedPreferencesModel
import br.com.usinasantafe.cvf.infra.models.sharedpreferences.sharedPreferencesModelToEntity
import br.com.usinasantafe.cvf.lib.BASE_SHARED_PREFERENCES_TABLE_MANAGER
import br.com.usinasantafe.cvf.utils.EmptyResult
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import br.com.usinasantafe.cvf.utils.result
import com.google.gson.Gson
import javax.inject.Inject

class IManagerSharedPreferencesDatasource @Inject constructor(
    private val sharedPreferences: SharedPreferences
): ManagerSharedPreferencesDatasource {

    override suspend fun save(model: ManagerSharedPreferencesModel): EmptyResult =
        result(getClassAndMethod()) {
            sharedPreferences.edit {
                putString(
                    BASE_SHARED_PREFERENCES_TABLE_MANAGER,
                    Gson().toJson(model)
                )
            }
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

    suspend fun get(): Result<ManagerSharedPreferencesModel> =
        result(getClassAndMethod()) {
            val data = sharedPreferences.getString(
                BASE_SHARED_PREFERENCES_TABLE_MANAGER,
                null
            )
            val model = Gson().fromJson(
                data,
                ManagerSharedPreferencesModel::class.java
            )
            model.sharedPreferencesModelToEntity()
            model
        }

}