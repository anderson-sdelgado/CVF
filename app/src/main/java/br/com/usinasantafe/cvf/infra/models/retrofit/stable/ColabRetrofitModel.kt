package br.com.usinasantafe.cvf.infra.models.retrofit.stable

import br.com.usinasantafe.cvf.domain.entities.stable.Colab
import br.com.usinasantafe.cvf.infra.models.room.stable.ColabRoomModel

data class ColabRetrofitInput(
    val status: String,
    val failure: String?,
    val data: List<ColabRetrofitModel>?
)

data class ColabRetrofitCheck(
    val status: String,
    val failure: String?,
    val data: ColabRetrofitModel?
)

data class ColabRetrofitModel(
    val reg: Long,
    val name: String
)

fun ColabRetrofitModel.retrofitModelToEntity(): Colab {
    return with(this) {
        Colab(
            reg = reg,
            name = name
        )
    }
}

fun ColabRetrofitModel.retrofitModelToRoomModel(): ColabRoomModel {
    return with(this) {
        ColabRoomModel(
            reg = reg,
            name = name
        )
    }
}
