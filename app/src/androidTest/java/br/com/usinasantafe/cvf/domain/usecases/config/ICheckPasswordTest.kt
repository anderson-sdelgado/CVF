package br.com.usinasantafe.cvf.domain.usecases.config

import br.com.usinasantafe.cvf.infra.datasource.sharedpreferences.ConfigSharedPreferencesDatasource
import br.com.usinasantafe.cvf.infra.models.sharedpreferences.ConfigSharedPreferencesModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import kotlin.test.assertEquals

@HiltAndroidTest
class ICheckPasswordTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var usecase: CheckPassword

    @Inject
    lateinit var configSharedPreferencesDatasource: ConfigSharedPreferencesDatasource

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun check_return_failure_if_not_have_data() =
        runTest {
            val result = usecase("12345")
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "ICheckPassword -> IConfigRepository.getPassword -> IConfigSharedPreferencesDatasource.getPassword",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.NullPointerException: password is required",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun check_return_false_if_password_input_is_incorrect() =
        runTest {
            configSharedPreferencesDatasource.save(
                ConfigSharedPreferencesModel(
                    number = 16997597840,
                    password = "12345",
                    idServ = 1,
                    version = "1.0"
                )
            )
            val result = usecase("123456")
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
    fun check_return_true_if_password_input_is_correct() =
        runTest {
            configSharedPreferencesDatasource.save(
                ConfigSharedPreferencesModel(
                    number = 16997597840,
                    password = "12345",
                    idServ = 1,
                    version = "1.0"
                )
            )
            val result = usecase("12345")
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                true,
                result.getOrNull()!!
            )
        }
}