package br.com.usinasantafe.cvf.external.retrofit.datasource.stable

import android.content.Context
import br.com.usinasantafe.cvf.di.external.ApiModuleTest.provideRetrofitTest
import br.com.usinasantafe.cvf.external.retrofit.api.stable.FrontApi
import br.com.usinasantafe.cvf.infra.models.retrofit.stable.FrontRetrofitModel
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
class IFrontRetrofitDatasourceTest {

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
            val service = retrofit.create(FrontApi::class.java)
            val datasource = IFrontRetrofitDatasource(context, service)
            val result = datasource.listAll("TOKEN")
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IFrontRetrofitDatasource.listAll",
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
            val service = retrofit.create(FrontApi::class.java)
            val datasource = IFrontRetrofitDatasource(context, service)
            val result = datasource.listAll("TOKEN")

            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IFrontRetrofitDatasource.listAll",
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
            val service = retrofit.create(FrontApi::class.java)
            val datasource = IFrontRetrofitDatasource(context, service)
            val result = datasource.listAll("TOKEN")

            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                Result.success(
                    listOf(
                        FrontRetrofitModel(
                            id = 1,
                            cd = 1,
                            description = "Test1"
                        ),
                        FrontRetrofitModel(
                            id = 2,
                            cd = 2,
                            description = "Test2"
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
                  {"id":1,"cd":1,"description":"Test1"},
                  {"id":2,"cd":2,"description":"Test2"}
                ]
        }
    """.trimIndent()

}