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
                true,
                result.isFailure
            )
            assertEquals(
                "IManagerRepository.clean -> IManagerSharedPreferencesDatasource.clean",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `clean - Check return correct if function execute successfully`() =
        runTest {
            val result = repository.clean()
            verify(managerSharedPreferencesDatasource, atLeastOnce()).clean()
            assertEquals(
                true,
                result.isSuccess
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
                true,
                result.isFailure
            )
            assertEquals(
                "IManagerRepository.has -> IManagerSharedPreferencesDatasource.has",
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
                managerSharedPreferencesDatasource.has()
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
    fun `getIdFront - Check return failure if have error in ManagerSharedPreferencesDatasource getIdFront`() =
        runTest {
            whenever(
                managerSharedPreferencesDatasource.getIdFront()
            ).thenReturn(
                resultFailure(
                    "IManagerSharedPreferencesDatasource.getIdFront",
                    "-",
                    Exception()
                )
            )
            val result = repository.getIdFront()
            assertEquals(
                result.isFailure,
                true
            )
            assertEquals(
                result.exceptionOrNull()!!.message,
                "IManagerRepository.getIdFront -> IManagerSharedPreferencesDatasource.getIdFront"
            )
            assertEquals(
                result.exceptionOrNull()!!.cause.toString(),
                "java.lang.Exception"
            )
        }

    @Test
    fun `getIdFront - Check return null if function execute successfully and db is empty`() =
        runTest {
            whenever(
                managerSharedPreferencesDatasource.getIdFront()
            ).thenReturn(
                Result.success(null)
            )
            val result = repository.getIdFront()
            assertEquals(
                result.isSuccess,
                true
            )
            assertEquals(
                result.getOrNull(),
                null
            )
        }

    @Test
    fun `getIdFront - Check return correct if function execute successfully`() =
        runTest {
            whenever(
                managerSharedPreferencesDatasource.getIdFront()
            ).thenReturn(
                Result.success(1)
            )
            val result = repository.getIdFront()
            assertEquals(
                result.isSuccess,
                true
            )
            assertEquals(
                result.getOrNull()!!,
                1
            )
        }

}