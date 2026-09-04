package br.com.usinasantafe.cvf.domain.usecases.note

import br.com.usinasantafe.cvf.domain.entities.stable.Equip
import br.com.usinasantafe.cvf.domain.entities.variable.Cart
import br.com.usinasantafe.cvf.domain.repositories.stable.EquipRepository
import br.com.usinasantafe.cvf.domain.repositories.variable.NoteRepository
import br.com.usinasantafe.cvf.lib.TypeEquip
import br.com.usinasantafe.cvf.utils.resultFailure
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

class IGetNroCartTest {

    private val noteRepository = mock<NoteRepository>()
    private val equipRepository = mock<EquipRepository>()
    private val usecase = IGetNroCart(
        noteRepository = noteRepository,
        equipRepository = equipRepository
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
            val result = usecase(1)
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IGetNroCart -> INoteRepository.cartList",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `Check return null if function execute successfully`() =
        runTest {
            whenever(
                noteRepository.cartList()
            ).thenReturn(
                Result.success(
                    listOf(
                        Cart(
                            pos = 1,
                            idCart = 1
                        )
                    )
                )
            )
            val result = usecase(2)
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
    fun `Check return failure if have error in EquipRepository getById`() =
        runTest {
            whenever(
                noteRepository.cartList()
            ).thenReturn(
                Result.success(
                    listOf(
                        Cart(
                            pos = 2,
                            idCart = 10
                        )
                    )
                )
            )
            whenever(
                equipRepository.getById(10)
            ).thenReturn(
                resultFailure(
                    "IEquipRepository.getById",
                    "-",
                    Exception()
                )
            )
            val result = usecase(2)
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IGetNroCart -> IEquipRepository.getById",
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
                noteRepository.cartList()
            ).thenReturn(
                Result.success(
                    listOf(
                        Cart(
                            pos = 2,
                            idCart = 10
                        )
                    )
                )
            )
            whenever(
                equipRepository.getById(10)
            ).thenReturn(
                Result.success(
                    Equip(
                        id = 10,
                        nro = 250,
                        cdOperClass = 200,
                        descOperClass = "Test",
                        type = TypeEquip.CART
                    )
                )
            )
            val result = usecase(2)
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                "250",
                result.getOrNull()!!
            )
        }

}