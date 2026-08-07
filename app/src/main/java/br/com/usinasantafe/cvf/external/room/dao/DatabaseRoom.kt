package br.com.usinasantafe.cvf.external.room.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import br.com.usinasantafe.cvf.external.room.dao.stable.*
import br.com.usinasantafe.cvf.infra.models.room.stable.*
import br.com.usinasantafe.cvf.lib.VERSION_DB
import java.util.Date

@Database(
    entities = [
        ColabRoomModel::class,
        EquipRoomModel::class,
        FrontRoomModel::class,
        ReleaseRoomModel::class,
    ],
    version = VERSION_DB,
    exportSchema = false,
)
@TypeConverters(Converters::class)
abstract class DatabaseRoom : RoomDatabase() {
    abstract fun colabDao(): ColabDao
    abstract fun equipDao(): EquipDao
    abstract fun frontDao(): FrontDao
    abstract fun releaseDao(): ReleaseDao
}

class Converters {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

}