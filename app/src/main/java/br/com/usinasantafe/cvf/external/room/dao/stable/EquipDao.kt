package br.com.usinasantafe.cvf.external.room.dao.stable

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import br.com.usinasantafe.cvf.infra.models.room.stable.EquipRoomModel
import br.com.usinasantafe.cvf.lib.TB_EQUIP
import br.com.usinasantafe.cvf.lib.TypeEquip

@Dao
interface EquipDao {

    @Insert
    suspend fun insert(equip: EquipRoomModel)

    @Insert
    suspend fun insertAll(list: List<EquipRoomModel>)

    @Query("DELETE FROM $TB_EQUIP")
    suspend fun deleteAll()

    @Query("SELECT * FROM $TB_EQUIP")
    suspend fun all(): List<EquipRoomModel>

    @Query("SELECT EXISTS(SELECT 1 FROM $TB_EQUIP WHERE nro = :nro AND type = :type)")
    suspend fun hasNroAndType(nro: Int, type: Int): Boolean

    @Query("DELETE FROM $TB_EQUIP WHERE nro = :nro")
    suspend fun deleteByNro(nro: Int)

    @Query("SELECT EXISTS(SELECT 1 FROM $TB_EQUIP WHERE nro = :nro)")
    suspend fun hasByNro(nro: Int): Boolean

    @Query("SELECT type FROM $TB_EQUIP WHERE nro = :nro")
    suspend fun getTypeByNro(nro: Int): TypeEquip

    @Query("SELECT * FROM $TB_EQUIP WHERE id = :id")
    suspend fun getById(id: Int): EquipRoomModel

    @Query("SELECT id FROM $TB_EQUIP WHERE nro = :nro")
    suspend fun getIdByNro(nro: Int): Int

    @Query("SELECT cdOperClass FROM $TB_EQUIP WHERE nro = :nro")
    suspend fun getCdClassOperByNro(nro: Int): Int

}