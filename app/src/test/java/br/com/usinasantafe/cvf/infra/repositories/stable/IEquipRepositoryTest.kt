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

}