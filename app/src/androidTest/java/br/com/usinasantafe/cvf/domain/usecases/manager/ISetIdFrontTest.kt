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
class ISetIdFrontTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var usecase: SetIdFront

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
                    idFront = 2,
                    idRelease = 2
                )
            )
            val modelBefore = managerSharedPreferencesDatasource.get().getOrThrow()
            assertEquals(
                2,
                modelBefore.idFront
            )
            assertEquals(
                2,
                modelBefore.idRelease
            )
            val result = usecase(1)
            assertEquals(
                true,
                result.isSuccess
            )
            val modelAfter = managerSharedPreferencesDatasource.get().getOrThrow()
            assertEquals(
                1,
                modelAfter.idFront
            )
            assertEquals(
                null,
                modelAfter.idRelease
            )
            assertEquals(
                StatusSend.STARTED,
                modelAfter.statusSend
            )
        }
}