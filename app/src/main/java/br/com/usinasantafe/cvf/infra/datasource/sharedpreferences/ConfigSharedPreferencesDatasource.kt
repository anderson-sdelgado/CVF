package br.com.usinasantafe.cvf.infra.datasource.sharedpreferences

import br.com.usinasantafe.cvf.infra.models.sharedpreferences.ConfigSharedPreferencesModel
import br.com.usinasantafe.cvf.utils.EmptyResult

interface ConfigSharedPreferencesDatasource {
    suspend fun save(model: ConfigSharedPreferencesModel): EmptyResult
    suspend fun get(): Result<ConfigSharedPreferencesModel>
    suspend fun has(): Result<Boolean>
    suspend fun setFlagUpdate(): EmptyResult
}