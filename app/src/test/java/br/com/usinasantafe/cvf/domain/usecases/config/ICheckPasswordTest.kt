package br.com.usinasantafe.cvf.domain.usecases.config

import br.com.usinasantafe.cvf.domain.repositories.variable.ConfigRepository
import br.com.usinasantafe.cvf.utils.resultFailure
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

class ICheckPasswordTest {

    private val configRepository = mock<ConfigRepository>()
    private val usecase = ICheckPassword(
        configRepository = configRepository
    )
    
    @Test
    fun `Check return failure if have error in ConfigRepository getPassword`() =
        runTest {
            whenever(
                configRepository.getPassword()
            ).thenReturn(
                resultFailure(
                    "IConfigRepository.getPassword",
                    "-",
                    Exception()
                )
            )
            val result = usecase("12345")
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "ICheckPassword -> IConfigRepository.getPassword",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `Check return false if password is incorrect`() =
        runTest {
            whenever(
                configRepository.getPassword()
            ).thenReturn(
                Result.success("123456")
            )
            val result = usecase("12345")
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
    fun `Check return true if password is correct`() =
        runTest {
            whenever(
                configRepository.getPassword()
            ).thenReturn(
                Result.success("123456")
            )
            val result = usecase("12345")
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