package br.com.usinasantafe.cvf.external.room.datasource.stable

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import br.com.usinasantafe.cvf.external.room.dao.DatabaseRoom
import br.com.usinasantafe.cvf.external.room.dao.stable.EquipDao
import br.com.usinasantafe.cvf.infra.models.room.stable.EquipRoomModel
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.intArrayOf
import kotlin.test.assertEquals

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class IEquipRoomDatasourceTest {

    private lateinit var equipDao: EquipDao
    private lateinit var db: DatabaseRoom
    private lateinit var datasource: IEquipRoomDatasource

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, DatabaseRoom::class.java
        ).allowMainThreadQueries().build()
        equipDao = db.equipDao()
        datasource = IEquipRoomDatasource(equipDao)
    }

    @After
    fun tearDown() {
        db.clearAllTables()
        db.close()
    }

    @Test
    fun `addAll - Check failure if have row repeated`() =
        runTest {
            val qtdBefore = equipDao.all().size
            assertEquals(
                0,
                qtdBefore
            )
            val result = datasource.addAll(
                listOf(
                    EquipRoomModel(
                        id = 1,
                        nro = 1,
                        cdOperClass = 1,
                        description = "Test"
                    ),
                    EquipRoomModel(
                        id = 1,
                        nro = 1,
                        cdOperClass = 1,
                        description = "Test"
                    ),
                )
            )
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IEquipRoomDatasource.addAll",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "android.database.sqlite.SQLiteConstraintException: UNIQUE constraint failed: tb_equip.id (code 1555 SQLITE_CONSTRAINT_PRIMARYKEY)",
                result.exceptionOrNull()!!.cause.toString()
            )
            val qtdAfter = equipDao.all().size
            assertEquals(
                0,
                qtdAfter
            )
        }

    @Test
    fun `addAll - Check success if have row is correct`() =
        runTest {
            val qtdBefore = equipDao.all().size
            assertEquals(
                0,
                qtdBefore
            )
            val result = datasource.addAll(
                listOf(
                    EquipRoomModel(
                        id = 1,
                        nro = 1,
                        cdOperClass = 1,
                        description = "Test"
                    ),
                    EquipRoomModel(
                        id = 2,
                        nro = 2,
                        cdOperClass = 2,
                        description = "Test2"
                    ),
                )
            )
            assertEquals(
                true,
                result.isSuccess
            )
            val qtdAfter = equipDao.all().size
            assertEquals(
                2,
                qtdAfter
            )
            val list = equipDao.all()
            assertEquals(
                2,
                list.size
            )
            val model1 = list[0]
            assertEquals(
                EquipRoomModel(
                    id = 1,
                    nro = 1,
                    cdOperClass = 1,
                    description = "Test"
                ),
                model1
            )
            val model2 = list[1]
            assertEquals(
                EquipRoomModel(
                    id = 2,
                    nro = 2,
                    cdOperClass = 2,
                    description = "Test2"
                ),
                model2
            )
        }

    @Test
    fun `deleteAll - Check execution correct`() =
        runTest {
            equipDao.insertAll(
                listOf(
                    EquipRoomModel(
                        id = 1,
                        nro = 1,
                        cdOperClass = 1,
                        description = "Test"
                    )
                )
            )
            val listBefore = equipDao.all()
            assertEquals(
                1,
                listBefore.size
            )
            val result = datasource.deleteAll()
            assertEquals(
                true,
                result.isSuccess
            )
            val listAfter = equipDao.all()
            assertEquals(
                0,
                listAfter.size
            )
        }

}