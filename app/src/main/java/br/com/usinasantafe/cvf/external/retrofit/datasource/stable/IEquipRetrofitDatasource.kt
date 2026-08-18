package br.com.usinasantafe.cvf.external.retrofit.datasource.stable

import br.com.usinasantafe.cvf.di.provider.DefaultApi
import br.com.usinasantafe.cvf.external.retrofit.api.stable.EquipApi
import br.com.usinasantafe.cvf.infra.datasource.retrofit.stable.EquipRetrofitDatasource
import br.com.usinasantafe.cvf.infra.models.retrofit.stable.EquipRetrofitModel
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import br.com.usinasantafe.cvf.utils.result
import javax.inject.Inject

class IEquipRetrofitDatasource @Inject constructor(
    @DefaultApi private val equipApi: EquipApi
): EquipRetrofitDatasource {

    override suspend fun listAll(token: String): Result<List<EquipRetrofitModel>> =
        result(getClassAndMethod()) {
            equipApi.all(token).body()!!
        }

}