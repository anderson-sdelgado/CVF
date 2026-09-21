package br.com.usinasantafe.cvf.external.sharedPreferences

import android.content.Context
import android.content.SharedPreferences
import androidx.test.core.app.ApplicationProvider
import br.com.usinasantafe.cvf.TestApp
import br.com.usinasantafe.cvf.infra.models.sharedpreferences.CartSharedPreferencesModel
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.intArrayOf
import kotlin.test.assertEquals

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34], application = TestApp::class)
class ICartSharedPreferencesDatasourceTest {

    private lateinit var context : Context
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var datasource: ICartSharedPreferencesDatasource

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        sharedPreferences = context.getSharedPreferences("test", Context.MODE_PRIVATE)
        datasource = ICartSharedPreferencesDatasource(sharedPreferences)
    }

    @Test
    fun `clean, add and list - Check clean data in table`() =
        runTest {
            val data = CartSharedPreferencesModel(
                position = 1,
                idCart = 1
            )
            datasource.add(data)
            val resultBefore = datasource.list()
            assertEquals(
                true,
                resultBefore.isSuccess
            )
            val listBefore = resultBefore.getOrNull()!!
            assertEquals(
                1,
                listBefore.size
            )
            val result = datasource.clean()
            assertEquals(
                true,
                result.isSuccess
            )
            val resultAfter = datasource.list()
            assertEquals(
                true,
                resultAfter.isSuccess
            )
            val listAfter = resultAfter.getOrNull()!!
            assertEquals(
                0,
                listAfter.size
            )
        }
}