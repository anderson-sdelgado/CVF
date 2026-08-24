package br.com.usinasantafe.cvf.domain.usecases.manager

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import br.com.usinasantafe.cvf.domain.entities.stable.Release
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
class IGetDescReleaseTest {

    private val releaseRepository = mock<ReleaseRepository>()
    private val managerRepository = mock<ManagerRepository>()
    private lateinit var usecase: IGetDescRelease

    @Before
    fun setup() {
        val context: Context = ApplicationProvider.getApplicationContext()
        usecase = IGetDescRelease(
            context = context,
            releaseRepository = releaseRepository,
            managerRepository = managerRepository
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
    fun `Check return failure if have error in ReleaseRepository getById`() =
        runTest {
            whenever(
                managerRepository.getIdRelease()
            ).thenReturn(
                Result.success(1)
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
    fun `Check return correct if function execute successfully`() =
        runTest {
            whenever(
                managerRepository.getIdRelease()
            ).thenReturn(
                Result.success(1)
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
            val result = usecase()
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                "LIBERAÇÃO: 123456\nO.S.: 456789\nPROPRIEDADE: TestDescProgAgr",
                result.getOrNull()!!
            )
        }

}