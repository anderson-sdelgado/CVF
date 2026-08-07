package br.com.usinasantafe.cvf.external.room.dao.stable

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import br.com.usinasantafe.cvf.infra.models.room.stable.FrontRoomModel
import br.com.usinasantafe.cvf.lib.TB_FRONT

@Dao
interface FrontDao {

    @Insert
    fun insertAll(list: List<FrontRoomModel>)

    @Query("DELETE FROM $TB_FRONT")
    suspend fun deleteAll()

    @Query("SELECT * FROM $TB_FRONT")
    suspend fun all(): List<FrontRoomModel>

}