package br.com.usinasantafe.cvf.domain.usecases.note

import br.com.usinasantafe.cvf.external.sharedPreferences.IHeaderSharedPreferencesDatasource
import br.com.usinasantafe.cvf.infra.models.sharedpreferences.HeaderSharedPreferencesModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import kotlin.test.assertEquals

@HiltAndroidTest
class ISetRegDriverTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var usecase: SetRegDriver

    @Inject
    lateinit var headerSharedPreferencesDatasource: IHeaderSharedPreferencesDatasource

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun check_return_failure_if_value_of_field_is_incorrect() =
        runTest {
            val result = usecase("de25")
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "ISetRegDriver -> stringToLong",
                result.exceptionOrNull()!!.message,
            )
            assertEquals(
                "java.lang.NumberFormatException: For input string: \"de25\"",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun check_alter_data_if_process_execute_successfully() =
        runTest {
            headerSharedPreferencesDatasource.save(
                HeaderSharedPreferencesModel(
                    regDriver = 123456
                )
            )
            val modelBefore = headerSharedPreferencesDatasource.get().getOrThrow()
            assertEquals(
                "123456",
                modelBefore.regDriver.toString()
            )
            val result = usecase("456789")
            assertEquals(
                true,
                result.isSuccess
            )
            val modelAfter = headerSharedPreferencesDatasource.get().getOrThrow()
            assertEquals(
                "456789",
                modelAfter.regDriver.toString()
            )
        }

}