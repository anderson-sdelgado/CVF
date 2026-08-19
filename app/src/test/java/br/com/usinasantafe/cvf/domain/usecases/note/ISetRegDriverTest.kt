package br.com.usinasantafe.cvf.domain.usecases.note

import br.com.usinasantafe.cvf.domain.repositories.variable.NoteRepository
import br.com.usinasantafe.cvf.utils.resultFailure
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.atLeastOnce
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

class ISetRegDriverTest {

    private val noteRepository = mock<NoteRepository>()
    private val usecase = ISetRegDriver(
        noteRepository = noteRepository
    )

    @Test
    fun `Check return failure if value of field is incorrect`() =
        runTest {
            val result = usecase("de25")
            assertEquals(
                result.isFailure,
                true
            )
            assertEquals(
                result.exceptionOrNull()!!.message,
                "ISetRegDriver -> toLong"
            )
            assertEquals(
                result.exceptionOrNull()!!.cause.toString(),
                "java.lang.NumberFormatException: For input string: \"de25\""
            )
        }

    @Test
    fun `Check return failure if have error in NoteRepository setRegDriver`() =
        runTest {
            whenever(
                noteRepository.setRegDriver(19759)
            ).thenReturn(
                resultFailure(
                    "INoteRepository.setRegDriver",
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
                "ISetRegDriver -> INoteRepository.setRegDriver",
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
            val result = usecase("19759")
            verify(noteRepository, atLeastOnce()).setRegDriver(19759)
            assertEquals(
                true,
                result.isSuccess
            )
        }


}