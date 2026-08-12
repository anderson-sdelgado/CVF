package br.com.usinasantafe.cvf.domain.usecases.config

import br.com.usinasantafe.cvf.infra.datasource.sharedpreferences.ConfigSharedPreferencesDatasource
import br.com.usinasantafe.cvf.infra.models.sharedpreferences.ConfigSharedPreferencesModel
import br.com.usinasantafe.cvf.presenter.model.ConfigScreenModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import kotlin.test.assertEquals

@HiltAndroidTest
class IGetConfigTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var usecase: GetConfig

    @Inject
    lateinit var configSharedPreferencesDatasource: ConfigSharedPreferencesDatasource

    @Before
    fun init() {
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
    fun check_return_failure_if_number_is_null() =
        runTest {
            configSharedPreferencesDatasource.save(
                ConfigSharedPreferencesModel(
                    idServ = 1,
                    version = "1.0"
                )
            )
            val result = usecase()
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IGetConfig -> IConfigRepository.get -> IConfigSharedPreferencesDatasource.get",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.NullPointerException: number is required",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun check_return_failure_if_password_is_null() =
        runTest {
            configSharedPreferencesDatasource.save(
                ConfigSharedPreferencesModel(
                    number = 16997417840,
                    idServ = 1,
                    version = "1.0"
                )
            )
            val result = usecase()
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IGetConfig -> IConfigRepository.get -> IConfigSharedPreferencesDatasource.get",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.NullPointerException: password is required",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun check_return_correct_if_data_is_complete() =
        runTest {
            configSharedPreferencesDatasource.save(
                ConfigSharedPreferencesModel(
                    number = 16997417840,
                    password = "12345",
                    idServ = 1,
                    version = "1.0"
                )
            )
            val result = usecase()
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                ConfigScreenModel(
                    number = "16997417840",
                    password = "12345",
                ),
                result.getOrNull()!!
            )
        }
}