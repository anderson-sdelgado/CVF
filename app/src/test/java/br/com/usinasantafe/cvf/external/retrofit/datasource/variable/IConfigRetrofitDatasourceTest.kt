package br.com.usinasantafe.cvf.external.retrofit.datasource.variable

import br.com.usinasantafe.cvf.di.external.ApiModuleTest.provideRetrofitTest
import br.com.usinasantafe.cvf.external.retrofit.api.variable.ConfigApi
import br.com.usinasantafe.cvf.infra.models.retrofit.variable.ConfigRetrofitModelOutput
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Test
import kotlin.test.assertEquals

class IConfigRetrofitDatasourceTest {

    @Test
    fun `recoverToken - Check return failure if have failure Connection`() =
        runTest {
            val retrofitModelOutput = ConfigRetrofitModelOutput(
                number = 16997417840,
                version = "1.00",
            )
            val server = MockWebServer()
            server.start()
            server.enqueue(MockResponse().setBody("Failure Connection BD"))
            val retrofit = provideRetrofitTest(server.url("/").toString())
            val service = retrofit.create(ConfigApi::class.java)
            val dataSource = IConfigRetrofitDatasource(service)
            val result = dataSource.recoverToken(retrofitModelOutput)
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IConfigRetrofitDatasource.recoverToken",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "com.google.gson.stream.MalformedJsonException: Use JsonReader.setStrictness(Strictness.LENIENT) to accept malformed JSON at line 1 column 1 path \$\n" +
                        "See https://github.com/google/gson/blob/main/Troubleshooting.md#malformed-json",
                result.exceptionOrNull()!!.cause!!.toString()
            )
            server.shutdown()
        }

    @Test
    fun `recoverToken - Check return failure if sent data incorrect`() =
        runTest {
            val retrofitModelOutput = ConfigRetrofitModelOutput(
                number = 16997417840,
                version = "1.00",
            )
            val server = MockWebServer()
            server.start()
            server.enqueue(MockResponse().setBody("""{"idServ":1a}"""))
            val retrofit = provideRetrofitTest(server.url("/").toString())
            val service = retrofit.create(ConfigApi::class.java)
            val dataSource = IConfigRetrofitDatasource(service)
            val result = dataSource.recoverToken(retrofitModelOutput)
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IConfigRetrofitDatasource.recoverToken",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "com.google.gson.stream.MalformedJsonException: Use JsonReader.setStrictness(Strictness.LENIENT) to accept malformed JSON at line 1 column 11 path \$.idServ\n" +
                        "See https://github.com/google/gson/blob/main/Troubleshooting.md#malformed-json",
                result.exceptionOrNull()!!.cause!!.toString()
            )
            server.shutdown()
        }

    @Test
    fun `recoverToken - Check return failure if have error 404`() =
        runTest {
            val retrofitModelOutput = ConfigRetrofitModelOutput(
                number = 16997417840,
                version = "1.00",
            )
            val server = MockWebServer()
            server.start()
            server.enqueue(MockResponse().setResponseCode(404))
            val retrofit = provideRetrofitTest(server.url("/").toString())
            val service = retrofit.create(ConfigApi::class.java)
            val dataSource = IConfigRetrofitDatasource(service)
            val result = dataSource.recoverToken(retrofitModelOutput)
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IConfigRetrofitDatasource.recoverToken",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.NullPointerException",
                result.exceptionOrNull()!!.cause!!.toString()
            )
            server.shutdown()
        }

    @Test
    fun `recoverToken - Check return correct if function execute successfully`() =
        runTest {
            val retrofitModelOutput = ConfigRetrofitModelOutput(
                number = 16997417840,
                version = "1.00"
            )
            val server = MockWebServer()
            server.start()
            server.enqueue(MockResponse().setBody(result))
            val retrofit = provideRetrofitTest(server.url("/").toString())
            val service = retrofit.create(ConfigApi::class.java)
            val dataSource = IConfigRetrofitDatasource(service)
            val result = dataSource.recoverToken(retrofitModelOutput)
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

    private val result = """
        {
            "idServ": 16
        }
    """.trimIndent()

}