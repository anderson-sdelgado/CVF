package br.com.usinasantafe.cvf.domain.usecases.config

import br.com.usinasantafe.cvf.infra.datasource.sharedpreferences.ConfigSharedPreferencesDatasource
import br.com.usinasantafe.cvf.infra.models.sharedpreferences.ConfigSharedPreferencesModel
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
class ISetStatusSendTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var usecase: SetStatusSend

    @Inject
    lateinit var configSharedPreferencesDatasource: ConfigSharedPreferencesDatasource

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun check_altered_data() =
        runTest {
            val data = ConfigSharedPreferencesModel(
                number = 16997417840,
                password = "123456",
                idServ = 1,
                version = "1.00",
                statusSend = StatusSend.STARTED
            )
            configSharedPreferencesDatasource.save(data)
            val resultBefore = configSharedPreferencesDatasource.get()
            assertEquals(
                true,
                resultBefore.isSuccess
            )
            val modelBefore = resultBefore.getOrNull()!!
            assertEquals(
                StatusSend.STARTED,
                modelBefore.statusSend
            )
            val result = usecase(StatusSend.SEND)
            assertEquals(
                true,
                result.isSuccess
            )
            val resultAfter = configSharedPreferencesDatasource.get()
            assertEquals(
                true,
                resultAfter.isSuccess
            )
            val modelAfter = resultAfter.getOrNull()!!
            assertEquals(
                StatusSend.SEND,
                modelAfter.statusSend
            )
        }
}