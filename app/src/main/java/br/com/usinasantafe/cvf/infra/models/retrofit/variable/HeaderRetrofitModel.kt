package br.com.usinasantafe.cvf.infra.models.retrofit.variable

import br.com.usinasantafe.cvf.infra.models.room.variable.HeaderRoomModel
import br.com.usinasantafe.cvf.utils.required
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

val formatter = SimpleDateFormat(
    "dd/MM/yyyy HH:mm",
    Locale.forLanguageTag("pt-BR")
)

data class HeaderRetrofitModelOutput(
    val id: Int,
    val regDriver: Long,
    val idTruck: Int,
    val idFront: Int,
    val idRelease: Int,
    val idConfigServ: Int,
    val dateHour: String,
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
    idFront: Int,
    idRelease: Int,
    cartList: List<CartRetrofitModelOutput>
): HeaderRetrofitModelOutput {
    return with(this){
        HeaderRetrofitModelOutput(
            id = ::id.required(),
            regDriver = regDriver,
            idTruck = idTruck,
            idFront = idFront,
            idRelease = idRelease,
            idConfigServ = idServ,
            dateHour = formatter.format(dateHour),
            cartList = cartList
        )
    }
}

