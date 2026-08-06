package br.com.usinasantafe.cvf.infra.models.retrofit.variable

import br.com.usinasantafe.cvf.domain.entities.variable.Config
import br.com.usinasantafe.cvf.utils.required

data class ConfigRetrofitModelOutput(
    val number: Long,
    val version: String,
)

data class ConfigRetrofitModelInput(
    val idServ: Int,
)

fun Config.entityToRetrofitModel(): ConfigRetrofitModelOutput {
    return ConfigRetrofitModelOutput(
        number = ::number.required(),
        version = ::version.required(),
    )
}

fun ConfigRetrofitModelInput.retrofitModelToEntity(): Config {
    return Config(
        idServ = ::idServ.required(),
    )
}