package br.com.usinasantafe.cvf.infra.models.sharedpreferences

import java.util.Date

data class HeaderSharedPreferencesModel(
    var regDriver: Long? = null,
    var idTruck: Int? = null,
    var nroTruck: Int? = null,
    var dateHourExitMill: Date = Date(),
    var dateHourArrivalField: Date = Date(),
)
