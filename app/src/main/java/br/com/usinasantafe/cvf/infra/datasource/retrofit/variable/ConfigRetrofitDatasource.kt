package br.com.usinasantafe.cvf.infra.datasource.retrofit.variable

import br.com.usinasantafe.cvf.infra.models.retrofit.variable.ConfigRetrofitModelInput
import br.com.usinasantafe.cvf.infra.models.retrofit.variable.ConfigRetrofitModelOutput

interface ConfigRetrofitDatasource {
    suspend fun recoverToken(retrofitModelOutput: ConfigRetrofitModelOutput): Result<ConfigRetrofitModelInput>
}