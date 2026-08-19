package br.com.usinasantafe.cvf.external.retrofit.datasource.stable

import android.content.Context
import br.com.usinasantafe.cvf.di.external.ApiModuleTest.provideRetrofitTest
import br.com.usinasantafe.cvf.external.retrofit.api.stable.ReleaseApi
import br.com.usinasantafe.cvf.infra.models.retrofit.stable.ReleaseRetrofitModel
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
class IReleaseRetrofitDatasourceTest {

    private val context = mock<Context>()

    @Test
    fun `Check return failure if token is invalid`() =
        runTest {
            val server = MockWebServer()
            server.start()
            server.enqueue(
                MockResponse().setBody(resultFailureAuthorization)
            )
            val retrofit = provideRetrofitTest(
                server.url("/").toString()
            )
            val service = retrofit.create(ReleaseApi::class.java)
            val datasource = IReleaseRetrofitDatasource(context, service)
            val result = datasource.listAll("TOKEN")
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IReleaseRetrofitDatasource.listAll",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception: Authorization header is missing",
                result.exceptionOrNull()!!.cause.toString()
            )
            server.shutdown()
        }

    @Test
    fun `Check return failure if have Error 404`() =
        runTest {
            val server = MockWebServer()
            server.start()
            server.enqueue(
                MockResponse().setResponseCode(404)
            )
            val retrofit = provideRetrofitTest(
                server.url("/").toString()
            )
            val service = retrofit.create(ReleaseApi::class.java)
            val datasource = IReleaseRetrofitDatasource(context, service)
            val result = datasource.listAll("TOKEN")

            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IReleaseRetrofitDatasource.listAll",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.NullPointerException",
                result.exceptionOrNull()!!.cause.toString()
            )
            server.shutdown()
        }

    @Test
    fun `Check return correct`() =
        runTest {
            val server = MockWebServer()
            server.start()
            server.enqueue(
                MockResponse().setBody(result)
            )
            val retrofit = provideRetrofitTest(
                server.url("").toString()
            )
            val service = retrofit.create(ReleaseApi::class.java)
            val datasource = IReleaseRetrofitDatasource(context, service)
            val result = datasource.listAll("TOKEN")

            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                Result.success(
                    listOf(
                        ReleaseRetrofitModel(
                            id = 1,
                            nroOS = 1,
                            idPropAgr = 1,
                            descPropAgr = "Test1",
                            idFront = 1
                        ),
                        ReleaseRetrofitModel(
                            id = 2,
                            nroOS = 2,
                            idPropAgr = 2,
                            descPropAgr = "Test2",
                            idFront = 2
                        )
                    )
                ),
                result
            )
            server.shutdown()
        }

    private val resultFailureAuthorization = """
        {
            "status": "error",
            "failure": "Authorization header is missing"
        }
    """.trimIndent()

    private val result = """
        {
            "status": "success",
            "data": 
                [
                  {"id":1,"nroOS":1,"idPropAgr":1,"descPropAgr":"Test1","idFront":1},
                  {"id":2,"nroOS":2,"idPropAgr":2,"descPropAgr":"Test2","idFront":2}
                ]
        }
    """.trimIndent()

}