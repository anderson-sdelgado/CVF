package br.com.usinasantafe.cvf.external.retrofit.datasource.variable

import android.content.Context
import br.com.usinasantafe.cvf.R
import br.com.usinasantafe.cvf.di.provider.DefaultApi
import br.com.usinasantafe.cvf.external.retrofit.api.variable.ConfigApi
import br.com.usinasantafe.cvf.infra.datasource.retrofit.variable.ConfigRetrofitDatasource
import br.com.usinasantafe.cvf.infra.models.retrofit.variable.ConfigRetrofitModelInput
import br.com.usinasantafe.cvf.infra.models.retrofit.variable.ConfigRetrofitModelOutput
import br.com.usinasantafe.cvf.infra.models.retrofit.variable.retrofitModelToEntity
import br.com.usinasantafe.cvf.lib.SUCCESS
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import br.com.usinasantafe.cvf.utils.result
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class IConfigRetrofitDatasource @Inject constructor(
    @ApplicationContext private val context: Context,
    @DefaultApi private val configApi: ConfigApi
): ConfigRetrofitDatasource {
    override suspend fun recoverToken(retrofitModelOutput: ConfigRetrofitModelOutput): Result<ConfigRetrofitModelInput> =
        result(getClassAndMethod()) {
            val result = configApi.send(retrofitModelOutput).body()!!
            if (result.status != SUCCESS) throw Exception(result.failure ?: context.getString(R.string.text_unknown_error))
            result.retrofitModelToEntity()
            result
        }
}