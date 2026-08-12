package br.com.usinasantafe.cvf.infra.repositories.variable

import br.com.usinasantafe.cvf.domain.entities.variable.Config
import br.com.usinasantafe.cvf.infra.datasource.retrofit.variable.ConfigRetrofitDatasource
import br.com.usinasantafe.cvf.infra.datasource.sharedpreferences.ConfigSharedPreferencesDatasource
import br.com.usinasantafe.cvf.infra.models.retrofit.variable.ConfigRetrofitModelInput
import br.com.usinasantafe.cvf.infra.models.retrofit.variable.ConfigRetrofitModelOutput
import br.com.usinasantafe.cvf.infra.models.sharedpreferences.ConfigSharedPreferencesModel
import br.com.usinasantafe.cvf.utils.resultFailure
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.atLeastOnce
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

class IConfigRepositoryTest {

    private val configSharedPreferencesDatasource = mock<ConfigSharedPreferencesDatasource>()
    private val configRetrofitDatasource = mock<ConfigRetrofitDatasource>()
    private val repository = IConfigRepository(
        configSharedPreferencesDatasource = configSharedPreferencesDatasource,
        configRetrofitDatasource = configRetrofitDatasource
    )

    @Test
    fun `get - Check return failure if have error in ConfigSharedPreferencesDatasource get`() =
        runTest {
            whenever(
                configSharedPreferencesDatasource.get()
            ).thenReturn(
                resultFailure(
                    "IConfigSharedPreferencesDatasource.get",
                    "-",
                    Exception()
                )
            )
            val result = repository.get()
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IConfigRepository.get -> IConfigSharedPreferencesDatasource.get",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `get - Check return failure if number is null`() =
        runTest {
            whenever(
                configSharedPreferencesDatasource.get()
            ).thenReturn(
                Result.success(
                    ConfigSharedPreferencesModel()
                )
            )
            val result = repository.get()
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IConfigRepository.get -> number is required",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "null",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `get - Check return failure if password is null`() =
        runTest {
            whenever(
                configSharedPreferencesDatasource.get()
            ).thenReturn(
                Result.success(
                    ConfigSharedPreferencesModel(
                        number = 16997417840
                    )
                )
            )
            val result = repository.get()
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IConfigRepository.get -> password is required",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "null",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `get - Check return correct if function execute successfully`() =
        runTest {
            whenever(
                configSharedPreferencesDatasource.get()
            ).thenReturn(
                Result.success(
                    ConfigSharedPreferencesModel(
                        number = 16997417840,
                        password = "12345"
                    )
                )
            )
            val result = repository.get()
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                Config(
                    number = 16997417840,
                    password = "12345"
                ),
                result.getOrNull()!!
            )
        }

    @Test
    fun `send - Check return failure if Config is null`() =
        runTest {
            val result = repository.send(Config())
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IConfigRepository.send -> number is required",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "null",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `send - Check return failure if have error in ConfigRetrofitDatasource recoverToken`() =
        runTest {
            whenever(
                configRetrofitDatasource.recoverToken(
                    ConfigRetrofitModelOutput(
                        number = 16997417840,
                        version = "1.00"
                    )
                )
            ).thenReturn(
                resultFailure(
                    "IConfigRetrofitDatasource.recoverToken",
                    "-",
                    Exception()
                )
            )
            val result = repository.send(
                Config(
                    number = 16997417840,
                    version = "1.00"
                )
            )
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IConfigRepository.send -> IConfigRetrofitDatasource.recoverToken",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `send - Check return correct if function execute successfully`() =
        runTest {
            whenever(
                configRetrofitDatasource.recoverToken(
                    ConfigRetrofitModelOutput(
                        number = 16997417840,
                        version = "1.00"
                    )
                )
            ).thenReturn(
                Result.success(
                    ConfigRetrofitModelInput(
                        idServ = 1
                    )
                )
            )
            val result = repository.send(
                Config(
                    number = 16997417840,
                    version = "1.00"
                )
            )
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                Config(idServ = 1),
                result.getOrNull()!!
            )
        }

    @Test
    fun `save - Check return failure if Config entity have input null`() =
        runTest {
            val result = repository.save(Config())
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IConfigRepository.save -> number is required",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "null",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `save - Check return failure if have error in ConfigSharedPreferencesDatasource save`() =
        runTest {
            whenever(
                configSharedPreferencesDatasource.save(
                    ConfigSharedPreferencesModel(
                        number = 16997417840,
                        password = "12345",
                        version = "1.00",
                        idServ = 1
                    )
                )
            ).thenReturn(
                resultFailure(
                    "IConfigSharedPreferencesDatasource.save",
                    "-",
                    Exception()
                )
            )
            val result = repository.save(
                Config(
                    number = 16997417840,
                    password = "12345",
                    version = "1.00",
                    idServ = 1
                )
            )
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IConfigRepository.save -> IConfigSharedPreferencesDatasource.save",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `save - Check return correct if function execute successfully`() =
        runTest {
            val result = repository.save(
                Config(
                    number = 16997417840,
                    password = "12345",
                    version = "1.00",
                    idServ = 1
                )
            )
            verify(configSharedPreferencesDatasource, atLeastOnce()).save(
                ConfigSharedPreferencesModel(
                    number = 16997417840,
                    password = "12345",
                    version = "1.00",
                    idServ = 1
                )
            )
            assertEquals(
                true,
                result.isSuccess
            )
        }

    @Test
    fun `has - Check return failure if have error in ConfigSharedPreferencesDatasource has`() =
        runTest {
            whenever(
                configSharedPreferencesDatasource.has()
            ).thenReturn(
                resultFailure(
                    "IConfigSharedPreferencesDatasource.has",
                    "-",
                    Exception()
                )
            )
            val result = repository.has()
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IConfigRepository.has -> IConfigSharedPreferencesDatasource.has",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `has - Check return correct if function execute successfully`() =
        runTest {
            whenever(
                configSharedPreferencesDatasource.has()
            ).thenReturn(
                Result.success(false)
            )
            val result = repository.has()
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                false,
                result.getOrNull()!!
            )
        }

    @Test
    fun `setFlagUpdate - Check return failure if have error in ConfigSharedPreferencesDatasource setFlagUpdate`() =
        runTest {
            whenever(
                configSharedPreferencesDatasource.setFlagUpdate()
            ).thenReturn(
                resultFailure(
                    "IConfigSharedPreferencesDatasource.setFlagUpdate",
                    "-",
                    Exception()
                )
            )
            val result = repository.setFlagUpdate()
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IConfigRepository.setFlagUpdate -> IConfigSharedPreferencesDatasource.setFlagUpdate",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `setFlagUpdate - Check return correct if function execute successfully`() =
        runTest {
            val result = repository.setFlagUpdate()
            verify(configSharedPreferencesDatasource, atLeastOnce()).setFlagUpdate()
            assertEquals(
                true,
                result.isSuccess
            )
        }

}