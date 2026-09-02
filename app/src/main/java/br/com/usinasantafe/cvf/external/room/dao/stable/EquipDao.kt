package br.com.usinasantafe.cvf.external.room.dao.stable

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import br.com.usinasantafe.cvf.infra.models.room.stable.EquipRoomModel
import br.com.usinasantafe.cvf.lib.TB_EQUIP

@Dao
interface EquipDao {

    @Insert
    suspend fun insertAll(list: List<EquipRoomModel>)

    @Query("DELETE FROM $TB_EQUIP")
    suspend fun deleteAll()

    @Query("SELECT * FROM $TB_EQUIP")
    suspend fun all(): List<EquipRoomModel>

    @Query("SELECT EXISTS(SELECT 1 FROM $TB_EQUIP WHERE nro = :nro AND type = :type)")
    suspend fun checkNroAndType(nro: Int, type: Int): Boolean

}