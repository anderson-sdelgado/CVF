package br.com.usinasantafe.cvf.external.room.datasource.stable

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import br.com.usinasantafe.cvf.external.room.dao.DatabaseRoom
import br.com.usinasantafe.cvf.external.room.dao.stable.ColabDao
import br.com.usinasantafe.cvf.infra.models.room.stable.ColabRoomModel
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
class IColabRoomDatasourceTest {

    private lateinit var colabDao: ColabDao
    private lateinit var db: DatabaseRoom
    private lateinit var datasource: IColabRoomDatasource

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, DatabaseRoom::class.java
        ).allowMainThreadQueries().build()
        colabDao = db.colabDao()
        datasource = IColabRoomDatasource(colabDao)
    }

    @After
    fun tearDown() {
        db.clearAllTables()
        db.close()
    }

    @Test
    fun `addAll - Check failure if have row repeated`() =
        runTest {
            val qtdBefore = colabDao.all().size
            assertEquals(
                0,
                qtdBefore
            )
            val result = datasource.addAll(
                listOf(
                    ColabRoomModel(
                        reg = 1,
                        name = "TEST",
                    ),
                    ColabRoomModel(
                        reg = 1,
                        name = "TEST",
                    ),
                )
            )
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IColabRoomDatasource.addAll",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "android.database.sqlite.SQLiteConstraintException: UNIQUE constraint failed: tb_colab.reg (code 1555 SQLITE_CONSTRAINT_PRIMARYKEY)",
                result.exceptionOrNull()!!.cause.toString()
            )
            val qtdAfter = colabDao.all().size
            assertEquals(
                qtdAfter,
                0
            )
        }

    @Test
    fun `addAll - Check success if have row is correct`() =
        runTest {
            val qtdBefore = colabDao.all().size
            assertEquals(
                0,
                qtdBefore
            )
            val result = datasource.addAll(
                listOf(
                    ColabRoomModel(
                        reg = 1,
                        name = "TEST",
                    ),
                    ColabRoomModel(
                        reg = 2,
                        name = "TEST2",
                    ),
                )
            )
            assertEquals(
                true,
                result.isSuccess
            )
            val qtdAfter = colabDao.all().size
            assertEquals(
                2,
                qtdAfter
            )
            val list = colabDao.all()
            assertEquals(
                2,
                list.size
            )
            val model1 = list[0]
            assertEquals(
                ColabRoomModel(
                    reg = 1,
                    name = "TEST",
                ),
                model1
            )
            val model2 = list[1]
            assertEquals(
                ColabRoomModel(
                    reg = 2,
                    name = "TEST2",
                ),
                model2
            )
        }

    @Test
    fun `deleteAll - Check execution correct`() =
        runTest {
            colabDao.insertAll(
                listOf(
                    ColabRoomModel(
                        reg = 1,
                        name = "TEST",
                    )
                )
            )
            val listBefore = colabDao.all()
            assertEquals(
                1,
                listBefore.size
            )
            val result = datasource.deleteAll()
            assertEquals(
                true,
                result.isSuccess
            )
            val listAfter = colabDao.all()
            assertEquals(
                0,
                listAfter.size
            )
        }

}