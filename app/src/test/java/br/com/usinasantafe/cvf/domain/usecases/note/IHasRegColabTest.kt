package br.com.usinasantafe.cvf.domain.usecases.note

import br.com.usinasantafe.cvf.domain.repositories.stable.ColabRepository
import br.com.usinasantafe.cvf.domain.usecases.common.Token
import br.com.usinasantafe.cvf.utils.CheckNetwork
import br.com.usinasantafe.cvf.utils.resultFailure
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import java.net.SocketTimeoutException
import kotlin.test.assertEquals

class IHasRegColabTest {

    private val token = mock<Token>()
    private val checkNetwork = mock<CheckNetwork>()
    private val colabRepository = mock<ColabRepository>()
    private val usecase = IHasRegColab(
        token = token,
        checkNetwork = checkNetwork,
        colabRepository = colabRepository,
    )

    @Test
    fun `Check return failure if value of field is incorrect`() =
        runTest {
            val result = usecase("de25")
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IHasRegDriver -> stringToLong",
                result.exceptionOrNull()!!.message,
            )
            assertEquals(
                "java.lang.NumberFormatException: For input string: \"de25\"",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `Check return failure if no connection and have error in ColabRepository check`() =
        runTest {
            whenever(
                checkNetwork.isConnected()
            ).thenReturn(
                false
            )
            whenever(
                colabRepository.check(19759)
            ).thenReturn(
                resultFailure(
                    "IColabRepository.check",
                    "-",
                    Exception()
                )
            )
            val result = usecase("19759")
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IHasRegDriver -> IColabRepository.check",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `Check return correct if no connection and function execute successfully`() =
        runTest {
            whenever(
                checkNetwork.isConnected()
            ).thenReturn(
                false
            )
            whenever(
                colabRepository.check(19759)
            ).thenReturn(
                Result.success(false)
            )
            val result = usecase("19759")
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
    fun `Check return failure if have connection and have error in token`() =
        runTest {
            whenever(
                checkNetwork.isConnected()
            ).thenReturn(
                true
            )
            whenever(
                token()
            ).thenReturn(
                resultFailure(
                    "IToken",
                    "-",
                    Exception()
                )
            )
            val result = usecase("19759")
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IHasRegDriver -> IToken",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `Check return correct if connection and ColabRepository check(Retrofit) execute successfully`() =
        runTest {
            whenever(
                checkNetwork.isConnected()
            ).thenReturn(
                true
            )
            whenever(
                token()
            ).thenReturn(
                Result.success("token")
            )
            whenever(
                colabRepository.check("token", 19759)
            ).thenReturn(
                Result.success(true)
            )
            val result = usecase("19759")
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                true,
                result.getOrNull()!!
            )
        }

    @Test
    fun `Check return failure if connection and have error in ColabRepository check(Retrofit)`() =
        runTest {
            whenever(
                checkNetwork.isConnected()
            ).thenReturn(
                true
            )
            whenever(
                token()
            ).thenReturn(
                Result.success("token")
            )
            whenever(
                colabRepository.check("token", 19759)
            ).thenReturn(
                resultFailure(
                    "IColabRepository.check(Retrofit)",
                    "-",
                    Exception()
                )
            )
            val result = usecase("19759")
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IHasRegDriver -> IColabRepository.check(Retrofit)",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `Check return failure if connection and have connection error in ColabRepository check(Retrofit) and have error in ColabRepository check(Room)`() =
        runTest {
            whenever(
                checkNetwork.isConnected()
            ).thenReturn(
                true
            )
            whenever(
                token()
            ).thenReturn(
                Result.success("token")
            )
            whenever(
                colabRepository.check("token", 19759)
            ).thenReturn(
                resultFailure(
                    "IColabRepository.check(Retrofit)",
                    "-",
                    SocketTimeoutException()
                )
            )
            whenever(
                colabRepository.check(19759)
            ).thenReturn(
                resultFailure(
                    "IColabRepository.check(Room)",
                    "-",
                    Exception()
                )
            )
            val result = usecase("19759")
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IHasRegDriver -> IColabRepository.check(Room)",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `Check return correct if connection and have connection error in ColabRepository check(Retrofit) and ColabRepository check(Room) execute successfully`() =
        runTest {
            whenever(
                checkNetwork.isConnected()
            ).thenReturn(
                true
            )
            whenever(
                token()
            ).thenReturn(
                Result.success("token")
            )
            whenever(
                colabRepository.check("token", 19759)
            ).thenReturn(
                resultFailure(
                    "IColabRepository.check(Retrofit)",
                    "-",
                    SocketTimeoutException()
                )
            )
            whenever(
                colabRepository.check(19759)
            ).thenReturn(
                Result.success(false)
            )
            val result = usecase("19759")
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