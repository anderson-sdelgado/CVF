package br.com.usinasantafe.cvf.infra.repositories.variable

import br.com.usinasantafe.cvf.domain.entities.variable.Config
import br.com.usinasantafe.cvf.domain.repositories.variable.ConfigRepository
import br.com.usinasantafe.cvf.infra.datasource.retrofit.variable.ConfigRetrofitDatasource
import br.com.usinasantafe.cvf.infra.datasource.sharedpreferences.ConfigSharedPreferencesDatasource
import br.com.usinasantafe.cvf.infra.models.retrofit.variable.entityToRetrofitModel
import br.com.usinasantafe.cvf.infra.models.sharedpreferences.entityToSharedPreferencesModel
import br.com.usinasantafe.cvf.infra.models.sharedpreferences.sharedPreferencesModelToEntity
import br.com.usinasantafe.cvf.lib.StatusSend
import br.com.usinasantafe.cvf.utils.EmptyResult
import br.com.usinasantafe.cvf.utils.call
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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
            val tokenFCM = configSharedPreferencesDatasource.getTokenFCM().getOrThrow()
            val model = entity.entityToRetrofitModel(tokenFCM)
            val configRetrofitModel = configRetrofitDatasource.recoverToken(model).getOrThrow()
            entity.idServ = configRetrofitModel.idServ
            entity
        }

    override suspend fun save(entity: Config): EmptyResult =
        call(getClassAndMethod()) {
            if(entity.tokenFCM == null) {
                entity.tokenFCM = configSharedPreferencesDatasource.getTokenFCM().getOrNull()
            }
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

    override suspend fun setStatusSend(statusSend: StatusSend): EmptyResult =
        call(getClassAndMethod()) {
            configSharedPreferencesDatasource.setStatusSend(statusSend).getOrThrow()
        }

    override suspend fun getPassword(): Result<String> =
        call(getClassAndMethod()) {
            configSharedPreferencesDatasource.getPassword().getOrThrow()
        }

    override suspend fun getFlagUpdate(): Result<Boolean> =
        call(getClassAndMethod()) {
            configSharedPreferencesDatasource.getFlagUpdate().getOrThrow()
        }

    override suspend fun setTokenFCM(token: String): Result<Boolean> =
        call(getClassAndMethod()) {
            configSharedPreferencesDatasource.setTokenFCM(token).getOrThrow()
        }

    override fun observe(): Flow<Config> =
        configSharedPreferencesDatasource.observe().map { it.sharedPreferencesModelToEntity() }

}