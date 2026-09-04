package br.com.usinasantafe.cvf.domain.usecases.note

import br.com.usinasantafe.cvf.domain.entities.stable.Equip
import br.com.usinasantafe.cvf.domain.repositories.stable.EquipRepository
import br.com.usinasantafe.cvf.domain.repositories.variable.NoteRepository
import br.com.usinasantafe.cvf.lib.TypeEquip
import br.com.usinasantafe.cvf.utils.resultFailure
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

class IGetNroTruckTest {

    private val noteRepository = mock<NoteRepository>()
    private val equipRepository = mock<EquipRepository>()
    private val usecase = IGetNroTruck(
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
                "IGetNroTruck -> INoteRepository.getIdTruck",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `Check return null if NoteRepository getIdTruck return null`() =
        runTest {
            whenever(
                noteRepository.getIdTruck()
            ).thenReturn(
                Result.success(null)
            )
            val result = usecase()
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
                "IGetNroTruck -> IEquipRepository.getById",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `Check return correct if function execute successfully`() =
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
                        cdOperClass = 200,
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
                "251",
                result.getOrNull()!!
            )
        }

}