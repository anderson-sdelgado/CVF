package br.com.usinasantafe.cvf.infra.models.room.variable

import androidx.room.Entity
import androidx.room.PrimaryKey
import br.com.usinasantafe.cvf.infra.models.sharedpreferences.CartSharedPreferencesModel
import br.com.usinasantafe.cvf.lib.TB_CART
import br.com.usinasantafe.cvf.utils.required

@Entity(tableName = TB_CART)
data class CartRoomModel(
    @PrimaryKey
    var id: Int? = null,
    val idHeader: Int,
    val position: Int,
    val idCart: Int
)

fun CartSharedPreferencesModel.sharedPreferencesModelToRoomModel(idHeader: Int): CartRoomModel {
    return with(this){
        CartRoomModel(
            idHeader = idHeader,
            position = ::position.required(),
            idCart = ::idCart.required()
        )
    }
}


