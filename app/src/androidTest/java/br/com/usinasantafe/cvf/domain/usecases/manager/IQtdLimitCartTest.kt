package br.com.usinasantafe.cvf.domain.usecases.manager

import br.com.usinasantafe.cvf.external.sharedPreferences.IManagerSharedPreferencesDatasource
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import kotlin.test.assertEquals

@HiltAndroidTest
class IQtdLimitCartTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var usecase: IQtdLimitCart

    @Inject
    lateinit var managerSharedPreferencesDatasource: IManagerSharedPreferencesDatasource

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun check_return_failure_if_have_db_is_empty() =
        runTest {
            val result = usecase()
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IQtdLimitCart -> IManagerRepository.getQtdLimitCart -> IManagerSharedPreferencesDatasource.getQtdLimitCart",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.NullPointerException: qtdLimitCart is required",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun check_return_correct_if_have_db_is_not_empty() =
        runTest {
            managerSharedPreferencesDatasource.setIdRelease(1)
            val result = usecase()
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                3,
                result.getOrNull()!!
            )
        }
}