package br.com.usinasantafe.cvf.infra.repositories.variable

import br.com.usinasantafe.cvf.domain.entities.variable.Manager
import br.com.usinasantafe.cvf.domain.repositories.variable.ManagerRepository
import br.com.usinasantafe.cvf.infra.datasource.sharedpreferences.ManagerSharedPreferencesDatasource
import br.com.usinasantafe.cvf.infra.models.sharedpreferences.entityToSharedPreferencesModel
import br.com.usinasantafe.cvf.utils.EmptyResult
import br.com.usinasantafe.cvf.utils.call
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import javax.inject.Inject

class IManagerRepository @Inject constructor(
    private val managerSharedPreferencesDatasource: ManagerSharedPreferencesDatasource
): ManagerRepository {

    override suspend fun clean(): EmptyResult =
        call(getClassAndMethod()) {
            managerSharedPreferencesDatasource.clean().getOrThrow()
        }

    override suspend fun has(): Result<Boolean> =
        call(getClassAndMethod()) {
            managerSharedPreferencesDatasource.has().getOrThrow()
        }

    override suspend fun getIdFront(): Result<Int?> =
        call(getClassAndMethod()) {
            managerSharedPreferencesDatasource.getIdFront().getOrThrow()
        }

    override suspend fun getIdRelease(): Result<Int?> =
        call(getClassAndMethod()) {
            managerSharedPreferencesDatasource.getIdRelease().getOrThrow()
        }

    override suspend fun save(entity: Manager): EmptyResult =
        call(getClassAndMethod()) {
            managerSharedPreferencesDatasource.save(entity.entityToSharedPreferencesModel()).getOrThrow()
        }

}