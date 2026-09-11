package br.com.usinasantafe.cvf.infra.datasource.retrofit.variable

import br.com.usinasantafe.cvf.infra.models.retrofit.variable.HeaderRetrofitInput
import br.com.usinasantafe.cvf.infra.models.retrofit.variable.HeaderRetrofitModelInput
import br.com.usinasantafe.cvf.infra.models.retrofit.variable.HeaderRetrofitModelOutput

interface NoteRetrofitDatasource {
    suspend fun send(
        token: String,
        data: List<HeaderRetrofitModelOutput>
    ): Result<List<HeaderRetrofitModelInput>>
}