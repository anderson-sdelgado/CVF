package br.com.usinasantafe.cvf.domain.usecases.config

import br.com.usinasantafe.cvf.domain.entities.variable.Config
import br.com.usinasantafe.cvf.domain.repositories.variable.ConfigRepository
import br.com.usinasantafe.cvf.presenter.model.ConfigScreenModel
import br.com.usinasantafe.cvf.utils.resultFailure
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

class IGetConfigTest {

    private val configRepository = mock<ConfigRepository>()
    private val usecase = IGetConfig(
        configRepository = configRepository
    )

    @Test
    fun `Check return failure if have error in ConfigRepository hasConfig`() =
        runTest {
            whenever(
                configRepository.has()
            ).thenReturn(
                resultFailure(
                    "IConfigRepository.hasConfig",
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
                "IGetConfig -> IConfigRepository.hasConfig",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `Check return null if ConfigRepository hasConfig return false`() =
        runTest {
            whenever(
                configRepository.has()
            ).thenReturn(
                Result.success(false)
            )
            val result = usecase()
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                null,
                result.getOrNull()
            )
        }

    @Test
    fun `Check return failure if have error in ConfigRepository get`() =
        runTest {
            whenever(
                configRepository.has()
            ).thenReturn(
                Result.success(true)
            )
            whenever(
                configRepository.get()
            ).thenReturn(
                resultFailure(
                    "IConfigRepository.get",
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
                "IGetConfig -> IConfigRepository.get",
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
                configRepository.has()
            ).thenReturn(
                Result.success(true)
            )
            whenever(
                configRepository.get()
            ).thenReturn(
                Result.success(
                    Config(
                        number = 16997417840,
                        password = "12345"
                    )
                )
            )
            val result = usecase()
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                ConfigScreenModel(
                    number = "16997417840",
                    password = "12345"
                ),
                result.getOrNull()!!
            )
        }

}