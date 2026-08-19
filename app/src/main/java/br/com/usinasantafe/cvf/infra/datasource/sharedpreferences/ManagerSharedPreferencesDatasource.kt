package br.com.usinasantafe.cvf.infra.datasource.sharedpreferences

import br.com.usinasantafe.cvf.infra.models.sharedpreferences.ManagerSharedPreferencesModel
import br.com.usinasantafe.cvf.lib.StatusSend
import br.com.usinasantafe.cvf.utils.EmptyResult

interface ManagerSharedPreferencesDatasource {
    suspend fun clean(): EmptyResult
    suspend fun has(): Result<Boolean>
    suspend fun getIdFront(): Result<Int?>
    suspend fun getIdRelease(): Result<Int?>
    suspend fun save(model: ManagerSharedPreferencesModel): EmptyResult
    suspend fun hasSend(): Result<Boolean>
    suspend fun get(): Result<ManagerSharedPreferencesModel>
    suspend fun setStatusSend(statusSend: StatusSend): EmptyResult
}