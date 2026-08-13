package br.com.usinasantafe.cvf.domain.usecases.manager

import android.content.Context
import br.com.usinasantafe.cvf.R
import br.com.usinasantafe.cvf.domain.repositories.stable.ReleaseRepository
import br.com.usinasantafe.cvf.domain.repositories.variable.ManagerRepository
import br.com.usinasantafe.cvf.presenter.model.ItemCheckBoxScreenModel
import br.com.usinasantafe.cvf.utils.call
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

interface ListRelease {
    suspend operator fun invoke(idFront: Int): Result<List<ItemCheckBoxScreenModel>>
}

class IListRelease @Inject constructor(
    @ApplicationContext private val context: Context,
    private val releaseRepository: ReleaseRepository,
    private val managerRepository: ManagerRepository
): ListRelease {

    override suspend fun invoke(idFront: Int): Result<List<ItemCheckBoxScreenModel>> =
        call(getClassAndMethod()) {
            val idFront = if(idFront > 0) idFront else managerRepository.getIdFront().getOrThrow() ?: 0
            val releaseList = releaseRepository.listByIdFront(idFront).getOrThrow()
            val idRelease = managerRepository.getIdRelease().getOrThrow()
            releaseList.map {
                val desc = context.getString(R.string.text_item_release, "${it.id}", "${it.nroOS}", it.descPropAgr)
                ItemCheckBoxScreenModel(
                    id = it.id,
                    desc = desc,
                    flag = it.id == idRelease
                )
            }
        }

}