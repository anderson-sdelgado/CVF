package br.com.usinasantafe.cvf.external.sharedPreferences

import android.content.SharedPreferences
import androidx.core.content.edit
import br.com.usinasantafe.cvf.infra.datasource.sharedpreferences.HeaderSharedPreferencesDatasource
import br.com.usinasantafe.cvf.infra.models.sharedpreferences.HeaderSharedPreferencesModel
import br.com.usinasantafe.cvf.lib.BASE_SHARED_PREFERENCES_TABLE_HEADER
import br.com.usinasantafe.cvf.utils.EmptyResult
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import br.com.usinasantafe.cvf.utils.result
import com.google.gson.Gson
import javax.inject.Inject

class IHeaderSharedPreferencesDatasource @Inject constructor(
    private val sharedPreferences: SharedPreferences
): HeaderSharedPreferencesDatasource {

    override suspend fun save(model: HeaderSharedPreferencesModel): EmptyResult =
        result(getClassAndMethod()) {
            sharedPreferences.edit {
                putString(
                    BASE_SHARED_PREFERENCES_TABLE_HEADER,
                    Gson().toJson(model)
                )
            }
        }

    override suspend fun getRegDriver(): Result<Long?> =
        result(getClassAndMethod()) {
            get().getOrThrow().regDriver
        }

    override suspend fun setRegDriver(reg: Long): EmptyResult =
        result(getClassAndMethod()) {
            val model = get().getOrThrow()
            model.regDriver = reg
            save(model).getOrThrow()
        }

    override suspend fun clean(): EmptyResult =
        result(getClassAndMethod()) {
            sharedPreferences.edit {
                remove(BASE_SHARED_PREFERENCES_TABLE_HEADER)
            }
        }

    override suspend fun getIdTruck(): Result<Int?> =
        result(getClassAndMethod()) {
            get().getOrThrow().idTruck
        }

    override suspend fun setIdTruck(id: Int): EmptyResult =
        result(getClassAndMethod()) {
            val model = get().getOrThrow()
            model.idTruck = id
            save(model).getOrThrow()
        }

    override suspend fun get(): Result<HeaderSharedPreferencesModel> =
        result(getClassAndMethod()) {
            val data = sharedPreferences.getString(
                BASE_SHARED_PREFERENCES_TABLE_HEADER,
                null
            )
            if(data.isNullOrEmpty()) return@result HeaderSharedPreferencesModel()
            val model = Gson().fromJson(
                data,
                HeaderSharedPreferencesModel::class.java
            )
            model
        }

    override suspend fun has(): Result<Boolean> =
        result(getClassAndMethod()) {
            val data = sharedPreferences.getString(
                BASE_SHARED_PREFERENCES_TABLE_HEADER,
                null
            )
            !data.isNullOrEmpty()
        }
}