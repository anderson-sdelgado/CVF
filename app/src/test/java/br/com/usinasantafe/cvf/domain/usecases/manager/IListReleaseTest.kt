package br.com.usinasantafe.cvf.domain.usecases.manager

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import br.com.usinasantafe.cvf.domain.entities.stable.Release
import br.com.usinasantafe.cvf.domain.repositories.stable.ReleaseRepository
import br.com.usinasantafe.cvf.domain.repositories.variable.ManagerRepository
import br.com.usinasantafe.cvf.presenter.model.ItemCheckBoxScreenModel
import br.com.usinasantafe.cvf.utils.resultFailure
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
@Config(sdk = [34])
class IListReleaseTest {

    private val releaseRepository = mock<ReleaseRepository>()
    private val managerRepository = mock<ManagerRepository>()
    private lateinit var usecase: IListRelease

    @Before
    fun setup() {
        val context: Context = ApplicationProvider.getApplicationContext()
        usecase = IListRelease(
            context = context,
            releaseRepository = releaseRepository,
            managerRepository = managerRepository
        )
    }

    @Test
    fun `Check return failure if have error in ReleaseRepository listByIdFront`() =
        runTest {
            whenever(
                releaseRepository.listByIdFront(1)
            ).thenReturn(
                resultFailure(
                    "IReleaseRepository.listByIdFront",
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
                "IListRelease -> IReleaseRepository.listByIdFront",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `Check return failure if have error in ManagerRepository getIdRelease`() =
        runTest {
            whenever(
                releaseRepository.listByIdFront(1)
            ).thenReturn(
                Result.success(emptyList())
            )
            whenever(
                managerRepository.getIdRelease()
            ).thenReturn(
                resultFailure(
                    "IManagerRepository.getIdRelease",
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
                "IListRelease -> IManagerRepository.getIdRelease",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `Check return empty list if function execute successfully and list is empty`() =
        runTest {
            whenever(
                releaseRepository.listByIdFront(1)
            ).thenReturn(
                Result.success(emptyList())
            )
            whenever(
                managerRepository.getIdRelease()
            ).thenReturn(
                Result.success(null)
            )
            val result = usecase(1)
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                emptyList(),
                result.getOrNull()!!
            )
        }

    @Test
    fun `Check return list without check if function execute successfully and idFront is null`() =
        runTest {
            whenever(
                releaseRepository.listByIdFront(1)
            ).thenReturn(
                Result.success(
                    listOf(
                        Release(
                            id = 1,
                            nroOS = 1,
                            idPropAgr = 1,
                            descPropAgr = "Test1",
                            idFront = 1
                        ),
                        Release(
                            id = 2,
                            nroOS = 2,
                            idPropAgr = 2,
                            descPropAgr = "Test2",
                            idFront = 2
                        ),
                        Release(
                            id = 3,
                            nroOS = 3,
                            idPropAgr = 3,
                            descPropAgr = "Test3",
                            idFront = 3
                        )
                    )
                )
            )
            whenever(
                managerRepository.getIdRelease()
            ).thenReturn(
                Result.success(null)
            )
            val result = usecase(1)
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                listOf(
                    ItemCheckBoxScreenModel(
                        id = 1,
                        desc = "LIBERAÇÃO: 1\n O.S.: 1\n PROPRIEDADE: Test1",
                        flag = false
                    ),
                    ItemCheckBoxScreenModel(
                        id = 2,
                        desc = "LIBERAÇÃO: 2\n O.S.: 2\n PROPRIEDADE: Test2",
                        flag = false
                    ),
                    ItemCheckBoxScreenModel(
                        id = 3,
                        desc = "LIBERAÇÃO: 3\n O.S.: 3\n PROPRIEDADE: Test3",
                        flag = false
                    )
                ),
                result.getOrNull()!!
            )
        }

    @Test
    fun `Check return list with check if function execute successfully and idFront is not null`() =
        runTest {
            whenever(
                releaseRepository.listByIdFront(1)
            ).thenReturn(
                Result.success(
                    listOf(
                        Release(
                            id = 1,
                            nroOS = 1,
                            idPropAgr = 1,
                            descPropAgr = "Test1",
                            idFront = 1
                        ),
                        Release(
                            id = 2,
                            nroOS = 2,
                            idPropAgr = 2,
                            descPropAgr = "Test2",
                            idFront = 2
                        ),
                        Release(
                            id = 3,
                            nroOS = 3,
                            idPropAgr = 3,
                            descPropAgr = "Test3",
                            idFront = 3
                        )
                    )
                )
            )
            whenever(
                managerRepository.getIdRelease()
            ).thenReturn(
                Result.success(2)
            )
            val result = usecase(1)
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                listOf(
                    ItemCheckBoxScreenModel(
                        id = 1,
                        desc = "LIBERAÇÃO: 1\n O.S.: 1\n PROPRIEDADE: Test1",
                        flag = false
                    ),
                    ItemCheckBoxScreenModel(
                        id = 2,
                        desc = "LIBERAÇÃO: 2\n O.S.: 2\n PROPRIEDADE: Test2",
                        flag = true
                    ),
                    ItemCheckBoxScreenModel(
                        id = 3,
                        desc = "LIBERAÇÃO: 3\n O.S.: 3\n PROPRIEDADE: Test3",
                        flag = false
                    )
                ),
                result.getOrNull()!!
            )
        }

}