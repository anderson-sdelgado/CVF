package br.com.usinasantafe.cvf.domain.usecases.manager

import android.content.Context
import br.com.usinasantafe.cvf.R
import br.com.usinasantafe.cvf.domain.repositories.stable.FrontRepository
import br.com.usinasantafe.cvf.domain.repositories.stable.ReleaseRepository
import br.com.usinasantafe.cvf.domain.repositories.variable.ManagerRepository
import br.com.usinasantafe.cvf.utils.call
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import br.com.usinasantafe.cvf.utils.required
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

interface GetTitleMenu {
    suspend operator fun invoke(): Result<String>
}

class IGetTitleMenu @Inject constructor(
    @ApplicationContext private val context: Context,
    private val releaseRepository: ReleaseRepository,
    private val managerRepository: ManagerRepository,
    private val frontRepository: FrontRepository
): GetTitleMenu {

    override suspend fun invoke(): Result<String> =
        call(getClassAndMethod()) {
            val idRelease = managerRepository.getIdRelease().getOrThrow().required("idRelease")
            val idFront = managerRepository.getIdFront().getOrThrow().required("idFront")
            val releaseEntity = releaseRepository.getById(idRelease).getOrThrow()
            val frontEntity = frontRepository.getById(idFront).getOrThrow()
            context.getString(R.string.text_data_menu, frontEntity.description, "${releaseEntity.id}", "${releaseEntity.nroOS}", releaseEntity.descPropAgr)
        }

}