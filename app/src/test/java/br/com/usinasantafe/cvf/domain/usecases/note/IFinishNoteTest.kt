package br.com.usinasantafe.cvf.domain.usecases.note

import br.com.usinasantafe.cvf.domain.repositories.variable.NoteRepository
import br.com.usinasantafe.cvf.domain.usecases.background.StartWorkManager
import br.com.usinasantafe.cvf.utils.resultFailure
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.atLeastOnce
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.math.atan
import kotlin.test.assertEquals

class IFinishNoteTest {

    private val noteRepository = mock<NoteRepository>()
    private val startWorkManager = mock<StartWorkManager>()
    private val usecase = IFinishNote(
        noteRepository = noteRepository,
        startWorkManager = startWorkManager
    )

    @Test
    fun `Check return failure if have error in NoteRepository finish`() =
        runTest {
            whenever(
                noteRepository.finish()
            ).thenReturn(
                resultFailure(
                    "INoteRepository.finish",
                    "-",
                    Exception()
                )
            )
            val result = usecase()
            verify(startWorkManager, never())()
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IFinishNote -> INoteRepository.finish",
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
            verify(noteRepository, atLeastOnce()).finish()
            verify(startWorkManager, atLeastOnce())()
            assertEquals(
                true,
                result.isSuccess
            )
        }

}