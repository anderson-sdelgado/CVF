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
class ISaveManagerTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var usecase: SaveManager

    @Inject
    lateinit var managerSharedPreferencesDatasource: IManagerSharedPreferencesDatasource

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun check_save_data() =
        runTest {
            assertEquals(
                false,
                managerSharedPreferencesDatasource.has().getOrThrow()
            )
            val result = usecase(
                idFront = 1,
                idRelease = 1
            )
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                true,
                managerSharedPreferencesDatasource.has().getOrThrow()
            )
            val model = managerSharedPreferencesDatasource.get().getOrThrow()
            assertEquals(
                1,
                model.idFront
            )
            assertEquals(
                1,
                model.idRelease
            )
        }

}