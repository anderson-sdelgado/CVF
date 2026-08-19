package br.com.usinasantafe.cvf.domain.usecases.update

import br.com.usinasantafe.cvf.domain.entities.stable.Colab
import br.com.usinasantafe.cvf.domain.repositories.stable.ColabRepository
import br.com.usinasantafe.cvf.domain.usecases.common.Token
import br.com.usinasantafe.cvf.lib.Errors
import br.com.usinasantafe.cvf.lib.LevelUpdate
import br.com.usinasantafe.cvf.utils.UiStatusStateUpdate
import br.com.usinasantafe.cvf.utils.resultFailure
import br.com.usinasantafe.cvf.utils.updatePercentage
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.atLeastOnce
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

class IUpdateTableColabTest {

    private val token = mock<Token>()
    private val colabRepository = mock<ColabRepository>()
    private val usecase = IUpdateTableColab(
        token = token,
        colabRepository = colabRepository
    )

    @Test
    fun `Check return failure if have error in GetToken`() =
        runTest {
            whenever(
                token()
            ).thenReturn(
                resultFailure(
                    "GetToken",
                    "-",
                    Exception()
                )
            )
            val result = usecase(
                sizeAll = 7f,
                count = 1f
            )
            val list = result.toList()
            assertEquals(
                2,
                result.count()
            )
            assertEquals(
                UiStatusStateUpdate(
                    flagProgress = true,
                    levelUpdate = LevelUpdate.RECOVERY,
                    tableUpdate = "tb_colab",
                    currentProgress = updatePercentage(1f, 1f, 7f)
                ),
                list[0]
            )
            assertEquals(
                UiStatusStateUpdate(
                    errors = Errors.UPDATE,
                    flagDialog = true,
                    flagFailure = true,
                    failure = "IUpdateTableColab -> GetToken -> java.lang.Exception",
                    currentProgress = 1f,
                ),
                list[1]
            )
        }

    @Test
    fun `Check return failure if have error in ColabRepository recoverAll`() =
        runTest {
            whenever(
                token()
            ).thenReturn(
                Result.success("token")
            )
            whenever(
                colabRepository.listAll("token")
            ).thenReturn(
                resultFailure(
                    "IColabRepository.recoverAll",
                    "-",
                    Exception()
                )
            )
            val result = usecase(
                sizeAll = 7f,
                count = 1f
            )
            val list = result.toList()
            assertEquals(
                2,
                result.count()
            )
            assertEquals(
                UiStatusStateUpdate(
                    flagProgress = true,
                    levelUpdate = LevelUpdate.RECOVERY,
                    tableUpdate = "tb_colab",
                    currentProgress = updatePercentage(1f, 1f, 7f)
                ),
                list[0]
            )
            assertEquals(
                UiStatusStateUpdate(
                    errors = Errors.UPDATE,
                    flagDialog = true,
                    flagFailure = true,
                    failure = "IUpdateTableColab -> IColabRepository.recoverAll -> java.lang.Exception",
                    currentProgress = 1f,
                ),
                list[1]
            )
        }

    @Test
    fun `Check return failure if have error in ColabRepository deleteAll`() =
        runTest {
            val list = listOf(
                Colab(
                    reg = 19759,
                    name = "ANDERSON"
                )
            )
            whenever(
                token()
            ).thenReturn(
                Result.success("token")
            )
            whenever(
                colabRepository.listAll("token")
            ).thenReturn(
                Result.success(
                    list
                )
            )
            whenever(
                colabRepository.deleteAll()
            ).thenReturn(
                resultFailure(
                    "IColabRepository.deleteAll",
                    "-",
                    Exception()
                )
            )
            val result = usecase(
                sizeAll = 7f,
                count = 1f
            )
            val resultList = result.toList()
            assertEquals(
                3,
                result.count()
            )
            assertEquals(
                UiStatusStateUpdate(
                    flagProgress = true,
                    levelUpdate = LevelUpdate.RECOVERY,
                    tableUpdate = "tb_colab",
                    currentProgress = updatePercentage(1f, 1f, 7f)
                ),
                resultList[0]
            )
            assertEquals(
                UiStatusStateUpdate(
                    flagProgress = true,
                    levelUpdate = LevelUpdate.CLEAN,
                    tableUpdate = "tb_colab",
                    currentProgress = updatePercentage(2f, 1f, 7f)
                ),
                resultList[1]
            )
            assertEquals(
                UiStatusStateUpdate(
                    errors = Errors.UPDATE,
                    flagDialog = true,
                    flagFailure = true,
                    failure = "IUpdateTableColab -> IColabRepository.deleteAll -> java.lang.Exception",
                    currentProgress = 1f,
                ),
                resultList[2]
            )
        }

