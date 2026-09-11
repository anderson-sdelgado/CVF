package br.com.usinasantafe.cvf.external.room.dao.stable

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import br.com.usinasantafe.cvf.infra.models.room.stable.ColabRoomModel
import br.com.usinasantafe.cvf.lib.TB_COLAB

@Dao
interface ColabDao {

    @Insert
    suspend fun insert(colab: ColabRoomModel)

    @Insert
    suspend fun insertAll(list: List<ColabRoomModel>)

    @Query("DELETE FROM $TB_COLAB")
    suspend fun deleteAll()

    @Query("SELECT * FROM $TB_COLAB")
    suspend fun all(): List<ColabRoomModel>

    @Query("DELETE FROM $TB_COLAB WHERE reg = :reg")
    suspend fun deleteByReg(reg: Long)

    @Query("SELECT EXISTS(SELECT 1 FROM $TB_COLAB WHERE reg = :reg)")
    suspend fun hasByReg(reg: Long): Boolean

    @Query("SELECT name FROM $TB_COLAB WHERE reg = :reg")
    suspend fun getNameByReg(reg: Long): String
}