package br.com.usinasantafe.cvf.infra.repositories.stable

import br.com.usinasantafe.cvf.domain.entities.stable.Equip
import br.com.usinasantafe.cvf.infra.datasource.retrofit.stable.EquipRetrofitDatasource
import br.com.usinasantafe.cvf.infra.datasource.room.stable.EquipRoomDatasource
import br.com.usinasantafe.cvf.infra.models.retrofit.stable.EquipRetrofitModel
import br.com.usinasantafe.cvf.infra.models.room.stable.EquipRoomModel
import br.com.usinasantafe.cvf.lib.TypeEquip
import br.com.usinasantafe.cvf.utils.resultFailure
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.atLeastOnce
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

class IEquipRepositoryTest {

    private val equipRoomDatasource = mock<EquipRoomDatasource>()
    private val equipRetrofitDatasource = mock<EquipRetrofitDatasource>()
    private val repository = IEquipRepository(
        equipRetrofitDatasource = equipRetrofitDatasource,
        equipRoomDatasource = equipRoomDatasource
    )

    @Test
    fun `addAll - Check return failure if have error`() =
        runTest {
            val roomModelList = listOf(
                EquipRoomModel(
                    id = 1,
                    nro = 1,
                    cdOperClass = 1,
                    descOperClass = "Test",
                    type = TypeEquip.CART
                )
            )
            val entityList = listOf(
                Equip(
                    id = 1,
                    nro = 1,
                    cdOperClass = 1,
                    descOperClass = "Test",
                    type = TypeEquip.CART
                )
            )
            whenever(
                equipRoomDatasource.addAll(roomModelList)
            ).thenReturn(
                resultFailure(
                    "IEquipRoomDatasource.addAll",
                    "-",
                    Exception()
                )
            )
            val result = repository.addAll(entityList)
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IEquipRepository.addAll -> IEquipRoomDatasource.addAll",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `addAll - Check return true if function execute successfully`() =
        runTest {
            val roomModelList = listOf(
                EquipRoomModel(
                    id = 1,
                    nro = 1,
                    cdOperClass = 1,
                    descOperClass = "Test",
                    type = TypeEquip.CART
                )
            )
            val entityList = listOf(
                Equip(
                    id = 1,
                    nro = 1,
                    cdOperClass = 1,
                    descOperClass = "Test",
                    type = TypeEquip.CART
                )
            )
            val result = repository.addAll(entityList)
            verify(equipRoomDatasource, atLeastOnce()).addAll(roomModelList)
            assertEquals(
                true,
                result.isSuccess
            )
        }

    @Test
    fun `deleteAll - Check return failure if have error`() =
        runTest {
            whenever(
                equipRoomDatasource.deleteAll()
            ).thenReturn(
                resultFailure(
                    "IEquipRoomDatasource.deleteAll",
                    "-",
                    Exception()
                )
            )
            val result = repository.deleteAll()
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IEquipRepository.deleteAll -> IEquipRoomDatasource.deleteAll",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `deleteAll - Check return true if function execute successfully`() =
        runTest {
            val result = repository.deleteAll()
            verify(equipRoomDatasource, atLeastOnce()).deleteAll()
            assertEquals(
                result.isSuccess,
                true
            )
        }

    @Test
    fun `listAll - Check return failure if have error`() =
        runTest {
            whenever(
                equipRetrofitDatasource.listAll("token")
            ).thenReturn(
                resultFailure(
                    "IEquipRetrofitDatasource.listAll",
                    "-",
                    Exception()
                )
            )
            val result = repository.listAll("token")
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IEquipRepository.listAll -> IEquipRetrofitDatasource.listAll",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `listAll - Check return true if function execute successfully`() =
        runTest {
            val retrofitModelList = listOf(
                EquipRetrofitModel(
                    id = 1,
                    nro = 1,
                    cdOperClass = 1,
                    descOperClass = "Test",
                    type = 1
                ),
                EquipRetrofitModel(
                    id = 2,
                    nro = 2,
                    cdOperClass = 2,
                    descOperClass = "Test2",
                    type = 1
                )
            )
            val entityList = listOf(
                Equip(
                    id = 1,
                    nro = 1,
                    cdOperClass = 1,
                    descOperClass = "Test",
                    type = TypeEquip.TRUCK
                ),
                Equip(
                    id = 2,
                    nro = 2,
                    cdOperClass = 2,
                    descOperClass = "Test2",
                    type = TypeEquip.TRUCK
                )
            )
            whenever(
                equipRetrofitDatasource.listAll("token")
            ).thenReturn(
                Result.success(
                    retrofitModelList
                )
            )
            val result = repository.listAll("token")
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                entityList,
                result.getOrNull()!!
            )
        }

    @Test
    fun `check(Retrofit) - Check return failure if have error in EquipRetrofitDatasource check`() =
        runTest {
            whenever(
                equipRetrofitDatasource.checkByNro("token", 123456)
            ).thenReturn(
                resultFailure(
                    "IEquipRetrofitDatasource.check",
                    "-",
                    Exception()
                )
            )
            val result = repository.check("token", 123456, 0)
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IEquipRepository.check -> IEquipRetrofitDatasource.check",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `check(Retrofit) - Check return failure if have error in ColabRoomDatasource deleteByReg`() =
        runTest {
            whenever(
                equipRetrofitDatasource.checkByNro("token", 123456)
            ).thenReturn(
                Result.success(
                    EquipRetrofitModel(
                        id = 0,
                        nro = 0,
                        cdOperClass = 0,
                        descOperClass = "NON-EXISTENT",
                        type = 0
                    )
                )
            )
            whenever(
                equipRoomDatasource.deleteByNro(123456)
            ).thenReturn(
                resultFailure(
                    "IEquipRoomDatasource.deleteByNro",
                    "-",
                    Exception()
                )
            )
            val result = repository.check("token", 123456, 0)
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IEquipRepository.check -> IEquipRoomDatasource.deleteByNro",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `check(Retrofit) - Check return false if function execute successfully and non-existent nro in web service`() =
        runTest {
            whenever(
                equipRetrofitDatasource.checkByNro("token", 123456)
            ).thenReturn(
                Result.success(
                    EquipRetrofitModel(
                        id = 0,
                        nro = 0,
                        cdOperClass = 0,
                        descOperClass = "NON-EXISTENT",
                        type = 0
                    )
                )
            )
            val result = repository.check("token", 123456, 0)
            verify(equipRoomDatasource, atLeastOnce()).deleteByNro(123456)
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
    fun `check(Retrofit) - Check return failure if have error in ColabRoomDatasource add`() =
        runTest {
            whenever(
                equipRetrofitDatasource.checkByNro("token", 123)
            ).thenReturn(
                Result.success(
                    EquipRetrofitModel(
                        id = 12,
                        nro = 123,
                        cdOperClass = 10,
                        descOperClass = "Test",
                        type = 1
                    )
                )
            )
            whenever(
                equipRoomDatasource.add(
                    EquipRoomModel(
                        id = 12,
                        nro = 123,
                        cdOperClass = 10,
                        descOperClass = "Test",
                        type = TypeEquip.TRUCK
                    )
                )
            ).thenReturn(
                resultFailure(
                    "IEquipRoomDatasource.add",
                    "-",
                    Exception()
                )
            )
            val result = repository.check("token", 123, 0)
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IEquipRepository.check -> IEquipRoomDatasource.add",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `check(Retrofit) - Check return false if function execute successfully and type is TypeEquip TRUCK and pos greater than 0`() =
        runTest {
            whenever(
                equipRetrofitDatasource.checkByNro("token", 123)
            ).thenReturn(
                Result.success(
                    EquipRetrofitModel(
                        id = 12,
                        nro = 123,
                        cdOperClass = 10,
                        descOperClass = "Test",
                        type = 1
                    )
                )
            )
            val result = repository.check("token", 123, 1)
            verify(
                equipRoomDatasource,
                atLeastOnce()
            ).add(
                EquipRoomModel(
                    id = 12,
                    nro = 123,
                    cdOperClass = 10,
                    descOperClass = "Test",
                    type = TypeEquip.TRUCK
                )
            )
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
    fun `check(Retrofit) - Check return false if function execute successfully and type is TypeEquip CART and pos is 0`() =
        runTest {
            whenever(
                equipRetrofitDatasource.checkByNro("token", 123)
            ).thenReturn(
                Result.success(
                    EquipRetrofitModel(
                        id = 12,
                        nro = 123,
                        cdOperClass = 10,
                        descOperClass = "Test",
                        type = 2
                    )
                )
            )
            val result = repository.check("token", 123, 0)
            verify(
                equipRoomDatasource,
                atLeastOnce()
            ).add(
                EquipRoomModel(
                    id = 12,
                    nro = 123,
                    cdOperClass = 10,
                    descOperClass = "Test",
                    type = TypeEquip.CART
                )
            )
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
    fun `check(Retrofit) - Check return true if function execute successfully and type is TypeEquip TRUCK and pos is 0`() =
        runTest {
            whenever(
                equipRetrofitDatasource.checkByNro("token", 123)
            ).thenReturn(
                Result.success(
                    EquipRetrofitModel(
                        id = 12,
                        nro = 123,
                        cdOperClass = 10,
                        descOperClass = "Test",
                        type = 1
                    )
                )
            )
            val result = repository.check("token", 123, 0)
            verify(
                equipRoomDatasource,
                atLeastOnce()
            ).add(
                EquipRoomModel(
                    id = 12,
                    nro = 123,
                    cdOperClass = 10,
                    descOperClass = "Test",
                    type = TypeEquip.TRUCK
                )
            )
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
    fun `check(Retrofit) - Check return true if function execute successfully and type is TypeEquip CART and pos greater than 0`() =
        runTest {
            whenever(
                equipRetrofitDatasource.checkByNro("token", 123)
            ).thenReturn(
                Result.success(
                    EquipRetrofitModel(
                        id = 12,
                        nro = 123,
                        cdOperClass = 10,
                        descOperClass = "Test",
                        type = 2
                    )
                )
            )
            val result = repository.check("token", 123, 2)
            verify(
                equipRoomDatasource,
                atLeastOnce()
            ).add(
                EquipRoomModel(
                    id = 12,
                    nro = 123,
                    cdOperClass = 10,
                    descOperClass = "Test",
                    type = TypeEquip.CART
                )
            )
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
    fun `check(Room) - Check return failure if have error in EquipRoomDatasource checkByNro`() =
        runTest {
            whenever(
                equipRoomDatasource.hasByNro(100)
            ).thenReturn(
                resultFailure(
                    "IEquipRoomDatasource.checkByNro",
                    "-",
                    Exception()
                )
            )
            val result = repository.check(100, 0)
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IEquipRepository.check -> IEquipRoomDatasource.checkByNro",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `check(Room) - Check return false if function execute successfully and EquipRoomDatasource checkByNro return false`() =
        runTest {
            whenever(
                equipRoomDatasource.hasByNro(100)
            ).thenReturn(
                Result.success(false)
            )
            val result = repository.check(100, 1)
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
    fun `check(Room) - Check return failure if have error in EquipRoomDatasource getTypeByNro`() =
        runTest {
            whenever(
                equipRoomDatasource.hasByNro(100)
            ).thenReturn(
                Result.success(true)
            )
            whenever(
                equipRoomDatasource.getTypeByNro(100)
            ).thenReturn(
                resultFailure(
                    "IEquipRoomDatasource.getTypeByNro",
                    "-",
                    Exception()
                )
            )
            val result = repository.check(100, 1)
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IEquipRepository.check -> IEquipRoomDatasource.getTypeByNro",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `check(Room) - Check return false if function execute successfully and type is TypeEquip TRUCK and pos greater than 0`() =
        runTest {
            whenever(
                equipRoomDatasource.hasByNro(100)
            ).thenReturn(
                Result.success(true)
            )
            whenever(
                equipRoomDatasource.getTypeByNro(100)
            ).thenReturn(
                Result.success(TypeEquip.TRUCK)
            )
            val result = repository.check(100, 1)
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
    fun `check(Room) - Check return false if function execute successfully and type is TypeEquip CART and pos is 0`() =
        runTest {
            whenever(
                equipRoomDatasource.hasByNro(100)
            ).thenReturn(
                Result.success(true)
            )
            whenever(
                equipRoomDatasource.getTypeByNro(100)
            ).thenReturn(
                Result.success(TypeEquip.CART)
            )
            val result = repository.check(100, 0)
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
    fun `check(Room) - Check return true if function execute successfully and type is TypeEquip TRUCK and pos is 0`() =
        runTest {
            whenever(
                equipRoomDatasource.hasByNro(100)
            ).thenReturn(
                Result.success(true)
            )
            whenever(
                equipRoomDatasource.getTypeByNro(100)
            ).thenReturn(
                Result.success(TypeEquip.TRUCK)
            )
            val result = repository.check(100, 0)
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
    fun `check(Room) - Check return true if function execute successfully and type is TypeEquip CART and pos greater than 0`() =
        runTest {
            whenever(
                equipRoomDatasource.hasByNro(100)
            ).thenReturn(
                Result.success(true)
            )
            whenever(
                equipRoomDatasource.getTypeByNro(100)
            ).thenReturn(
                Result.success(TypeEquip.CART)
            )
            val result = repository.check(100, 3)
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
    fun `getById - Check return failure if have error in EquipRoomDatasource getById`() =
        runTest {
            whenever(
                equipRoomDatasource.getById(10)
            ).thenReturn(
                resultFailure(
                    "IEquipRoomDatasource.getById",
                    "-",
                    Exception()
                )
            )
            val result = repository.getById(10)
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IEquipRepository.getById -> IEquipRoomDatasource.getById",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `getById - Check return correct if function execute successfully`() =
        runTest {
            whenever(
                equipRoomDatasource.getById(10)
            ).thenReturn(
                Result.success(
                    EquipRoomModel(
                        id = 10,
                        nro = 10,
                        cdOperClass = 10,
                        descOperClass = "Test",
                        type = TypeEquip.TRUCK
                    )
                )
            )
            val result = repository.getById(10)
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                Equip(
                    id = 10,
                    nro = 10,
                    cdOperClass = 10,
                    descOperClass = "Test",
                    type = TypeEquip.TRUCK
                ),
                result.getOrNull()!!
            )
        }

    @Test
    fun `getIdByNro - Check return failure if have error in EquipRoomDatasource getIdByNro`() =
        runTest {
            whenever(
                equipRoomDatasource.getIdByNro(200)
            ).thenReturn(
                resultFailure(
                    "IEquipRoomDatasource.getIdByNro",
                    "-",
                    Exception()
                )
            )
            val result = repository.getIdByNro(200)
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IEquipRepository.getIdByNro -> IEquipRoomDatasource.getIdByNro",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `getIdByNro - Check return correct if function execute successfully`() =
        runTest {
            whenever(
                equipRoomDatasource.getIdByNro(200)
            ).thenReturn(
                Result.success(10)
            )
            val result = repository.getIdByNro(200)
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                10,
                result.getOrNull()!!
            )
        }

    @Test
    fun `getCdClassOperByNro - Check return failure if have error in EquipRoomDatasource getCdClassOperByNro`() =
        runTest {
            whenever(
                equipRoomDatasource.getCdClassOperByNro(100)
            ).thenReturn(
                resultFailure(
                    "IEquipRoomDatasource.getCdClassOperByNro",
                    "-",
                    Exception()
                )
            )
            val result = repository.getCdClassOperByNro(100)
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IEquipRepository.getCdClassOperByNro -> IEquipRoomDatasource.getCdClassOperByNro",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `getCdClassOperByNro - Check return correct if function execute successfully`() =
        runTest {
            whenever(
                equipRoomDatasource.getCdClassOperByNro(100)
            ).thenReturn(
                Result.success(5)
            )
            val result = repository.getCdClassOperByNro(100)
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                5,
                result.getOrNull()!!
            )
        }

}