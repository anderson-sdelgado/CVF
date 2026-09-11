package br.com.usinasantafe.cvf.infra.datasource.sharedpreferences

import br.com.usinasantafe.cvf.infra.models.sharedpreferences.CartSharedPreferencesModel
import br.com.usinasantafe.cvf.utils.EmptyResult

interface CartSharedPreferencesDatasource {
    suspend fun clean(): EmptyResult
    suspend fun list(): Result<List<CartSharedPreferencesModel>>
    suspend fun add(model: CartSharedPreferencesModel): EmptyResult
}