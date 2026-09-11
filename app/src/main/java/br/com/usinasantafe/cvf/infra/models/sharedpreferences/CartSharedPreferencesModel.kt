package br.com.usinasantafe.cvf.infra.models.sharedpreferences

import br.com.usinasantafe.cvf.domain.entities.variable.Cart
import br.com.usinasantafe.cvf.utils.required

data class CartSharedPreferencesModel(
    val position: Int,
    val idCart: Int,
)

fun CartSharedPreferencesModel.sharedPreferencesModelToEntity(): Cart {
    return with(this){
        Cart(
            position = position,
            idCart = idCart
        )
    }
}

fun Cart.entityToSharedPreferencesModel(): CartSharedPreferencesModel {
    return with(this){
        CartSharedPreferencesModel(
            position = position,
            idCart = idCart
        )
    }
}
