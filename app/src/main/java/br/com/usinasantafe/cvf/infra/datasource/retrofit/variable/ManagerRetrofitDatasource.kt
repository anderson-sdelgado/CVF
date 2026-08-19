package br.com.usinasantafe.cvf.infra.datasource.retrofit.variable

import br.com.usinasantafe.cvf.infra.models.retrofit.variable.ManagerRetrofitModelInput
import br.com.usinasantafe.cvf.infra.models.retrofit.variable.ManagerRetrofitModelOutput

interface ManagerRetrofitDatasource {
    suspend fun send(
        token: String,
        model: ManagerRetrofitModelOutput
    ): Result<ManagerRetrofitModelInput>
}