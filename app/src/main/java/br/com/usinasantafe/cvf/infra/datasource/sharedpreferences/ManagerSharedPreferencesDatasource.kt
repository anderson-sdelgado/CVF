package br.com.usinasantafe.cvf.infra.datasource.sharedpreferences

import br.com.usinasantafe.cvf.utils.EmptyResult

interface ManagerSharedPreferencesDatasource {
    suspend fun clean(): EmptyResult
    suspend fun has(): Result<Boolean>
    suspend fun getIdFront(): Result<Int?>
}