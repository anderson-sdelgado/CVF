package br.com.usinasantafe.cvf.domain.usecases.note

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import br.com.usinasantafe.cvf.domain.entities.stable.Equip
import br.com.usinasantafe.cvf.domain.entities.variable.Cart
import br.com.usinasantafe.cvf.domain.repositories.stable.ColabRepository
import br.com.usinasantafe.cvf.domain.repositories.stable.EquipRepository
import br.com.usinasantafe.cvf.domain.repositories.stable.FrontRepository
import br.com.usinasantafe.cvf.domain.repositories.stable.ReleaseRepository
import br.com.usinasantafe.cvf.domain.repositories.variable.ManagerRepository
import br.com.usinasantafe.cvf.domain.repositories.variable.NoteRepository
import br.com.usinasantafe.cvf.domain.usecases.manager.GetTitleMenu
import br.com.usinasantafe.cvf.lib.TypeEquip
import br.com.usinasantafe.cvf.presenter.model.CartScreenModel
import br.com.usinasantafe.cvf.presenter.model.ReviewScreenModel
import br.com.usinasantafe.cvf.utils.resultFailure
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.assertEquals

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class IGetDescReviewTest {

    private val getTitleMenu = mock<GetTitleMenu>()
    private val noteRepository = mock<NoteRepository>()
    private val colabRepository = mock<ColabRepository>()
    private val equipRepository = mock<EquipRepository>()
    private val managerRepository = mock<ManagerRepository>()
    private val releaseRepository = mock<ReleaseRepository>()
    private val frontRepository = mock<FrontRepository>()
    private lateinit var usecase: IGetDescReview


    @Before
    fun setup() {
        usecase = IGetDescReview(
            noteRepository = noteRepository,
            colabRepository = colabRepository,
            equipRepository = equipRepository,
            managerRepository = managerRepository,
            releaseRepository = releaseRepository,
            frontRepository = frontRepository,
        )
    }

    @Test
    fun `Check return failure if have error in IGetTitleMenu`() =
        runTest {
            whenever(
                getTitleMenu()
            ).thenReturn(
                flow { throw Exception("IGetTitleMenu") }
            )
            val result = usecase()
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IGetDescReview -> IGetTitleMenu",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `Check return failure if have error in NoteRepository getRegDriver`() =
        runTest {
            whenever(
                getTitleMenu()
            ).thenReturn(
                flowOf("FRENTE: 2\nLIBERAÇÃO: 123456\nO.S.: 456789\nPROPRIEDADE: RANCHO AZUL")
            )
            whenever(
                noteRepository.getRegDriver()
            ).thenReturn(
                resultFailure(
                    "INoteRepository.getRegDriver",
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
                "IGetDescReview -> INoteRepository.getRegDriver",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `Check return failure if NoteRepository getRegDriver is null`() =
        runTest {
            whenever(
                getTitleMenu()
            ).thenReturn(
                flowOf("FRENTE: 2\nLIBERAÇÃO: 123456\nO.S.: 456789\nPROPRIEDADE: RANCHO AZUL")
            )
            whenever(
                noteRepository.getRegDriver()
            ).thenReturn(
                Result.success(null)
            )
            val result = usecase()
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IGetDescReview -> regDriver is required",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.NullPointerException: regDriver is required",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `Check return failure if have error in ColabRepository getNameByReg`() =
        runTest {
            whenever(
                getTitleMenu()
            ).thenReturn(
                flowOf("FRENTE: 2\nLIBERAÇÃO: 123456\nO.S.: 456789\nPROPRIEDADE: RANCHO AZUL")
            )
            whenever(
                noteRepository.getRegDriver()
            ).thenReturn(
                Result.success(123456)
            )
            whenever(
                colabRepository.getNameByReg(123456)
            ).thenReturn(
                resultFailure(
                    "IColabRepository.getNameByReg",
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
                "IGetDescReview -> IColabRepository.getNameByReg",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `Check return failure if have error in NoteRepository getIdTruck`() =
        runTest {
            whenever(
                getTitleMenu()
            ).thenReturn(
                flowOf("FRENTE: 2\nLIBERAÇÃO: 123456\nO.S.: 456789\nPROPRIEDADE: RANCHO AZUL")
            )
            whenever(
                noteRepository.getRegDriver()
            ).thenReturn(
                Result.success(123456)
            )
            whenever(
                colabRepository.getNameByReg(123456)
            ).thenReturn(
                Result.success("Test Name Driver")
            )
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
                "IGetDescReview -> INoteRepository.getIdTruck",
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
                getTitleMenu()
            ).thenReturn(
                flowOf("FRENTE: 2\nLIBERAÇÃO: 123456\nO.S.: 456789\nPROPRIEDADE: RANCHO AZUL")
            )
            whenever(
                noteRepository.getRegDriver()
            ).thenReturn(
                Result.success(123456)
            )
            whenever(
                colabRepository.getNameByReg(123456)
            ).thenReturn(
                Result.success("Test Name Driver")
            )
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
                "IGetDescReview -> idTruck is required",
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
                getTitleMenu()
            ).thenReturn(
                flowOf("FRENTE: 2\nLIBERAÇÃO: 123456\nO.S.: 456789\nPROPRIEDADE: RANCHO AZUL")
            )
            whenever(
                noteRepository.getRegDriver()
            ).thenReturn(
                Result.success(123456)
            )
            whenever(
                colabRepository.getNameByReg(123456)
            ).thenReturn(
                Result.success("Test Name Driver")
            )
            whenever(
                noteRepository.getIdTruck()
            ).thenReturn(
                Result.success(100)
            )
            whenever(
                equipRepository.getById(100)
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
                "IGetDescReview -> IEquipRepository.getById",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `Check return failure if have error in NoteRepository cartList`() =
        runTest {
            whenever(
                getTitleMenu()
            ).thenReturn(
                flowOf("FRENTE: 2\nLIBERAÇÃO: 123456\nO.S.: 456789\nPROPRIEDADE: RANCHO AZUL")
            )
            whenever(
                noteRepository.getRegDriver()
            ).thenReturn(
                Result.success(123456)
            )
            whenever(
                colabRepository.getNameByReg(123456)
            ).thenReturn(
                Result.success("Test Name Driver")
            )
            whenever(
                noteRepository.getIdTruck()
            ).thenReturn(
                Result.success(100)
            )
            whenever(
                equipRepository.getById(100)
            ).thenReturn(
                Result.success(
                    Equip(
                        id = 100,
                        nro = 100,
                        idOperClass = 230,
                        type = TypeEquip.TRUCK
                    )
                )
            )
            whenever(
                noteRepository.cartList()
            ).thenReturn(
                resultFailure(
                    "INoteRepository.cartList",
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
                "IGetDescReview -> INoteRepository.cartList",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `Check return failure if have error in EquipRepository getById(cart)`() =
        runTest {
            whenever(
                getTitleMenu()
            ).thenReturn(
                flowOf("FRENTE: 2\nLIBERAÇÃO: 123456\nO.S.: 456789\nPROPRIEDADE: RANCHO AZUL")
            )
            whenever(
                noteRepository.getRegDriver()
            ).thenReturn(
                Result.success(123456)
            )
            whenever(
                colabRepository.getNameByReg(123456)
            ).thenReturn(
                Result.success("Test Name Driver")
            )
            whenever(
                noteRepository.getIdTruck()
            ).thenReturn(
                Result.success(100)
            )
            whenever(
                equipRepository.getById(100)
            ).thenReturn(
                Result.success(
                    Equip(
                        id = 100,
                        nro = 100,
                        idOperClass = 230,
                        type = TypeEquip.TRUCK
                    )
                )
            )
            whenever(
                noteRepository.cartList()
            ).thenReturn(
                Result.success(
                    listOf(
                        Cart(
                            idCart = 20,
                            position = 1,
                            nroCart = 200
                        ),
                        Cart(
                            idCart = 30,
                            position = 2,
                            nroCart = 300
                        )
                    )
                )
            )
            whenever(
                equipRepository.getById(20)
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
                "IGetDescReview -> IEquipRepository.getById",
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
                getTitleMenu()
            ).thenReturn(
                flowOf("FRENTE: 2\nLIBERAÇÃO: 123456\nO.S.: 456789\nPROPRIEDADE: RANCHO AZUL")
            )
            whenever(
                noteRepository.getRegDriver()
            ).thenReturn(
                Result.success(123456)
            )
            whenever(
                colabRepository.getNameByReg(123456)
            ).thenReturn(
                Result.success("Test Name Driver")
            )
            whenever(
                noteRepository.getIdTruck()
            ).thenReturn(
                Result.success(100)
            )
            whenever(
                equipRepository.getById(100)
            ).thenReturn(
                Result.success(
                    Equip(
                        id = 100,
                        nro = 100,
                        idOperClass = 230,
                        type = TypeEquip.TRUCK
                    )
                )
            )
            whenever(
                noteRepository.cartList()
            ).thenReturn(
                Result.success(
                    listOf(
                        Cart(
                            idCart = 20,
                            position = 1,
                            nroCart = 200
                        ),
                        Cart(
                            idCart = 30,
                            position = 2,
                            nroCart = 300
                        )
                    )
                )
            )
            whenever(
                equipRepository.getById(20)
            ).thenReturn(
                Result.success(
                    Equip(
                        id = 20,
                        nro = 200,
                        idOperClass = 230,
                        type = TypeEquip.CART
                    ),
                )
            )

            whenever(
                equipRepository.getById(30)
            ).thenReturn(
                Result.success(
                    Equip(
                        id = 30,
                        nro = 300,
                        idOperClass = 230,
                        type = TypeEquip.CART
                    ),
                )
            )
            val result = usecase()
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                ReviewScreenModel(
                    cdFront = "2",
                    idRelease = "123456",
                    nroOSRelease = "456789",
                    descPropAgrRelease = "RANCHO AZUL",
                    regDriver = "123456",
                    nameDriver = "Test Name Driver",
                    nroTruck = "100",
                    dateHourExitField = "",
                    cartList = listOf(
                        CartScreenModel(
                            position = "1",
                            nro = "200",
                        ),
                        CartScreenModel(
                            position = "2",
                            nro = "300",
                        )
                    )
                ),
                result.getOrNull()!!
            )
        }

}