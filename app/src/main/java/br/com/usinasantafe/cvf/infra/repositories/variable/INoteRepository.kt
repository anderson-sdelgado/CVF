package br.com.usinasantafe.cvf.infra.repositories.variable

import br.com.usinasantafe.cvf.domain.repositories.variable.NoteRepository
import br.com.usinasantafe.cvf.infra.datasource.sharedpreferences.HeaderSharedPreferencesDatasource
import br.com.usinasantafe.cvf.infra.datasource.sharedpreferences.TrailerSharedPreferencesDatasource
import br.com.usinasantafe.cvf.utils.EmptyResult
import br.com.usinasantafe.cvf.utils.call
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import javax.inject.Inject

class INoteRepository @Inject constructor(
    private val headerSharedPreferencesDatasource: HeaderSharedPreferencesDatasource,
    private val trailerSharedPreferencesDatasource: TrailerSharedPreferencesDatasource
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

    override suspend fun getRegDriver(): Result<Long?> =
        call(getClassAndMethod()) {
            headerSharedPreferencesDatasource.getRegDriver().getOrThrow()
        }

    override suspend fun setRegDriver(reg: Long): EmptyResult =
        call(getClassAndMethod()) {
            headerSharedPreferencesDatasource.setRegDriver(reg).getOrThrow()
        }

    override suspend fun deleteNote(): EmptyResult =
        call(getClassAndMethod()) {
            trailerSharedPreferencesDatasource.clean().getOrThrow()
            headerSharedPreferencesDatasource.clean().getOrThrow()
        }

}