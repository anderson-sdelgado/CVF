package br.com.usinasantafe.cvf.external.retrofit.datasource.stable

import br.com.usinasantafe.cvf.external.retrofit.api.stable.ReleaseApi
import br.com.usinasantafe.cvf.infra.datasource.retrofit.stable.ReleaseRetrofitDatasource
import br.com.usinasantafe.cvf.infra.models.retrofit.stable.ReleaseRetrofitModel
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import br.com.usinasantafe.cvf.utils.result
import javax.inject.Inject

class IReleaseRetrofitDatasource @Inject constructor(
    private val releaseApi: ReleaseApi
): ReleaseRetrofitDatasource {

    override suspend fun listAll(token: String): Result<List<ReleaseRetrofitModel>> =
        result(getClassAndMethod()) {
            releaseApi.all(token).body()!!
        }

}