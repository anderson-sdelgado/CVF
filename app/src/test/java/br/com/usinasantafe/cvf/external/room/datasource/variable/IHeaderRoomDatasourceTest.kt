package br.com.usinasantafe.cvf.external.room.datasource.variable

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import br.com.usinasantafe.cvf.external.room.dao.DatabaseRoom
import br.com.usinasantafe.cvf.external.room.dao.variable.HeaderDao
import br.com.usinasantafe.cvf.infra.models.room.variable.HeaderRoomModel
import br.com.usinasantafe.cvf.lib.StatusSend
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
class IHeaderRoomDatasourceTest {

    private lateinit var headerDao: HeaderDao
    private lateinit var db: DatabaseRoom
    private lateinit var datasource: IHeaderRoomDatasource

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, DatabaseRoom::class.java
        ).allowMainThreadQueries().build()
        headerDao = db.headerDao()
        datasource = IHeaderRoomDatasource(headerDao)
    }

    @After
    fun tearDown() {
        db.clearAllTables()
        db.close()
    }

    @Test
    fun `hasByStatusSend - Check return false if not have row fielded`() =
        runTest {
            headerDao.insert(
                HeaderRoomModel(
                    regDriver = 123,
                    idTruck = 456,
                    statusSend = StatusSend.STARTED
                )
            )
            val result = datasource.hasByStatusSend(StatusSend.SEND)
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                false,
                result.getOrNull()!!
            )
        }

    @Test
    fun `hasByStatusSend - Check return true if have row fielded`() =
        runTest {
            headerDao.insert(
                HeaderRoomModel(
                    regDriver = 123,
                    idTruck = 456,
                    statusSend = StatusSend.SEND
                )
            )
            val result = datasource.hasByStatusSend(StatusSend.SEND)
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                false,
                result.getOrNull()!!
            )
        }

    @Test
    fun `listByStatusSend - Check return empty list if not have row fielded`() =
        runTest {
            headerDao.insert(
                HeaderRoomModel(
                    regDriver = 123,
                    idTruck = 456,
                    statusSend = StatusSend.STARTED
                )
            )
            val result = datasource.listByStatusSend(StatusSend.SEND)
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                emptyList(),
                result.getOrNull()!!
            )
        }

    @Test
    fun `listByStatusSend - Check return list if have row fielded`() =
        runTest {
            headerDao.insert(
                HeaderRoomModel(
                    regDriver = 123,
                    idTruck = 456,
                    statusSend = StatusSend.SEND
                )
            )
            val result = datasource.listByStatusSend(StatusSend.SEND)
            assertEquals(
                true,
                result.isSuccess
            )
            val list = result.getOrNull()!!
            assertEquals(
                1,
                list.size
            )
            val model = list[0]
            assertEquals(
                HeaderRoomModel(
                    id = 1,
                    regDriver = 123,
                    idTruck = 456,
                    statusSend = StatusSend.SEND
                ).copy(dateHour = model.dateHour),
                model
            )
        }

    @Test
    fun `updateStatusSend - Check return failure if id is null`() =
        runTest {
            val result = datasource.updateStatusSend(
                StatusSend.SENT,
                listOf(
                    HeaderRoomModel(
                        regDriver = 123,
                        idTruck = 456
                    )
                )
            )
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IHeaderRoomDatasource.updateStatusSend",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.NullPointerException: id is required",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `updateStatusSend - Check return failure if idServ is null`() =
        runTest {
            val result = datasource.updateStatusSend(
                StatusSend.SENT,
                listOf(
                    HeaderRoomModel(
                        id = 1,
                        regDriver = 123,
                        idTruck = 456
                    )
                )
            )
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IHeaderRoomDatasource.updateStatusSend",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.NullPointerException: idServ is required",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `updateStatusSend - Check update data if process executed successfully`() =
        runTest {
            headerDao.insert(
                HeaderRoomModel(
                    id = 1,
                    regDriver = 123,
                    idTruck = 456
                )
            )
            headerDao.insert(
                HeaderRoomModel(
                    id = 2,
                    regDriver = 100100,
                    idTruck = 100
                )
            )
            val listBefore = headerDao.all()
            assertEquals(
                2,
                listBefore.size
            )
            val modelBefore1 = listBefore[0]
            assertEquals(
                HeaderRoomModel(
                    id = 1,
                    regDriver = 123,
                    idTruck = 456,
                    idServ = null,
                    statusSend = StatusSend.SEND
                ).copy(dateHour = modelBefore1.dateHour),
                modelBefore1
            )
            val modelBefore2 = listBefore[1]
            assertEquals(
                HeaderRoomModel(
                    id = 2,
                    regDriver = 100100,
                    idTruck = 100,
                    idServ = null,
                    statusSend = StatusSend.SEND
                    ).copy(dateHour = modelBefore2.dateHour),
                modelBefore2
            )
            val result = datasource.updateStatusSend(
                StatusSend.SENT,
                listOf(
                    HeaderRoomModel(
                        id = 1,
                        regDriver = 123,
                        idTruck = 456,
                        idServ = 112,
                    ),
                    HeaderRoomModel(
                        id = 2,
                        regDriver = 100100,
                        idTruck = 100,
                        idServ = 113
                    )
                )
            )
            assertEquals(
                true,
                result.isSuccess
            )
            val listAfter = headerDao.all()
            assertEquals(
                2,
                listAfter.size
            )
            val modelAfter1 = listAfter[0]
            assertEquals(
                HeaderRoomModel(
                    id = 1,
                    regDriver = 123,
                    idTruck = 456,
                    idServ = 112,
                    statusSend = StatusSend.SENT
                    ).copy(dateHour = modelAfter1.dateHour),
                modelAfter1
            )
            val modelAfter2 = listAfter[1]
            assertEquals(
                HeaderRoomModel(
                    id = 2,
                    regDriver = 100100,
                    idTruck = 100,
                    idServ = 113,
                    statusSend = StatusSend.SENT
                    ).copy(dateHour = modelAfter2.dateHour),
                modelAfter2
            )
        }

    @Test
    fun `save - Check failure if have row repeated`() =
        runTest {
            headerDao.insert(
                HeaderRoomModel(
                    id = 1,
                    regDriver = 123,
                    idTruck = 456
                )
            )
            val result = datasource.save(
                HeaderRoomModel(
                    id = 1,
                    regDriver = 123,
                    idTruck = 456
                )
            )
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IHeaderRoomDatasource.save",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "android.database.sqlite.SQLiteConstraintException: UNIQUE constraint failed: tb_header.id (code 1555 SQLITE_CONSTRAINT_PRIMARYKEY)",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `save - Check data if process executed successfully`() =
        runTest {
            headerDao.insert(
                HeaderRoomModel(
                    regDriver = 123,
                    idTruck = 456
                )
            )
            val result = datasource.save(
                HeaderRoomModel(
                    regDriver = 100100,
                    idTruck = 100
                )
            )
            assertEquals(
                true,
                result.isSuccess
            )
            val list = headerDao.all()
            assertEquals(
                2,
                list.size
            )
            val model1 = list[0]
            assertEquals(
                HeaderRoomModel(
                    id = 1,
                    regDriver = 123,
                    idTruck = 456,
                    idServ = null,
                    statusSend = StatusSend.SEND
                ).copy(dateHour = model1.dateHour),
                model1
            )
            val model2 = list[1]
            assertEquals(
                HeaderRoomModel(
                    id = 2,
                    regDriver = 100100,
                    idTruck = 100,
                    idServ = null,
                    statusSend = StatusSend.SEND
                    ).copy(dateHour = model2.dateHour),
                model2
            )
        }

    @Test
    fun `delete - Check data if process executed successfully`() =
        runTest {
            headerDao.insert(
                HeaderRoomModel(
                    id = 1,
                    regDriver = 123,
                    idTruck = 456
                )
            )
            headerDao.insert(
                HeaderRoomModel(
                    id = 2,
                    regDriver = 100100,
                    idTruck = 100
                )
            )
            val listBefore = headerDao.all()
            assertEquals(
                2,
                listBefore.size
            )
            val modelBefore1 = listBefore[0]
            assertEquals(
                HeaderRoomModel(
                    id = 1,
                    regDriver = 123,
                    idTruck = 456
                ).copy(dateHour = modelBefore1.dateHour),
                modelBefore1
            )
            val modelBefore2 = listBefore[1]
            assertEquals(
                HeaderRoomModel(
                    id = 2,
                    regDriver = 100100,
                    idTruck = 100
                ).copy(dateHour = modelBefore2.dateHour),
                modelBefore2
            )
            val result = datasource.delete(1)
            assertEquals(
                true,
                result.isSuccess
            )
            val listAfter = headerDao.all()
            assertEquals(
                1,
                listAfter.size
            )
            val modelAfter1 = listAfter[0]
            assertEquals(
                HeaderRoomModel(
                    id = 2,
                    regDriver = 100100,
                    idTruck = 100
                ).copy(dateHour = modelBefore2.dateHour),
                modelAfter1
            )
        }
}