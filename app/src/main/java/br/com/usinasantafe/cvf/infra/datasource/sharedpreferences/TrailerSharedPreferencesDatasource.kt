package br.com.usinasantafe.cvf.infra.datasource.sharedpreferences

import br.com.usinasantafe.cvf.utils.EmptyResult

interface TrailerSharedPreferencesDatasource {
    suspend fun clean(): EmptyResult
}