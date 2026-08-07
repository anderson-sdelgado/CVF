package br.com.usinasantafe.cvf.infra.datasource.retrofit.stable

import br.com.usinasantafe.cvf.infra.models.retrofit.stable.EquipRetrofitModel

interface EquipRetrofitDatasource {
    suspend fun listAll(token: String): Result<List<EquipRetrofitModel>>
}