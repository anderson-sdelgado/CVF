package br.com.usinasantafe.cvf.domain.entities.stable

import br.com.usinasantafe.cvf.lib.TypeEquip
import java.util.Date

data class Equip (
    val id: Int,
    val nro: Int,
    val idOperClass: Int,
    val type: TypeEquip,
    val idFront: Int? = null,
    val cdFront: Int? = null,
    val descriptionFront: String? = null,
    val idRelease: Int? = null,
    val nroOSRelease: Int? = null,
    val idPropAgrRelease: Int? = null,
    val descPropAgrRelease: String? = null,
    val dateHourExitMill: Date? = null,
    val dateHourArrivalField: Date? = null,
)
