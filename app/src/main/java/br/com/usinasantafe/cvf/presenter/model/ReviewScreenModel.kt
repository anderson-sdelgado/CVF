package br.com.usinasantafe.cvf.presenter.model

data class ReviewScreenModel(
    val cdFront: String = "",
    val idRelease: String = "",
    val nroOSRelease: String = "",
    val descPropAgrRelease: String = "",
    val regDriver: String = "",
    val nameDriver: String = "",
    val nroTruck: String = "",
    val dateHourExitField: String = "",
    val cartList: List<CartScreenModel> = emptyList()
)
