package br.com.usinasantafe.cvf.domain.entities.variable

import java.util.Date

data class Header(
    var regDriver: Long? = null,
    var idTruck: Int? = null,
    var dateHourExitMill: Date? = null,
    var dateHourArrivalField: Date? = null
)
