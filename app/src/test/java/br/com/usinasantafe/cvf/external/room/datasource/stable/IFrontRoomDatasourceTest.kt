package br.com.usinasantafe.cvf.external.room.datasource.stable

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import br.com.usinasantafe.cvf.external.room.dao.DatabaseRoom
import br.com.usinasantafe.cvf.external.room.dao.stable.FrontDao
import br.com.usinasantafe.cvf.infra.models.room.stable.FrontRoomModel
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
class IFrontRoomDatasourceTest {

    private lateinit var frontDao: FrontDao
    private lateinit var db: DatabaseRoom
    private lateinit var datasource: IFrontRoomDatasource

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, DatabaseRoom::class.java
        ).allowMainThreadQueries().build()
        frontDao = db.frontDao()
        datasource = IFrontRoomDatasource(frontDao)
    }

    @After
    fun tearDown() {
        db.clearAllTables()
        db.close()
    }

    @Test
    fun `addAll - Check failure if have row repeated`() =
        runTest {
            val list = frontDao.all()
            assertEquals(
                list.size,
                0
            )
            val result = datasource.addAll(
                listOf(
                    FrontRoomModel(
                        id = 1,
                        cd = 1,
                        description = "Test"
                    ),
                    FrontRoomModel(
                        id = 1,
                        cd = 1,
                        description = "Test"
                    ),
                )
            )
            assertEquals(
                result.isFailure,
                true
            )
            assertEquals(
                result.exceptionOrNull()!!.message,
                "IFrontRoomDatasource.addAll"
            )
            assertEquals(
                result.exceptionOrNull()!!.cause.toString(),
                "android.database.sqlite.SQLiteConstraintException: UNIQUE constraint failed: tb_front.id (code 1555 SQLITE_CONSTRAINT_PRIMARYKEY)"
            )
            val qtdAfter = frontDao.all().size
            assertEquals(
                qtdAfter,
                0
            )
        }

    @Test
    fun `addAll - Check success if have row is correct`() =
        runTest {
            val qtdBefore = frontDao.all().size
            assertEquals(
                qtdBefore,
                0
            )
            val result = datasource.addAll(
                listOf(
                    FrontRoomModel(
                        id = 1,
                        cd = 1,
                        description = "Test1"
                    ),
                    FrontRoomModel(
                        id = 2,
                        cd = 2,
                        description = "Test2"
                    ),
                )
            )
            assertEquals(
                result.isSuccess,
                true
            )
            val qtdAfter = frontDao.all().size
            assertEquals(
                qtdAfter,
                2
            )
            val list = frontDao.all()
            assertEquals(
                list.size,
                2
            )
            val model1 = list[0]
            assertEquals(
                model1.id,
                1
            )
            assertEquals(
                model1.cd,
                1
            )
            assertEquals(
                model1.description,
                "Test1"
            )
            val model2 = list[1]
            assertEquals(
                model2.id,
                2
            )
            assertEquals(
                model2.cd,
                2
            )
            assertEquals(
                model2.description,
                "Test2"
            )
        }

    @Test
    fun `deleteAll - Check execution correct`() =
        runTest {
            frontDao.insertAll(
                listOf(
                    FrontRoomModel(
                        id = 1,
                        cd = 1,
                        description = "Test1"
                    )
                )
            )
            val listBefore = frontDao.all()
            assertEquals(
                listBefore.size,
                1
            )
            val result = datasource.deleteAll()
            assertEquals(
                result.isSuccess,
                true
            )
            assertEquals(
                result.getOrNull()!!,
                Unit
            )
            val listAfter = frontDao.all()
            assertEquals(
                listAfter.size,
                0
            )
        }

}