package br.com.usinasantafe.cvf.domain.usecases.config

import br.com.usinasantafe.cvf.domain.repositories.variable.ConfigRepository
import br.com.usinasantafe.cvf.domain.usecases.background.StartWorkManager
import br.com.usinasantafe.cvf.utils.resultFailure
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.atLeastOnce
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

class ISetTokenFCMTest {

    private val configRepository = mock<ConfigRepository>()
    private val startWorkManager = mock<StartWorkManager>()
    private val usecase = ISetTokenFCM(
        configRepository = configRepository,
        startWorkManager = startWorkManager
    )

    @Test
    fun `Check return failure if have error in ConfigRepository setTokenFCM`() =
        runTest {
            whenever(
                configRepository.setTokenFCM("token")
            ).thenReturn(
                resultFailure(
                    "IConfigRepository.setTokenFCM",
                    "-",
                    Exception()
                )
            )
            val result = usecase("token")
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "ISetTokenFCM -> IConfigRepository.setTokenFCM",
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
            val result = usecase("token")
            verify(configRepository, atLeastOnce()).setTokenFCM("token")
            assertEquals(
                true,
                result.isSuccess
            )
        }

}