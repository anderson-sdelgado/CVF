package br.com.usinasantafe.cvf.domain.usecases.note

import br.com.usinasantafe.cvf.domain.entities.stable.Equip
import br.com.usinasantafe.cvf.domain.repositories.stable.EquipRepository
import br.com.usinasantafe.cvf.domain.repositories.variable.NoteRepository
import br.com.usinasantafe.cvf.lib.TypeEquip
import br.com.usinasantafe.cvf.lib.TypeTruck
import br.com.usinasantafe.cvf.utils.resultFailure
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.not
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

class IGetTypeTruckTest {

    private val noteRepository = mock<NoteRepository>()
    private val equipRepository = mock<EquipRepository>()
    private val usecase = IGetTypeTruck(
        noteRepository = noteRepository,
        equipRepository = equipRepository
    )

    @Test
    fun `Check return failure if have error in NoteRepository getIdTruck`() =
        runTest {
            whenever(
                noteRepository.getIdTruck()
            ).thenReturn(
                resultFailure(
                    "INoteRepository.getIdTruck",
                    "-",
                    Exception()
                )
            )
            val result = usecase()
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IGetTypeTruck -> INoteRepository.getIdTruck",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `Check return failure if NoteRepository getIdTruck is null`() =
        runTest {
            whenever(
                noteRepository.getIdTruck()
            ).thenReturn(
                Result.success(null)
            )
            val result = usecase()
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IGetTypeTruck -> idTruck is required",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.NullPointerException: idTruck is required",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `Check return failure if have error in EquipRepository getById`() =
        runTest {
            whenever(
                noteRepository.getIdTruck()
            ).thenReturn(
                Result.success(10)
            )
            whenever(
                equipRepository.getById(10)
            ).thenReturn(
                resultFailure(
                    "IEquipRepository.getById",
                    "-",
                    Exception()
                )
            )
            val result = usecase()
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IGetTypeTruck -> IEquipRepository.getById",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `Check return TypeTruck TRUCK if function execute successfully and cdOperClass is 1`() =
        runTest {
            whenever(
                noteRepository.getIdTruck()
            ).thenReturn(
                Result.success(10)
            )
            whenever(
                equipRepository.getById(10)
            ).thenReturn(
                Result.success(
                    Equip(
                        id = 10,
                        nro = 251,
                        cdOperClass = 1,
                        descOperClass = "Test",
                        type = TypeEquip.TRUCK
                    )
                )
            )
            val result = usecase()
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                TypeTruck.TRUCK,
                result.getOrNull()!!
            )
        }

    @Test
    fun `Check return TypeTruck HAULAGE_TRUCK if function execute successfully and cdOperClass is 8`() =
        runTest {
            whenever(
                noteRepository.getIdTruck()
            ).thenReturn(
                Result.success(10)
            )
            whenever(
                equipRepository.getById(10)
            ).thenReturn(
                Result.success(
                    Equip(
                        id = 10,
                        nro = 251,
                        cdOperClass = 8,
                        descOperClass = "Test2",
                        type = TypeEquip.TRUCK
                    )
                )
            )
            val result = usecase()
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                TypeTruck.HAULAGE_TRUCK,
                result.getOrNull()!!
            )
        }


}