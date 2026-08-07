package br.com.usinasantafe.cvf.external.room.dao.stable

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import br.com.usinasantafe.cvf.infra.models.room.stable.ReleaseRoomModel
import br.com.usinasantafe.cvf.lib.TB_RELEASE

@Dao
interface ReleaseDao {

    @Insert
    fun insertAll(list: List<ReleaseRoomModel>)

    @Query("DELETE FROM $TB_RELEASE")
    suspend fun deleteAll()

    @Query("SELECT * FROM $TB_RELEASE")
    suspend fun all(): List<ReleaseRoomModel>

}