package br.com.usinasantafe.cvf.domain.usecases.note

import br.com.usinasantafe.cvf.domain.entities.variable.Cart
import br.com.usinasantafe.cvf.domain.repositories.variable.NoteRepository
import br.com.usinasantafe.cvf.utils.resultFailure
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

class IPosCartTest {

    private val noteRepository = mock<NoteRepository>()
    private val usecase = IPosCart(
        noteRepository = noteRepository
    )

    @Test
    fun `Check return failure if have error in NoteRepository cartList`() =
        runTest {
            whenever(
                noteRepository.cartList()
            ).thenReturn(
                resultFailure(
                    "INoteRepository.cartList",
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
                "IPosCart -> INoteRepository.cartList",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `Check return failure if NoteRepository cartList is empty`() =
        runTest {
            whenever(
                noteRepository.cartList()
            ).thenReturn(
                Result.success(emptyList())
            )
            val result = usecase()
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IPosCart",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.util.NoSuchElementException",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `Check return correct if function execute successfully`() =
        runTest {
            whenever(
                noteRepository.cartList()
            ).thenReturn(
                Result.success(
                    listOf(
                        Cart(
                            position = 1,
                            idCart = 20
                        ),
                        Cart(
                            position = 56,
                            idCart = 21
                        ),
                        Cart(
                            position = 41,
                            idCart = 22
                        ),
                        Cart(
                            position = 4,
                            idCart = 23
                        )
                    )
                )
            )
            val result = usecase()
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                57,
                result.getOrNull()!!
            )
        }

}