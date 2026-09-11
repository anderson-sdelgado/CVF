package br.com.usinasantafe.cvf.external.retrofit.datasource.stable

import android.content.Context
import br.com.usinasantafe.cvf.R
import br.com.usinasantafe.cvf.di.provider.DefaultApi
import br.com.usinasantafe.cvf.di.provider.ShortTimeoutApi
import br.com.usinasantafe.cvf.external.retrofit.api.stable.EquipApi
import br.com.usinasantafe.cvf.infra.datasource.retrofit.stable.EquipRetrofitDatasource
import br.com.usinasantafe.cvf.infra.models.retrofit.stable.ColabRetrofitModel
import br.com.usinasantafe.cvf.infra.models.retrofit.stable.EquipRetrofitModel
import br.com.usinasantafe.cvf.lib.SUCCESS
import br.com.usinasantafe.cvf.utils.UNKNOWN_ERROR
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import br.com.usinasantafe.cvf.utils.result
import dagger.hilt.android.qualifiers.ApplicationContext
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

    override suspend fun checkByNro(token: String, nro: Int): Result<EquipRetrofitModel> =
        result(getClassAndMethod()) {
            val result = equipApiShortTime.checkByNro(token, nro).body()!!
            if (result.status != SUCCESS) throw Exception(result.failure ?: UNKNOWN_ERROR)
            result.data!!
        }

}