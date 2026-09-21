package br.com.usinasantafe.cvf.external.room.datasource.variable

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import br.com.usinasantafe.cvf.TestApp
import br.com.usinasantafe.cvf.external.room.dao.DatabaseRoom
import br.com.usinasantafe.cvf.external.room.dao.variable.CartDao
import br.com.usinasantafe.cvf.infra.models.room.variable.CartRoomModel
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
@Config(sdk = [34], application = TestApp::class)
class ICartRoomDatasourceTest {

    private lateinit var cartDao: CartDao
    private lateinit var db: DatabaseRoom
    private lateinit var datasource: ICartRoomDatasource

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, DatabaseRoom::class.java
        ).allowMainThreadQueries().build()
        cartDao = db.cartDao()
        datasource = ICartRoomDatasource(cartDao)
    }

    @After
    fun tearDown() {
        db.clearAllTables()
        db.close()
    }

    @Test
    fun `listByIdHeader - Check return empty list if not have row fielded`() =
        runTest {
            val result = datasource.listByIdHeader(1)
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
    fun `listByIdHeader - Check return list if have row fielded`() =
        runTest {
            cartDao.insert(
                CartRoomModel(
                    idHeader = 1,
                    position = 1,
                    idCart = 25
                )
            )
            cartDao.insert(
                CartRoomModel(
                    idHeader = 1,
                    position = 2,
                    idCart = 10
                )
            )
            cartDao.insert(
                CartRoomModel(
                    idHeader = 2,
                    position = 1,
                    idCart = 145
                )
            )
            val result = datasource.listByIdHeader(1)
            assertEquals(
                true,
                result.isSuccess
            )
            val list = result.getOrNull()!!
            assertEquals(
                2,
                list.size
            )
            val model1 = list[0]
            assertEquals(
                CartRoomModel(
                    id = 1,
                    idHeader = 1,
                    position = 1,
                    idCart = 25
                ),
                model1
            )
            val model2 = list[1]
            assertEquals(
                CartRoomModel(
                    id = 2,
                    idHeader = 1,
                    position = 2,
                    idCart = 10
                ),
                model2
            )
        }

    @Test
    fun `save - Check failure if have row repeated`() =
        runTest {
            cartDao.insert(
                CartRoomModel(
                    id = 1,
                    idHeader = 1,
                    position = 1,
                    idCart = 25
                )
            )
            val result = datasource.save(
                CartRoomModel(
                    id = 1,
                    idHeader = 1,
                    position = 1,
                    idCart = 25
                )
            )
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "ICartRoomDatasource.save",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "android.database.sqlite.SQLiteConstraintException: UNIQUE constraint failed: tb_cart.id (code 1555 SQLITE_CONSTRAINT_PRIMARYKEY)",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `save - Check data if process executed successfully`() =
        runTest {
            cartDao.insert(
                CartRoomModel(
                    idHeader = 1,
                    position = 1,
                    idCart = 25
                )
            )
            val result = datasource.save(
                CartRoomModel(
                    idHeader = 1,
                    position = 2,
                    idCart = 63
                )
            )
            assertEquals(
                true,
                result.isSuccess
            )
            val list = cartDao.all()
            assertEquals(
                2,
                list.size
            )
            val model1 = list[0]
            assertEquals(
                CartRoomModel(
                    id = 1,
                    idHeader = 1,
                    position = 1,
                    idCart = 25
                ),
                model1
            )
            val model2 = list[1]
            assertEquals(
                CartRoomModel(
                    id = 2,
                    idHeader = 1,
                    position = 2,
                    idCart = 63
                ),
                model2
            )
        }

    @Test
    fun `deleteByIdHeader - Check data if process executed successfully`() =
        runTest {
            cartDao.insert(
                CartRoomModel(
                    idHeader = 1,
                    position = 1,
                    idCart = 25
                )
            )
            cartDao.insert(
                CartRoomModel(
                    idHeader = 1,
                    position = 2,
                    idCart = 63
                )
            )
            cartDao.insert(
                CartRoomModel(
                    idHeader = 2,
                    position = 1,
                    idCart = 145
                )
            )
            val listBefore = cartDao.all()
            assertEquals(
                3,
                listBefore.size
            )
            val modelBefore1 = listBefore[0]
            assertEquals(
                CartRoomModel(
                    id = 1,
                    idHeader = 1,
                    position = 1,
                    idCart = 25
                ),
                modelBefore1
            )
            val modelBefore2 = listBefore[1]
            assertEquals(
                CartRoomModel(
                    id = 2,
                    idHeader = 1,
                    position = 2,
                    idCart = 63
                ),
                modelBefore2
            )
            val modelBefore3 = listBefore[2]
            assertEquals(
                CartRoomModel(
                    id = 3,
                    idHeader = 2,
                    position = 1,
                    idCart = 145
                ),
                modelBefore3
            )
            val result = datasource.deleteByIdHeader(1)
            assertEquals(
                true,
                result.isSuccess
            )
            val listAfter = cartDao.all()
            assertEquals(
                1,
                listAfter.size
            )
            val modelAfter = listAfter[0]
            assertEquals(
                CartRoomModel(
                    id = 3,
                    idHeader = 2,
                    position = 1,
                    idCart = 145
                ),
                modelAfter
            )
        }
}