package br.com.usinasantafe.cvf.infra.repositories.variable

import br.com.usinasantafe.cvf.infra.datasource.sharedpreferences.ManagerSharedPreferencesDatasource
import br.com.usinasantafe.cvf.utils.resultFailure
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.atLeastOnce
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

class IManagerRepositoryTest {
    
    private val managerSharedPreferencesDatasource = mock<ManagerSharedPreferencesDatasource>()
    private val repository = IManagerRepository(
        managerSharedPreferencesDatasource = managerSharedPreferencesDatasource
    )

    @Test
    fun `clean - Check return failure if have error in ManagerSharedPreferencesDatasource clean`() =
        runTest {
            whenever(
                managerSharedPreferencesDatasource.clean()
            ).thenReturn(
                resultFailure(
                    "IManagerSharedPreferencesDatasource.clean",
                    "-",
                    Exception()
                )
            )
            val result = repository.clean()
            assertEquals(
                result.isFailure,
                true
            )
            assertEquals(
                result.exceptionOrNull()!!.message,
                "IManagerRepository.clean -> IManagerSharedPreferencesDatasource.clean"
            )
            assertEquals(
                result.exceptionOrNull()!!.cause.toString(),
                "java.lang.Exception"
            )
        }

    @Test
    fun `clean - Check return correct if function execute successfully`() =
        runTest {
            val result = repository.clean()
            verify(managerSharedPreferencesDatasource, atLeastOnce()).clean()
            assertEquals(
                result.isSuccess,
                true
            )
        }

    @Test
    fun `has - Check return failure if have error in ManagerSharedPreferencesDatasource has`() =
        runTest {
            whenever(
                managerSharedPreferencesDatasource.has()
            ).thenReturn(
                resultFailure(
                    "IManagerSharedPreferencesDatasource.has",
                    "-",
                    Exception()
                )
            )
            val result = repository.has()
            assertEquals(
                result.isFailure,
                true
            )
            assertEquals(
                result.exceptionOrNull()!!.message,
                "IManagerRepository.has -> IManagerSharedPreferencesDatasource.has"
            )
            assertEquals(
                result.exceptionOrNull()!!.cause.toString(),
                "java.lang.Exception"
            )
        }

    @Test
    fun `has - Check return correct if function execute successfully`() =
        runTest {
            whenever(
                managerSharedPreferencesDatasource.has()
            ).thenReturn(
                Result.success(false)
            )
            val result = repository.has()
            assertEquals(
                result.isSuccess,
                true
            )
            assertEquals(
                result.getOrNull()!!,
                false
            )
        }

}