    @Test
    fun `Check return failure if have error in ColabRepository addAll`() =
        runTest {
            val list = listOf(
                Colab(
                    reg = 19759,
                    name = "ANDERSON"
                )
            )
            whenever(
                token()
            ).thenReturn(
                Result.success("token")
            )
            whenever(
                colabRepository.listAll("token")
            ).thenReturn(
                Result.success(
                    list
                )
            )
            whenever(
                colabRepository.addAll(list)
            ).thenReturn(
                resultFailure(
                    "IColabRepository.addAll",
                    "-",
                    Exception()
                )
            )
            val result = usecase(
                sizeAll = 7f,
                count = 1f
            )
            val resultList = result.toList()
            verify(colabRepository, atLeastOnce()).deleteAll()
            assertEquals(
                4,
                result.count()
            )
            assertEquals(
                UiStatusStateUpdate(
                    flagProgress = true,
                    levelUpdate = LevelUpdate.RECOVERY,
                    tableUpdate = "tb_colab",
                    currentProgress = updatePercentage(1f, 1f, 7f)
                ),
                resultList[0]
            )
            assertEquals(
                UiStatusStateUpdate(
                    flagProgress = true,
                    levelUpdate = LevelUpdate.CLEAN,
                    tableUpdate = "tb_colab",
                    currentProgress = updatePercentage(2f, 1f, 7f)
                ),
                resultList[1]
            )
            assertEquals(
                UiStatusStateUpdate(
                    flagProgress = true,
                    levelUpdate = LevelUpdate.SAVE,
                    tableUpdate = "tb_colab",
                    currentProgress = updatePercentage(3f, 1f, 7f)
                ),
                resultList[2]
            )
            assertEquals(
                UiStatusStateUpdate(
                    errors = Errors.UPDATE,
                    flagDialog = true,
                    flagFailure = true,
                    failure = "IUpdateTableColab -> IColabRepository.addAll -> java.lang.Exception",
                    currentProgress = 1f,
                ),
                resultList[3]
            )
        }

    @Test
    fun `Check return correct if function execute successfully`() =
        runTest {
            val list = listOf(
                Colab(
                    reg = 19759,
                    name = "ANDERSON"
                )
            )
            whenever(
                token()
            ).thenReturn(
                Result.success("token")
            )
            whenever(
                colabRepository.listAll("token")
            ).thenReturn(
                Result.success(
                    list
                )
            )
            val result = usecase(
                sizeAll = 7f,
                count = 1f
            )
            val resultList = result.toList()
            verify(colabRepository, atLeastOnce()).deleteAll()
            verify(colabRepository, atLeastOnce()).addAll(list)
            assertEquals(
                3,
                result.count()
            )
            assertEquals(
                UiStatusStateUpdate(
                    flagProgress = true,
                    levelUpdate = LevelUpdate.RECOVERY,
                    tableUpdate = "tb_colab",
                    currentProgress = updatePercentage(1f, 1f, 7f)
                ),
                resultList[0]
            )
            assertEquals(
                UiStatusStateUpdate(
                    flagProgress = true,
                    levelUpdate = LevelUpdate.CLEAN,
                    tableUpdate = "tb_colab",
                    currentProgress = updatePercentage(2f, 1f, 7f)
                ),
                resultList[1]
            )
            assertEquals(
                UiStatusStateUpdate(
                    flagProgress = true,
                    levelUpdate = LevelUpdate.SAVE,
                    tableUpdate = "tb_colab",
                    currentProgress = updatePercentage(3f, 1f, 7f)
                ),
                resultList[2]
            )
        }

}