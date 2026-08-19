package br.com.usinasantafe.cvf.external.sharedPreferences

import android.content.Context
import android.content.SharedPreferences
import androidx.test.core.app.ApplicationProvider
import br.com.usinasantafe.cvf.infra.models.sharedpreferences.ConfigSharedPreferencesModel
import br.com.usinasantafe.cvf.lib.StatusSend
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.intArrayOf
import kotlin.test.assertEquals
import kotlin.text.get

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class IConfigSharedPreferencesDatasourceTest {

    private lateinit var context : Context
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var datasource: IConfigSharedPreferencesDatasource

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        sharedPreferences = context.getSharedPreferences("test", Context.MODE_PRIVATE)
        datasource = IConfigSharedPreferencesDatasource(sharedPreferences)
    }

    @Test
    fun `get - Check return failure if number is null`() =
        runTest {
            val data = ConfigSharedPreferencesModel()
            datasource.save(data)
            val result = datasource.get()
            assertEquals(
                true,
                result.isFailure,
            )
            assertEquals(
                "IConfigSharedPreferencesDatasource.get",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.NullPointerException: number is required",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `get - Check return failure if password is null`() =
        runTest {
            val data = ConfigSharedPreferencesModel(
                number = 16997417840
            )
            datasource.save(data)
            val result = datasource.get()
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IConfigSharedPreferencesDatasource.get",
                result.exceptionOrNull()!!.message,
            )
            assertEquals(
                "java.lang.NullPointerException: password is required",
                result.exceptionOrNull()!!.cause.toString(),
            )
        }

    @Test
    fun `get - Check return correct if function execute successfully`() =
        runTest {
            val data = ConfigSharedPreferencesModel(
                number = 16997417840,
                password = "12345"
            )
            datasource.save(data)
            val result = datasource.get()
            assertEquals(
                true,
                result.isSuccess,
            )
            assertEquals(
                ConfigSharedPreferencesModel(
                    number = 16997417840,
                    password = "12345"
                ),
                result.getOrNull()!!
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
                result.getOrNull()!!
            )
        }

    @Test
    fun `has - Check return true if have data`() =
        runTest {
            val data = ConfigSharedPreferencesModel(
                number = 1,
                password = "123456",
                idServ = 1,
                version = "1.00",
                statusSend = StatusSend.SENT
            )
            datasource.save(data)
            val result = datasource.has()
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                true,
                result.getOrNull()!!
            )
        }

    @Test
    fun `setFlagUpdate - Check return data correct the Config SharedPreferences internal`() =
        runTest {
            val data = ConfigSharedPreferencesModel(
                number = 16997417840,
                password = "123456",
                idServ = 1,
                version = "1.00"
            )
            datasource.save(data)
            val resultBefore = datasource.get()
            assertEquals(
                true,
                resultBefore.isSuccess
            )
            val modelBefore = resultBefore.getOrNull()!!
            assertEquals(
                StatusSend.STARTED,
                modelBefore.statusSend
            )
            assertEquals(
                false,
                modelBefore.flagUpdate
            )
            val result = datasource.setFlagUpdate()
            assertEquals(
                true,
                result.isSuccess
            )
            val resultAfter = datasource.get()
            assertEquals(
                true,
                resultAfter.isSuccess
            )
            val modelAfter = resultAfter.getOrNull()!!
            assertEquals(
                StatusSend.SENT,
                modelAfter.statusSend
            )
            assertEquals(
                true,
                modelAfter.flagUpdate
            )
        }

    @Test
    fun `setStatusSend - Check return data correct the Config SharedPreferences internal`() =
        runTest {
            val data = ConfigSharedPreferencesModel(
                number = 16997417840,
                password = "123456",
                idServ = 1,
                version = "1.00",
                statusSend = StatusSend.STARTED
            )
            datasource.save(data)
            val resultBefore = datasource.get()
            assertEquals(
                true,
                resultBefore.isSuccess
            )
            val modelBefore = resultBefore.getOrNull()!!
            assertEquals(
                StatusSend.STARTED,
                modelBefore.statusSend
            )
            val result = datasource.setStatusSend(StatusSend.SEND)
            assertEquals(
                true,
                result.isSuccess
            )
            val resultAfter = datasource.get()
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