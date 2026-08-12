package br.com.usinasantafe.cvf.domain.usecases.config

import br.com.usinasantafe.cvf.domain.repositories.variable.ConfigRepository
import br.com.usinasantafe.cvf.utils.resultFailure
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.atLeastOnce
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

class ISetFinishUpdateAllTableTest {

    private val configRepository = mock<ConfigRepository>()
    private val usecase = ISetFinishUpdateAllTable(
        configRepository = configRepository
    )

    @Test
    fun `Check return failure if have error in ConfigRepository setFlagUpdate`() =
        runTest {
            whenever(
                configRepository.setFlagUpdate()
            ).thenReturn(
                resultFailure(
                    "IConfigRepository.setFlagUpdate",
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
                "ISetFinishUpdateAllTable -> IConfigRepository.setFlagUpdate",
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
            val result = usecase()
            verify(configRepository, atLeastOnce()).setFlagUpdate()
            assertEquals(
                true,
                result.isSuccess
            )
        }

}