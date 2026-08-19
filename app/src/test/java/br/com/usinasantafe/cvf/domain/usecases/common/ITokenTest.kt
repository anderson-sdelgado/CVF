package br.com.usinasantafe.cvf.domain.usecases.common

import br.com.usinasantafe.cvf.domain.entities.variable.Config
import br.com.usinasantafe.cvf.domain.repositories.variable.ConfigRepository
import br.com.usinasantafe.cvf.utils.resultFailure
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

class ITokenTest {

    private val configRepository = mock<ConfigRepository>()
    private val usecase = IToken(
        configRepository = configRepository
    )

    @Test
    fun `Check return failure if have error in ConfigRepository get`() =
        runTest {
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
                "IGetToken -> IConfigRepository.get",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `Check return failure if token is null`() =
        runTest {
            whenever(
                configRepository.get()
            ).thenReturn(
                Result.success(Config())
            )
            val result = usecase()
            assertEquals(
                result.isFailure,
                true
            )
            assertEquals(
                "IGetToken -> token",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.NullPointerException: idServ is required",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `Check return correct if function execute successfully`() =
        runTest {
            whenever(
                configRepository.get()
            ).thenReturn(
                Result.success(
                    Config(
                        number = 16997417840,
                        version = "1.00",
                        idServ = 0
                    )
                )
            )
            val result = usecase()
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                "Bearer ABAEBF582FAE11B64AD1C11711A99CD9",
                result.getOrNull()!!
            )
        }

}