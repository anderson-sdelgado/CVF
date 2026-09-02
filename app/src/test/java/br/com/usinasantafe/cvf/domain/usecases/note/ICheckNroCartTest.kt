package br.com.usinasantafe.cvf.domain.usecases.note

import br.com.usinasantafe.cvf.domain.repositories.stable.EquipRepository
import br.com.usinasantafe.cvf.domain.repositories.variable.NoteRepository
import br.com.usinasantafe.cvf.domain.usecases.common.Token
import br.com.usinasantafe.cvf.utils.CheckNetwork
import org.mockito.Mockito.mock

class ICheckNroCartTest {

    private val token = mock<Token>()
    private val checkNetwork = mock<CheckNetwork>()
    private val equipRepository = mock<EquipRepository>()
    private val noteRepository = mock<NoteRepository>()
    private val usecase = ICheckNroCart(
        token = token,
        checkNetwork = checkNetwork,
        equipRepository = equipRepository,
        noteRepository = noteRepository
    )


}