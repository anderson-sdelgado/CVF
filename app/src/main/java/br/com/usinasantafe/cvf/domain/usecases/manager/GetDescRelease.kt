package br.com.usinasantafe.cvf.domain.usecases.manager

import android.content.Context
import br.com.usinasantafe.cvf.R
import br.com.usinasantafe.cvf.domain.repositories.stable.ReleaseRepository
import br.com.usinasantafe.cvf.domain.repositories.variable.ManagerRepository
import br.com.usinasantafe.cvf.utils.call
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import br.com.usinasantafe.cvf.utils.required
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

interface GetDescRelease {
    suspend operator fun invoke(): Result<String>
}

class IGetDescRelease @Inject constructor(
    @ApplicationContext private val context: Context,
    private val releaseRepository: ReleaseRepository,
    private val managerRepository: ManagerRepository
): GetDescRelease {

    override suspend fun invoke(): Result<String> =
        call(getClassAndMethod()) {
            val idRelease = managerRepository.getIdRelease().getOrThrow().required("idRelease")
            val entity = releaseRepository.getById(idRelease).getOrThrow()
            context.getString(R.string.text_item_release, "${entity.id}", "${entity.nroOS}", entity.descPropAgr)
        }

}