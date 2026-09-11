package br.com.usinasantafe.cvf.external.retrofit.datasource.variable

import br.com.usinasantafe.cvf.di.provider.DefaultApi
import br.com.usinasantafe.cvf.external.retrofit.api.variable.ManagerApi
import br.com.usinasantafe.cvf.infra.datasource.retrofit.variable.ManagerRetrofitDatasource
import br.com.usinasantafe.cvf.infra.models.retrofit.variable.ManagerRetrofitModelInput
import br.com.usinasantafe.cvf.infra.models.retrofit.variable.ManagerRetrofitModelOutput
import br.com.usinasantafe.cvf.lib.SUCCESS
import br.com.usinasantafe.cvf.utils.UNKNOWN_ERROR
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import br.com.usinasantafe.cvf.utils.result
import javax.inject.Inject

class IManagerRetrofitDatasource @Inject constructor(
    @DefaultApi private val managerApi: ManagerApi
): ManagerRetrofitDatasource {
    override suspend fun send(
        token: String,
        data: ManagerRetrofitModelOutput
    ): Result<ManagerRetrofitModelInput> =
        result(getClassAndMethod()) {
            val result = managerApi.send(token, data).body()!!
            if (result.status != SUCCESS) throw Exception(result.failure ?: UNKNOWN_ERROR)
            result
        }
}