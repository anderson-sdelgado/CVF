package br.com.usinasantafe.cvf.domain.entities.stable

data class Colab (
    val reg: Long,
    val name: String,
) {
    init {
        require(reg != 0L) { "The field 'reg' cannot is null." }
    }
}
