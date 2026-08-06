package br.com.usinasantafe.cvf.external.room.dao.stable

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import br.com.usinasantafe.cvf.infra.models.room.stable.ColabRoomModel
import br.com.usinasantafe.cvf.lib.TB_COLAB

@Dao
interface ColabDao {

    @Insert
    fun insertAll(list: List<ColabRoomModel>)

    @Query("DELETE FROM $TB_COLAB")
    suspend fun deleteAll()

    @Query("SELECT * FROM $TB_COLAB")
    suspend fun all(): List<ColabRoomModel>

}