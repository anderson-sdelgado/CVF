package br.com.usinasantafe.cvf.domain.usecases.manager

import br.com.usinasantafe.cvf.external.sharedPreferences.IManagerSharedPreferencesDatasource
import br.com.usinasantafe.cvf.infra.models.sharedpreferences.ManagerSharedPreferencesModel
import br.com.usinasantafe.cvf.lib.StatusSend
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import kotlin.test.assertEquals

@HiltAndroidTest
class IHasSendManagerTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var usecase: HasSendManager

    @Inject
    lateinit var managerSharedPreferencesDatasource: IManagerSharedPreferencesDatasource

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun check_return_false_if_not_have_data() =
        runTest {
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
    fun check_return_false_if_have_data_and_state_send_is_not_send() =
        runTest {
            managerSharedPreferencesDatasource.setIdRelease(1)
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
    fun check_return_true_if_have_data_and_state_send_is_send() =
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