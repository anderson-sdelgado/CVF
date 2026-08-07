package br.com.usinasantafe.cvf.infra.datasource.retrofit.stable

import br.com.usinasantafe.cvf.infra.models.retrofit.stable.ReleaseRetrofitModel

interface ReleaseRetrofitDatasource {
    suspend fun listAll(token: String): Result<List<ReleaseRetrofitModel>>
}