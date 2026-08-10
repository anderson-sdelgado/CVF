package br.com.usinasantafe.cvf.infra.repositories.variable

import br.com.usinasantafe.cvf.domain.entities.variable.Config
import br.com.usinasantafe.cvf.domain.repositories.variable.ConfigRepository
import br.com.usinasantafe.cvf.infra.datasource.retrofit.variable.ConfigRetrofitDatasource
import br.com.usinasantafe.cvf.infra.datasource.sharedpreferences.ConfigSharedPreferencesDatasource
import br.com.usinasantafe.cvf.infra.models.retrofit.variable.entityToRetrofitModel
import br.com.usinasantafe.cvf.infra.models.retrofit.variable.retrofitModelToEntity
import br.com.usinasantafe.cvf.infra.models.sharedpreferences.entityToSharedPreferencesModel
import br.com.usinasantafe.cvf.infra.models.sharedpreferences.sharedPreferencesModelToEntity
import br.com.usinasantafe.cvf.utils.EmptyResult
import br.com.usinasantafe.cvf.utils.call
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import javax.inject.Inject

class IConfigRepository @Inject constructor(
    private val configSharedPreferencesDatasource: ConfigSharedPreferencesDatasource,
    private val configRetrofitDatasource: ConfigRetrofitDatasource
): ConfigRepository {

    override suspend fun get(): Result<Config> =
        call(getClassAndMethod()) {
            configSharedPreferencesDatasource.get().getOrThrow().sharedPreferencesModelToEntity()
        }

    override suspend fun send(entity: Config): Result<Config> =
        call(getClassAndMethod()) {
            val model = entity.entityToRetrofitModel()
            val configRetrofitModel = configRetrofitDatasource.recoverToken(model).getOrThrow()
            configRetrofitModel.retrofitModelToEntity()
        }

    override suspend fun save(entity: Config): EmptyResult =
        call(getClassAndMethod()) {
            val sharedPreferencesModel = entity.entityToSharedPreferencesModel()
            configSharedPreferencesDatasource.save(sharedPreferencesModel).getOrThrow()
        }

    override suspend fun has(): Result<Boolean> =
        call(getClassAndMethod()) {
            configSharedPreferencesDatasource.has().getOrThrow()
        }

    override suspend fun setFlagUpdate(): EmptyResult =
        call(getClassAndMethod()) {
            configSharedPreferencesDatasource.setFlagUpdate().getOrThrow()
        }

}