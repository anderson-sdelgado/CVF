package br.com.usinasantafe.cvf.domain.entities.variable

import br.com.usinasantafe.cvf.lib.StatusSend

data class Config(
    var number: Long? = null,
    var password: String? = null,
    var version: String? = null,
    var idServ: Int? = null,
    var statusSend: StatusSend = StatusSend.STARTED,
    var flagUpdate: Boolean = false,
)