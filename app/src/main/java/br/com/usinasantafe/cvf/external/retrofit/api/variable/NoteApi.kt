package br.com.usinasantafe.cvf.external.retrofit.api.variable

import br.com.usinasantafe.cvf.infra.models.retrofit.variable.HeaderRetrofitInput
import br.com.usinasantafe.cvf.infra.models.retrofit.variable.HeaderRetrofitModelOutput
import br.com.usinasantafe.cvf.lib.WEB_CHECK_NRO_EQUIP
import br.com.usinasantafe.cvf.lib.WEB_SAVE_NOTE
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface NoteApi {

    @POST(WEB_SAVE_NOTE)
    suspend fun send(
        @Header("Authorization") auth: String,
        @Body data: List<HeaderRetrofitModelOutput>
    ): Response<HeaderRetrofitInput>
}