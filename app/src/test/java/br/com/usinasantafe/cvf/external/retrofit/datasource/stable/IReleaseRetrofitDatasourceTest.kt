package br.com.usinasantafe.cvf.external.retrofit.datasource.stable

import br.com.usinasantafe.cvf.di.external.ApiModuleTest.provideRetrofitTest
import br.com.usinasantafe.cvf.external.retrofit.api.stable.ReleaseApi
import br.com.usinasantafe.cvf.infra.models.retrofit.stable.ReleaseRetrofitModel
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Test
import kotlin.test.assertEquals

class IReleaseRetrofitDatasourceTest {

    @Test
    fun `Check return failure if token is invalid`() =
        runTest {
            val server = MockWebServer()
            server.start()
            server.enqueue(
                MockResponse().setBody("{ error : Authorization header is missing }")
            )
            val retrofit = provideRetrofitTest(
                server.url("").toString()
            )
            val service = retrofit.create(ReleaseApi::class.java)
            val datasource = IReleaseRetrofitDatasource(service)
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
                "java.lang.IllegalStateException: Expected BEGIN_ARRAY but was BEGIN_OBJECT at line 1 column 2 path \$\n" +
                        "See https://github.com/google/gson/blob/main/Troubleshooting.md#unexpected-json-structure",
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
                server.url("").toString()
            )
            val service = retrofit.create(ReleaseApi::class.java)
            val datasource = IReleaseRetrofitDatasource(service)
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
                NullPointerException().toString(),
                result.exceptionOrNull()!!.cause.toString()
            )
            server.shutdown()
        }

    @Test
    fun `Check return correct`() =
        runTest {
            val response = """
                [
                  {"id":1,"nroOS":1,"idPropAgr":1,"descPropAgr":"Test1","idFront":1},
                  {"id":2,"nroOS":2,"idPropAgr":2,"descPropAgr":"Test2","idFront":2}
                ]
            """.trimIndent()
            val server = MockWebServer()
            server.start()
            server.enqueue(
                MockResponse().setBody(response)
            )
            val retrofit = provideRetrofitTest(
                server.url("").toString()
            )
            val service = retrofit.create(ReleaseApi::class.java)
            val datasource = IReleaseRetrofitDatasource(service)
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

}