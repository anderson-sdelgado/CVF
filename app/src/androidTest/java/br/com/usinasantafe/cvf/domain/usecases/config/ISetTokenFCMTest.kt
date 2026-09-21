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
class ISetTokenFCMTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var usecase: SetTokenFCM

    @Inject
    lateinit var configSharedPreferencesDatasource: ConfigSharedPreferencesDatasource

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun check_not_altered_status_send_if_config_shared_preferences_is_empty() =
        runTest {
            val result = usecase("token")
            assertEquals(
                true,
                result.isSuccess
            )
            val model = configSharedPreferencesDatasource.get().getOrThrow()
            assertEquals(
                "token",
                model.tokenFCM
            )
            assertEquals(
                StatusSend.STARTED,
                model.statusSend
            )
        }

    @Test
    fun check_altered_status_send_if_token_is_different() =
        runTest {
            configSharedPreferencesDatasource.save(
                ConfigSharedPreferencesModel(
                    number = 123,
                    password = "123",
                    tokenFCM = "tokenOld",
                    statusSend = StatusSend.SENT
                )
            )
            val result = usecase("tokenNew")
            assertEquals(
                true,
                result.isSuccess
            )
            val model = configSharedPreferencesDatasource.get().getOrThrow()
            assertEquals(
                "tokenNew",
                model.tokenFCM
            )
            assertEquals(
                StatusSend.SEND,
                model.statusSend
            )
        }

    @Test
    fun check_not_altered_status_send_if_token_is_equal() =
        runTest {
            configSharedPreferencesDatasource.save(
                ConfigSharedPreferencesModel(
                    number = 123,
                    password = "123",
                    tokenFCM = "token",
                    statusSend = StatusSend.SENT
                )
            )
            val result = usecase("token")
            assertEquals(
                true,
                result.isSuccess
            )
            val model = configSharedPreferencesDatasource.get().getOrThrow()
            assertEquals(
                "token",
                model.tokenFCM
            )
            assertEquals(
                StatusSend.SENT,
                model.statusSend
            )
        }
}
