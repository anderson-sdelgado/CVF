package br.com.usinasantafe.cvf.infra.repositories.variable

import br.com.usinasantafe.cvf.infra.datasource.sharedpreferences.HeaderSharedPreferencesDatasource
import br.com.usinasantafe.cvf.infra.datasource.sharedpreferences.TrailerSharedPreferencesDatasource
import br.com.usinasantafe.cvf.utils.resultFailure
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.atLeastOnce
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

class INoteRepositoryTest {

    private val headerSharedPreferencesDatasource = mock<HeaderSharedPreferencesDatasource>()
    private val trailerSharedPreferencesDatasource = mock<TrailerSharedPreferencesDatasource>()
    private val repository = INoteRepository(
        headerSharedPreferencesDatasource = headerSharedPreferencesDatasource,
        trailerSharedPreferencesDatasource = trailerSharedPreferencesDatasource
    )

    @Test
    fun `getRegDriver - Check return failure if have error in PreCECSharedPreferencesDatasource getRegDriver`() =
        runTest {
            whenever(
                headerSharedPreferencesDatasource.getRegDriver()
            ).thenReturn(
                resultFailure(
                    "IPreCECSharedPreferencesDatasource.getRegDriver",
                    "-",
                    Exception()
                )
            )
            val result = repository.getRegDriver()
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "INoteRepository.getRegDriver -> IPreCECSharedPreferencesDatasource.getRegDriver",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `getRegDriver - Check return correct if regDriver is null`() =
        runTest {
            whenever(
                headerSharedPreferencesDatasource.getRegDriver()
            ).thenReturn(
                Result.success(null)
            )
            val result = repository.getRegDriver()
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                null,
                result.getOrNull()
            )
        }

    @Test
    fun `getRegDriver - Check return correct if function execute successfully`() =
        runTest {
            whenever(
                headerSharedPreferencesDatasource.getRegDriver()
            ).thenReturn(
                Result.success(19759)
            )
            val result = repository.getRegDriver()
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                19759,
                result.getOrNull()!!
            )
        }

    @Test
    fun `setRegDriver - Check return failure if have error in HeaderSharedPreferencesDatasource setRegDriver`() =
        runTest {
            whenever(
                headerSharedPreferencesDatasource.setRegDriver(19759)
            ).thenReturn(
                resultFailure(
                    "IHeaderSharedPreferencesDatasource.setRegDriver",
                    "-",
                    Exception()
                )
            )
            val result = repository.setRegDriver(19759)
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "INoteRepository.setRegDriver -> IHeaderSharedPreferencesDatasource.setRegDriver",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }
    
    @Test
    fun `setRegDriver - Check return correct if function execute successfully`() =
        runTest {
            val result = repository.setRegDriver(19759)
            verify(headerSharedPreferencesDatasource, atLeastOnce()).setRegDriver(19759)
            assertEquals(
                true,
                result.isSuccess
            )
        }

    @Test
    fun `deleteNote - Check return failure if have error in TrailerSharedPreferencesDatasource clean`() =
        runTest {
            whenever(
                trailerSharedPreferencesDatasource.clean()
            ).thenReturn(
                resultFailure(
                    "ITrailerSharedPreferencesDatasource.clean",
                    "-",
                    Exception()
                )
            )
            val result = repository.deleteNote()
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "INoteRepository.deleteNote -> ITrailerSharedPreferencesDatasource.clean",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `deleteNote - Check return failure if have error in HeaderSharedPreferencesDatasource clean`() =
        runTest {
            whenever(
                headerSharedPreferencesDatasource.clean()
            ).thenReturn(
                resultFailure(
                    "IHeaderSharedPreferencesDatasource.clean",
                    "-",
                    Exception()
                )
            )
            val result = repository.deleteNote()
            verify(trailerSharedPreferencesDatasource, atLeastOnce()).clean()
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "INoteRepository.deleteNote -> IHeaderSharedPreferencesDatasource.clean",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `deleteNote - Check return correct if function execute successfully`() =
        runTest {
            val result = repository.deleteNote()
            verify(trailerSharedPreferencesDatasource, atLeastOnce()).clean()
            verify(headerSharedPreferencesDatasource, atLeastOnce()).clean()
            assertEquals(
                true,
                result.isSuccess
            )
        }
}