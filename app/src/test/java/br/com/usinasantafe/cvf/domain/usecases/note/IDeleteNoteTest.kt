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

class IDeleteNoteTest {

    private val noteRepository = mock<NoteRepository>()
    private val usecase = IDeleteNote(
        noteRepository = noteRepository
    )

    @Test
    fun `Check return failure if have error in NoteRepository deleteNote`() =
        runTest {
            whenever(
                noteRepository.deleteNote()
            ).thenReturn(
                resultFailure(
                    "INoteRepository.deleteNote",
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
                "IDeleteNote -> INoteRepository.deleteNote",
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
            val result = usecase()
            verify(noteRepository, atLeastOnce()).deleteNote()
            assertEquals(
                true,
                result.isSuccess
            )
        }

}