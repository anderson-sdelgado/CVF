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
                result.isFailure,
                true
            )
            assertEquals(
                result.exceptionOrNull()!!.message,
                "IFrontRepository.addAll -> IFrontRoomDatasource.addAll"
            )
            assertEquals(
                result.exceptionOrNull()!!.cause.toString(),
                "java.lang.Exception"
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
            whenever(
                frontRoomDatasource.addAll(roomModelList)
            ).thenReturn(
                Result.success(Unit)
            )
            val result = repository.addAll(entityList)
            assertEquals(
                result.isSuccess,
                true
            )
            assertEquals(
                result.getOrNull()!!,
                Unit
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
                result.isFailure,
                true
            )
            assertEquals(
                result.exceptionOrNull()!!.message,
                "IFrontRepository.deleteAll -> IFrontRoomDatasource.deleteAll"
            )
            assertEquals(
                result.exceptionOrNull()!!.cause.toString(),
                "java.lang.Exception"
            )
        }

    @Test
    fun `deleteAll - Check return true if function execute successfully`() =
        runTest {
            whenever(
                frontRoomDatasource.deleteAll()
            ).thenReturn(
                Result.success(Unit)
            )
            val result = repository.deleteAll()
            assertEquals(
                result.isSuccess,
                true
            )
            assertEquals(
                result.getOrNull()!!,
                Unit
            )
        }

    @Test
    fun `listAll - Check return failure if have error`() =
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
                result.isFailure,
                true
            )
            assertEquals(
                result.exceptionOrNull()!!.message,
                "IFrontRepository.listAll -> IFrontRetrofitDatasource.listAll"
            )
            assertEquals(
                result.exceptionOrNull()!!.cause.toString(),
                "java.lang.Exception"
            )
        }

    @Test
    fun `listAll - Check return true if function execute successfully`() =
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
                result.isSuccess,
                true
            )
            assertEquals(
                result.getOrNull()!!,
                entityList
            )
        }

}