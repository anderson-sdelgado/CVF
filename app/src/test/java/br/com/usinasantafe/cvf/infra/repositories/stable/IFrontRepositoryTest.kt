package br.com.usinasantafe.cvf.infra.repositories.stable

import br.com.usinasantafe.cvf.domain.entities.stable.Front
import br.com.usinasantafe.cvf.infra.datasource.retrofit.stable.FrontRetrofitDatasource
import br.com.usinasantafe.cvf.infra.datasource.room.stable.FrontRoomDatasource
import br.com.usinasantafe.cvf.infra.models.retrofit.stable.FrontRetrofitModel
import br.com.usinasantafe.cvf.infra.models.room.stable.FrontRoomModel
import br.com.usinasantafe.cvf.utils.resultFailure
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.atLeastOnce
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

class IFrontRepositoryTest {

    private val frontRoomDatasource = mock<FrontRoomDatasource>()
    private val frontRetrofitDatasource = mock<FrontRetrofitDatasource>()
    private val repository = IFrontRepository(
        frontRetrofitDatasource = frontRetrofitDatasource,
        frontRoomDatasource = frontRoomDatasource
    )

    @Test
    fun `addAll - Check return failure if have error`() =
        runTest {
            val roomModelList = listOf(
                FrontRoomModel(
                    id = 1,
                    cd = 1,
                    description = "Test"
                )
            )
            val entityList = listOf(
                Front(
                    id = 1,
                    cd = 1,
                    description = "Test"
                )
            )
            whenever(
                frontRoomDatasource.addAll(roomModelList)
            ).thenReturn(
                resultFailure(
                    "IFrontRoomDatasource.addAll",
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
                "IFrontRepository.addAll -> IFrontRoomDatasource.addAll",
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
                FrontRoomModel(
                    id = 1,
                    cd = 1,
                    description = "Test"
                )
            )
            val entityList = listOf(
                Front(
                    id = 1,
                    cd = 1,
                    description = "Test"
                )
            )
            val result = repository.addAll(entityList)
            verify(frontRoomDatasource, atLeastOnce()).addAll(roomModelList)
            assertEquals(
                result.isSuccess,
                true
            )
        }

    @Test
    fun `deleteAll - Check return failure if have error`() =
        runTest {
            whenever(
                frontRoomDatasource.deleteAll()
            ).thenReturn(
                resultFailure(
                    "IFrontRoomDatasource.deleteAll",
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
                "IFrontRepository.deleteAll -> IFrontRoomDatasource.deleteAll",
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
            verify(frontRoomDatasource, atLeastOnce()).deleteAll()
            assertEquals(
                result.isSuccess,
                true
            )
        }

    @Test
    fun `listAll(token) - Check return failure if have error`() =
        runTest {
            whenever(
                frontRetrofitDatasource.listAll("token")
            ).thenReturn(
                resultFailure(
                    "IFrontRetrofitDatasource.listAll",
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
                "IFrontRepository.listAll -> IFrontRetrofitDatasource.listAll",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `listAll(token) - Check return true if function execute successfully`() =
        runTest {
            val retrofitModelList = listOf(
                FrontRetrofitModel(
                    id = 1,
                    cd = 1,
                    description = "Test"
                ),
                FrontRetrofitModel(
                    id = 2,
                    cd = 2,
                    description = "Test"
                )
            )
            val entityList = listOf(
                Front(
                    id = 1,
                    cd = 1,
                    description = "Test"
                ),
                Front(
                    id = 2,
                    cd = 2,
                    description = "Test"
                )
            )
            whenever(
                frontRetrofitDatasource.listAll("token")
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
    fun `listAll - Check return failure if have error in FrontRoomDatasource listAll`() =
        runTest {
            whenever(
                frontRoomDatasource.listAll()
            ).thenReturn(
                resultFailure(
                    "IFrontRoomDatasource.listAll",
                    "-",
                    Exception()
                )
            )
            val result = repository.listAll()
            assertEquals(
                result.isFailure,
                true
            )
            assertEquals(
                result.exceptionOrNull()!!.message,
                "IFrontRepository.listAll -> IFrontRoomDatasource.listAll"
            )
            assertEquals(
                result.exceptionOrNull()!!.cause.toString(),
                "java.lang.Exception"
            )
        }

    @Test
    fun `listAll - Check return correct if function execute successfully`() =
        runTest {
            val roomModelList = listOf(
                FrontRoomModel(
                    id = 1,
                    cd = 1,
                    description = "Test"
                ),
                FrontRoomModel(
                    id = 2,
                    cd = 2,
                    description = "Test"
                )
            )
            val entityList = listOf(
                Front(
                    id = 1,
                    cd = 1,
                    description = "Test"
                ),
                Front(
                    id = 2,
                    cd = 2,
                    description = "Test"
                )
            )
            whenever(
                frontRoomDatasource.listAll()
            ).thenReturn(
                Result.success(roomModelList)
            )
            val result = repository.listAll()
            assertEquals(
                result.isSuccess,
                true
            )
            assertEquals(
                result.getOrNull()!!,
                entityList
            )
        }

}