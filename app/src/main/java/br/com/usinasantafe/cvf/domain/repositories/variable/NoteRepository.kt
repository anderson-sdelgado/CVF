package br.com.usinasantafe.cvf.domain.repositories.variable

import br.com.usinasantafe.cvf.utils.EmptyResult

interface NoteRepository {
    suspend fun hasSend(): Result<Boolean>
    suspend fun send(token: String, idServ: Int): EmptyResult
    suspend fun getRegDriver(): Result<Long>
    suspend fun setRegDriver(reg: Long): EmptyResult
}