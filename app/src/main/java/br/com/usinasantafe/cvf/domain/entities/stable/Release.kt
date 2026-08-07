package br.com.usinasantafe.cvf.domain.entities.stable

data class Release(
    val id: Int,
    val nroOS: Int,
    val idPropAgr: Int,
    val descPropAgr: String,
    val idFront: Int,
)
