package br.com.usinasantafe.cvf.domain.usecases.manager

import br.com.usinasantafe.cvf.MainCoroutineRule
import br.com.usinasantafe.cvf.domain.entities.stable.Front
import br.com.usinasantafe.cvf.domain.entities.stable.Release
import br.com.usinasantafe.cvf.domain.repositories.stable.FrontRepository
import br.com.usinasantafe.cvf.domain.repositories.stable.ReleaseRepository
import br.com.usinasantafe.cvf.domain.repositories.variable.ManagerRepository
import br.com.usinasantafe.cvf.utils.resultFailure
import com.google.gson.Gson
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class IUpdateManagerTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val managerRepository = mock<ManagerRepository>()
    private val frontRepository = mock<FrontRepository>()
    private val releaseRepository = mock<ReleaseRepository>()
    private val usecase = IUpdateManager(
        managerRepository = managerRepository,
        frontRepository = frontRepository,
        releaseRepository = releaseRepository
    )

    private val managerJson = """
        {
            "idFront": 10,
            "idRelease": 7,
            "qtdLimitCart": 3
        }
    """.trimIndent()

    private val frontJson = """
        {
            "id": 10,
            "cd": 12,
            "description": "Test1"
        }
    """.trimIndent()

    private val releaseJson = """
        {
            "id": 7,
            "nroOS": 1528,
            "idPropAgr": 1,
            "descPropAgr": "Test1",
            "idFront": 1
        }
    """.trimIndent()

    @Test
    fun `Check return failure if have error in FrontRepository add`() =
        runTest {
            val frontEntity = Gson().fromJson(frontJson, Front::class.java)
            whenever(
                frontRepository.add(frontEntity)
            ).thenReturn(
                resultFailure(
                    "IFrontRepository.add",
                    "-",
                    Exception()
                )
            )
            val result = usecase(managerJson, frontJson, releaseJson)
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IUpdateManager -> IFrontRepository.add",
                result.exceptionOrNull()!!.message
            )
        }

    @Test
    fun `Check return failure if have error in ReleaseRepository add`() =
        runTest {
            val frontEntity = Gson().fromJson(frontJson, Front::class.java)
            val releaseEntity = Gson().fromJson(releaseJson, Release::class.java)
            whenever(
                frontRepository.add(frontEntity)
            ).thenReturn(
                Result.success(Unit)
            )
            whenever(
                releaseRepository.add(releaseEntity)
            ).thenReturn(
                resultFailure(
                    "IReleaseRepository.add",
                    "-",
                    Exception()
                )
            )
            val result = usecase(managerJson, frontJson, releaseJson)
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IUpdateManager -> IReleaseRepository.add",
                result.exceptionOrNull()!!.message
            )
        }

    @Test
    fun `Check return failure if have error in ManagerRepository update`() =
        runTest {
            val frontEntity = Gson().fromJson(frontJson, Front::class.java)
            val releaseEntity = Gson().fromJson(releaseJson, Release::class.java)
            whenever(
                frontRepository.add(frontEntity)
            ).thenReturn(
                Result.success(Unit)
            )
            whenever(
                releaseRepository.add(releaseEntity)
            ).thenReturn(
                Result.success(Unit)
            )
            whenever(
                managerRepository.update(10, 7, 3)
            ).thenReturn(
                resultFailure(
                    "IManagerRepository.update",
                    "-",
                    Exception()
                )
            )
            val result = usecase(managerJson, frontJson, releaseJson)
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IUpdateManager -> IManagerRepository.update",
                result.exceptionOrNull()!!.message
            )
        }

    @Test
    fun `Check return failure if have malformed managerJson`() =
        runTest {
            val frontEntity = Gson().fromJson(frontJson, Front::class.java)
            val releaseEntity = Gson().fromJson(releaseJson, Release::class.java)
            whenever(
                frontRepository.add(frontEntity)
            ).thenReturn(
                Result.success(Unit)
            )
            whenever(
                releaseRepository.add(releaseEntity)
            ).thenReturn(
                Result.success(Unit)
            )
            val result = usecase("malformed", frontJson, releaseJson)
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                true,
                result.exceptionOrNull()!!.message!!.contains("IUpdateManager")
            )
        }

    @Test
    fun `Check return correct if function execute successfully`() =
        runTest {
            val frontEntity = Gson().fromJson(frontJson, Front::class.java)
            val releaseEntity = Gson().fromJson(releaseJson, Release::class.java)

            whenever(
                frontRepository.add(frontEntity)
            ).thenReturn(
                Result.success(Unit)
            )
            whenever(
                releaseRepository.add(releaseEntity)
            ).thenReturn(
                Result.success(Unit)
            )
            whenever(
                managerRepository.update(10, 7, 3)
            ).thenReturn(
                Result.success(Unit)
            )

            val result = usecase(managerJson, frontJson, releaseJson)

            verify(frontRepository).add(frontEntity)
            verify(releaseRepository).add(releaseEntity)
            verify(managerRepository).update(10, 7, 3)

            assertEquals(
                true,
                result.isSuccess
            )
        }

}
