package br.com.usinasantafe.cvf.domain.usecases.config

import br.com.usinasantafe.cvf.domain.repositories.variable.ConfigRepository
import br.com.usinasantafe.cvf.lib.StatusSend
import br.com.usinasantafe.cvf.utils.resultFailure
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.atLeastOnce
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

class ISetStatusSendTest {

    private val configRepository = mock<ConfigRepository>()
    private val usecase = ISetStatusSend(
        configRepository = configRepository
    )

    @Test
    fun `Check return failure if have error in ConfigRepository setStatusSend`() =
        runTest {
            whenever(
                configRepository.setStatusSend(StatusSend.SEND)
            ).thenReturn(
                resultFailure(
                    "IConfigRepository.setStatusSend",
                    "-",
                    Exception()
                )
            )
            val result = usecase(StatusSend.SEND)
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "ISetStatusSend -> IConfigRepository.setStatusSend",
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
            val result = usecase(StatusSend.SEND)
            verify(configRepository, atLeastOnce()).setStatusSend(StatusSend.SEND)
            assertEquals(
                true,
                result.isSuccess
            )
        }

}