package br.com.usinasantafe.cvf.infra.models.retrofit.stable

import br.com.usinasantafe.cvf.domain.entities.stable.Front

data class FrontRetrofitInput(
    val status: String,
    val failure: String?,
    val data: List<FrontRetrofitModel>?
)

data class FrontRetrofitModel(
    val id: Int,
    val cd: Int,
    val description: String,
)

fun FrontRetrofitModel.retrofitModelToEntity(): Front {
    return with(this) {
        Front(
            id = id,
            cd = cd,
            description = description
        )
    }
}
