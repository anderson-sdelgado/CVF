package br.com.usinasantafe.cvf.external.retrofit.datasource.stable

import br.com.usinasantafe.cvf.di.provider.DefaultApi
import br.com.usinasantafe.cvf.di.provider.ShortTimeoutApi
import br.com.usinasantafe.cvf.external.retrofit.api.stable.EquipApi
import br.com.usinasantafe.cvf.infra.datasource.retrofit.stable.EquipRetrofitDatasource
import br.com.usinasantafe.cvf.infra.models.retrofit.stable.EquipRetrofitModel
import br.com.usinasantafe.cvf.lib.SUCCESS
import br.com.usinasantafe.cvf.utils.UNKNOWN_ERROR
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import br.com.usinasantafe.cvf.utils.result
import javax.inject.Inject

class IEquipRetrofitDatasource @Inject constructor(
    @DefaultApi private val equipApi: EquipApi,
    @ShortTimeoutApi private val equipApiShortTime: EquipApi
): EquipRetrofitDatasource {

    override suspend fun listAll(token: String): Result<List<EquipRetrofitModel>> =
        result(getClassAndMethod()) {
            val result = equipApi.all(token).body()!!
            if (result.status != SUCCESS) throw Exception(result.failure ?: UNKNOWN_ERROR)
            result.data!!
        }

    override suspend fun checkByNroCart(token: String, nro: Int): Result<EquipRetrofitModel> =
        result(getClassAndMethod()) {
            val result = equipApiShortTime.checkByNroCart(token, nro).body()!!
            if (result.status != SUCCESS) throw Exception(result.failure ?: UNKNOWN_ERROR)
            result.data!!
        }

    override suspend fun checkByNroTruck(token: String, nro: Int): Result<EquipRetrofitModel> =
        result(getClassAndMethod()) {
            val result = equipApiShortTime.checkByNroTruck(token, nro).body()!!
            if (result.status != SUCCESS) throw Exception(result.failure ?: UNKNOWN_ERROR)
            result.data!!
        }

}