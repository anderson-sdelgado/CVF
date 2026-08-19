package br.com.usinasantafe.cvf.domain.usecases.note

import br.com.usinasantafe.cvf.domain.entities.variable.Config
import br.com.usinasantafe.cvf.domain.repositories.variable.ConfigRepository
import br.com.usinasantafe.cvf.domain.repositories.variable.NoteRepository
import br.com.usinasantafe.cvf.domain.usecases.common.Token
import br.com.usinasantafe.cvf.utils.resultFailure
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.atLeastOnce
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

class ISendNoteTest {

    private val token = mock<Token>()
    private val configRepository = mock<ConfigRepository>()
    private val noteRepository = mock<NoteRepository>()
    private val usecase = ISendNote(
        token = token,
        configRepository = configRepository,
        noteRepository = noteRepository
    )

    @Test
    fun `Check return failure if have error in Token`() =
        runTest {
            whenever(
                token()
            ).thenReturn(
                resultFailure(
                    "Token",
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
                "ISendNote -> Token",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `Check return failure if have error in ConfigRepository get`() =
        runTest {
            whenever(
                token()
            ).thenReturn(
                Result.success("token")
            )
            whenever(
                configRepository.get()
            ).thenReturn(
                resultFailure(
                    "IConfigRepository.get",
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
                "ISendNote -> IConfigRepository.get",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `Check return failure if have error in ManagerRepository send`() =
        runTest {
            whenever(
                token()
            ).thenReturn(
                Result.success("token")
            )
            whenever(
                configRepository.get()
            ).thenReturn(
                Result.success(
                    Config(
                        idServ = 1,
                    )
                )
            )
            whenever(
                noteRepository.send("token", 1)
            ).thenReturn(
                resultFailure(
                    "INoteRepository.send",
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
                "ISendNote -> INoteRepository.send",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `Check return failure if idServ is null`() =
        runTest {
            whenever(
                token()
            ).thenReturn(
                Result.success("token")
            )
            whenever(
                configRepository.get()
            ).thenReturn(
                Result.success(
                    Config()
                )
            )
            whenever(
                noteRepository.send("token", 1)
            ).thenReturn(
                resultFailure(
                    "INoteRepository.send",
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
                "ISendNote -> idServ is required",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.NullPointerException: idServ is required",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `Check return correct if function execute successfully`() =
        runTest {
            whenever(
                token()
            ).thenReturn(
                Result.success("token")
            )
            whenever(
                configRepository.get()
            ).thenReturn(
                Result.success(
                    Config(
                        idServ = 1,
                    )
                )
            )
            val result = usecase()
            verify(noteRepository, atLeastOnce()).send("token", 1)
            assertEquals(
                true,
                result.isSuccess
            )
        }
}