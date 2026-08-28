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
class ICheckStatusManagerTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var usecase: CheckStatusManager

    @Inject
    lateinit var managerSharedPreferencesDatasource: IManagerSharedPreferencesDatasource

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun check_return_failure_if_not_have_data_in_manager() =
        runTest {
            val result = usecase()
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "ICheckStatusManager -> IManagerRepository.getStatusSend -> IManagerSharedPreferencesDatasource.getStatusSend",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.NullPointerException: statusSend is required",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun check_return_false_if_add_id_front_in_manager() =
        runTest {
            managerSharedPreferencesDatasource.setIdFront(1)
            val result = usecase()
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
    fun check_return_true_if_add_id_release_in_manager() =
        runTest {
            managerSharedPreferencesDatasource.setIdRelease(1)
            val result = usecase()
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