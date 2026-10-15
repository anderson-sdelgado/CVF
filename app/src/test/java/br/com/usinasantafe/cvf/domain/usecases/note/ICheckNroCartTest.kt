package br.com.usinasantafe.cvf.domain.usecases.note

import br.com.usinasantafe.cvf.domain.entities.stable.Equip
import br.com.usinasantafe.cvf.domain.repositories.stable.EquipRepository
import br.com.usinasantafe.cvf.domain.usecases.common.Token
import br.com.usinasantafe.cvf.lib.CheckCart
import br.com.usinasantafe.cvf.lib.TypeEquip
import br.com.usinasantafe.cvf.utils.CheckNetwork
import br.com.usinasantafe.cvf.utils.resultFailure
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import java.net.SocketTimeoutException
import kotlin.test.assertEquals

class ICheckNroCartTest {

    private val token = mock<Token>()
    private val checkNetwork = mock<CheckNetwork>()
    private val equipRepository = mock<EquipRepository>()
    private val usecase = ICheckNroCart(
        token = token,
        checkNetwork = checkNetwork,
        equipRepository = equipRepository
    )

    @Test
    fun `Check return failure if value of field is incorrect`() =
        runTest {
            val result = usecase("de25", 1)
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IHasNroCart -> stringToInt",
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
                equipRepository.checkNroCart(100,2)
            ).thenReturn(
                resultFailure(
                    "IEquipRepository.check",
                    "-",
                    Exception()
                )
            )
            val result = usecase("100", 2)
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IHasNroCart -> IEquipRepository.check",
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
                equipRepository.checkNroCart(100, 2)
            ).thenReturn(
                Result.success(false)
            )
            val result = usecase("100", 2)
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                CheckCart.INVALID,
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
            val result = usecase("100", 2)
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IHasNroCart -> IToken",
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
                equipRepository.checkNroCart("token", 100, 2)
            ).thenReturn(
                Result.success(
                    Equip(
                        id = 1,
                        nro = 100,
                        idOperClass = 1,
                        type = TypeEquip.CART
                    )
                )
            )
            val result = usecase("100", 2)
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                CheckCart.OK,
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
                equipRepository.checkNroCart("token", 100, 2)
            ).thenReturn(
                resultFailure(
                    "IEquipRepository.check(Retrofit)",
                    "-",
                    Exception()
                )
            )
            val result = usecase("100", 2)
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IHasNroCart -> IEquipRepository.check(Retrofit)",
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
                equipRepository.checkNroCart("token", 100, 2)
            ).thenReturn(
                resultFailure(
                    "IEquipRepository.check(Retrofit)",
                    "-",
                    SocketTimeoutException()
                )
            )
            whenever(
                equipRepository.checkNroCart(100, 2)
            ).thenReturn(
                resultFailure(
                    "IEquipRepository.check(Room)",
                    "-",
                    Exception()
                )
            )
            val result = usecase("100", 2)
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IHasNroCart -> IEquipRepository.check(Room)",
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
                equipRepository.checkNroCart("token", 100, 2)
            ).thenReturn(
                resultFailure(
                    "IEquipRepository.check(Retrofit)",
                    "-",
                    SocketTimeoutException()
                )
            )
            whenever(
                equipRepository.checkNroCart(100, 2)
            ).thenReturn(
                Result.success(false)
            )
            val result = usecase("100", 2)
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                CheckCart.INVALID,
                result.getOrNull()!!
            )
        }

}