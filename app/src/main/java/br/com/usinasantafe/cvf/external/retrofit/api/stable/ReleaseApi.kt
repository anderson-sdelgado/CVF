package br.com.usinasantafe.cvf.external.retrofit.api.stable

import br.com.usinasantafe.cvf.infra.models.retrofit.stable.ReleaseRetrofitInput
import br.com.usinasantafe.cvf.infra.models.retrofit.stable.ReleaseRetrofitModel
import br.com.usinasantafe.cvf.lib.WEB_ALL_RELEASE
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface ReleaseApi {

    @GET(WEB_ALL_RELEASE)
    suspend fun all(@Header("Authorization") auth: String): Response<ReleaseRetrofitInput>

}