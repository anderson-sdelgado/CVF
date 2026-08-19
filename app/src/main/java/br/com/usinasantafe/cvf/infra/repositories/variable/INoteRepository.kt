package br.com.usinasantafe.cvf.infra.repositories.variable

import br.com.usinasantafe.cvf.domain.repositories.variable.NoteRepository
import br.com.usinasantafe.cvf.utils.EmptyResult
import javax.inject.Inject

class INoteRepository @Inject constructor(

): NoteRepository {

    override suspend fun hasSend(): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun send(
        token: String,
        idServ: Int
    ): EmptyResult {
        TODO("Not yet implemented")
    }

    override suspend fun getRegDriver(): Result<Long> {
        TODO("Not yet implemented")
    }

    override suspend fun setRegDriver(reg: Long): EmptyResult {
        TODO("Not yet implemented")
    }

}