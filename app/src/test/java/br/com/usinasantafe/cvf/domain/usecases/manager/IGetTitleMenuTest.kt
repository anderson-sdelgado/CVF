package br.com.usinasantafe.cvf.domain.usecases.manager

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import br.com.usinasantafe.cvf.domain.entities.stable.Front
import br.com.usinasantafe.cvf.domain.entities.stable.Release
import br.com.usinasantafe.cvf.domain.repositories.stable.FrontRepository
import br.com.usinasantafe.cvf.domain.repositories.stable.ReleaseRepository
import br.com.usinasantafe.cvf.domain.repositories.variable.ManagerRepository
import br.com.usinasantafe.cvf.utils.resultFailure
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.intArrayOf
import kotlin.test.assertEquals

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class IGetTitleMenuTest {

    private val releaseRepository = mock<ReleaseRepository>()
    private val managerRepository = mock<ManagerRepository>()
    private val frontRepository = mock<FrontRepository>()
    private lateinit var usecase: IGetTitleMenu

    @Before
    fun setup() {
        val context: Context = ApplicationProvider.getApplicationContext()
        usecase = IGetTitleMenu(
            context = context,
            releaseRepository = releaseRepository,
            managerRepository = managerRepository,
            frontRepository = frontRepository
        )
    }

    @Test
    fun `Check return failure if have error in ManagerRepository getIdRelease`() =
        runTest {
            whenever(
                managerRepository.getIdRelease()
            ).thenReturn(
                resultFailure(
                    "IManagerRepository.getIdRelease",
                    "-",
                    Exception()
                )
            )
            val result = usecase()
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IGetDescRelease -> IManagerRepository.getIdRelease",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `Check return failure if getIdRelease is null`() =
        runTest {
            whenever(
                managerRepository.getIdRelease()
            ).thenReturn(
                Result.success(null)
            )
            val result = usecase()
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IGetDescRelease -> idRelease is required",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.NullPointerException: idRelease is required",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `Check return failure if have error in ManagerRepository getIdFront`() =
        runTest {
            whenever(
                managerRepository.getIdRelease()
            ).thenReturn(
                Result.success(1)
            )
            whenever(
                managerRepository.getIdFront()
            ).thenReturn(
                resultFailure(
                    "IManagerRepository.getIdFront",
                    "-",
                    Exception()
                )
            )
            val result = usecase()
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IGetDescRelease -> IManagerRepository.getIdFront",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `Check return failure if getIdFront is null`() =
        runTest {
            whenever(
                managerRepository.getIdRelease()
            ).thenReturn(
                Result.success(1)
            )
            whenever(
                managerRepository.getIdFront()
            ).thenReturn(
                Result.success(null)
            )
            val result = usecase()
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IGetDescRelease -> idFront is required",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.NullPointerException: idFront is required",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `Check return failure if have error in ReleaseRepository getById`() =
        runTest {
            whenever(
                managerRepository.getIdRelease()
            ).thenReturn(
                Result.success(1)
            )
            whenever(
                managerRepository.getIdFront()
            ).thenReturn(
                Result.success(2)
            )
            whenever(
                releaseRepository.getById(1)
            ).thenReturn(
                resultFailure(
                    "IReleaseRepository.getById",
                    "-",
                    Exception()
                )
            )
            val result = usecase()
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IGetDescRelease -> IReleaseRepository.getById",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `Check return failure if have error in FrontRepository getById`() =
        runTest {
            whenever(
                managerRepository.getIdRelease()
            ).thenReturn(
                Result.success(1)
            )
            whenever(
                managerRepository.getIdFront()
            ).thenReturn(
                Result.success(2)
            )
            whenever(
                releaseRepository.getById(1)
            ).thenReturn(
                Result.success(
                    Release(
                        id = 123456,
                        nroOS = 456789,
                        idPropAgr = 1,
                        descPropAgr = "TestDescProgAgr",
                        idFront = 1
                    )
                )
            )
            whenever(
                frontRepository.getById(2)
            ).thenReturn(
                resultFailure(
                    "IFrontRepository.getById",
                    "-",
                    Exception()
                )
            )
            val result = usecase()
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IGetDescRelease -> IFrontRepository.getById",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `Check return correct if function execute successfully`() =
        runTest {
            whenever(
                managerRepository.getIdRelease()
            ).thenReturn(
                Result.success(1)
            )
            whenever(
                managerRepository.getIdFront()
            ).thenReturn(
                Result.success(2)
            )
            whenever(
                releaseRepository.getById(1)
            ).thenReturn(
                Result.success(
                    Release(
                        id = 123456,
                        nroOS = 456789,
                        idPropAgr = 1,
                        descPropAgr = "ProgAgr",
                        idFront = 1
                    )
                )
            )
            whenever(
                frontRepository.getById(2)
            ).thenReturn(
                Result.success(
                    Front(
                        id = 2,
                        cd = 20,
                        description = "Front 20"
                    )
                )
            )
            val result = usecase()
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                "FRENTE: Front 20\nLIBERAÇÃO: 123456\nO.S.: 456789\nPROPRIEDADE: ProgAgr",
                result.getOrNull()!!
            )
        }

}