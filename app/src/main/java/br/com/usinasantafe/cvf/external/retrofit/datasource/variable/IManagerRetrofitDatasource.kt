package br.com.usinasantafe.cvf.external.retrofit.datasource.variable

import android.content.Context
import br.com.usinasantafe.cvf.R
import br.com.usinasantafe.cvf.di.provider.DefaultApi
import br.com.usinasantafe.cvf.external.retrofit.api.variable.ManagerApi
import br.com.usinasantafe.cvf.infra.datasource.retrofit.variable.ManagerRetrofitDatasource
import br.com.usinasantafe.cvf.infra.models.retrofit.variable.ManagerRetrofitModelInput
import br.com.usinasantafe.cvf.infra.models.retrofit.variable.ManagerRetrofitModelOutput
import br.com.usinasantafe.cvf.lib.SUCCESS
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import br.com.usinasantafe.cvf.utils.result
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class IManagerRetrofitDatasource @Inject constructor(
    @ApplicationContext private val context: Context,
    @DefaultApi private val managerApi: ManagerApi
): ManagerRetrofitDatasource {
    override suspend fun send(
        token: String,
        model: ManagerRetrofitModelOutput
    ): Result<ManagerRetrofitModelInput> =
        result(getClassAndMethod()) {
            val model = managerApi.send(token, model).body()!!
            if (model.status != SUCCESS) throw Exception(model.failure ?: context.getString(R.string.text_unknown_error))
            model
        }
}