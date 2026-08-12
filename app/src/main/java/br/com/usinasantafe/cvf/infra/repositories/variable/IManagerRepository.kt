package br.com.usinasantafe.cvf.infra.repositories.variable

import br.com.usinasantafe.cvf.domain.repositories.variable.ManagerRepository
import br.com.usinasantafe.cvf.infra.datasource.sharedpreferences.ManagerSharedPreferencesDatasource
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

}