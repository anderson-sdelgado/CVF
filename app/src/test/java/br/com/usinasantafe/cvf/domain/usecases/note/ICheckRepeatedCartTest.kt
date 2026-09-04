package br.com.usinasantafe.cvf.domain.usecases.note

import br.com.usinasantafe.cvf.domain.entities.variable.Cart
import br.com.usinasantafe.cvf.domain.repositories.stable.EquipRepository
import br.com.usinasantafe.cvf.domain.repositories.variable.NoteRepository
import br.com.usinasantafe.cvf.utils.resultFailure
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

class ICheckRepeatedCartTest {

    private val noteRepository = mock<NoteRepository>()
    private val equipRepository = mock<EquipRepository>()
    private val usecase = ICheckRepeatedCart(
        noteRepository = noteRepository,
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
                "ICheckRepeatedCart -> stringToInt",
                result.exceptionOrNull()!!.message,
            )
            assertEquals(
                "java.lang.NumberFormatException: For input string: \"de25\"",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `Check return failure if have error in EquipRepository getIdByNro`() =
        runTest {
            whenever(
                equipRepository.getIdByNro(100)
            ).thenReturn(
                resultFailure(
                    "IEquipRepository.getIdByNro",
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
                "ICheckRepeatedCart -> IEquipRepository.getIdByNro",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `Check return failure if have error in NoteRepository cartList`() =
        runTest {
            whenever(
                equipRepository.getIdByNro(100)
            ).thenReturn(
                Result.success(12)
            )
            whenever(
                noteRepository.cartList()
            ).thenReturn(
                resultFailure(
                    "INoteRepository.cartList",
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
                "ICheckRepeatedCart -> INoteRepository.cartList",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `Check return true if cart is repeated`() =
        runTest {
            whenever(
                equipRepository.getIdByNro(100)
            ).thenReturn(
                Result.success(12)
            )
            whenever(
                noteRepository.cartList()
            ).thenReturn(
                Result.success(
                    listOf(
                        Cart(
                            pos = 1,
                            idCart = 12
                        ),
                        Cart(
                            pos = 2,
                            idCart = 26
                        )
                    )
                )
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
    fun `Check return false if cart is not repeated`() =
        runTest {
            whenever(
                equipRepository.getIdByNro(100)
            ).thenReturn(
                Result.success(15)
            )
            whenever(
                noteRepository.cartList()
            ).thenReturn(
                Result.success(
                    listOf(
                        Cart(
                            pos = 1,
                            idCart = 12
                        ),
                        Cart(
                            pos = 2,
                            idCart = 26
                        )
                    )
                )
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