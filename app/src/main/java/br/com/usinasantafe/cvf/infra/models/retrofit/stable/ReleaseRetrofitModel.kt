package br.com.usinasantafe.cvf.infra.models.retrofit.stable

import br.com.usinasantafe.cvf.domain.entities.stable.Release

data class ReleaseRetrofitModel(
    val id: Int,
    val nroOS: Int,
    val idPropAgr: Int,
    val descPropAgr: String,
    val idFront: Int,
)

fun ReleaseRetrofitModel.retrofitModelToEntity(): Release {
    return with(this) {
        Release(
            id = id,
            nroOS = nroOS,
            idPropAgr = idPropAgr,
            descPropAgr = descPropAgr,
            idFront = idFront
        )
    }
}
