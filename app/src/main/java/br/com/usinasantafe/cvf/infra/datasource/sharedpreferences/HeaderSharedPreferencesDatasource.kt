package br.com.usinasantafe.cvf.infra.datasource.sharedpreferences

import br.com.usinasantafe.cvf.utils.EmptyResult

interface HeaderSharedPreferencesDatasource {
    suspend fun getRegDriver(): Result<Long?>
    suspend fun setRegDriver(reg: Long): EmptyResult
}