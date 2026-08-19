package br.com.usinasantafe.cvf.external.retrofit.datasource.stable

import android.content.Context
import br.com.usinasantafe.cvf.R
import br.com.usinasantafe.cvf.di.provider.DefaultApi
import br.com.usinasantafe.cvf.external.retrofit.api.stable.ReleaseApi
import br.com.usinasantafe.cvf.infra.datasource.retrofit.stable.ReleaseRetrofitDatasource
import br.com.usinasantafe.cvf.infra.models.retrofit.stable.ReleaseRetrofitModel
import br.com.usinasantafe.cvf.lib.SUCCESS
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import br.com.usinasantafe.cvf.utils.result
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class IReleaseRetrofitDatasource @Inject constructor(
    @ApplicationContext private val context: Context,
    @DefaultApi private val releaseApi: ReleaseApi
): ReleaseRetrofitDatasource {

    override suspend fun listAll(token: String): Result<List<ReleaseRetrofitModel>> =
        result(getClassAndMethod()) {
            val result = releaseApi.all(token).body()!!
            if (result.status != SUCCESS) throw Exception(result.failure ?: context.getString(R.string.text_unknown_error))
            result.data!!
        }

}