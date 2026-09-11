package br.com.usinasantafe.cvf.external.retrofit.datasource.stable

import br.com.usinasantafe.cvf.di.provider.DefaultApi
import br.com.usinasantafe.cvf.di.provider.ShortTimeoutApi
import br.com.usinasantafe.cvf.external.retrofit.api.stable.ColabApi
import br.com.usinasantafe.cvf.infra.datasource.retrofit.stable.ColabRetrofitDatasource
import br.com.usinasantafe.cvf.infra.models.retrofit.stable.ColabRetrofitModel
import br.com.usinasantafe.cvf.lib.SUCCESS
import br.com.usinasantafe.cvf.utils.UNKNOWN_ERROR
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import br.com.usinasantafe.cvf.utils.result
import javax.inject.Inject

class IColabRetrofitDatasource @Inject constructor(
    @DefaultApi private val colabApi: ColabApi,
    @ShortTimeoutApi private val colabApiShortTime: ColabApi
): ColabRetrofitDatasource {

    override suspend fun listAll(token: String): Result<List<ColabRetrofitModel>> =
        result(getClassAndMethod()) {
            val result = colabApi.all(token).body()!!
            if (result.status != SUCCESS) throw Exception(result.failure ?: UNKNOWN_ERROR)
            result.data!!
        }

    override suspend fun checkByReg(token: String, reg: Long): Result<ColabRetrofitModel> =
        result(getClassAndMethod()) {
            val result = colabApiShortTime.checkByReg(token, reg).body()!!
            if (result.status != SUCCESS) throw Exception(result.failure ?: UNKNOWN_ERROR)
            result.data!!
        }

}