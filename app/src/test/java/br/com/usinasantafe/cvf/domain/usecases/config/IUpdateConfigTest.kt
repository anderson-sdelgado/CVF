package br.com.usinasantafe.cvf.domain.usecases.config

import br.com.usinasantafe.cvf.domain.entities.variable.Config
import br.com.usinasantafe.cvf.domain.repositories.variable.ConfigRepository
import br.com.usinasantafe.cvf.domain.repositories.variable.ManagerRepository
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

class IUpdateConfigTest {

    private val configRepository = mock<ConfigRepository>()
    private val managerRepository = mock<ManagerRepository>()
    private val usecase = IUpdateConfig(
        configRepository = configRepository,
        managerRepository = managerRepository
    )

    @Test
    fun `Check return failure if value number is incorrect`() =
        runTest {
            val result = usecase("dfjslçahf", "123456", "1.00", 3f)
            val list = result.toList()
            assertEquals(
                2,
                result.count()
            )
            assertEquals(
                UiStatusStateUpdate(
                    flagProgress = true,
                    levelUpdate = LevelUpdate.GET_TOKEN,
                    currentProgress = updatePercentage(1f, 1f, 3f)
                ),
                list[0]
            )
            assertEquals(
                UiStatusStateUpdate(
                    errors = Errors.TOKEN,
                    flagDialog = true,
                    flagFailure = true,
                    failure = "IUpdateConfig -> toLong -> java.lang.NumberFormatException: For input string: \"dfjslçahf\"",
                    currentProgress = 1f,
                ),
                list[1]
            )
        }

    @Test
    fun `Check return failure if have error in ConfigRepository send`() =
        runTest {
            whenever(
                configRepository.send(
                    Config(
                        number = 16997417840,
                        password = "123456",
                        version = "1.00"
                    )
                )
            ).thenReturn(
                resultFailure(
                    "IConfigRepository.send",
                    "-",
                    Exception()
                )
            )
            val result = usecase(
                "16997417840",
                "123456",
                "1.00",
                3f
            )
            val list = result.toList()
            assertEquals(
                2,
                result.count()
            )
            assertEquals(
                UiStatusStateUpdate(
                    flagProgress = true,
                    levelUpdate = LevelUpdate.GET_TOKEN,
                    currentProgress = updatePercentage(1f, 1f, 3f)
                ),
                list[0]
            )
            assertEquals(
                UiStatusStateUpdate(
                    errors = Errors.TOKEN,
                    flagDialog = true,
                    flagFailure = true,
                    failure = "IUpdateConfig -> IConfigRepository.send -> java.lang.Exception",
                    currentProgress = 1f,
                ),
                list[1]
            )
        }

    @Test
    fun `Check return failure if have error in ConfigRepository save`() =
        runTest {
            whenever(
                configRepository.send(
                    Config(
                        number = 16997417840,
                        password = "123456",
                        version = "1.00"
                    )
                )
            ).thenReturn(
                Result.success(
                    Config(
                        number = 16997417840,
                        password = "123456",
                        version = "1.00",
                        idServ = 1
                    )
                )
            )
            whenever(
                configRepository.save(
                    Config(
                        number = 16997417840,
                        password = "123456",
                        version = "1.00",
                        idServ = 1
                    )
                )
            ).thenReturn(
                resultFailure(
                    "IConfigRepository.save",
                    "-",
                    Exception()
                )
            )
            val result = usecase(
                "16997417840",
                "123456",
                "1.00",
                3f
            )
            val list = result.toList()
            assertEquals(
                3,
                result.count()
            )
            assertEquals(
                UiStatusStateUpdate(
                    flagProgress = true,
                    levelUpdate = LevelUpdate.GET_TOKEN,
                    currentProgress = updatePercentage(1f, 1f, 3f)
                ),
                list[0]
            )
            assertEquals(
                UiStatusStateUpdate(
                    flagProgress = true,
                    levelUpdate = LevelUpdate.SAVE_TOKEN,
                    currentProgress = updatePercentage(2f, 1f, 3f)
                ),
                list[1]
            )
            assertEquals(
                UiStatusStateUpdate(
                    errors = Errors.TOKEN,
                    flagDialog = true,
                    flagFailure = true,
                    failure = "IUpdateConfig -> IConfigRepository.save -> java.lang.Exception",
                    currentProgress = 1f,
                ),
                list[2]
            )
        }

    @Test
    fun `Check return failure if have error in Manager clean`() =
        runTest {
            val entity = Config(
                number = 16997417840,
                password = "123456",
                version = "1.00"
            )
            whenever(
                configRepository.send(entity)
            ).thenReturn(
                Result.success(
                    Config(
                        number = 16997417840,
                        password = "123456",
                        version = "1.00",
                        idServ = 1
                    )
                )
            )
            whenever(
                managerRepository.clean()
            ).thenReturn(
                resultFailure(
                    "IManagerRepository.clean",
                    "-",
                    Exception()
                )
            )
            val result = usecase(
                "16997417840",
                "123456",
                "1.00",
                3f
            )
            val list = result.toList()
            assertEquals(
                3,
                result.count()
            )
            assertEquals(
                UiStatusStateUpdate(
                    flagProgress = true,
                    levelUpdate = LevelUpdate.GET_TOKEN,
                    currentProgress = updatePercentage(1f, 1f, 3f)
                ),
                list[0]
            )
            assertEquals(
                UiStatusStateUpdate(
                    flagProgress = true,
                    levelUpdate = LevelUpdate.SAVE_TOKEN,
                    currentProgress = updatePercentage(2f, 1f, 3f)
                ),
                list[1]
            )
            assertEquals(
                UiStatusStateUpdate(
                    errors = Errors.TOKEN,
                    flagDialog = true,
                    flagFailure = true,
                    failure = "IUpdateConfig -> IManagerRepository.clean -> java.lang.Exception",
                    currentProgress = 1f,
                ),
                list[2]
            )
        }

    @Test
    fun `Check return correct if function execute successfully`() =
        runTest {
            val entity = Config(
                number = 16997417840,
                password = "123456",
                version = "1.00"
            )
            whenever(
                configRepository.send(entity)
            ).thenReturn(
                Result.success(
                    Config(
                        number = 16997417840,
                        password = "123456",
                        version = "1.00",
                        idServ = 1
                    )
                )
            )
            val result = usecase(
                "16997417840",
                "123456",
                "1.00",
                3f
            )
            val list = result.toList()
            assertEquals(
                3,
                result.count()
            )
            assertEquals(
                UiStatusStateUpdate(
                    flagProgress = true,
                    levelUpdate = LevelUpdate.GET_TOKEN,
                    currentProgress = updatePercentage(1f, 1f, 3f)
                ),
                list[0]
            )
            assertEquals(
                UiStatusStateUpdate(
                    flagProgress = true,
                    levelUpdate = LevelUpdate.SAVE_TOKEN,
                    currentProgress = updatePercentage(2f, 1f, 3f)
                ),
                list[1]
            )
            assertEquals(
                UiStatusStateUpdate(
                    flagProgress = true,
                    levelUpdate = LevelUpdate.FINISH_UPDATE_INITIAL,
                    currentProgress = updatePercentage(3f, 1f, 3f)
                ),
                list[2]
            )
        }
}