package br.com.usinasantafe.cvf.external.retrofit.datasource.variable

import br.com.usinasantafe.cvf.di.provider.DefaultApi
import br.com.usinasantafe.cvf.external.retrofit.api.variable.ConfigApi
import br.com.usinasantafe.cvf.infra.datasource.retrofit.variable.ConfigRetrofitDatasource
import br.com.usinasantafe.cvf.infra.models.retrofit.variable.ConfigRetrofitModelInput
import br.com.usinasantafe.cvf.infra.models.retrofit.variable.ConfigRetrofitModelOutput
import br.com.usinasantafe.cvf.infra.models.retrofit.variable.retrofitModelToEntity
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import br.com.usinasantafe.cvf.utils.result
import javax.inject.Inject

class IConfigRetrofitDatasource @Inject constructor(
    @DefaultApi private val configApi: ConfigApi
): ConfigRetrofitDatasource {
    override suspend fun recoverToken(retrofitModelOutput: ConfigRetrofitModelOutput): Result<ConfigRetrofitModelInput> =
        result(getClassAndMethod()) {
            val model = configApi.send(retrofitModelOutput).body()!!
            model.retrofitModelToEntity()
            model
        }
}