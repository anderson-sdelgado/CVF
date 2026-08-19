package br.com.usinasantafe.cvf.domain.usecases.note

import br.com.usinasantafe.cvf.domain.repositories.variable.NoteRepository
import br.com.usinasantafe.cvf.utils.resultFailure
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

class IGetRegDriverTest {

    private val noteRepository = mock<NoteRepository>()
    private val usecase = IGetRegDriver(
        noteRepository = noteRepository
    )

    @Test
    fun `Check return failure if have error in NoteRepository getRegDriver`() =
        runTest {
            whenever(
                noteRepository.getRegDriver()
            ).thenReturn(
                resultFailure(
                    "INoteRepository.getRegDriver",
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
                "IGetRegDriver -> INoteRepository.getRegDriver",
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
                noteRepository.getRegDriver()
            ).thenReturn(
                Result.success(19759)
            )
            val result = usecase()
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                19759,
                result.getOrNull()!!
            )
        }

}