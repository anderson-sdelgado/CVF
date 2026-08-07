package br.com.usinasantafe.cvf.infra.datasource.retrofit.stable

import br.com.usinasantafe.cvf.infra.models.retrofit.stable.FrontRetrofitModel

interface FrontRetrofitDatasource {
    suspend fun listAll(token: String): Result<List<FrontRetrofitModel>>
}