package br.com.usinasantafe.cvf.external.sharedPreferences

import android.content.SharedPreferences
import androidx.core.content.edit
import br.com.usinasantafe.cvf.infra.datasource.sharedpreferences.CartSharedPreferencesDatasource
import br.com.usinasantafe.cvf.infra.models.sharedpreferences.CartSharedPreferencesModel
import br.com.usinasantafe.cvf.lib.BASE_SHARED_PREFERENCES_TABLE_CART_LIST
import br.com.usinasantafe.cvf.utils.EmptyResult
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import br.com.usinasantafe.cvf.utils.result
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject
import kotlin.collections.isNotEmpty
import kotlin.collections.toMutableList

class ICartSharedPreferencesDatasource @Inject constructor(
    private val sharedPreferences: SharedPreferences
): CartSharedPreferencesDatasource {

    private val typeToken = object : TypeToken<List<CartSharedPreferencesModel>>() {}.type

    override suspend fun add(model: CartSharedPreferencesModel): EmptyResult =
        result(getClassAndMethod()) {
            val list = list().getOrThrow()
            var mutableList = list.toMutableList()
            if(list.isNotEmpty()) mutableList = list.toMutableList()
            mutableList.add(model)
            sharedPreferences.edit {
                putString(
                    BASE_SHARED_PREFERENCES_TABLE_CART_LIST,
                    Gson().toJson(mutableList, typeToken)
                )
            }
            mutableList.clear()
        }

    override suspend fun clean(): EmptyResult =
        result(getClassAndMethod()) {
            sharedPreferences.edit {
                remove(BASE_SHARED_PREFERENCES_TABLE_CART_LIST)
            }
        }

    override suspend fun list(): Result<List<CartSharedPreferencesModel>> =
        result(getClassAndMethod()) {
            val result = sharedPreferences.getString(
                BASE_SHARED_PREFERENCES_TABLE_CART_LIST,
                null
            )
            if(result.isNullOrEmpty()) return@result emptyList()
            Gson().fromJson(result, typeToken)
        }

}