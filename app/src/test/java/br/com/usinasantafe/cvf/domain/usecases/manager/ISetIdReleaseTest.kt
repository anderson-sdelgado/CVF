package br.com.usinasantafe.cvf.domain.usecases.manager

import br.com.usinasantafe.cvf.domain.repositories.variable.ManagerRepository
import br.com.usinasantafe.cvf.domain.usecases.background.StartWorkManager
import br.com.usinasantafe.cvf.utils.resultFailure
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.atLeastOnce
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

class ISetIdReleaseTest {

    private val managerRepository = mock<ManagerRepository>()
    private val startWorkManager = mock<StartWorkManager>()
    private val usecase = ISetIdRelease(
        managerRepository = managerRepository,
        startWorkManager = startWorkManager
    )

    @Test
    fun `Check return failure if have error in ManagerRepository setIdRelease`() =
        runTest {
            whenever(
                managerRepository.setIdRelease(1)
            ).thenReturn(
                resultFailure(
                    "IManagerRepository.setIdRelease",
                    "-",
                    Exception()
                )
            )
            val result = usecase(1)
            verify(startWorkManager, never()).invoke()
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "ISetRelease -> IManagerRepository.setIdRelease",
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
            val result = usecase(1)
            verify(managerRepository, atLeastOnce()).setIdRelease(1)
            verify(startWorkManager, atLeastOnce()).invoke()
            assertEquals(
                true,
                result.isSuccess
            )
        }

}