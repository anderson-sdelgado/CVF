package br.com.usinasantafe.cvf.infra.repositories.stable

import br.com.usinasantafe.cvf.domain.entities.stable.Front
import br.com.usinasantafe.cvf.domain.entities.stable.Release
import br.com.usinasantafe.cvf.infra.datasource.retrofit.stable.ReleaseRetrofitDatasource
import br.com.usinasantafe.cvf.infra.datasource.room.stable.ReleaseRoomDatasource
import br.com.usinasantafe.cvf.infra.models.retrofit.stable.ReleaseRetrofitModel
import br.com.usinasantafe.cvf.infra.models.room.stable.FrontRoomModel
import br.com.usinasantafe.cvf.infra.models.room.stable.ReleaseRoomModel
import br.com.usinasantafe.cvf.utils.resultFailure
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.atLeastOnce
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

class IReleaseRepositoryTest {

    private val releaseRoomDatasource = mock<ReleaseRoomDatasource>()
    private val releaseRetrofitDatasource = mock<ReleaseRetrofitDatasource>()
    private val repository = IReleaseRepository(
        releaseRetrofitDatasource = releaseRetrofitDatasource,
        releaseRoomDatasource = releaseRoomDatasource
    )

    @Test
    fun `addAll - Check return failure if have error`() =
        runTest {
            val roomModelList = listOf(
                ReleaseRoomModel(
                    id = 1,
                    nroOS = 1,
                    idPropAgr = 1,
                    descPropAgr = "Test",
                    idFront = 1
                )
            )
            val entityList = listOf(
                Release(
                    id = 1,
                    nroOS = 1,
                    idPropAgr = 1,
                    descPropAgr = "Test",
                    idFront = 1
                )
            )
            whenever(
                releaseRoomDatasource.addAll(roomModelList)
            ).thenReturn(
                resultFailure(
                    "IReleaseRoomDatasource.addAll",
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
                "IReleaseRepository.addAll -> IReleaseRoomDatasource.addAll",
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
                ReleaseRoomModel(
                    id = 1,
                    nroOS = 1,
                    idPropAgr = 1,
                    descPropAgr = "Test",
                    idFront = 1
                )
            )
            val entityList = listOf(
                Release(
                    id = 1,
                    nroOS = 1,
                    idPropAgr = 1,
                    descPropAgr = "Test",
                    idFront = 1
                )
            )
            val result = repository.addAll(entityList)
            verify(releaseRoomDatasource, atLeastOnce()).addAll(roomModelList)
            assertEquals(
                result.isSuccess,
                true
            )
        }

    @Test
    fun `deleteAll - Check return failure if have error`() =
        runTest {
            whenever(
                releaseRoomDatasource.deleteAll()
            ).thenReturn(
                resultFailure(
                    "IReleaseRoomDatasource.deleteAll",
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
                "IReleaseRepository.deleteAll -> IReleaseRoomDatasource.deleteAll",
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
            verify(releaseRoomDatasource, atLeastOnce()).deleteAll()
            assertEquals(
                result.isSuccess,
                true
            )
        }

    @Test
    fun `listAll - Check return failure if have error`() =
        runTest {
            whenever(
                releaseRetrofitDatasource.listAll("token")
            ).thenReturn(
                resultFailure(
                    "IReleaseRetrofitDatasource.listAll",
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
                "IReleaseRepository.listAll -> IReleaseRetrofitDatasource.listAll",
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
                ReleaseRetrofitModel(
                    id = 1,
                    nroOS = 1,
                    idPropAgr = 1,
                    descPropAgr = "Test",
                    idFront = 1
                ),
                ReleaseRetrofitModel(
                    id = 2,
                    nroOS = 2,
                    idPropAgr = 2,
                    descPropAgr = "Test2",
                    idFront = 2
                )
            )
            val entityList = listOf(
                Release(
                    id = 1,
                    nroOS = 1,
                    idPropAgr = 1,
                    descPropAgr = "Test",
                    idFront = 1
                ),
                Release(
                    id = 2,
                    nroOS = 2,
                    idPropAgr = 2,
                    descPropAgr = "Test2",
                    idFront = 2
                )
            )
            whenever(
                releaseRetrofitDatasource.listAll("token")
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
    fun `listByIdFront - Check return failure if have error in ReleaseRoomDatasource listByIdFront`() =
        runTest {
            whenever(
                releaseRoomDatasource.listByIdFront(1)
            ).thenReturn(
                resultFailure(
                    "IReleaseRoomDatasource.listByIdFront",
                    "-",
                    Exception()
                )
            )
            val result = repository.listByIdFront(1)
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IReleaseRepository.listByIdFront -> IReleaseRoomDatasource.listByIdFront",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `listByIdFront - Check return correct if function execute successfully`() =
        runTest {
            whenever(
                releaseRoomDatasource.listByIdFront(1)
            ).thenReturn(
                Result.success(
                    listOf(
                        ReleaseRoomModel(
                            id = 1,
                            nroOS = 1,
                            idPropAgr = 1,
                            descPropAgr = "Test",
                            idFront = 1
                        )
                    )
                )
            )
            val result = repository.listByIdFront(1)
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                listOf(
                    Release(
                        id = 1,
                        nroOS = 1,
                        idPropAgr = 1,
                        descPropAgr = "Test",
                        idFront = 1
                    )
                ),
                result.getOrNull()!!
            )
        }

    @Test
    fun `getById - Check return failure if have error in ReleaseRoomDatasource getById`() =
        runTest {
            whenever(
                releaseRoomDatasource.getById(1)
            ).thenReturn(
                resultFailure(
                    "IReleaseRoomDatasource.getById",
                    "-",
                    Exception()
                )
            )
            val result = repository.getById(1)
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IReleaseRepository.getById -> IReleaseRoomDatasource.getById",
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
                releaseRoomDatasource.getById(1)
            ).thenReturn(
                Result.success(
                    ReleaseRoomModel(
                        id = 1,
                        nroOS = 1,
                        idPropAgr = 1,
                        descPropAgr = "Test",
                        idFront = 1
                    )
                )
            )
            val result = repository.getById(1)
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                Release(
                    id = 1,
                    nroOS = 1,
                    idPropAgr = 1,
                    descPropAgr = "Test",
                    idFront = 1
                ),
                result.getOrNull()!!
            )
        }

    @Test
    fun `add - Check return failure if have error in ReleaseRoomDatasource hasById`() =
        runTest {
            whenever(
                releaseRoomDatasource.hasById(1)
            ).thenReturn(
                resultFailure(
                    "IReleaseRoomDatasource.hasById",
                    "-",
                    Exception()
                )
            )
            val result = repository.add(
                Release(
                    id = 1,
                    nroOS = 1,
                    idPropAgr = 1,
                    descPropAgr = "Test",
                    idFront = 1
                )
            )
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IReleaseRepository.add -> IReleaseRoomDatasource.hasById",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `add - Check return correct if function execute successfully and FrontRoomDatasource hasById return true`() =
        runTest {
            whenever(
                releaseRoomDatasource.hasById(1)
            ).thenReturn(
                Result.success(true)
            )
            val result = repository.add(
                Release(
                    id = 1,
                    nroOS = 1,
                    idPropAgr = 1,
                    descPropAgr = "Test",
                    idFront = 1
                )
            )
            verify(
                releaseRoomDatasource,
                never()
            ).add(
                ReleaseRoomModel(
                    id = 1,
                    nroOS = 1,
                    idPropAgr = 1,
                    descPropAgr = "Test",
                    idFront = 1
                )
            )
            assertEquals(
                true,
                result.isSuccess
            )
        }

    @Test
    fun `add - Check return failure if have error in ReleaseRoomDatasource add`() =
        runTest {
            whenever(
                releaseRoomDatasource.hasById(1)
            ).thenReturn(
                Result.success(false)
            )
            whenever(
                releaseRoomDatasource.add(
                    ReleaseRoomModel(
                        id = 1,
                        nroOS = 1,
                        idPropAgr = 1,
                        descPropAgr = "Test",
                        idFront = 1
                    )
                )
            ).thenReturn(
                resultFailure(
                    "IReleaseRoomDatasource.add",
                    "-",
                    Exception()
                )
            )
            val result = repository.add(
                Release(
                    id = 1,
                    nroOS = 1,
                    idPropAgr = 1,
                    descPropAgr = "Test",
                    idFront = 1
                )
            )
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IReleaseRepository.add -> IReleaseRoomDatasource.add",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `add - Check return correct if function execute successfully and FrontRoomDatasource hasById return false`() =
        runTest {

            whenever(
                releaseRoomDatasource.hasById(1)
            ).thenReturn(
                Result.success(false)
            )
            val result = repository.add(
                Release(
                    id = 1,
                    nroOS = 1,
                    idPropAgr = 1,
                    descPropAgr = "Test",
                    idFront = 1
                )
            )
            verify(
                releaseRoomDatasource,
                atLeastOnce()
            ).add(
                ReleaseRoomModel(
                    id = 1,
                    nroOS = 1,
                    idPropAgr = 1,
                    descPropAgr = "Test",
                    idFront = 1
                )
            )
            assertEquals(
                true,
                result.isSuccess
            )
        }
}