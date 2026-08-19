package br.com.usinasantafe.cvf.external.retrofit.datasource.variable

import android.content.Context
import br.com.usinasantafe.cvf.di.external.ApiModuleTest.provideRetrofitTest
import br.com.usinasantafe.cvf.external.retrofit.api.variable.ConfigApi
import br.com.usinasantafe.cvf.external.retrofit.api.variable.ManagerApi
import br.com.usinasantafe.cvf.infra.models.retrofit.variable.ConfigRetrofitModelOutput
import br.com.usinasantafe.cvf.infra.models.retrofit.variable.ManagerRetrofitModelOutput
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.intArrayOf
import kotlin.test.assertEquals

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class IManagerRetrofitDatasourceTest {

    private val context = mock<Context>()

    private val model = ManagerRetrofitModelOutput(
        idFront = 1,
        idRelease = 2,
        idServ = 3
    )

    @Test
    fun `send - Check return failure if token is invalid`() =
        runTest {

            val server = MockWebServer()
            server.start()
            server.enqueue(
                MockResponse().setBody(resultFailureAuthorization)
            )
            val retrofit = provideRetrofitTest(
                server.url("/").toString()
            )
            val service = retrofit.create(ManagerApi::class.java)
            val datasource = IManagerRetrofitDatasource(context, service)
            val result = datasource.send("TOKEN", model)
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IManagerRetrofitDatasource.send",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception: Authorization header is missing",
                result.exceptionOrNull()!!.cause.toString()
            )
            server.shutdown()
        }

    @Test
    fun `send - Check return failure if sent data incorrect`() =
        runTest {
            val server = MockWebServer()
            server.start()
            server.enqueue(MockResponse().setBody(resultFailureInfoIncorrect))
            val retrofit = provideRetrofitTest(server.url("/").toString())
            val service = retrofit.create(ManagerApi::class.java)
            val datasource = IManagerRetrofitDatasource(context, service)
            val result = datasource.send("TOKEN", model)
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IManagerRetrofitDatasource.send",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "com.google.gson.stream.MalformedJsonException: Use JsonReader.setStrictness(Strictness.LENIENT) to accept malformed JSON at line 3 column 15 path \$.idServ\n" +
                        "See https://github.com/google/gson/blob/main/Troubleshooting.md#malformed-json",
                result.exceptionOrNull()!!.cause!!.toString()
            )
            server.shutdown()
        }

    @Test
    fun `send - Check return failure if have Error 404`() =
        runTest {
            val server = MockWebServer()
            server.start()
            server.enqueue(
                MockResponse().setResponseCode(404)
            )
            val retrofit = provideRetrofitTest(
                server.url("/").toString()
            )
            val service = retrofit.create(ManagerApi::class.java)
            val datasource = IManagerRetrofitDatasource(context, service)
            val result = datasource.send("TOKEN", model)
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IManagerRetrofitDatasource.send",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.NullPointerException",
                result.exceptionOrNull()!!.cause.toString()
            )
            server.shutdown()
        }

    @Test
    fun `send - Check return correct if function execute successfully`() =
        runTest {
            val server = MockWebServer()
            server.start()
            server.enqueue(MockResponse().setBody(result))
            val retrofit = provideRetrofitTest(server.url("/").toString())
            val service = retrofit.create(ManagerApi::class.java)
            val datasource = IManagerRetrofitDatasource(context, service)
            val result = datasource.send("TOKEN", model)
            assertEquals(
                true,
                result.isSuccess
            )
            val modelInput = result.getOrNull()!!
            assertEquals(
                16,
                modelInput.idServ
            )
            server.shutdown()
        }

    private val resultFailureAuthorization = """
        {
            "status": "error",
            "failure": "Authorization header is missing"
        }
    """.trimIndent()

    private val resultFailureInfoIncorrect = """
        {
            "status": "success",
            "idServ": s4dq
        }
    """.trimIndent()

    private val result = """
        {
            "status": "success",
            "idServ": 16
        }
    """.trimIndent()
}