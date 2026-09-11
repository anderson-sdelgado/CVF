package br.com.usinasantafe.cvf.external.room.dao.variable

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import br.com.usinasantafe.cvf.infra.models.room.variable.HeaderRoomModel
import br.com.usinasantafe.cvf.lib.StatusSend
import br.com.usinasantafe.cvf.lib.TB_EQUIP
import br.com.usinasantafe.cvf.lib.TB_HEADER

@Dao
interface HeaderDao {

    @Insert
    suspend fun insert(header: HeaderRoomModel): Long

    @Query("UPDATE $TB_HEADER SET statusSend = :statusSend, idServ = :idServ WHERE id = :id")
    suspend fun updateStatusSend(statusSend: StatusSend, id: Int, idServ: Int)

    @Query("SELECT * FROM $TB_HEADER")
    suspend fun all(): List<HeaderRoomModel>

    @Query("SELECT EXISTS(SELECT 1 FROM $TB_HEADER WHERE statusSend = :statusSend)")
    suspend fun hasByStatusSend(statusSend: StatusSend): Boolean

    @Query("SELECT * FROM $TB_HEADER WHERE statusSend = :statusSend")
    suspend fun listByStatusSend(statusSend: StatusSend): List<HeaderRoomModel>

    @Query("DELETE FROM $TB_HEADER WHERE id = :id")
    suspend fun deleteById(id: Int)

}