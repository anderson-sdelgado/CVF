package br.com.usinasantafe.cvf.external.retrofit.api.stable

import br.com.usinasantafe.cvf.infra.models.retrofit.stable.EquipRetrofitCheck
import br.com.usinasantafe.cvf.infra.models.retrofit.stable.EquipRetrofitInput
import br.com.usinasantafe.cvf.lib.WEB_ALL_EQUIP
import br.com.usinasantafe.cvf.lib.WEB_CHECK_NRO_CART
import br.com.usinasantafe.cvf.lib.WEB_CHECK_NRO_TRUCK
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface EquipApi {

    @GET(WEB_ALL_EQUIP)
    suspend fun all(@Header("Authorization") auth: String): Response<EquipRetrofitInput>

    @POST(WEB_CHECK_NRO_CART)
    suspend fun checkByNroCart(
        @Header("Authorization") auth: String,
        @Body nro: Int
    ): Response<EquipRetrofitCheck>

    @POST(WEB_CHECK_NRO_TRUCK)
    suspend fun checkByNroTruck(
        @Header("Authorization") auth: String,
        @Body nro: Int
    ): Response<EquipRetrofitCheck>

}