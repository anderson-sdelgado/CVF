package br.com.usinasantafe.cvf.domain.usecases.manager

import br.com.usinasantafe.cvf.domain.entities.variable.Config
import br.com.usinasantafe.cvf.domain.repositories.variable.ConfigRepository
import br.com.usinasantafe.cvf.domain.repositories.variable.ManagerRepository
import br.com.usinasantafe.cvf.domain.usecases.common.Token
import br.com.usinasantafe.cvf.utils.resultFailure
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.atLeastOnce
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

class ISendManagerTest {

    private val token = mock<Token>()
    private val configRepository = mock<ConfigRepository>()
    private val managerRepository = mock<ManagerRepository>()
    private val usecase = ISendManager(
        token = token,
        configRepository = configRepository,
        managerRepository = managerRepository
    )

    @Test
    fun `Check return failure if have error in Token`() =
        runTest {
            whenever(
                token()
            ).thenReturn(
                resultFailure(
                    "Token",
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
                "ISendManager -> Token",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `Check return failure if have error in ConfigRepository get`() =
        runTest {
            whenever(
                token()
            ).thenReturn(
                Result.success("token")
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
                "ISendManager -> IConfigRepository.get",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `Check return failure if have error in ManagerRepository send`() =
        runTest {
            whenever(
                token()
            ).thenReturn(
                Result.success("token")
            )
            whenever(
                configRepository.get()
            ).thenReturn(
                Result.success(
                    Config(
                        idServ = 1,
                    )
                )
            )
            whenever(
                managerRepository.send("token", 1)
            ).thenReturn(
                resultFailure(
                    "IManagerRepository.send",
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
                "ISendManager -> IManagerRepository.send",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `Check return failure if idServ is null`() =
        runTest {
            whenever(
                token()
            ).thenReturn(
                Result.success("token")
            )
            whenever(
                configRepository.get()
            ).thenReturn(
                Result.success(
                    Config()
                )
            )
            whenever(
                managerRepository.send("token", 1)
            ).thenReturn(
                resultFailure(
                    "IManagerRepository.send",
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
                "ISendManager -> idServ is required",
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
                token()
            ).thenReturn(
                Result.success("token")
            )
            whenever(
                configRepository.get()
            ).thenReturn(
                Result.success(
                    Config(
                        idServ = 1,
                    )
                )
            )
            val result = usecase()
            verify(managerRepository, atLeastOnce()).send("token", 1)
            assertEquals(
                true,
                result.isSuccess
            )
        }
}