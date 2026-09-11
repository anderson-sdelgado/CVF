package br.com.usinasantafe.cvf.external.retrofit.api.stable

import br.com.usinasantafe.cvf.infra.models.retrofit.stable.ColabRetrofitCheck
import br.com.usinasantafe.cvf.infra.models.retrofit.stable.ColabRetrofitInput
import br.com.usinasantafe.cvf.lib.WEB_ALL_COLAB
import br.com.usinasantafe.cvf.lib.WEB_CHECK_REG_COLAB
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ColabApi {

    @GET(WEB_ALL_COLAB)
    suspend fun all(@Header("Authorization") auth: String): Response<ColabRetrofitInput>

    @POST(WEB_CHECK_REG_COLAB)
    suspend fun checkByReg(
        @Header("Authorization") auth: String,
        @Body reg: Long
    ): Response<ColabRetrofitCheck>

}