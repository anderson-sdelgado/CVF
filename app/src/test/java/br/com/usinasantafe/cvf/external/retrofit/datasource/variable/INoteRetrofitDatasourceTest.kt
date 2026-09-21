package br.com.usinasantafe.cvf.external.retrofit.datasource.variable

import br.com.usinasantafe.cvf.di.external.ApiModuleTest.provideRetrofitTest
import br.com.usinasantafe.cvf.external.retrofit.api.variable.NoteApi
import br.com.usinasantafe.cvf.infra.models.retrofit.variable.CartRetrofitModelOutput
import br.com.usinasantafe.cvf.infra.models.retrofit.variable.HeaderRetrofitModelOutput
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Test
import java.util.Date
import kotlin.test.assertEquals

class INoteRetrofitDatasourceTest {

    private val data = listOf(
        HeaderRetrofitModelOutput(
            id = 1,
            regDriver = 19759,
            idTruck = 100,
            idConfigServ = 4,
            idFront = 1,
            idRelease = 2,
            dateHour = "",
            cartList = listOf(
                CartRetrofitModelOutput(
                    id = 1,
                    position = 1,
                    idCart = 10
                )
            )
        )
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
            val service = retrofit.create(NoteApi::class.java)
            val datasource = INoteRetrofitDatasource(service)
            val result = datasource.send("TOKEN", data)
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "INoteRetrofitDatasource.send",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception: Authorization header is missing",
                result.exceptionOrNull()!!.cause.toString()
            )
            server.shutdown()
        }

    @Test
    fun `send - Check return failure if web service return data incorrect`() =
        runTest {
            val server = MockWebServer()
            server.start()
            server.enqueue(MockResponse().setBody(resultFailureInfoIncorrect))
            val retrofit = provideRetrofitTest(server.url("/").toString())
            val service = retrofit.create(NoteApi::class.java)
            val datasource = INoteRetrofitDatasource(service)
            val result = datasource.send("TOKEN", data)
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "INoteRetrofitDatasource.send",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "com.google.gson.stream.MalformedJsonException: Use JsonReader.setStrictness(Strictness.LENIENT) to accept malformed JSON at line 3 column 13 path \$.data\n" +
                        "See https://github.com/google/gson/blob/main/Troubleshooting.md#malformed-json",
                result.exceptionOrNull()!!.cause!!.toString()
            )
            server.shutdown()
        }

    @Test
    fun `send - Check return failure if web service return Error 404`() =
        runTest {
            val server = MockWebServer()
            server.start()
            server.enqueue(
                MockResponse().setResponseCode(404)
            )
            val retrofit = provideRetrofitTest(
                server.url("/").toString()
            )
            val service = retrofit.create(NoteApi::class.java)
            val datasource = INoteRetrofitDatasource(service)
            val result = datasource.send("TOKEN", data)
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "INoteRetrofitDatasource.send",
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
            server.enqueue(MockResponse().setBody(resultSuccess))
            val retrofit = provideRetrofitTest(server.url("/").toString())
            val service = retrofit.create(NoteApi::class.java)
            val datasource = INoteRetrofitDatasource(service)
            val result = datasource.send("TOKEN", this@INoteRetrofitDatasourceTest.data)
            assertEquals(
                true,
                result.isSuccess
            )
            val data = result.getOrNull()!!
            assertEquals(
                2,
                data.size
            )
            val model1 = data[0]
            assertEquals(
                16,
                model1.idServ
            )
            assertEquals(
                1,
                model1.id
            )
            val model2 = data[1]
            assertEquals(
                20,
                model2.idServ
            )
            assertEquals(
                2,
                model2.id
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
            "data": s4dq
        }
    """.trimIndent()

    private val resultSuccess = """
        {
            "status": "success",
            "data": [
                {"idServ": 16, "id": 1},
                {"idServ": 20, "id": 2}
            ]
        }
    """.trimIndent()

}