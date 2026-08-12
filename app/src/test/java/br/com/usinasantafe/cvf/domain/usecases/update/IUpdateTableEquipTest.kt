package br.com.usinasantafe.cvf.domain.usecases.update

import br.com.usinasantafe.cvf.domain.entities.stable.Equip
import br.com.usinasantafe.cvf.domain.repositories.stable.EquipRepository
import br.com.usinasantafe.cvf.domain.usecases.common.GetToken
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

class IUpdateTableEquipTest {

    private val getToken = mock<GetToken>()
    private val equipRepository = mock<EquipRepository>()
    private val usecase = IUpdateTableEquip(
        getToken = getToken,
        equipRepository = equipRepository
    )

    @Test
    fun `Check return failure if have error in GetToken`() =
        runTest {
            whenever(
                getToken()
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
                    tableUpdate = "tb_equip",
                    currentProgress = updatePercentage(1f, 1f, 7f)
                ),
                list[0]
            )
            assertEquals(
                UiStatusStateUpdate(
                    errors = Errors.UPDATE,
                    flagDialog = true,
                    flagFailure = true,
                    failure = "IUpdateTableEquip -> GetToken -> java.lang.Exception",
                    currentProgress = 1f,
                ),
                list[1]
            )
        }

    @Test
    fun `Check return failure if have error in EquipRepository recoverAll`() =
        runTest {
            whenever(
                getToken()
            ).thenReturn(
                Result.success("token")
            )
            whenever(
                equipRepository.listAll("token")
            ).thenReturn(
                resultFailure(
                    "IEquipRepository.recoverAll",
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
                result.count(),
                2
            )
            assertEquals(
                UiStatusStateUpdate(
                    flagProgress = true,
                    levelUpdate = LevelUpdate.RECOVERY,
                    tableUpdate = "tb_equip",
                    currentProgress = updatePercentage(1f, 1f, 7f)
                ),
                list[0]
            )
            assertEquals(
                UiStatusStateUpdate(
                    errors = Errors.UPDATE,
                    flagDialog = true,
                    flagFailure = true,
                    failure = "IUpdateTableEquip -> IEquipRepository.recoverAll -> java.lang.Exception",
                    currentProgress = 1f,
                ),
                list[1]
            )
        }

    @Test
    fun `Check return failure if have error in EquipRepository deleteAll`() =
        runTest {
            val list = listOf(
                Equip(
                    id = 1,
                    nro = 1,
                    cdOperClass = 1,
                    description = "Test"
                )
            )
            whenever(
                getToken()
            ).thenReturn(
                Result.success("token")
            )
            whenever(
                equipRepository.listAll("token")
            ).thenReturn(
                Result.success(
                    list
                )
            )
            whenever(
                equipRepository.deleteAll()
            ).thenReturn(
                resultFailure(
                    "IEquipRepository.deleteAll",
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
                    tableUpdate = "tb_equip",
                    currentProgress = updatePercentage(1f, 1f, 7f)
                ),
                resultList[0]
            )
            assertEquals(
                UiStatusStateUpdate(
                    flagProgress = true,
                    levelUpdate = LevelUpdate.CLEAN,
                    tableUpdate = "tb_equip",
                    currentProgress = updatePercentage(2f, 1f, 7f)
                ),
                resultList[1]
            )
            assertEquals(
                UiStatusStateUpdate(
                    errors = Errors.UPDATE,
                    flagDialog = true,
                    flagFailure = true,
                    failure = "IUpdateTableEquip -> IEquipRepository.deleteAll -> java.lang.Exception",
                    currentProgress = 1f,
                ),
                resultList[2]
            )
        }

    @Test
    fun `Check return failure if have error in ColabRepository addAll`() =
        runTest {
            val list = listOf(
                Equip(
                    id = 1,
                    nro = 1,
                    cdOperClass = 1,
                    description = "Test"
                )
            )
            whenever(
                getToken()
            ).thenReturn(
                Result.success("token")
            )
            whenever(
                equipRepository.listAll("token")
            ).thenReturn(
                Result.success(
                    list
                )
            )
            whenever(
                equipRepository.addAll(list)
            ).thenReturn(
                resultFailure(
                    "IEquipRepository.addAll",
                    "-",
                    Exception()
                )
            )
            val result = usecase(
                sizeAll = 7f,
                count = 1f
            )
            val resultList = result.toList()
            verify(equipRepository, atLeastOnce()).deleteAll()
            assertEquals(
                4,
                result.count()
            )
            assertEquals(
                UiStatusStateUpdate(
                    flagProgress = true,
                    levelUpdate = LevelUpdate.RECOVERY,
                    tableUpdate = "tb_equip",
                    currentProgress = updatePercentage(1f, 1f, 7f)
                ),
                resultList[0]
            )
            assertEquals(
                UiStatusStateUpdate(
                    flagProgress = true,
                    levelUpdate = LevelUpdate.CLEAN,
                    tableUpdate = "tb_equip",
                    currentProgress = updatePercentage(2f, 1f, 7f)
                ),
                resultList[1]
            )
            assertEquals(
                UiStatusStateUpdate(
                    flagProgress = true,
                    levelUpdate = LevelUpdate.SAVE,
                    tableUpdate = "tb_equip",
                    currentProgress = updatePercentage(3f, 1f, 7f)
                ),
                resultList[2]
            )
            assertEquals(
                UiStatusStateUpdate(
                    errors = Errors.UPDATE,
                    flagDialog = true,
                    flagFailure = true,
                    failure = "IUpdateTableEquip -> IEquipRepository.addAll -> java.lang.Exception",
                    currentProgress = 1f,
                ),
                resultList[3]
            )
        }

    @Test
    fun `Check return correct if function execute successfully`() =
        runTest {

            val list = listOf(
                Equip(
                    id = 1,
                    nro = 1,
                    cdOperClass = 1,
                    description = "Test"
                )
            )
            whenever(
                getToken()
            ).thenReturn(
                Result.success("token")
            )
            whenever(
                equipRepository.listAll("token")
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
            verify(equipRepository, atLeastOnce()).deleteAll()
            verify(equipRepository, atLeastOnce()).addAll(list)
            assertEquals(
                3,
                result.count()
            )
            assertEquals(
                UiStatusStateUpdate(
                    flagProgress = true,
                    levelUpdate = LevelUpdate.RECOVERY,
                    tableUpdate = "tb_equip",
                    currentProgress = updatePercentage(1f, 1f, 7f)
                ),
                resultList[0]
            )
            assertEquals(
                UiStatusStateUpdate(
                    flagProgress = true,
                    levelUpdate = LevelUpdate.CLEAN,
                    tableUpdate = "tb_equip",
                    currentProgress = updatePercentage(2f, 1f, 7f)
                ),
                resultList[1]
            )
            assertEquals(
                UiStatusStateUpdate(
                    flagProgress = true,
                    levelUpdate = LevelUpdate.SAVE,
                    tableUpdate = "tb_equip",
                    currentProgress = updatePercentage(3f, 1f, 7f)
                ),
                resultList[2]
            )
        }
}