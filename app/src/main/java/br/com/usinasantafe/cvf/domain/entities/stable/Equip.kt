package br.com.usinasantafe.cvf.domain.entities.stable

import br.com.usinasantafe.cvf.lib.TypeEquip

data class Equip (
    val id: Int,
    val nro: Int,
    val cdOperClass: Int,
    val descOperClass: String,
    val type: TypeEquip,
)
