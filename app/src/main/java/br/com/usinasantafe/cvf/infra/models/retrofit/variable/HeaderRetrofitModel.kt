package br.com.usinasantafe.cvf.infra.models.retrofit.variable

import br.com.usinasantafe.cvf.infra.models.room.variable.HeaderRoomModel
import br.com.usinasantafe.cvf.utils.required

data class HeaderRetrofitModelOutput(
    val id: Int,
    val regDriver: Long,
    val idTruck: Int,
    val idConfigServ: Int,
    val cartList: List<CartRetrofitModelOutput>
)

data class HeaderRetrofitInput(
    val status: String,
    val failure: String?,
    val data: List<HeaderRetrofitModelInput>
)

data class HeaderRetrofitModelInput(
    val idServ: Int,
    val id: Int,
)

fun HeaderRoomModel.roomModelToRetrofitModel(
    idServ: Int,
    cartList: List<CartRetrofitModelOutput>
): HeaderRetrofitModelOutput {
    return with(this){
        HeaderRetrofitModelOutput(
            id = ::id.required(),
            regDriver = regDriver,
            idTruck = idTruck,
            idConfigServ = idServ,
            cartList = cartList
        )
    }
}

