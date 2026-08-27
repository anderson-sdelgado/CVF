package br.com.usinasantafe.cvf.domain.usecases.manager

import br.com.usinasantafe.cvf.external.sharedPreferences.IManagerSharedPreferencesDatasource
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
class ISetIdReleaseTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var usecase: SetIdRelease

    @Inject
    lateinit var managerSharedPreferencesDatasource: IManagerSharedPreferencesDatasource

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun check_altered_data() =
        runTest {
            val result = usecase(1)
            assertEquals(
                true,
                result.isSuccess
            )
            val modelAfter = managerSharedPreferencesDatasource.get().getOrThrow()
            assertEquals(
                null,
                modelAfter.idFront
            )
            assertEquals(
                1,
                modelAfter.idRelease
            )
            assertEquals(
                StatusSend.SEND,
                modelAfter.statusSend
            )
        }

}