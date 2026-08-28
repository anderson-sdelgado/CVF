package br.com.usinasantafe.cvf.domain.usecases.manager

import br.com.usinasantafe.cvf.domain.repositories.variable.ManagerRepository
import br.com.usinasantafe.cvf.lib.StatusSend
import br.com.usinasantafe.cvf.utils.resultFailure
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

class ICheckStatusManagerTest {

    private val managerRepository = mock<ManagerRepository>()
    private val usecase = ICheckStatusManager(
        managerRepository = managerRepository
    )

    @Test
    fun `Check return failure if have error in ManagerRepository getStatusSend`() =
        runTest {
            whenever(
                managerRepository.getStatusSend()
            ).thenReturn(
                resultFailure(
                    "IManagerRepository.getStatusSend",
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
                "ICheckStatusManager -> IManagerRepository.getStatusSend",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `Check return false if function execute successfully and Status Send is STARTED`() =
        runTest {
            whenever(
                managerRepository.getStatusSend()
            ).thenReturn(
                Result.success(StatusSend.STARTED)
            )
            val result = usecase()
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
    fun `Check return true if function execute successfully and Status Send is not STARTED`() =
        runTest {
            whenever(
                managerRepository.getStatusSend()
            ).thenReturn(
                Result.success(StatusSend.SEND)
            )
            val result = usecase()
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                true,
                result.getOrNull()!!
            )
        }
}