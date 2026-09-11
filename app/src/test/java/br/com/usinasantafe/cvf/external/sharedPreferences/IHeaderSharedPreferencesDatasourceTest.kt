package br.com.usinasantafe.cvf.external.sharedPreferences

import android.content.Context
import android.content.SharedPreferences
import androidx.test.core.app.ApplicationProvider
import br.com.usinasantafe.cvf.infra.models.sharedpreferences.HeaderSharedPreferencesModel
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
class IHeaderSharedPreferencesDatasourceTest {

    private lateinit var context : Context
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var datasource: IHeaderSharedPreferencesDatasource

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        sharedPreferences = context.getSharedPreferences("test", Context.MODE_PRIVATE)
        datasource = IHeaderSharedPreferencesDatasource(sharedPreferences)
    }

    @Test
    fun `getRegDriver - Check return null if not have data`() =
        runTest {
            val result = datasource.getRegDriver()
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                null,
                result.getOrNull()
            )
        }

    @Test
    fun `getRegDriver - Check return correct if have data`() =
        runTest {
            val data = HeaderSharedPreferencesModel(
                regDriver = 19759
            )
            datasource.save(data)
            val result = datasource.getRegDriver()
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                19759,
                result.getOrNull()
            )
        }

    @Test
    fun `setRegDriver - Check altered data`() =
        runTest {
            val data = HeaderSharedPreferencesModel(
                regDriver = 19759
            )
            datasource.save(data)
            val resultBefore = datasource.getRegDriver()
            assertEquals(
                true,
                resultBefore.isSuccess
            )
            assertEquals(
                19759,
                resultBefore.getOrNull()
            )
            val result = datasource.setRegDriver(18017)
            assertEquals(
                true,
                result.isSuccess
            )
            val resultAfter = datasource.getRegDriver()
            assertEquals(
                true,
                resultAfter.isSuccess
            )
            assertEquals(
                18017,
                resultAfter.getOrNull()
            )
        }

    @Test
    fun `clean - Check clean data in table`() =
        runTest {
            val data = HeaderSharedPreferencesModel(
                regDriver = 19759
            )
            datasource.save(data)
            val resultBefore = datasource.getRegDriver()
            assertEquals(
                true,
                resultBefore.isSuccess
            )
            assertEquals(
                19759,
                resultBefore.getOrNull()
            )
            val result = datasource.clean()
            assertEquals(
                true,
                result.isSuccess
            )
            val resultAfter = datasource.getRegDriver()
            assertEquals(
                true,
                resultAfter.isSuccess
            )
            assertEquals(
                null,
                resultAfter.getOrNull()
            )
        }

    @Test
    fun `getIdTruck - Check return null if not have data`() =
        runTest {
            val result = datasource.getIdTruck()
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                null,
                result.getOrNull()
            )
        }

    @Test
    fun `getIdTruck - Check return correct if have data`() =
        runTest {
            val data = HeaderSharedPreferencesModel(
                idTruck = 100
            )
            datasource.save(data)
            val result = datasource.getIdTruck()
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                100,
                result.getOrNull()
            )
        }

    @Test
    fun `setIdTruck - Check altered data`() =
        runTest {
            val data = HeaderSharedPreferencesModel(
                idTruck = 100
            )
            datasource.save(data)
            val resultBefore = datasource.getIdTruck()
            assertEquals(
                true,
                resultBefore.isSuccess
            )
            assertEquals(
                100,
                resultBefore.getOrNull()
            )
            val result = datasource.setIdTruck(200)
            assertEquals(
                true,
                result.isSuccess
            )
            val resultAfter = datasource.getIdTruck()
            assertEquals(
                true,
                resultAfter.isSuccess
            )
            assertEquals(
                200,
                resultAfter.getOrNull()
            )
        }

    @Test
    fun `get - Check return empty if not have data`() =
        runTest {
            val result = datasource.get()
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                HeaderSharedPreferencesModel(),
                result.getOrNull()
            )
        }

    @Test
    fun `get - Check return model if have data`() =
        runTest {
            val data = HeaderSharedPreferencesModel(
                regDriver = 19759,
                idTruck = 100
            )
            datasource.save(data)
            val result = datasource.get()
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                data,
                result.getOrNull()
            )
        }

    @Test
    fun `has - Check return false if not have data`() =
        runTest {
            val result = datasource.has()
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                false,
                result.getOrNull()
            )
        }

    @Test
    fun `has - Check return true if have data`() =
        runTest {
            val data = HeaderSharedPreferencesModel(
                regDriver = 19759,
                idTruck = 100
            )
            datasource.save(data)
            val result = datasource.has()
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                true,
                result.getOrNull()
            )
        }


}