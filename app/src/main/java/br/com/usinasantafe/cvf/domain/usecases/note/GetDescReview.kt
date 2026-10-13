package br.com.usinasantafe.cvf.domain.usecases.note

import android.content.Context
import br.com.usinasantafe.cvf.R
import br.com.usinasantafe.cvf.domain.entities.variable.Header
import br.com.usinasantafe.cvf.domain.repositories.stable.ColabRepository
import br.com.usinasantafe.cvf.domain.repositories.stable.EquipRepository
import br.com.usinasantafe.cvf.domain.repositories.stable.FrontRepository
import br.com.usinasantafe.cvf.domain.repositories.stable.ReleaseRepository
import br.com.usinasantafe.cvf.domain.repositories.variable.ManagerRepository
import br.com.usinasantafe.cvf.domain.repositories.variable.NoteRepository
import br.com.usinasantafe.cvf.domain.usecases.manager.GetTitleMenu
import br.com.usinasantafe.cvf.presenter.model.CartScreenModel
import br.com.usinasantafe.cvf.presenter.model.ReviewScreenModel
import br.com.usinasantafe.cvf.utils.call
import br.com.usinasantafe.cvf.utils.dateToString
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import br.com.usinasantafe.cvf.utils.required
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import java.util.Date
import javax.inject.Inject

interface GetDescReview {
    suspend operator fun invoke(): Result<ReviewScreenModel>
}



class IGetDescReview @Inject constructor(
    private val managerRepository: ManagerRepository,
    private val releaseRepository: ReleaseRepository,
    private val frontRepository: FrontRepository,
    private val noteRepository: NoteRepository,
    private val colabRepository: ColabRepository,
    private val equipRepository: EquipRepository
): GetDescReview {

    override suspend fun invoke(): Result<ReviewScreenModel> =
        call(getClassAndMethod()) {
            val idRelease = managerRepository.getIdRelease().getOrThrow().required("idRelease")
            val idFront = managerRepository.getIdFront().getOrThrow().required("idFront")
            val releaseEntity = releaseRepository.getById(idRelease).getOrThrow()
            val frontEntity = frontRepository.getById(idFront).getOrThrow()
            val regDriver = noteRepository.getRegDriver().getOrThrow().required("regDriver")
            val nameDriver = colabRepository.getNameByReg(regDriver).getOrThrow()
            val idTruck = noteRepository.getIdTruck().getOrThrow().required("idTruck")
            val equip = equipRepository.getById(idTruck).getOrThrow()
            val cartList = noteRepository.cartList().getOrThrow()
            val cartScreenModelList = cartList.map {
                val equip = equipRepository.getById(it.idCart).getOrThrow()
                CartScreenModel(
                    position = "${it.position}",
                    nro = "${equip.nro}",
                )
            }
            ReviewScreenModel(
                cdFront = "${frontEntity.cd}",
                idRelease = "${releaseEntity.id}",
                nroOSRelease = "${releaseEntity.nroOS}",
                descPropAgrRelease = releaseEntity.descPropAgr,
                regDriver = "$regDriver",
                nameDriver = nameDriver,
                nroTruck = "${equip.nro}",
                cartList = cartScreenModelList,
                dateHourExitField = Date().dateToString(),
            )
        }

}
