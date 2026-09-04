package br.com.usinasantafe.cvf.infra.repositories.variable

import br.com.usinasantafe.cvf.domain.entities.variable.Cart
import br.com.usinasantafe.cvf.domain.repositories.variable.NoteRepository
import br.com.usinasantafe.cvf.infra.datasource.sharedpreferences.HeaderSharedPreferencesDatasource
import br.com.usinasantafe.cvf.infra.datasource.sharedpreferences.CartSharedPreferencesDatasource
import br.com.usinasantafe.cvf.utils.EmptyResult
import br.com.usinasantafe.cvf.utils.call
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import javax.inject.Inject

class INoteRepository @Inject constructor(
    private val headerSharedPreferencesDatasource: HeaderSharedPreferencesDatasource,
    private val cartSharedPreferencesDatasource: CartSharedPreferencesDatasource
): NoteRepository {

    override suspend fun hasSend(): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun send(token: String, idServ: Int): EmptyResult {
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
            cartSharedPreferencesDatasource.clean().getOrThrow()
            headerSharedPreferencesDatasource.clean().getOrThrow()
        }

    override suspend fun finish(): EmptyResult =
        call(getClassAndMethod()) {
            TODO("Not yet implemented")
        }

    override suspend fun getIdTruck(): Result<Int> =
        call(getClassAndMethod()) {
            headerSharedPreferencesDatasource.getIdTruck().getOrThrow()
        }

    override suspend fun cartList(): Result<List<Cart>> {
        TODO("Not yet implemented")
    }

    override suspend fun setCart(entity: Cart): EmptyResult {
        TODO("Not yet implemented")
    }

    override suspend fun setIdTruck(id: Int): EmptyResult {
        TODO("Not yet implemented")
    }

}