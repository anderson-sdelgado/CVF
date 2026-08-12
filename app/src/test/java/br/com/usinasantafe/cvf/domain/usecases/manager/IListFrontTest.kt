package br.com.usinasantafe.cvf.domain.usecases.manager

import br.com.usinasantafe.cvf.domain.entities.stable.Front
import br.com.usinasantafe.cvf.domain.repositories.stable.FrontRepository
import br.com.usinasantafe.cvf.domain.repositories.variable.ManagerRepository
import br.com.usinasantafe.cvf.presenter.model.ItemCheckBoxScreenModel
import br.com.usinasantafe.cvf.utils.resultFailure
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

class IListFrontTest {

    private val frontRepository = mock<FrontRepository>()
    private val managerRepository = mock<ManagerRepository>()
    private val usecase = IListFront(
        frontRepository = frontRepository,
        managerRepository = managerRepository
    )

    @Test
    fun `Check return failure if have error in FrontRepository listAll`() =
        runTest {
            whenever(
                frontRepository.listAll()
            ).thenReturn(
                resultFailure(
                    "IFrontRepository.listAll",
                    "-",
                    Exception()
                )
            )
            val result = usecase()
            assertEquals(
                result.isFailure,
                true
            )
            assertEquals(
                result.exceptionOrNull()!!.message,
                "IListFront -> IFrontRepository.listAll"
            )
            assertEquals(
                result.exceptionOrNull()!!.cause.toString(),
                "java.lang.Exception"
            )
        }

    @Test
    fun `Check return failure if have error in ManagerRepository getIdFront`() =
        runTest {
            whenever(
                frontRepository.listAll()
            ).thenReturn(
                Result.success(emptyList())
            )
            whenever(
                managerRepository.getIdFront()
            ).thenReturn(
                resultFailure(
                    "IManagerRepository.getIdFront",
                    "-",
                    Exception()
                )
            )
            val result = usecase()
            assertEquals(
                result.isFailure,
                true
            )
            assertEquals(
                result.exceptionOrNull()!!.message,
                "IListFront -> IManagerRepository.getIdFront"
            )
            assertEquals(
                result.exceptionOrNull()!!.cause.toString(),
                "java.lang.Exception"
            )
        }

    @Test
    fun `Check return empty list if function execute successfully and list is empty`() =
        runTest {
            whenever(
                frontRepository.listAll()
            ).thenReturn(
                Result.success(emptyList())
            )
            whenever(
                managerRepository.getIdFront()
            ).thenReturn(
                Result.success(null)
            )
            val result = usecase()
            assertEquals(
                result.isSuccess,
                true
            )
            assertEquals(
                result.getOrNull()!!,
                emptyList()
            )
        }

    @Test
    fun `Check return list without check if function execute successfully and idFront is null`() =
        runTest {
            whenever(
                frontRepository.listAll()
            ).thenReturn(
                Result.success(
                    listOf(
                        Front(
                            id = 1,
                            cd = 1,
                            description = "Front1"
                        )
                    )
                )
            )
            whenever(
                managerRepository.getIdFront()
            ).thenReturn(
                Result.success(null)
            )
            val result = usecase()
            assertEquals(
                result.isSuccess,
                true
            )
            assertEquals(
                result.getOrNull()!!,
                listOf(
                    ItemCheckBoxScreenModel(
                        id = 1,
                        desc = "Front1",
                        flag = false
                    )
                )
            )
        }

    @Test
    fun `Check return list with check if function execute successfully and idFront is not null`() =
        runTest {
            whenever(
                frontRepository.listAll()
            ).thenReturn(
                Result.success(
                    listOf(
                        Front(
                            id = 1,
                            cd = 1,
                            description = "Front1"
                        ),
                        Front(
                            id = 2,
                            cd = 2,
                            description = "Front2"
                        ),
                        Front(
                            id = 3,
                            cd = 3,
                            description = "Front3"
                        )
                    )
                )
            )
            whenever(
                managerRepository.getIdFront()
            ).thenReturn(
                Result.success(2)
            )
            val result = usecase()
            assertEquals(
                result.isSuccess,
                true
            )
            assertEquals(
                result.getOrNull()!!,
                listOf(
                    ItemCheckBoxScreenModel(
                        id = 1,
                        desc = "Front1",
                        flag = false
                    ),
                    ItemCheckBoxScreenModel(
                        id = 2,
                        desc = "Front2",
                        flag = true
                    ),
                    ItemCheckBoxScreenModel(
                        id = 3,
                        desc = "Front3",
                        flag = false
                    )
                )
            )
        }


}