package br.com.usinasantafe.cvf.domain.usecases.update

import br.com.usinasantafe.cvf.domain.entities.stable.Release
import br.com.usinasantafe.cvf.domain.repositories.stable.ReleaseRepository
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

class IUpdateTableReleaseTest {

    private val token = mock<Token>()
    private val releaseRepository = mock<ReleaseRepository>()
    private val usecase = IUpdateTableRelease(
        token = token,
        releaseRepository = releaseRepository
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
                    tableUpdate = "tb_release",
                    currentProgress = updatePercentage(1f, 1f, 7f)
                ),
                list[0]
            )
            assertEquals(
                UiStatusStateUpdate(
                    errors = Errors.UPDATE,
                    flagDialog = true,
                    flagFailure = true,
                    failure = "IUpdateTableRelease -> GetToken -> java.lang.Exception",
                    currentProgress = 1f,
                ),
                list[1]
            )
        }

    @Test
    fun `Check return failure if have error in ReleaseRepository recoverAll`() =
        runTest {
            whenever(
                token()
            ).thenReturn(
                Result.success("token")
            )
            whenever(
                releaseRepository.listAll("token")
            ).thenReturn(
                resultFailure(
                    "IReleaseRepository.recoverAll",
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
                    tableUpdate = "tb_release",
                    currentProgress = updatePercentage(1f, 1f, 7f)
                ),
                list[0]
            )
            assertEquals(
                UiStatusStateUpdate(
                    errors = Errors.UPDATE,
                    flagDialog = true,
                    flagFailure = true,
                    failure = "IUpdateTableRelease -> IReleaseRepository.recoverAll -> java.lang.Exception",
                    currentProgress = 1f,
                ),
                list[1]
            )
        }

    @Test
    fun `Check return failure if have error in ReleaseRepository deleteAll`() =
        runTest {
            val list = listOf(
                Release(
                    id = 1,
                    nroOS = 2,
                    idPropAgr = 1,
                    descPropAgr = "Test",
                    idFront = 1
                )
            )
            whenever(
                token()
            ).thenReturn(
                Result.success("token")
            )
            whenever(
                releaseRepository.listAll("token")
            ).thenReturn(
                Result.success(
                    list
                )
            )
            whenever(
                releaseRepository.deleteAll()
            ).thenReturn(
                resultFailure(
                    "IReleaseRepository.deleteAll",
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
                    tableUpdate = "tb_release",
                    currentProgress = updatePercentage(1f, 1f, 7f)
                ),
                resultList[0]
            )
            assertEquals(
                UiStatusStateUpdate(
                    flagProgress = true,
                    levelUpdate = LevelUpdate.CLEAN,
                    tableUpdate = "tb_release",
                    currentProgress = updatePercentage(2f, 1f, 7f)
                ),
                resultList[1]
            )
            assertEquals(
                UiStatusStateUpdate(
                    errors = Errors.UPDATE,
                    flagDialog = true,
                    flagFailure = true,
                    failure = "IUpdateTableRelease -> IReleaseRepository.deleteAll -> java.lang.Exception",
                    currentProgress = 1f,
                ),
                resultList[2]
            )
        }

    @Test
    fun `Check return failure if have error in ReleaseRepository addAll`() =
        runTest {
            val list = listOf(
                Release(
                    id = 1,
                    nroOS = 2,
                    idPropAgr = 1,
                    descPropAgr = "Test",
                    idFront = 1
                )
            )
            whenever(
                token()
            ).thenReturn(
                Result.success("token")
            )
            whenever(
                releaseRepository.listAll("token")
            ).thenReturn(
                Result.success(
                    list
                )
            )
            whenever(
                releaseRepository.addAll(list)
            ).thenReturn(
                resultFailure(
                    "IReleaseRepository.addAll",
                    "-",
                    Exception()
                )
            )
            val result = usecase(
                sizeAll = 7f,
                count = 1f
            )
            val resultList = result.toList()
            verify(releaseRepository, atLeastOnce()).deleteAll()
            assertEquals(
                4,
                result.count()
            )
            assertEquals(
                UiStatusStateUpdate(
                    flagProgress = true,
                    levelUpdate = LevelUpdate.RECOVERY,
                    tableUpdate = "tb_release",
                    currentProgress = updatePercentage(1f, 1f, 7f)
                ),
                resultList[0]
            )
            assertEquals(
                UiStatusStateUpdate(
                    flagProgress = true,
                    levelUpdate = LevelUpdate.CLEAN,
                    tableUpdate = "tb_release",
                    currentProgress = updatePercentage(2f, 1f, 7f)
                ),
                resultList[1]
            )
            assertEquals(
                UiStatusStateUpdate(
                    flagProgress = true,
                    levelUpdate = LevelUpdate.SAVE,
                    tableUpdate = "tb_release",
                    currentProgress = updatePercentage(3f, 1f, 7f)
                ),
                resultList[2]
            )
            assertEquals(
                UiStatusStateUpdate(
                    errors = Errors.UPDATE,
                    flagDialog = true,
                    flagFailure = true,
                    failure = "IUpdateTableRelease -> IReleaseRepository.addAll -> java.lang.Exception",
                    currentProgress = 1f,
                ),
                resultList[3]
            )
        }

    @Test
    fun `Check return correct if function execute successfully`() =
        runTest {
            val list = listOf(
                Release(
                    id = 1,
                    nroOS = 2,
                    idPropAgr = 1,
                    descPropAgr = "Test",
                    idFront = 1
                )
            )
            whenever(
                token()
            ).thenReturn(
                Result.success("token")
            )
            whenever(
                releaseRepository.listAll("token")
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
            verify(releaseRepository, atLeastOnce()).deleteAll()
            verify(releaseRepository, atLeastOnce()).addAll(list)
            assertEquals(
                3,
                result.count()
            )
            assertEquals(
                UiStatusStateUpdate(
                    flagProgress = true,
                    levelUpdate = LevelUpdate.RECOVERY,
                    tableUpdate = "tb_release",
                    currentProgress = updatePercentage(1f, 1f, 7f)
                ),
                resultList[0]
            )
            assertEquals(
                UiStatusStateUpdate(
                    flagProgress = true,
                    levelUpdate = LevelUpdate.CLEAN,
                    tableUpdate = "tb_release",
                    currentProgress = updatePercentage(2f, 1f, 7f)
                ),
                resultList[1]
            )
            assertEquals(
                UiStatusStateUpdate(
                    flagProgress = true,
                    levelUpdate = LevelUpdate.SAVE,
                    tableUpdate = "tb_release",
                    currentProgress = updatePercentage(3f, 1f, 7f)
                ),
                resultList[2]
            )
        }
}