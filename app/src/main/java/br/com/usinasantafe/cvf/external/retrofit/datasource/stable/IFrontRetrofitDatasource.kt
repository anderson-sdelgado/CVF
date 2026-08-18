package br.com.usinasantafe.cvf.external.retrofit.datasource.stable

import br.com.usinasantafe.cvf.di.provider.DefaultApi
import br.com.usinasantafe.cvf.external.retrofit.api.stable.FrontApi
import br.com.usinasantafe.cvf.infra.datasource.retrofit.stable.FrontRetrofitDatasource
import br.com.usinasantafe.cvf.infra.models.retrofit.stable.FrontRetrofitModel
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import br.com.usinasantafe.cvf.utils.result
import javax.inject.Inject

class IFrontRetrofitDatasource @Inject constructor(
    @DefaultApi private val frontApi: FrontApi
): FrontRetrofitDatasource {

    override suspend fun listAll(token: String): Result<List<FrontRetrofitModel>> =
        result(getClassAndMethod()) {
            frontApi.all(token).body()!!
        }

}