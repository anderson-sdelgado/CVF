package br.com.usinasantafe.cvf.external.sharedPreferences

import android.content.Context
import android.content.SharedPreferences
import androidx.test.core.app.ApplicationProvider
import br.com.usinasantafe.cvf.infra.models.sharedpreferences.ConfigSharedPreferencesModel
import br.com.usinasantafe.cvf.infra.models.sharedpreferences.ManagerSharedPreferencesModel
import br.com.usinasantafe.cvf.lib.StatusSend
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.Date
import kotlin.intArrayOf
import kotlin.test.assertEquals

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class IManagerSharedPreferencesDatasourceTest {

    private lateinit var context : Context
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var datasource: IManagerSharedPreferencesDatasource

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        sharedPreferences = context.getSharedPreferences("test", Context.MODE_PRIVATE)
        datasource = IManagerSharedPreferencesDatasource(sharedPreferences)
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
            val data = ManagerSharedPreferencesModel(
                idRelease = 1,
                idFront = 1
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
    fun `clean - Check altered data`() =
        runTest {
            val data = ManagerSharedPreferencesModel(
                idRelease = 1,
                idFront = 1
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
            datasource.clean()
            val resultAfter = datasource.has()
            assertEquals(
                true,
                resultAfter.isSuccess
            )
            assertEquals(
                false,
                resultAfter.getOrNull()!!
            )
        }

    @Test
    fun `getIdFront - Check return failure if have db is empty`() =
        runTest {
            val result = datasource.getIdFront()
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
    fun `getIdFront - Check return correct if have data`() =
        runTest {
            val data = ManagerSharedPreferencesModel(
                idRelease = 1,
                idFront = 20
            )
            datasource.save(data)
            val result = datasource.getIdFront()
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                20,
                result.getOrNull()!!
            )
        }

    @Test
    fun `getIdRelease - Check return failure if have db is empty`() =
        runTest {
            val result = datasource.getIdRelease()
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
    fun `getIdRelease - Check return correct if have data`() =
        runTest {
            val data = ManagerSharedPreferencesModel(
                idRelease = 1,
                idFront = 20
            )
            datasource.save(data)
            val result = datasource.getIdRelease()
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
    fun `save - Check data save`() =
        runTest {
            val data = ManagerSharedPreferencesModel(
                idRelease = 1,
                idFront = 20,
                dateHourCreate = Date(1786647885000),
                dateHourUpdate = Date(1786647885000),
                stateSend = StatusSend.SENT
            )
            datasource.save(data)
            val result = datasource.get()
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                ManagerSharedPreferencesModel(
                    idRelease = 1,
                    idFront = 20,
                    dateHourCreate = Date(1786647885000),
                    dateHourUpdate = Date(1786647885000),
                    stateSend = StatusSend.SENT
                ),
                result.getOrNull()!!
            )

        }


}