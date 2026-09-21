package br.com.usinasantafe.cvf.domain.usecases.note

import android.content.Context
import br.com.usinasantafe.cvf.R
import br.com.usinasantafe.cvf.domain.repositories.stable.ColabRepository
import br.com.usinasantafe.cvf.domain.repositories.stable.EquipRepository
import br.com.usinasantafe.cvf.domain.repositories.variable.NoteRepository
import br.com.usinasantafe.cvf.domain.usecases.manager.GetTitleMenu
import br.com.usinasantafe.cvf.utils.call
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import br.com.usinasantafe.cvf.utils.required
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject

interface GetDescReview {
    suspend operator fun invoke(): Result<String>
}

class IGetDescReview @Inject constructor(
    @ApplicationContext private val context: Context,
    private val getTitleMenu: GetTitleMenu,
    private val noteRepository: NoteRepository,
    private val colabRepository: ColabRepository,
    private val equipRepository: EquipRepository
): GetDescReview {

    override suspend fun invoke(): Result<String> =
        call(getClassAndMethod()) {
            val title = getTitleMenu().first()
            val regDriver = noteRepository.getRegDriver().getOrThrow().required("regDriver")
            val nameDriver = colabRepository.getNameByReg(regDriver).getOrThrow()
            val idTruck = noteRepository.getIdTruck().getOrThrow().required("idTruck")
            val equip = equipRepository.getById(idTruck).getOrThrow()
            val header = context.getString(
                R.string.text_header_note,
                "$regDriver",
                nameDriver,
                "${equip.nro}",
                equip.descOperClass
            )
            val cartList = noteRepository.cartList().getOrThrow()
            val descCartList = cartList.map {
                val equip = equipRepository.getById(it.idCart).getOrThrow()
                context.getString(
                    R.string.text_cart_note,
                    "${it.position}",
                    "${equip.nro}",
                    equip.descOperClass
                )
            }
            "${title}\n\n${header}\n${descCartList.joinToString("\n")}"
        }

}