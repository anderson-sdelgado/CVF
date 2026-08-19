package br.com.usinasantafe.cvf.external.retrofit.datasource.stable

import android.content.Context
import br.com.usinasantafe.cvf.R
import br.com.usinasantafe.cvf.di.provider.DefaultApi
import br.com.usinasantafe.cvf.external.retrofit.api.stable.FrontApi
import br.com.usinasantafe.cvf.infra.datasource.retrofit.stable.FrontRetrofitDatasource
import br.com.usinasantafe.cvf.infra.models.retrofit.stable.FrontRetrofitModel
import br.com.usinasantafe.cvf.lib.SUCCESS
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import br.com.usinasantafe.cvf.utils.result
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class IFrontRetrofitDatasource @Inject constructor(
    @ApplicationContext private val context: Context,
    @DefaultApi private val frontApi: FrontApi
): FrontRetrofitDatasource {

    override suspend fun listAll(token: String): Result<List<FrontRetrofitModel>> =
        result(getClassAndMethod()) {
            val result = frontApi.all(token).body()!!
            if (result.status != SUCCESS) throw Exception(result.failure ?: context.getString(R.string.text_unknown_error))
            result.data!!
        }

}