package br.com.usinasantafe.cvf.domain.usecases.manager

import br.com.usinasantafe.cvf.domain.repositories.variable.ManagerRepository
import br.com.usinasantafe.cvf.utils.resultFailure
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

class IHasSendManagerTest {

    private val managerRepository = mock<ManagerRepository>()
    private val usecase = IHasSendManager(
        managerRepository = managerRepository
    )
    
    @Test
    fun `Check return failure if have error in ManagerRepository hasSend`() =
        runTest {
            whenever(
                managerRepository.hasSend()
            ).thenReturn(
                resultFailure(
                    "IManagerRepository.hasSend",
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
                "IHasSendManager -> IManagerRepository.hasSend",
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
                managerRepository.hasSend()
            ).thenReturn(
                Result.success(false)
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

}