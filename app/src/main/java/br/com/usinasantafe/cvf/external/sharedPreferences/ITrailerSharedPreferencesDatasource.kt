package br.com.usinasantafe.cvf.external.sharedPreferences

import android.content.SharedPreferences
import androidx.core.content.edit
import br.com.usinasantafe.cvf.infra.datasource.sharedpreferences.TrailerSharedPreferencesDatasource
import br.com.usinasantafe.cvf.infra.models.sharedpreferences.TrailerSharedPreferencesModel
import br.com.usinasantafe.cvf.lib.BASE_SHARED_PREFERENCES_TABLE_HEADER
import br.com.usinasantafe.cvf.lib.BASE_SHARED_PREFERENCES_TABLE_TRAILER_LIST
import br.com.usinasantafe.cvf.utils.EmptyResult
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import br.com.usinasantafe.cvf.utils.result
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject
import kotlin.collections.isNotEmpty
import kotlin.collections.toMutableList

class ITrailerSharedPreferencesDatasource @Inject constructor(
    private val sharedPreferences: SharedPreferences
): TrailerSharedPreferencesDatasource {

    private val typeToken = object : TypeToken<List<TrailerSharedPreferencesModel>>() {}.type

    suspend fun add(model: TrailerSharedPreferencesModel): EmptyResult =
        result(getClassAndMethod()) {
            val list = list().getOrThrow()
            var mutableList = list.toMutableList()
            if(list.isNotEmpty()) mutableList = list.toMutableList()
            mutableList.add(model)
            sharedPreferences.edit {
                putString(
                    BASE_SHARED_PREFERENCES_TABLE_TRAILER_LIST,
                    Gson().toJson(mutableList, typeToken)
                )
            }
            mutableList.clear()
        }

    override suspend fun clean(): EmptyResult =
        result(getClassAndMethod()) {
            sharedPreferences.edit {
                remove(BASE_SHARED_PREFERENCES_TABLE_TRAILER_LIST)
            }
        }

    suspend fun list(): Result<List<TrailerSharedPreferencesModel>> =
        result(getClassAndMethod()) {
            val result = sharedPreferences.getString(
                BASE_SHARED_PREFERENCES_TABLE_TRAILER_LIST,
                null
            )
            if(result.isNullOrEmpty()) return@result emptyList()
            Gson().fromJson(result, typeToken)
        }

}