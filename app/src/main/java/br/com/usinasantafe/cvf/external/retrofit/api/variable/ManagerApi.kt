package br.com.usinasantafe.cvf.external.retrofit.api.variable

import br.com.usinasantafe.cvf.infra.models.retrofit.variable.ManagerRetrofitModelInput
import br.com.usinasantafe.cvf.infra.models.retrofit.variable.ManagerRetrofitModelOutput
import br.com.usinasantafe.cvf.lib.WEB_SAVE_MANAGER
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ManagerApi {

    @POST(WEB_SAVE_MANAGER)
    suspend fun send(
        @Header("Authorization") auth: String,
        @Body model: ManagerRetrofitModelOutput
    ): Response<ManagerRetrofitModelInput>

}