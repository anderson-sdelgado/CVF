package br.com.usinasantafe.cvf.domain.usecases.note

import br.com.usinasantafe.cvf.domain.repositories.stable.EquipRepository
import br.com.usinasantafe.cvf.domain.usecases.common.Token
import br.com.usinasantafe.cvf.utils.CheckNetwork
import br.com.usinasantafe.cvf.utils.resultFailure
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import java.net.SocketTimeoutException
import kotlin.test.assertEquals

class ICheckNroTruckTest {

    private val token = mock<Token>()
    private val checkNetwork = mock<CheckNetwork>()
    private val equipRepository = mock<EquipRepository>()
    private val usecase = ICheckNroTruck(
        token = token,
        checkNetwork = checkNetwork,
        equipRepository = equipRepository
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
                "ICheckNroTruck -> stringToInt",
                result.exceptionOrNull()!!.message,
            )
            assertEquals(
                "java.lang.NumberFormatException: For input string: \"de25\"",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `Check return failure if have error in EquipRepository check`() =
        runTest {
            whenever(
                checkNetwork.isConnected()
            ).thenReturn(
                false
            )
            whenever(
                equipRepository.check(100)
            ).thenReturn(
                resultFailure(
                    "IEquipRepository.check",
                    "-",
                    Exception()
                )
            )
            val result = usecase("100")
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "ICheckNroTruck -> IEquipRepository.check",
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
                equipRepository.check(100)
            ).thenReturn(
                Result.success(false)
            )
            val result = usecase("100")
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
            val result = usecase("100")
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "ICheckNroTruck -> IToken",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `Check return correct if connection and EquipRepository check(Retrofit) execute successfully`() =
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
                equipRepository.check("token", 100)
            ).thenReturn(
                Result.success(true)
            )
            val result = usecase("100")
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
    fun `Check return failure if connection and have error in EquipRepository check(Retrofit)`() =
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
                equipRepository.check("token", 100)
            ).thenReturn(
                resultFailure(
                    "IEquipRepository.check(Retrofit)",
                    "-",
                    Exception()
                )
            )
            val result = usecase("100")
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "ICheckNroTruck -> IEquipRepository.check(Retrofit)",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `Check return failure if connection and have connection error in EquipRepository check(Retrofit) and have error in EquipRepository check(Room)`() =
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
                equipRepository.check("token", 100)
            ).thenReturn(
                resultFailure(
                    "IEquipRepository.check(Retrofit)",
                    "-",
                    SocketTimeoutException()
                )
            )
            whenever(
                equipRepository.check(100)
            ).thenReturn(
                resultFailure(
                    "IEquipRepository.check(Room)",
                    "-",
                    Exception()
                )
            )
            val result = usecase("100")
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "ICheckNroTruck -> IEquipRepository.check(Room)",
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
                equipRepository.check("token", 100)
            ).thenReturn(
                resultFailure(
                    "IEquipRepository.check(Retrofit)",
                    "-",
                    SocketTimeoutException()
                )
            )
            whenever(
                equipRepository.check(100)
            ).thenReturn(
                Result.success(false)
            )
            val result = usecase("100")
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