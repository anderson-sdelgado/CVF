package br.com.usinasantafe.cvf.domain.repositories.variable

import br.com.usinasantafe.cvf.domain.entities.variable.Cart
import br.com.usinasantafe.cvf.utils.EmptyResult
import java.util.Date

interface NoteRepository {
    suspend fun hasSend(): Result<Boolean>
    suspend fun send(token: String, idConfigServ: Int, idFront: Int, idRelease: Int): EmptyResult
    suspend fun getRegDriver(): Result<Long?>
    suspend fun setRegDriver(reg: Long): EmptyResult
    suspend fun deleteNote(): EmptyResult
    suspend fun finish(): EmptyResult
    suspend fun getIdTruck(): Result<Int?>
    suspend fun cartList(): Result<List<Cart>>
    suspend fun setCart(entity: Cart): EmptyResult
    suspend fun setTruck(id: Int, nro: Int): EmptyResult
    suspend fun clean(): EmptyResult
    suspend fun setDate(dateHourExitMill: Date, dateHourArrivalField: Date): EmptyResult
}