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
class IGetRegDriverTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var usecase: GetRegDriver

    @Inject
    lateinit var headerSharedPreferencesDatasource: IHeaderSharedPreferencesDatasource

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun check_return_null_if_not_have_data() =
        runTest {
            val result = usecase()
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
    fun check_return_data_if_have_table_and_regColab_is_null() =
        runTest {
            headerSharedPreferencesDatasource.save(
                HeaderSharedPreferencesModel()
            )
            val result = usecase()
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
    fun check_return_data_if_have_data() =
        runTest {
            headerSharedPreferencesDatasource.save(
                HeaderSharedPreferencesModel(
                    regDriver = 12345
                )
            )
            val result = usecase()
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                "12345",
                result.getOrNull()!!
            )
        }

}