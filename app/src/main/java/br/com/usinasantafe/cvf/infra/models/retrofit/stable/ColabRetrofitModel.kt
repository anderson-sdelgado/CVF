package br.com.usinasantafe.cvf.infra.models.retrofit.stable

import br.com.usinasantafe.cvf.domain.entities.stable.Colab

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
