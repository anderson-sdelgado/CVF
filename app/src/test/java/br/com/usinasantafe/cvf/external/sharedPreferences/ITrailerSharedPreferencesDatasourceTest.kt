package br.com.usinasantafe.cvf.external.sharedPreferences

import android.content.Context
import android.content.SharedPreferences
import androidx.test.core.app.ApplicationProvider
import br.com.usinasantafe.cvf.infra.models.sharedpreferences.TrailerSharedPreferencesModel
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.intArrayOf
import kotlin.test.assertEquals

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ITrailerSharedPreferencesDatasourceTest {

    private lateinit var context : Context
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var datasource: ITrailerSharedPreferencesDatasource

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        sharedPreferences = context.getSharedPreferences("test", Context.MODE_PRIVATE)
        datasource = ITrailerSharedPreferencesDatasource(sharedPreferences)
    }

    @Test
    fun `clean - Check clean data in table`() =
        runTest {
            val data = TrailerSharedPreferencesModel(
                position = 1,
                idTrailer = 1
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