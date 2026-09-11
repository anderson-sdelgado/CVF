package br.com.usinasantafe.cvf.infra.models.retrofit.variable

import br.com.usinasantafe.cvf.infra.models.room.variable.CartRoomModel
import br.com.usinasantafe.cvf.utils.required

data class CartRetrofitModelOutput(
    val id: Int,
    var position: Int,
    var idCart: Int,
)

fun CartRoomModel.roomModelToRetrofitModel(): CartRetrofitModelOutput {
    return with(this){
        CartRetrofitModelOutput(
            id = ::id.required(),
            position = position,
            idCart = idCart
        )
    }
}
