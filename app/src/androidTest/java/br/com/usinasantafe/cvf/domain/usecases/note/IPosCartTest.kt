package br.com.usinasantafe.cvf.domain.usecases.note

import br.com.usinasantafe.cvf.external.sharedPreferences.ICartSharedPreferencesDatasource
import br.com.usinasantafe.cvf.infra.models.sharedpreferences.CartSharedPreferencesModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import kotlin.test.assertEquals

@HiltAndroidTest
class IPosCartTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var usecase: PosCart

    @Inject
    lateinit var cartSharedPreferencesDatasource: ICartSharedPreferencesDatasource

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun check_return_1_if_have_no_cart() =
        runTest {
            val result = usecase()
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                1,
                result.getOrNull()!!
            )
        }

    @Test
    fun check_return_if_have_cart() =
        runTest {
            cartSharedPreferencesDatasource.add(
                CartSharedPreferencesModel(
                    position = 1,
                    idCart = 1
                )
            )
            cartSharedPreferencesDatasource.add(
                CartSharedPreferencesModel(
                    position = 3,
                    idCart = 2
                )
            )
            val result = usecase()
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                4,
                result.getOrNull()!!
            )
        }
}