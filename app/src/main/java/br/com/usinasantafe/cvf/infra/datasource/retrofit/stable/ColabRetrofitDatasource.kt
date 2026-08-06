package br.com.usinasantafe.cvf.infra.datasource.retrofit.stable

import br.com.usinasantafe.cvf.infra.models.retrofit.stable.ColabRetrofitModel

interface ColabRetrofitDatasource {
    suspend fun listAll(token: String): Result<List<ColabRetrofitModel>>
}