package br.com.usinasantafe.cvf.infra.repositories.stable

import br.com.usinasantafe.cvf.domain.entities.stable.Colab
import br.com.usinasantafe.cvf.infra.datasource.retrofit.stable.ColabRetrofitDatasource
import br.com.usinasantafe.cvf.infra.datasource.room.stable.ColabRoomDatasource
import br.com.usinasantafe.cvf.infra.models.retrofit.stable.ColabRetrofitModel
import br.com.usinasantafe.cvf.infra.models.room.stable.ColabRoomModel
import br.com.usinasantafe.cvf.utils.resultFailure
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.atLeastOnce
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

class IColabRepositoryTest {

    private val colabRoomDatasource = mock<ColabRoomDatasource>()
    private val colabRetrofitDatasource = mock<ColabRetrofitDatasource>()
    private val repository = IColabRepository(
        colabRetrofitDatasource = colabRetrofitDatasource,
        colabRoomDatasource = colabRoomDatasource
    )

    @Test
    fun `addAll - Check return failure if have error`() =
        runTest {
            val roomModelList = listOf(
                ColabRoomModel(
                    reg = 12345L,
                    name = "ANDERSON DA SILVA"
                )
            )
            val entityList = listOf(
                Colab(
                    reg = 12345L,
                    name = "ANDERSON DA SILVA"
                )
            )
            whenever(
                colabRoomDatasource.addAll(roomModelList)
            ).thenReturn(
                resultFailure(
                    "IColabRoomDatasource.addAll",
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
                "IColabRepository.addAll -> IColabRoomDatasource.addAll",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString(),
            )
        }

    @Test
    fun `addAll - Check return true if function execute successfully`() =
        runTest {
            val roomModelList = listOf(
                ColabRoomModel(
                    reg = 12345L,
                    name = "ANDERSON DA SILVA"
                )
            )
            val entityList = listOf(
                Colab(
                    reg = 12345L,
                    name = "ANDERSON DA SILVA"
                )
            )
            val result = repository.addAll(entityList)
            verify(colabRoomDatasource, atLeastOnce()).addAll(roomModelList)
            assertEquals(
                true,
                result.isSuccess,
            )
        }

    @Test
    fun `deleteAll - Check return failure if have error`() =
        runTest {
            whenever(
                colabRoomDatasource.deleteAll()
            ).thenReturn(
                resultFailure(
                    "IColabRoomDatasource.deleteAll",
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
                "IColabRepository.deleteAll -> IColabRoomDatasource.deleteAll",
                result.exceptionOrNull()!!.message,
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString(),
            )
        }

    @Test
    fun `deleteAll - Check return true if function execute successfully`() =
        runTest {
            val result = repository.deleteAll()
            verify(colabRoomDatasource, atLeastOnce()).deleteAll()
            assertEquals(
                true,
                result.isSuccess,
            )
        }

    @Test
    fun `listAll - Check return failure if have error`() =
        runTest {
            whenever(
                colabRetrofitDatasource.listAll("token")
            ).thenReturn(
                resultFailure(
                    "IColabRetrofitDatasource.listAll",
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
                "IColabRepository.listAll -> IColabRetrofitDatasource.listAll",
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
                ColabRetrofitModel(
                    reg = 12345L,
                    name = "ANDERSON DA SILVA"
                ),
                ColabRetrofitModel(
                    reg = 67890L,
                    name = "JOSE APARECIDO"
                )
            )
            val entityList = listOf(
                Colab(
                    reg = 12345L,
                    name = "ANDERSON DA SILVA"
                ),
                Colab(
                    reg = 67890L,
                    name = "JOSE APARECIDO"
                )
            )
            whenever(
                colabRetrofitDatasource.listAll("token")
            ).thenReturn(
                Result.success(
                    retrofitModelList
                )
            )
            val result = repository.listAll("token")
            assertEquals(
                true,
                result.isSuccess,
            )
            assertEquals(
                entityList,
                result.getOrNull()!!
            )
        }

    @Test
    fun `check(Retrofit) - Check return failure if have error in ColabRetrofitDatasource check`() =
        runTest {
            whenever(
                colabRetrofitDatasource.check("token", 12345L)
            ).thenReturn(
                resultFailure(
                    "IColabRetrofitDatasource.check",
                    "-",
                    Exception()
                )
            )
            val result = repository.check("token", 12345L)
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IColabRepository.check -> IColabRetrofitDatasource.check",
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
                colabRetrofitDatasource.check("token", 12345L)
            ).thenReturn(
                Result.success(
                    ColabRetrofitModel(
                        reg = 0L,
                        name = "NON-EXISTENT"
                    )
                )
            )
            whenever(
                colabRoomDatasource.deleteByReg(12345L)
            ).thenReturn(
                resultFailure(
                    "IColabRoomDatasource.deleteByReg",
                    "-",
                    Exception()
                )
            )
            val result = repository.check("token", 12345L)
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IColabRepository.check -> IColabRoomDatasource.deleteByReg",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `check(Retrofit) - Check return false if function execute successfully and non-existent reg in web service`() =
        runTest {
            whenever(
                colabRetrofitDatasource.check("token", 12345L)
            ).thenReturn(
                Result.success(
                    ColabRetrofitModel(
                        reg = 0L,
                        name = "NON-EXISTENT"
                    )
                )
            )
            val result = repository.check("token", 12345L)
            verify(colabRoomDatasource, atLeastOnce()).deleteByReg(12345L)
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
                colabRetrofitDatasource.check("token", 12345L)
            ).thenReturn(
                Result.success(
                    ColabRetrofitModel(
                        reg = 12345L,
                        name = "ANDERSON DA SILVA"
                    )
                )
            )
            whenever(
                colabRoomDatasource.add(
                    ColabRoomModel(
                        reg = 12345L,
                        name = "ANDERSON DA SILVA"
                    )
                )
            ).thenReturn(
                resultFailure(
                    "IColabRoomDatasource.add",
                    "-",
                    Exception()
                )
            )
            val result = repository.check("token", 12345L)
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IColabRepository.check -> IColabRoomDatasource.add",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `check(Retrofit) - Check return false if function execute successfully and existent reg in web service`() =
        runTest {
            whenever(
                colabRetrofitDatasource.check("token", 12345L)
            ).thenReturn(
                Result.success(
                    ColabRetrofitModel(
                        reg = 12345L,
                        name = "ANDERSON DA SILVA"
                    )
                )
            )
            val result = repository.check("token", 12345L)
            verify(colabRoomDatasource, atLeastOnce()).add(
                ColabRoomModel(
                    reg = 12345L,
                    name = "ANDERSON DA SILVA"
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
    fun `check(Room) - Check return failure if have error in ColabRoomDatasource checkByReg`() =
        runTest {
            whenever(
                colabRoomDatasource.checkByReg(12345L)
            ).thenReturn(
                resultFailure(
                    "IColabRoomDatasource.checkByReg",
                    "-",
                    Exception()
                )
            )
            val result = repository.check(12345L)
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IColabRepository.check -> IColabRoomDatasource.checkByReg",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `check - Check return correct if function execute successfully`() =
        runTest {
            whenever(
                colabRoomDatasource.checkByReg(12345L)
            ).thenReturn(
                Result.success(false)
            )
            val result = repository.check(12345L)
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                false,
                result.getOrNull()!!
            )
        }


}