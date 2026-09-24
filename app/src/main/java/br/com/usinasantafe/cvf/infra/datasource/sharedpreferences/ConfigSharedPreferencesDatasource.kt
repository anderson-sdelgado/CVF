package br.com.usinasantafe.cvf.infra.datasource.sharedpreferences

import br.com.usinasantafe.cvf.infra.models.sharedpreferences.ConfigSharedPreferencesModel
import br.com.usinasantafe.cvf.lib.StatusSend
import br.com.usinasantafe.cvf.utils.EmptyResult
import kotlinx.coroutines.flow.Flow

interface ConfigSharedPreferencesDatasource {
    suspend fun save(model: ConfigSharedPreferencesModel): EmptyResult
    suspend fun get(): Result<ConfigSharedPreferencesModel>
    suspend fun has(): Result<Boolean>
    suspend fun setFlagUpdate(): EmptyResult
    suspend fun setStatusSend(statusSend: StatusSend): EmptyResult
    suspend fun getPassword(): Result<String>
    suspend fun getFlagUpdate(): Result<Boolean>
    suspend fun setTokenFCM(token: String): Result<Boolean>
    suspend fun getTokenFCM(): Result<String>
    fun observe(): Flow<ConfigSharedPreferencesModel>
}