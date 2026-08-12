package br.com.usinasantafe.cvf.domain.usecases.manager

import br.com.usinasantafe.cvf.domain.repositories.stable.FrontRepository
import br.com.usinasantafe.cvf.domain.repositories.variable.ManagerRepository
import br.com.usinasantafe.cvf.presenter.model.ItemCheckBoxScreenModel
import br.com.usinasantafe.cvf.utils.call
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import javax.inject.Inject

interface ListFront {
    suspend operator fun invoke(): Result<List<ItemCheckBoxScreenModel>>
}

class IListFront @Inject constructor(
    private val frontRepository: FrontRepository,
    private val managerRepository: ManagerRepository
): ListFront {

    override suspend fun invoke(): Result<List<ItemCheckBoxScreenModel>> =
        call(getClassAndMethod()) {
            val frontList = frontRepository.listAll().getOrThrow()
            val idFront = managerRepository.getIdFront().getOrThrow()
            frontList.map {
                ItemCheckBoxScreenModel(
                    id = it.id,
                    desc = it.description,
                    flag = it.id == idFront
                )
            }
        }

}