package br.com.usinasantafe.cvf.external.retrofit.datasource.variable

import br.com.usinasantafe.cvf.di.provider.DefaultApi
import br.com.usinasantafe.cvf.external.retrofit.api.variable.NoteApi
import br.com.usinasantafe.cvf.infra.datasource.retrofit.variable.NoteRetrofitDatasource
import br.com.usinasantafe.cvf.infra.models.retrofit.variable.HeaderRetrofitInput
import br.com.usinasantafe.cvf.infra.models.retrofit.variable.HeaderRetrofitModelInput
import br.com.usinasantafe.cvf.infra.models.retrofit.variable.HeaderRetrofitModelOutput
import br.com.usinasantafe.cvf.lib.SUCCESS
import br.com.usinasantafe.cvf.utils.UNKNOWN_ERROR
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import br.com.usinasantafe.cvf.utils.result
import javax.inject.Inject

class INoteRetrofitDatasource @Inject constructor(
    @DefaultApi private val noteApi: NoteApi
): NoteRetrofitDatasource {

    override suspend fun send(
        token: String,
        data: List<HeaderRetrofitModelOutput>
    ): Result<List<HeaderRetrofitModelInput>> =
        result(getClassAndMethod()) {
            val result = noteApi.send(token, data).body()!!
            if (result.status != SUCCESS) throw Exception(result.failure ?: UNKNOWN_ERROR)
            result.data
        }

}