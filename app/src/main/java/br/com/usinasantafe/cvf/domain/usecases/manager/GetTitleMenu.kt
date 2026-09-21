package br.com.usinasantafe.cvf.domain.usecases.manager

import android.content.Context
import br.com.usinasantafe.cvf.R
import br.com.usinasantafe.cvf.domain.repositories.stable.FrontRepository
import br.com.usinasantafe.cvf.domain.repositories.stable.ReleaseRepository
import br.com.usinasantafe.cvf.domain.repositories.variable.ManagerRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface GetTitleMenu {
    operator fun invoke(): Flow<String>
}

class IGetTitleMenu @Inject constructor(
    @ApplicationContext private val context: Context,
    private val releaseRepository: ReleaseRepository,
    private val managerRepository: ManagerRepository,
    private val frontRepository: FrontRepository
): GetTitleMenu {

    override fun invoke(): Flow<String> =
        managerRepository.observe().map { manager ->
            if (manager.idRelease == null || manager.idFront == null) {
                ""
            } else {
                val releaseEntity = releaseRepository.getById(manager.idRelease).getOrNull()
                val frontEntity = frontRepository.getById(manager.idFront).getOrNull()
                if (releaseEntity == null || frontEntity == null) {
                    ""
                } else {
                    context.getString(
                        R.string.text_data_menu,
                        frontEntity.description,
                        "${releaseEntity.id}",
                        "${releaseEntity.nroOS}",
                        releaseEntity.descPropAgr
                    )
                }
            }
        }

}
