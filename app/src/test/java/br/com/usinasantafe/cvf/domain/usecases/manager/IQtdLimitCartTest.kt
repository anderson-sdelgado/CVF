package br.com.usinasantafe.cvf.domain.usecases.manager

import br.com.usinasantafe.cvf.domain.repositories.variable.ManagerRepository
import br.com.usinasantafe.cvf.utils.resultFailure
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

class IQtdLimitCartTest {

    private val managerRepository = mock<ManagerRepository>()
    private val usecase = IQtdLimitCart(
        managerRepository = managerRepository
    )

    @Test
    fun `Check return failure if have error in ManagerRepository getQtdLimitCart`() =
        runTest {
            whenever(
                managerRepository.getQtdLimitCart()
            ).thenReturn(
                resultFailure(
                    "IManagerRepository.getQtdLimitCart",
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
                "IQtdLimitCart -> IManagerRepository.getQtdLimitCart",
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
                managerRepository.getQtdLimitCart()
            ).thenReturn(
                Result.success(3)
            )
            val result = usecase()
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                3,
                result.getOrNull()!!
            )
        }

}