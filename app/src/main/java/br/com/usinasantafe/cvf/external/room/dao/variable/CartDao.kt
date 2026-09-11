package br.com.usinasantafe.cvf.external.room.dao.variable

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import br.com.usinasantafe.cvf.infra.models.room.variable.CartRoomModel
import br.com.usinasantafe.cvf.lib.TB_CART

@Dao
interface CartDao {

    @Insert
    suspend fun insert(cart: CartRoomModel): Long

    @Query("SELECT * FROM $TB_CART")
    suspend fun all(): List<CartRoomModel>

    @Query("SELECT * FROM $TB_CART WHERE idHeader = :idHeader")
    suspend fun listByIdHeader(idHeader: Int): List<CartRoomModel>

    @Query("DELETE FROM $TB_CART WHERE idHeader = :idHeader")
    suspend fun deleteByIdHeader(idHeader: Int)

}