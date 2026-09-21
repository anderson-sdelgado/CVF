package br.com.usinasantafe.cvf.external.sharedPreferences

import android.content.Context
import android.content.SharedPreferences
import androidx.test.core.app.ApplicationProvider
import br.com.usinasantafe.cvf.TestApp
import br.com.usinasantafe.cvf.infra.models.sharedpreferences.ManagerSharedPreferencesModel
import br.com.usinasantafe.cvf.lib.StatusSend
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
            datasource.setIdRelease(1)
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
            datasource.setIdRelease(1)
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
            datasource.setIdFront(20)
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
            datasource.setIdRelease(1)
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
    fun `setIdFront - Check altered field idFront`() =
        runTest {
            datasource.setIdFront(1)
            val result = datasource.get()
            assertEquals(
                true,
                result.isSuccess
            )
            val model = result.getOrNull()!!
            assertEquals(
                1,
                model.idFront
            )
            assertEquals(
                null,
                model.idRelease
            )
            assertEquals(
                StatusSend.STARTED,
                model.statusSend
            )
        }

    @Test
    fun `setIdRelease - Check altered field idRelease`() =
        runTest {
            datasource.setIdRelease(1)
            val result = datasource.get()
            assertEquals(
                true,
                result.isSuccess
            )
            val model = result.getOrNull()!!
            assertEquals(
                null,
                model.idFront
            )
            assertEquals(
                1,
                model.idRelease
            )
            assertEquals(
                StatusSend.SEND,
                model.statusSend
            )
        }

    @Test
    fun `hasSend - Check return false if not have data`() =
        runTest {
            val result = datasource.hasSend()
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
    fun `hasSend - Check return false if have data and stateSend is not SEND`() =
        runTest {
            datasource.setIdFront(1)
            val result = datasource.hasSend()
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
    fun `hasSend - Check return true if have data and stateSend is SEND`() =
        runTest {
            datasource.setIdRelease(1)
            val result = datasource.hasSend()
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
            datasource.setIdRelease(1)
            val resultBefore = datasource.get()
            assertEquals(
                true,
                resultBefore.isSuccess
            )
            val modelBefore = resultBefore.getOrNull()!!
            assertEquals(
                StatusSend.SEND,
                modelBefore.statusSend
            )
            val result = datasource.setStatusSend(StatusSend.SENT)
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
        }

    @Test
    fun `setStatusSend and getStatusSend - Check return data correct the Config SharedPreferences internal`() =
        runTest {
            datasource.setStatusSend(StatusSend.SENT)
            val result = datasource.getStatusSend()
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                StatusSend.SENT,
                result.getOrNull()!!
            )
        }

    @Test
    fun `getQtdLimitCart - Check return failure if have db is empty`() =
        runTest {
            val result = datasource.getQtdLimitCart()
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IManagerSharedPreferencesDatasource.getQtdLimitCart",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.NullPointerException: qtdLimitCart is required",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `getQtdLimitCart - Check return correct if have db is not empty`() =
        runTest {
            datasource.setIdRelease(1)
            val result = datasource.getQtdLimitCart()
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                3,
                result.getOrNull()!!
            )
        }

    @Test
    fun `update - Check altered data`() =
        runTest {
            datasource.save(
                ManagerSharedPreferencesModel(
                    idFront = 10,
                    idRelease = 20,
                    qtdLimitCart = 30
                )
            )
            val modelBefore = datasource.get().getOrThrow()
            assertEquals(
                10,
                modelBefore.idFront
            )
            assertEquals(
                20,
                modelBefore.idRelease
            )
            assertEquals(
                30,
                modelBefore.qtdLimitCart
            )
            val result = datasource.update(101, 2, 2)
            assertEquals(
                true,
                result.isSuccess
            )
            val modelAfter = datasource.get().getOrThrow()
            assertEquals(
                101,
                modelAfter.idFront
            )
            assertEquals(
                2,
                modelAfter.idRelease
            )
            assertEquals(
                2,
                modelAfter.qtdLimitCart
            )
        }

}