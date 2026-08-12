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
            val qtdBefore = frontDao.all().size
            assertEquals(
                0,
                qtdBefore
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
                true,
                result.isFailure
            )
            assertEquals(
                "IFrontRoomDatasource.addAll",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "android.database.sqlite.SQLiteConstraintException: UNIQUE constraint failed: tb_front.id (code 1555 SQLITE_CONSTRAINT_PRIMARYKEY)",
                result.exceptionOrNull()!!.cause.toString()
            )
            val qtdAfter = frontDao.all().size
            assertEquals(
                0,
                qtdAfter
            )
        }

    @Test
    fun `addAll - Check success if have row is correct`() =
        runTest {
            val qtdBefore = frontDao.all().size
            assertEquals(
                0,
                qtdBefore
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
                true,
                result.isSuccess
            )
            val qtdAfter = frontDao.all().size
            assertEquals(
                2,
                qtdAfter
            )
            val list = frontDao.all()
            assertEquals(
                2,
                list.size
            )
            val model1 = list[0]
            assertEquals(
                FrontRoomModel(
                    id = 1,
                    cd = 1,
                    description = "Test1"
                ),
                model1
            )
            val model2 = list[1]
            assertEquals(
                FrontRoomModel(
                    id = 2,
                    cd = 2,
                    description = "Test2"
                ),
                model2
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
                1,
                listBefore.size
            )
            val result = datasource.deleteAll()
            assertEquals(
                true,
                result.isSuccess
            )
            val listAfter = frontDao.all()
            assertEquals(
                0,
                listAfter.size
            )
        }

    @Test
    fun `listAll - Check return empty list if not have row in table`() =
        runTest {
            val result = datasource.listAll()
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                emptyList(),
                result.getOrNull()
            )
        }

    @Test
    fun `listAll - Check return list if have row in table`() =
        runTest {
            frontDao.insertAll(
                listOf(
                    FrontRoomModel(
                        id = 1,
                        cd = 1,
                        description = "Test1"
                    ),
                    FrontRoomModel(
                        id = 3,
                        cd = 3,
                        description = "Test3"
                    ),
                    FrontRoomModel(
                        id = 2,
                        cd = 2,
                        description = "Test2"
                    ),
                )
            )
            val result = datasource.listAll()
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
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
                    FrontRoomModel(
                        id = 3,
                        cd = 3,
                        description = "Test3"
                    ),
                ),
                result.getOrNull()
            )
        }
}