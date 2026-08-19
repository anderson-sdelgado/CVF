package br.com.usinasantafe.cvf.external.retrofit.api.stable

import br.com.usinasantafe.cvf.infra.models.retrofit.stable.FrontRetrofitInput
import br.com.usinasantafe.cvf.infra.models.retrofit.stable.FrontRetrofitModel
import br.com.usinasantafe.cvf.lib.WEB_ALL_FRONT
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface FrontApi {

    @GET(WEB_ALL_FRONT)
    suspend fun all(@Header("Authorization") auth: String): Response<FrontRetrofitInput>

}