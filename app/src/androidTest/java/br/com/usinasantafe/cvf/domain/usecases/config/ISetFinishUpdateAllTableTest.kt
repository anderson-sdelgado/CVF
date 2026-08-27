package br.com.usinasantafe.cvf.domain.usecases.config

import br.com.usinasantafe.cvf.external.sharedPreferences.IManagerSharedPreferencesDatasource
import br.com.usinasantafe.cvf.infra.datasource.sharedpreferences.ConfigSharedPreferencesDatasource
import br.com.usinasantafe.cvf.infra.models.sharedpreferences.ConfigSharedPreferencesModel
import br.com.usinasantafe.cvf.infra.models.sharedpreferences.ManagerSharedPreferencesModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import kotlin.test.assertEquals

@HiltAndroidTest
class ISetFinishUpdateAllTableTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var usecase: SetFinishUpdateAllTable

    @Inject
    lateinit var configSharedPreferencesDatasource: ConfigSharedPreferencesDatasource

    @Inject
    lateinit var managerSharedPreferencesDatasource: IManagerSharedPreferencesDatasource

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun check_altered_data() =
        runTest {
            managerSharedPreferencesDatasource.save(
                ManagerSharedPreferencesModel(
                    idRelease = 1,
                    idFront = 1
                )
            )
            configSharedPreferencesDatasource.save(
                ConfigSharedPreferencesModel(
                    number = 16997417840,
                    password = "12345",
                    idServ = 1,
                    version = "1.0"
                )
            )
            val modelConfigBefore = configSharedPreferencesDatasource.get().getOrThrow()
            assertEquals(
                false,
                modelConfigBefore.flagUpdate
            )
            val hasManagerAfter = managerSharedPreferencesDatasource.has().getOrThrow()
            assertEquals(
                true,
                hasManagerAfter
            )
            val result = usecase()
            assertEquals(
                true,
                result.isSuccess
            )
            val modelAfter = configSharedPreferencesDatasource.get().getOrThrow()
            assertEquals(
                true,
                modelAfter.flagUpdate
            )
            val modelManagerAfter = managerSharedPreferencesDatasource.has().getOrThrow()
            assertEquals(
                false,
                modelManagerAfter
            )
        }
}