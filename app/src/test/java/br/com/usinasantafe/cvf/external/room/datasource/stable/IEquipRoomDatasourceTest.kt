package br.com.usinasantafe.cvf.external.room.datasource.stable

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import br.com.usinasantafe.cvf.external.room.dao.DatabaseRoom
import br.com.usinasantafe.cvf.external.room.dao.stable.EquipDao
import br.com.usinasantafe.cvf.infra.models.room.stable.EquipRoomModel
import br.com.usinasantafe.cvf.lib.TypeEquip
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
                        descOperClass = "Test",
                        type = TypeEquip.CART
                    ),
                    EquipRoomModel(
                        id = 1,
                        nro = 1,
                        cdOperClass = 1,
                        descOperClass = "Test",
                        type = TypeEquip.CART
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
                        descOperClass = "Test",
                        type = TypeEquip.CART
                    ),
                    EquipRoomModel(
                        id = 2,
                        nro = 2,
                        cdOperClass = 2,
                        descOperClass = "Test2",
                        type = TypeEquip.CART
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
                    descOperClass = "Test",
                    type = TypeEquip.CART
                ),
                model1
            )
            val model2 = list[1]
            assertEquals(
                EquipRoomModel(
                    id = 2,
                    nro = 2,
                    cdOperClass = 2,
                    descOperClass = "Test2",
                    type = TypeEquip.CART
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
                        descOperClass = "Test",
                        type = TypeEquip.CART
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

    @Test
    fun `deleteByNro - Check execution correct`() =
        runTest {
            equipDao.insertAll(
                listOf(
                    EquipRoomModel(
                        id = 1,
                        nro = 100,
                        cdOperClass = 20,
                        descOperClass = "Test",
                        type = TypeEquip.CART
                    ),
                    EquipRoomModel(
                        id = 2,
                        nro = 124,
                        cdOperClass = 30,
                        descOperClass = "Test2",
                        type = TypeEquip.CART
                    ),
                )
            )
            val listBefore = equipDao.all()
            assertEquals(
                2,
                listBefore.size
            )
            val result = datasource.deleteByNro(100)
            assertEquals(
                true,
                result.isSuccess
            )
            val listAfter = equipDao.all()
            assertEquals(
                1,
                listAfter.size
            )
            val model = listAfter[0]
            assertEquals(
                EquipRoomModel(
                    id = 2,
                    nro = 124,
                    cdOperClass = 30,
                    descOperClass = "Test2",
                    type = TypeEquip.CART
                ),
                model
            )
        }

    @Test
    fun `add - Check failure if have row repeated`() =
        runTest {
            equipDao.insertAll(
                listOf(
                    EquipRoomModel(
                        id = 1,
                        nro = 1,
                        cdOperClass = 1,
                        descOperClass = "Test",
                        type = TypeEquip.CART
                    )
                )
            )
            val listBefore = equipDao.all()
            assertEquals(
                1,
                listBefore.size
            )
            val result = datasource.add(
                EquipRoomModel(
                    id = 1,
                    nro = 1,
                    cdOperClass = 1,
                    descOperClass = "Test",
                    type = TypeEquip.CART
                )
            )
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IEquipRoomDatasource.add",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "android.database.sqlite.SQLiteConstraintException: UNIQUE constraint failed: tb_equip.id (code 1555 SQLITE_CONSTRAINT_PRIMARYKEY)",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `add - Check success if have row is correct`() =
        runTest {
            equipDao.insertAll(
                listOf(
                    EquipRoomModel(
                        id = 1,
                        nro = 1,
                        cdOperClass = 1,
                        descOperClass = "Test",
                        type = TypeEquip.CART
                    )
                )
            )
            val listBefore = equipDao.all()
            assertEquals(
                1,
                listBefore.size
            )
            val result = datasource.add(
                EquipRoomModel(
                    id = 2,
                    nro = 2,
                    cdOperClass = 2,
                    descOperClass = "Test2",
                    type = TypeEquip.CART
                )
            )
            assertEquals(
                true,
                result.isSuccess
            )
            val listAfter = equipDao.all()
            assertEquals(
                2,
                listAfter.size
            )
            val model1 = listAfter[0]
            assertEquals(
                EquipRoomModel(
                    id = 1,
                    nro = 1,
                    cdOperClass = 1,
                    descOperClass = "Test",
                    type = TypeEquip.CART
                ),
                model1
            )
            val model2 = listAfter[1]
            assertEquals(
                EquipRoomModel(
                    id = 2,
                    nro = 2,
                    cdOperClass = 2,
                    descOperClass = "Test2",
                    type = TypeEquip.CART
                ),
                model2
            )
        }

    @Test
    fun `hasByNro - Check return false if have not nro fielded`() =
        runTest {
            val result = datasource.hasByNro(1)
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
    fun `hasByNro - Check return true if have nro fielded`() =
        runTest {
            equipDao.insertAll(
                listOf(
                    EquipRoomModel(
                        id = 10,
                        nro = 125,
                        cdOperClass = 200,
                        descOperClass = "Test",
                        type = TypeEquip.CART
                    )
                )
            )
            val result = datasource.hasByNro(125)
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                true,
                result.getOrNull()!!
            )
        }

    @Test
    fun `getTypeByNro - Check return failure if have not nro fielded`() =
        runTest {
            val result = datasource.getTypeByNro(1)
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IEquipRoomDatasource.getTypeByNro",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.IllegalStateException: The query result was empty, but expected a single row to return a NON-NULL object of type 'br.com.usinasantafe.cvf.lib.TypeEquip'.",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `getTypeByNro - Check return true if have nro fielded`() =
        runTest {
            equipDao.insertAll(
                listOf(
                    EquipRoomModel(
                        id = 10,
                        nro = 125,
                        cdOperClass = 200,
                        descOperClass = "Test",
                        type = TypeEquip.CART
                    )
                )
            )
            val result = datasource.getTypeByNro(125)
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                TypeEquip.CART,
                result.getOrNull()!!
            )
        }

    @Test
    fun `getById - Check return failure if have not id fielded`() =
        runTest {
            val result = datasource.getById(1)
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IEquipRoomDatasource.getById",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
        "java.lang.IllegalStateException: The query result was empty, but expected a single row to return a NON-NULL object of type 'br.com.usinasantafe.cvf.infra.models.room.stable.EquipRoomModel'.",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `getById - Check return true if have id fielded`() =
        runTest {
            equipDao.insertAll(
                listOf(
                    EquipRoomModel(
                        id = 10,
                        nro = 125,
                        cdOperClass = 200,
                        descOperClass = "Test",
                        type = TypeEquip.CART
                    )
                )
            )
            val result = datasource.getById(10)
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                EquipRoomModel(
                        id = 10,
                        nro = 125,
                        cdOperClass = 200,
                        descOperClass = "Test",
                        type = TypeEquip.CART
                    ),
                result.getOrNull()!!
            )
        }

    @Test
    fun `getIdByNro - Check return failure if have not nro fielded`() =
        runTest {
            val result = datasource.getIdByNro(13)
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IEquipRoomDatasource.getIdByNro",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception: id is 0",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `getIdByNro - Check return true if have nro fielded`() =
        runTest {
            equipDao.insertAll(
                listOf(
                    EquipRoomModel(
                        id = 5,
                        nro = 13,
                        cdOperClass = 52,
                        descOperClass = "Test",
                        type = TypeEquip.CART
                    )
                )
            )
            val result = datasource.getIdByNro(13)
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                5,
                result.getOrNull()!!
            )
        }

    @Test
    fun `getCdClassOperByNro - Check return failure if have not nro fielded`() =
        runTest {
            val result = datasource.getCdClassOperByNro(13)
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IEquipRoomDatasource.getCdClassOperByNro",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception: cdClassOper is 0",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `getCdClassOperByNro - Check return true if have nro fielded`() =
        runTest {
            equipDao.insertAll(
                listOf(
                    EquipRoomModel(
                        id = 5,
                        nro = 13,
                        cdOperClass = 52,
                        descOperClass = "Test",
                        type = TypeEquip.CART
                    )
                )
            )
            val result = datasource.getCdClassOperByNro(13)
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                52,
                result.getOrNull()!!
            )
        }


}