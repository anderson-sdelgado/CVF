package br.com.usinasantafe.cvf.domain.usecases.manager

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import br.com.usinasantafe.cvf.R
import br.com.usinasantafe.cvf.TestApp
import br.com.usinasantafe.cvf.domain.entities.stable.Front
import br.com.usinasantafe.cvf.domain.entities.stable.Release
import br.com.usinasantafe.cvf.domain.entities.variable.Manager
import br.com.usinasantafe.cvf.domain.repositories.stable.FrontRepository
import br.com.usinasantafe.cvf.domain.repositories.stable.ReleaseRepository
import br.com.usinasantafe.cvf.domain.repositories.variable.ManagerRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.assertEquals

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34], application = TestApp::class)
class IGetTitleMenuTest {

    private val releaseRepository = mock<ReleaseRepository>()
    private val managerRepository = mock<ManagerRepository>()
    private val frontRepository = mock<FrontRepository>()
    private lateinit var usecase: IGetTitleMenu
    private lateinit var context: Context

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        usecase = IGetTitleMenu(
            context = context,
            releaseRepository = releaseRepository,
            managerRepository = managerRepository,
            frontRepository = frontRepository
        )
    }

    @Test
    fun `Check return empty string if observe error`() =
        runTest {
            whenever(
                managerRepository.observe()
            ).thenReturn(
                flow { throw Exception("Observe Error") }
            )
            val result = runCatching { usecase().first() }
            assertEquals(true, result.isFailure)
        }

    @Test
    fun `Check return empty string if idRelease is null`() =
        runTest {
            whenever(
                managerRepository.observe()
            ).thenReturn(
                flowOf(Manager(idRelease = null, idFront = 1))
            )
            val result = usecase().first()
            assertEquals("", result)
        }

    @Test
    fun `Check return empty string if idFront is null`() =
        runTest {
            whenever(
                managerRepository.observe()
            ).thenReturn(
                flowOf(Manager(idRelease = 1, idFront = null))
            )
            val result = usecase().first()
            assertEquals("", result)
        }

    @Test
    fun `Check return empty string if release not found`() =
        runTest {
            whenever(
                managerRepository.observe()
            ).thenReturn(
                flowOf(Manager(idRelease = 1, idFront = 2))
            )
            whenever(
                releaseRepository.getById(1)
            ).thenReturn(
                Result.failure(Exception("Not found"))
            )
            val result = usecase().first()
            assertEquals("", result)
        }

    @Test
    fun `Check return empty string if front not found`() =
        runTest {
            whenever(
                managerRepository.observe()
            ).thenReturn(
                flowOf(Manager(idRelease = 1, idFront = 2))
            )
            whenever(
                releaseRepository.getById(1)
            ).thenReturn(
                Result.success(Release(1, 123, 1, "Prop", 2))
            )
            whenever(
                frontRepository.getById(2)
            ).thenReturn(
                Result.failure(Exception("Not found"))
            )
            val result = usecase().first()
            assertEquals("", result)
        }

    @Test
    fun `Check return correct string if everything is ok`() =
        runTest {
            whenever(
                managerRepository.observe()
            ).thenReturn(
                flowOf(Manager(idRelease = 1, idFront = 2))
            )
            whenever(
                releaseRepository.getById(1)
            ).thenReturn(
                Result.success(Release(1, 123, 1, "Prop", 2))
            )
            whenever(
                frontRepository.getById(2)
            ).thenReturn(
                Result.success(Front(2, 22, "Front22"))
            )
            val result = usecase().first()
            val expected = context.getString(R.string.text_data_menu, "Front22", "1", "123", "Prop")
            assertEquals(expected, result)
        }

}
