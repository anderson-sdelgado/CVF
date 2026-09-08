package br.com.usinasantafe.cvf.domain.usecases.note

import br.com.usinasantafe.cvf.domain.repositories.stable.EquipRepository
import br.com.usinasantafe.cvf.lib.TypeTruck
import br.com.usinasantafe.cvf.utils.resultFailure
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

class ICheckInvertedCartTest {

    private val equipRepository = mock<EquipRepository>()
    private val usecase = ICheckInvertedCart(
        equipRepository = equipRepository
    )

    @Test
    fun `Check return failure if value of field is incorrect`() =
        runTest {
            val result = usecase("de25", 1, TypeTruck.TRUCK)
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "ICheckInvertedCart -> stringToInt",
                result.exceptionOrNull()!!.message,
            )
            assertEquals(
                "java.lang.NumberFormatException: For input string: \"de25\"",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `Check return failure if have error in EquipRepository getCdClassOperByNro`() =
        runTest {
            whenever(
                equipRepository.getCdClassOperByNro(100)
            ).thenReturn(
                resultFailure(
                    "IEquipRepository.getCdClassOperByNro",
                    "-",
                    Exception()
                )
            )
            val result = usecase("100", 1, TypeTruck.TRUCK)
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "ICheckInvertedCart -> IEquipRepository.getCdClassOperByNro",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `Check return true if function execute successfully and typeTruck is TRUCK and cdClassOper is 21`() =
        runTest {
            whenever(
                equipRepository.getCdClassOperByNro(100)
            ).thenReturn(
                Result.success(21)
            )
            val result = usecase("100", 1, TypeTruck.TRUCK)
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
    fun `Check return false if function execute successfully and typeTruck is TRUCK and cdClassOper is not 21`() =
        runTest {
            whenever(
                equipRepository.getCdClassOperByNro(100)
            ).thenReturn(
                Result.success(5)
            )
            val result = usecase("100", 1, TypeTruck.TRUCK)
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
    fun `Check return true if function execute successfully and typeTruck is HAULAGE_TRUCK and cdClassOper is 21 and pos is not 1`() =
        runTest {
            whenever(
                equipRepository.getCdClassOperByNro(100)
            ).thenReturn(
                Result.success(21)
            )
            val result = usecase("100", 2, TypeTruck.HAULAGE_TRUCK)
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
    fun `Check return false if function execute successfully and typeTruck is HAULAGE_TRUCK and cdClassOper is 21 and pos is 1`() =
        runTest {
            whenever(
                equipRepository.getCdClassOperByNro(100)
            ).thenReturn(
                Result.success(21)
            )
            val result = usecase("100", 1, TypeTruck.HAULAGE_TRUCK)
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
    fun `Check return true if function execute successfully and typeTruck is HAULAGE_TRUCK and cdClassOper is not 21 and pos is 1`() =
        runTest {
            whenever(
                equipRepository.getCdClassOperByNro(100)
            ).thenReturn(
                Result.success(5)
            )
            val result = usecase("100", 1, TypeTruck.HAULAGE_TRUCK)
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
    fun `Check return false if function execute successfully and typeTruck is HAULAGE_TRUCK and cdClassOper is not 21 and pos is not 1`() =
        runTest {
            whenever(
                equipRepository.getCdClassOperByNro(100)
            ).thenReturn(
                Result.success(5)
            )
            val result = usecase("100", 2, TypeTruck.HAULAGE_TRUCK)
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                false,
                result.getOrNull()!!
            )
        }

}