package br.com.usinasantafe.cvf.domain.usecases.manager

import br.com.usinasantafe.cvf.domain.repositories.variable.ManagerRepository
import br.com.usinasantafe.cvf.utils.resultFailure
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.atLeastOnce
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

class ISetIdFrontTest {

    private val managerRepository = mock<ManagerRepository>()
    private val usecase = ISetIdFront(
        managerRepository = managerRepository
    )

    @Test
    fun `Check return failure if have error in ManagerRepository setIdFront`() =
        runTest {
            whenever(
                managerRepository.setIdFront(1)
            ).thenReturn(
                resultFailure(
                    "IManagerRepository.setIdFront",
                    "-",
                    Exception()
                )
            )
            val result = usecase(1)
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "ISetIdFront -> IManagerRepository.setIdFront",
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
            verify(managerRepository, atLeastOnce()).setIdFront(1)
            assertEquals(
                true,
                result.isSuccess
            )
        }


}