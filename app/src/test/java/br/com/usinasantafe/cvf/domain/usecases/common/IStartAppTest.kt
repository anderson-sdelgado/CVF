package br.com.usinasantafe.cvf.domain.usecases.common

import br.com.usinasantafe.cvf.domain.repositories.variable.ConfigRepository
import br.com.usinasantafe.cvf.domain.repositories.variable.ManagerRepository
import br.com.usinasantafe.cvf.domain.repositories.variable.NoteRepository
import br.com.usinasantafe.cvf.lib.FlowApp
import br.com.usinasantafe.cvf.lib.StatusSend
import br.com.usinasantafe.cvf.utils.resultFailure
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.atLeastOnce
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

class IStartAppTest {

    private val configRepository = mock<ConfigRepository>()
    private val managerRepository = mock<ManagerRepository>()
    private val noteRepository = mock<NoteRepository>()
    private val usecase = IStartApp(
        configRepository = configRepository,
        managerRepository = managerRepository,
        noteRepository = noteRepository
    )

    @Test
    fun `Check return failure if have error in ConfigRepository getFlagUpdate`() =
        runTest {
            whenever(
                configRepository.getFlagUpdate()
            ).thenReturn(
                resultFailure(
                    "IConfigRepository.getFlagUpdate",
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
                "IStartApp -> IConfigRepository.getFlagUpdate",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `Check return FlowApp CONFIG if ConfigRepository getFlagUpdate return false`() =
        runTest {
            whenever(
                configRepository.getFlagUpdate()
            ).thenReturn(
                Result.success(false)
            )
            val result = usecase()
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                FlowApp.CONFIG,
                result.getOrNull()!!
            )
        }

    @Test
    fun `Check return failure if have error in ManagerRepository has`() =
        runTest {
            whenever(
                configRepository.getFlagUpdate()
            ).thenReturn(
                Result.success(true)
            )
            whenever(
                managerRepository.has()
            ).thenReturn(
                resultFailure(
                    "IManagerRepository.has",
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
                "IStartApp -> IManagerRepository.has",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `Check return FlowApp FRONT if ManagerRepository has return false`() =
        runTest {
            whenever(
                configRepository.getFlagUpdate()
            ).thenReturn(
                Result.success(true)
            )
            whenever(
                managerRepository.has()
            ).thenReturn(
                Result.success(false)
            )
            val result = usecase()
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                FlowApp.FRONT,
                result.getOrNull()!!
            )
        }

    @Test
    fun `Check return failure if have error in ManagerRepository getStatusSend`() =
        runTest {
            whenever(
                configRepository.getFlagUpdate()
            ).thenReturn(
                Result.success(true)
            )
            whenever(
                managerRepository.has()
            ).thenReturn(
                Result.success(true)
            )
            whenever(
                managerRepository.getStatusSend()
            ).thenReturn(
                resultFailure(
                    "IManagerRepository.getStatusSend",
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
                "IStartApp -> IManagerRepository.getStatusSend",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `Check return FlowApp RELEASE if ManagerRepository getStatusSend is StatusSend STARTED`() =
        runTest {
            whenever(
                configRepository.getFlagUpdate()
            ).thenReturn(
                Result.success(true)
            )
            whenever(
                managerRepository.has()
            ).thenReturn(
                Result.success(true)
            )
            whenever(
                managerRepository.getStatusSend()
            ).thenReturn(
                Result.success(StatusSend.STARTED)
            )
            val result = usecase()
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                FlowApp.RELEASE,
                result.getOrNull()!!
            )
        }

    @Test
    fun `Check return failure if have error in NoteRepository clean`() =
        runTest {
            whenever(
                configRepository.getFlagUpdate()
            ).thenReturn(
                Result.success(true)
            )
            whenever(
                managerRepository.has()
            ).thenReturn(
                Result.success(true)
            )
            whenever(
                managerRepository.getStatusSend()
            ).thenReturn(
                Result.success(StatusSend.SEND)
            )
            whenever(
                noteRepository.clean()
            ).thenReturn(
                resultFailure(
                    "INoteRepository.clean",
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
                "IStartApp -> INoteRepository.clean",
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
                configRepository.getFlagUpdate()
            ).thenReturn(
                Result.success(true)
            )
            whenever(
                managerRepository.has()
            ).thenReturn(
                Result.success(true)
            )
            whenever(
                managerRepository.getStatusSend()
            ).thenReturn(
                Result.success(StatusSend.SEND)
            )
            val result = usecase()
            verify(noteRepository, atLeastOnce()).clean()
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                FlowApp.NOTE,
                result.getOrNull()!!
            )
        }

}