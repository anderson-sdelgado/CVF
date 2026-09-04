package br.com.usinasantafe.cvf.domain.usecases.note

import br.com.usinasantafe.cvf.domain.entities.variable.Cart
import br.com.usinasantafe.cvf.domain.repositories.stable.EquipRepository
import br.com.usinasantafe.cvf.domain.repositories.variable.NoteRepository
import br.com.usinasantafe.cvf.utils.resultFailure
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.atLeastOnce
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

class ISetNroTruckTest {

    private val noteRepository = mock<NoteRepository>()
    private val equipRepository = mock<EquipRepository>()
    private val usecase = ISetNroTruck(
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
                "ISetNroTruck -> stringToInt",
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
                "ISetNroTruck -> IEquipRepository.getIdByNro",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `Check return failure if have error in NoteRepository setIdTruck`() =
        runTest {
            whenever(
                equipRepository.getIdByNro(100)
            ).thenReturn(
                Result.success(20)
            )
            whenever(
                noteRepository.setIdTruck(20)
            ).thenReturn(
                resultFailure(
                    "INoteRepository.setIdTruck",
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
                "ISetNroTruck -> INoteRepository.setIdTruck",
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
                equipRepository.getIdByNro(100)
            ).thenReturn(
                Result.success(20)
            )
            val result = usecase("100")
            verify(noteRepository, atLeastOnce()).setIdTruck(20)
            assertEquals(
                true,
                result.isSuccess
            )
        }

}