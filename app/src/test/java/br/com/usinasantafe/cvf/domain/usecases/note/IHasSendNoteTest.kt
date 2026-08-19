package br.com.usinasantafe.cvf.domain.usecases.note

import br.com.usinasantafe.cvf.domain.repositories.variable.NoteRepository
import br.com.usinasantafe.cvf.utils.resultFailure
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

class IHasSendNoteTest {

    private val noteRepository = mock<NoteRepository>()
    private val usecase = IHasSendNote(
        noteRepository = noteRepository
    )

    @Test
    fun `Check return failure if have error in NoteRepository hasSend`() =
        runTest {
            whenever(
                noteRepository.hasSend()
            ).thenReturn(
                resultFailure(
                    "INoteRepository.hasSend",
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
                "IHasSendNote -> INoteRepository.hasSend",
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
                noteRepository.hasSend()
            ).thenReturn(
                Result.success(false)
            )
            val result = usecase()
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