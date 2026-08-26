package br.com.usinasantafe.cvf.domain.usecases.manager

import br.com.usinasantafe.cvf.domain.entities.variable.Manager
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

class ISetReleaseTest {

    private val managerRepository = mock<ManagerRepository>()
    private val startWorkManager = mock<StartWorkManager>()
    private val usecase = ISetRelease(
        managerRepository = managerRepository,
        startWorkManager = startWorkManager
    )

    @Test
    fun `Check return failure if have error in ManagerRepository save`() =
        runTest {
            whenever(
                managerRepository.setIdRelease(
                    Manager(
                        idFront = 1,
                        idRelease = 1
                    )
                )
            ).thenReturn(
                resultFailure(
                    "IManagerRepository.save",
                    "-",
                    Exception()
                )
            )
            val result = usecase(
                idFront = 1,
                idRelease = 1
            )
            verify(startWorkManager, never()).invoke()
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "ISaveManager -> IManagerRepository.save",
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
            val result = usecase(
                idFront = 1,
                idRelease = 1
            )
            verify(managerRepository, atLeastOnce()).setIdRelease(
                Manager(
                    idFront = 1,
                    idRelease = 1
                )
            )
            verify(startWorkManager, atLeastOnce()).invoke()
            assertEquals(
                true,
                result.isSuccess
            )
        }

}