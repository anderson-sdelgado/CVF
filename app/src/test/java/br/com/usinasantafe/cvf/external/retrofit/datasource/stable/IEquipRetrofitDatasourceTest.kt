package br.com.usinasantafe.cvf.external.retrofit.datasource.stable

import br.com.usinasantafe.cvf.di.external.ApiModuleTest.provideRetrofitTest
import br.com.usinasantafe.cvf.external.retrofit.api.stable.EquipApi
import br.com.usinasantafe.cvf.infra.models.retrofit.stable.EquipRetrofitModel
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Test
import kotlin.test.assertEquals

class IEquipRetrofitDatasourceTest {

    @Test
    fun `listAll - Check return failure if token is invalid`() =
        runTest {
            val server = MockWebServer()
            server.start()
            server.enqueue(
                MockResponse().setBody(resultFailureAuthorization)
            )
            val retrofit = provideRetrofitTest(
                server.url("/").toString()
            )
            val service = retrofit.create(EquipApi::class.java)
            val datasource = IEquipRetrofitDatasource(service, service)
            val result = datasource.listAll("TOKEN")
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IEquipRetrofitDatasource.listAll",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception: Authorization header is missing",
                result.exceptionOrNull()!!.cause.toString()
            )
            server.shutdown()
        }

    @Test
    fun `listAll - Check return failure if have Error 404`() =
        runTest {
            val server = MockWebServer()
            server.start()
            server.enqueue(
                MockResponse().setResponseCode(404)
            )
            val retrofit = provideRetrofitTest(
                server.url("/").toString()
            )
            val service = retrofit.create(EquipApi::class.java)
            val datasource = IEquipRetrofitDatasource(service, service)
            val result = datasource.listAll("TOKEN")

            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IEquipRetrofitDatasource.listAll",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.NullPointerException",
                result.exceptionOrNull()!!.cause.toString()
            )
            server.shutdown()
        }

    @Test
    fun `listAll - Check return correct`() =
        runTest {
            val server = MockWebServer()
            server.start()
            server.enqueue(
                MockResponse().setBody(resultSuccessList)
            )
            val retrofit = provideRetrofitTest(
                server.url("").toString()
            )
            val service = retrofit.create(EquipApi::class.java)
            val datasource = IEquipRetrofitDatasource( service, service)
            val result = datasource.listAll("TOKEN")

            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                Result.success(
                    listOf(
                        EquipRetrofitModel(
                            id = 1,
                            nro = 1,
                            cdOperClass = 1,
                            descOperClass = "Equip1",
                            type = 1
                        ),
                        EquipRetrofitModel(
                            id = 2,
                            nro = 2,
                            cdOperClass = 2,
                            descOperClass = "Equip2",
                            type = 1
                        )
                    )
                ),
                result
            )
            server.shutdown()
        }

    @Test
    fun `check - Check return failure if token is invalid`() =
        runTest {
            val server = MockWebServer()
            server.start()
            server.enqueue(
                MockResponse().setBody(resultFailureAuthorization)
            )
            val retrofit = provideRetrofitTest(
                server.url("/").toString()
            )
            val service = retrofit.create(EquipApi::class.java)
            val datasource = IEquipRetrofitDatasource(service, service)
            val result = datasource.checkByNro("TOKEN", 1)
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IEquipRetrofitDatasource.checkByNro",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception: Authorization header is missing",
                result.exceptionOrNull()!!.cause.toString()
            )
            server.shutdown()
        }

    @Test
    fun `check - Check return failure if have Error 404`() =
        runTest {
            val server = MockWebServer()
            server.start()
            server.enqueue(
                MockResponse().setResponseCode(404)
            )
            val retrofit = provideRetrofitTest(
                server.url("/").toString()
            )
            val service = retrofit.create(EquipApi::class.java)
            val datasource = IEquipRetrofitDatasource(service, service)
            val result = datasource.checkByNro("TOKEN", 1)
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IEquipRetrofitDatasource.checkByNro",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.NullPointerException",
                result.exceptionOrNull()!!.cause.toString()
            )
            server.shutdown()
        }

    @Test
    fun `check - Check return correct`() =
        runTest {
            val server = MockWebServer()
            server.start()
            server.enqueue(
                MockResponse().setBody(resultSuccess)
            )
            val retrofit = provideRetrofitTest(
                server.url("/").toString()
            )
            val service = retrofit.create(EquipApi::class.java)
            val datasource = IEquipRetrofitDatasource(service, service)
            val result = datasource.checkByNro("TOKEN", 1)
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                EquipRetrofitModel(
                    id = 1,
                    nro = 1,
                    cdOperClass = 1,
                    descOperClass = "Equip1",
                    type = 1
                ),
                result.getOrNull()!!
            )
            server.shutdown()
        }

    private val resultFailureAuthorization = """
        {
            "status": "error",
            "failure": "Authorization header is missing"
        }
    """.trimIndent()

    private val resultSuccessList = """
        {
            "status": "success",
            "data": 
                [
                  {"id":1,"nro":1,"cdOperClass":1,"descOperClass":"Equip1","type":1},
                  {"id":2,"nro":2,"cdOperClass":2,"descOperClass":"Equip2","type":1}
                ]
        }
    """.trimIndent()

    private val resultSuccess = """
        {
            "status": "success",
            "data": {"id":1,"nro":1,"cdOperClass":1,"descOperClass":"Equip1","type":1}
        }
    """.trimIndent()

}