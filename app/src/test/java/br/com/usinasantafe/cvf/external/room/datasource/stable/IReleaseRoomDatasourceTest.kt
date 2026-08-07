package br.com.usinasantafe.cvf.external.room.datasource.stable

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import br.com.usinasantafe.cvf.external.room.dao.DatabaseRoom
import br.com.usinasantafe.cvf.external.room.dao.stable.ReleaseDao
import br.com.usinasantafe.cvf.infra.models.room.stable.ReleaseRoomModel
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
class IReleaseRoomDatasourceTest {

    private lateinit var releaseDao: ReleaseDao
    private lateinit var db: DatabaseRoom
    private lateinit var datasource: IReleaseRoomDatasource

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, DatabaseRoom::class.java
        ).allowMainThreadQueries().build()
        releaseDao = db.releaseDao()
        datasource = IReleaseRoomDatasource(releaseDao)
    }

    @After
    fun tearDown() {
        db.clearAllTables()
        db.close()
    }

    @Test
    fun `addAll - Check failure if have row repeated`() =
        runTest {
            val list = releaseDao.all()
            assertEquals(
                list.size,
                0
            )
            val result = datasource.addAll(
                listOf(
                    ReleaseRoomModel(
                        id = 1,
                        nroOS = 1,
                        idPropAgr = 1,
                        descPropAgr = "Test",
                        idFront = 1
                    ),
                    ReleaseRoomModel(
                        id = 1,
                        nroOS = 1,
                        idPropAgr = 1,
                        descPropAgr = "Test",
                        idFront = 1
                    ),
                )
            )
            assertEquals(
                result.isFailure,
                true
            )
            assertEquals(
                result.exceptionOrNull()!!.message,
                "IReleaseRoomDatasource.addAll"
            )
            assertEquals(
                result.exceptionOrNull()!!.cause.toString(),
                "android.database.sqlite.SQLiteConstraintException: UNIQUE constraint failed: tb_release.id (code 1555 SQLITE_CONSTRAINT_PRIMARYKEY)"
            )
            val qtdAfter = releaseDao.all().size
            assertEquals(
                qtdAfter,
                0
            )
        }

    @Test
    fun `addAll - Check success if have row is correct`() =
        runTest {
            val qtdBefore = releaseDao.all().size
            assertEquals(
                qtdBefore,
                0
            )
            val result = datasource.addAll(
                listOf(
                    ReleaseRoomModel(
                        id = 1,
                        nroOS = 1,
                        idPropAgr = 1,
                        descPropAgr = "Test1",
                        idFront = 1
                    ),
                    ReleaseRoomModel(
                        id = 2,
                        nroOS = 2,
                        idPropAgr = 2,
                        descPropAgr = "Test2",
                        idFront = 2
                    ),
                )
            )
            assertEquals(
                result.isSuccess,
                true
            )
            val qtdAfter = releaseDao.all().size
            assertEquals(
                qtdAfter,
                2
            )
            val list = releaseDao.all()
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
                model1.nroOS,
                1
            )
            assertEquals(
                model1.idPropAgr,
                1
            )
            assertEquals(
                model1.descPropAgr,
                "Test1"
            )
            assertEquals(
                model1.idFront,
                1
            )
            val model2 = list[1]
            assertEquals(
                model2.id,
                2
            )
            assertEquals(
                model2.nroOS,
                2
            )
            assertEquals(
                model2.idPropAgr,
                2
            )
            assertEquals(
                model2.descPropAgr,
                "Test2"
            )
            assertEquals(
                model2.idFront,
                2
            )
        }

    @Test
    fun `deleteAll - Check execution correct`() =
        runTest {
            releaseDao.insertAll(
                listOf(
                    ReleaseRoomModel(
                        id = 1,
                        nroOS = 1,
                        idPropAgr = 1,
                        descPropAgr = "Test1",
                        idFront = 1
                    )
                )
            )
            val listBefore = releaseDao.all()
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
            val listAfter = releaseDao.all()
            assertEquals(
                listAfter.size,
                0
            )
        }

}