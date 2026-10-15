package br.com.usinasantafe.cvf.domain.usecases.note

import br.com.usinasantafe.cvf.domain.entities.stable.Equip
import br.com.usinasantafe.cvf.domain.repositories.stable.EquipRepository
import br.com.usinasantafe.cvf.domain.repositories.stable.FrontRepository
import br.com.usinasantafe.cvf.domain.repositories.stable.ReleaseRepository
import br.com.usinasantafe.cvf.domain.repositories.variable.ManagerRepository
import br.com.usinasantafe.cvf.domain.repositories.variable.NoteRepository
import br.com.usinasantafe.cvf.domain.usecases.common.Token
import br.com.usinasantafe.cvf.lib.CheckTruck
import br.com.usinasantafe.cvf.lib.TypeEquip
import br.com.usinasantafe.cvf.utils.CheckNetwork
import br.com.usinasantafe.cvf.utils.resultFailure
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import java.net.SocketTimeoutException
import java.util.Date
import kotlin.test.assertEquals

class ICheckNroTruckTest {

    private val token = mock<Token>()
    private val checkNetwork = mock<CheckNetwork>()
    private val equipRepository = mock<EquipRepository>()
    private val managerRepository = mock<ManagerRepository>()
    private val noteRepository = mock<NoteRepository>()
    private val usecase = ICheckNroTruck(
        token = token,
        checkNetwork = checkNetwork,
        equipRepository = equipRepository,
        managerRepository = managerRepository,
        noteRepository = noteRepository
    )

    @Test
    fun `Check return failure if value of field is incorrect`() =
        runTest {
            val result = usecase("de25")
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "ICheckNroTruck -> stringToInt",
                result.exceptionOrNull()!!.message,
            )
            assertEquals(
                "java.lang.NumberFormatException: For input string: \"de25\"",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `Check return failure if have error in EquipRepository check`() =
        runTest {
            whenever(
                checkNetwork.isConnected()
            ).thenReturn(
                false
            )
            whenever(
                equipRepository.checkNroTruck(100)
            ).thenReturn(
                resultFailure(
                    "IEquipRepository.check",
                    "-",
                    Exception()
                )
            )
            val result = usecase("100")
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "ICheckNroTruck -> IEquipRepository.check",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `Check return correct if no connection and function execute successfully`() =
        runTest {
            whenever(
                checkNetwork.isConnected()
            ).thenReturn(
                false
            )
            whenever(
                equipRepository.checkNroTruck(100)
            ).thenReturn(
                Result.success(false)
            )
            val result = usecase("100")
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                CheckTruck.INVALID,
                result.getOrNull()!!
            )
        }

    @Test
    fun `Check return failure if have connection and have error in token`() =
        runTest {
            whenever(
                checkNetwork.isConnected()
            ).thenReturn(
                true
            )
            whenever(
                token()
            ).thenReturn(
                resultFailure(
                    "IToken",
                    "-",
                    Exception()
                )
            )
            val result = usecase("100")
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "ICheckNroTruck -> IToken",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `Check return failure if have error in ManagerRepository getIdFront`() =
        runTest {
            whenever(
                checkNetwork.isConnected()
            ).thenReturn(
                true
            )
            whenever(
                token()
            ).thenReturn(
                Result.success("token")
            )
            whenever(
                managerRepository.getIdFront()
            ).thenReturn(
                resultFailure(
                    "IManagerRepository.getIdFront",
                    "-",
                    Exception()
                )
            )
            val result = usecase("100")
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "ICheckNroTruck -> IManagerRepository.getIdFront",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `Check return failure if ManagerRepository getIdFront is null`() =
        runTest {
            whenever(
                checkNetwork.isConnected()
            ).thenReturn(
                true
            )
            whenever(
                token()
            ).thenReturn(
                Result.success("token")
            )
            whenever(
                managerRepository.getIdFront()
            ).thenReturn(
                Result.success(null)
            )
            val result = usecase("100")
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "ICheckNroTruck -> idFront is required",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.NullPointerException: idFront is required",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `Check return failure if connection and have error in EquipRepository check(Retrofit)`() =
        runTest {
            whenever(
                checkNetwork.isConnected()
            ).thenReturn(
                true
            )
            whenever(
                token()
            ).thenReturn(
                Result.success("token")
            )
            whenever(
                managerRepository.getIdFront()
            ).thenReturn(
                Result.success(1)
            )
            whenever(
                equipRepository.checkNroTruck("token", 100, 1)
            ).thenReturn(
                resultFailure(
                    "IEquipRepository.check(Retrofit)",
                    "-",
                    Exception()
                )
            )
            val result = usecase("100")
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "ICheckNroTruck -> IEquipRepository.check(Retrofit)",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `Check return failure if connection and have connection error in EquipRepository check(Retrofit) and have error in EquipRepository check(Room)`() =
        runTest {
            whenever(
                checkNetwork.isConnected()
            ).thenReturn(
                true
            )
            whenever(
                token()
            ).thenReturn(
                Result.success("token")
            )
            whenever(
                managerRepository.getIdFront()
            ).thenReturn(
                Result.success(1)
            )
            whenever(
                equipRepository.checkNroTruck("token", 100, 1)
            ).thenReturn(
                resultFailure(
                    "IEquipRepository.check(Retrofit)",
                    "-",
                    SocketTimeoutException()
                )
            )
            whenever(
                equipRepository.checkNroTruck(100)
            ).thenReturn(
                resultFailure(
                    "IEquipRepository.check(Room)",
                    "-",
                    Exception()
                )
            )
            val result = usecase("100")
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "ICheckNroTruck -> IEquipRepository.check(Room)",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `Check return CheckTruck INVALID if connection and have connection error in ColabRepository check(Retrofit) and ColabRepository check(Room) execute successfully`() =
        runTest {
            whenever(
                checkNetwork.isConnected()
            ).thenReturn(
                true
            )
            whenever(
                token()
            ).thenReturn(
                Result.success("token")
            )
            whenever(
                managerRepository.getIdFront()
            ).thenReturn(
                Result.success(1)
            )
            whenever(
                equipRepository.checkNroTruck("token", 100, 1)
            ).thenReturn(
                resultFailure(
                    "IEquipRepository.check(Retrofit)",
                    "-",
                    SocketTimeoutException()
                )
            )
            whenever(
                equipRepository.checkNroTruck(100)
            ).thenReturn(
                Result.success(false)
            )
            val result = usecase("100")
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                CheckTruck.INVALID,
                result.getOrNull()!!
            )
        }

    @Test
    fun `Check return CheckTruck INVALID if id is 0`() =
        runTest {
            whenever(
                checkNetwork.isConnected()
            ).thenReturn(
                true
            )
            whenever(
                token()
            ).thenReturn(
                Result.success("token")
            )
            whenever(
                managerRepository.getIdFront()
            ).thenReturn(
                Result.success(1)
            )
            whenever(
                equipRepository.checkNroTruck("token", 100, 1)
            ).thenReturn(
                Result.success(
                    Equip(
                        id = 0,
                        nro = 0,
                        idOperClass = 1,
                        type = TypeEquip.TRUCK
                    )
                )
            )
            val result = usecase("100")
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                CheckTruck.INVALID,
                result.getOrNull()!!
            )
        }

    @Test
    fun `Check return failure if flagPreCEC is null`() =
        runTest {
            whenever(
                checkNetwork.isConnected()
            ).thenReturn(
                true
            )
            whenever(
                token()
            ).thenReturn(
                Result.success("token")
            )
            whenever(
                managerRepository.getIdFront()
            ).thenReturn(
                Result.success(1)
            )
            whenever(
                equipRepository.checkNroTruck("token", 100, 1)
            ).thenReturn(
                Result.success(
                    Equip(
                        id = 1,
                        nro = 200,
                        idOperClass = 1,
                        type = TypeEquip.TRUCK
                    )
                )
            )
            val result = usecase("100")
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "ICheckNroTruck -> flagPreCEC is required",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.NullPointerException: flagPreCEC is required",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `Check return CheckTruck PRE_CEC_EXISTENT if flagPreCEC is true`() =
        runTest {
            whenever(
                checkNetwork.isConnected()
            ).thenReturn(
                true
            )
            whenever(
                token()
            ).thenReturn(
                Result.success("token")
            )
            whenever(
                managerRepository.getIdFront()
            ).thenReturn(
                Result.success(1)
            )
            whenever(
                equipRepository.checkNroTruck("token", 100, 1)
            ).thenReturn(
                Result.success(
                    Equip(
                        id = 1,
                        nro = 200,
                        idOperClass = 1,
                        type = TypeEquip.TRUCK,
                        flagPreCEC = true
                    )
                )
            )
            val result = usecase("100")
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                CheckTruck.PRE_CEC_EXISTENT,
                result.getOrNull()!!
            )
        }

        @Test
        fun `Check return CheckTruck RELEASE_AND_FRONT_INVALID if entity idFront is null and idRelease is null`() =
            runTest {
                whenever(
                    checkNetwork.isConnected()
                ).thenReturn(
                    true
                )
                whenever(
                    token()
                ).thenReturn(
                    Result.success("token")
                )
                whenever(
                    managerRepository.getIdFront()
                ).thenReturn(
                    Result.success(1)
                )
                whenever(
                    equipRepository.checkNroTruck("token", 100, 1)
                ).thenReturn(
                    Result.success(
                        Equip(
                            id = 1,
                            nro = 200,
                            idOperClass = 1,
                            type = TypeEquip.TRUCK,
                            flagPreCEC = false
                        )
                    )
                )
                val result = usecase("100")
                assertEquals(
                    true,
                    result.isSuccess
                )
                assertEquals(
                    CheckTruck.RELEASE_AND_FRONT_INVALID,
                    result.getOrNull()!!
                )
            }

    @Test
    fun `Check return CheckTruck RELEASE_AND_FRONT_INVALID if entity idFront is not null and idRelease is null`() =
        runTest {
            whenever(
                checkNetwork.isConnected()
            ).thenReturn(
                true
            )
            whenever(
                token()
            ).thenReturn(
                Result.success("token")
            )
            whenever(
                managerRepository.getIdFront()
            ).thenReturn(
                Result.success(1)
            )
            whenever(
                equipRepository.checkNroTruck("token", 100, 1)
            ).thenReturn(
                Result.success(
                    Equip(
                        id = 1,
                        nro = 200,
                        idOperClass = 1,
                        type = TypeEquip.TRUCK,
                        flagPreCEC = false,
                        idFront = 1
                    )
                )
            )
            val result = usecase("100")
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                CheckTruck.RELEASE_AND_FRONT_INVALID,
                result.getOrNull()!!
            )
        }

    @Test
    fun `Check return CheckTruck RELEASE_AND_FRONT_INVALID if entity idFront is null and idRelease is not null`() =
        runTest {
            whenever(
                checkNetwork.isConnected()
            ).thenReturn(
                true
            )
            whenever(
                token()
            ).thenReturn(
                Result.success("token")
            )
            whenever(
                managerRepository.getIdFront()
            ).thenReturn(
                Result.success(1)
            )
            whenever(
                equipRepository.checkNroTruck("token", 100, 1)
            ).thenReturn(
                Result.success(
                    Equip(
                        id = 1,
                        nro = 200,
                        idOperClass = 1,
                        type = TypeEquip.TRUCK,
                        flagPreCEC = false,
                        idRelease = 1
                    )
                )
            )
            val result = usecase("100")
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                CheckTruck.RELEASE_AND_FRONT_INVALID,
                result.getOrNull()!!
            )
        }

    @Test
    fun `Check return failure if have error in ManagerRepository getIdRelease`() =
        runTest {
            whenever(
                checkNetwork.isConnected()
            ).thenReturn(
                true
            )
            whenever(
                token()
            ).thenReturn(
                Result.success("token")
            )
            whenever(
                managerRepository.getIdFront()
            ).thenReturn(
                Result.success(1)
            )
            whenever(
                equipRepository.checkNroTruck("token", 100, 1)
            ).thenReturn(
                Result.success(
                    Equip(
                        id = 1,
                        nro = 200,
                        idOperClass = 1,
                        type = TypeEquip.TRUCK,
                        flagPreCEC = false,
                        idRelease = 1,
                        idFront = 1
                    )
                )
            )
            whenever(
                managerRepository.getIdRelease()
            ).thenReturn(
                resultFailure(
                    "IManagerRepository.getIdRelease",
                    "-",
                    Exception()
                )
            )
            val result = usecase("100")
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "ICheckNroTruck -> IManagerRepository.getIdRelease",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `Check return failure if ManagerRepository getIdRelease is null`() =
        runTest {
            whenever(
                checkNetwork.isConnected()
            ).thenReturn(
                true
            )
            whenever(
                token()
            ).thenReturn(
                Result.success("token")
            )
            whenever(
                managerRepository.getIdFront()
            ).thenReturn(
                Result.success(1)
            )
            whenever(
                equipRepository.checkNroTruck("token", 100, 1)
            ).thenReturn(
                Result.success(
                    Equip(
                        id = 1,
                        nro = 200,
                        idOperClass = 1,
                        type = TypeEquip.TRUCK,
                        flagPreCEC = false,
                        idRelease = 1,
                        idFront = 1
                    )
                )
            )
            whenever(
                managerRepository.getIdRelease()
            ).thenReturn(
                Result.success(null)
            )
            val result = usecase("100")
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "ICheckNroTruck -> idRelease is required",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.NullPointerException: idRelease is required",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `Check return CheckTruck RELEASE_AND_FRONT_INVALID if entity idFront is different from idFront and entity idRelease is different from idRelease`() =
        runTest {
            whenever(
                checkNetwork.isConnected()
            ).thenReturn(
                true
            )
            whenever(
                token()
            ).thenReturn(
                Result.success("token")
            )
            whenever(
                managerRepository.getIdFront()
            ).thenReturn(
                Result.success(1)
            )
            whenever(
                equipRepository.checkNroTruck("token", 100, 1)
            ).thenReturn(
                Result.success(
                    Equip(
                        id = 1,
                        nro = 200,
                        idOperClass = 1,
                        type = TypeEquip.TRUCK,
                        flagPreCEC = false,
                        idRelease = 2,
                        idFront = 2
                    )
                )
            )
            whenever(
                managerRepository.getIdRelease()
            ).thenReturn(
                Result.success(1)
            )
            val result = usecase("100")
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                CheckTruck.RELEASE_AND_FRONT_INVALID,
                result.getOrNull()!!
            )
        }

    @Test
    fun `Check return CheckTruck RELEASE_AND_FRONT_INVALID if entity idFront is equal from idFront and entity idRelease is different from idRelease`() =
        runTest {
            whenever(
                checkNetwork.isConnected()
            ).thenReturn(
                true
            )
            whenever(
                token()
            ).thenReturn(
                Result.success("token")
            )
            whenever(
                managerRepository.getIdFront()
            ).thenReturn(
                Result.success(1)
            )
            whenever(
                equipRepository.checkNroTruck("token", 100, 1)
            ).thenReturn(
                Result.success(
                    Equip(
                        id = 1,
                        nro = 200,
                        idOperClass = 1,
                        type = TypeEquip.TRUCK,
                        flagPreCEC = false,
                        idRelease = 2,
                        idFront = 1
                    )
                )
            )
            whenever(
                managerRepository.getIdRelease()
            ).thenReturn(
                Result.success(1)
            )
            val result = usecase("100")
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                CheckTruck.RELEASE_AND_FRONT_INVALID,
                result.getOrNull()!!
            )
        }

    @Test
    fun `Check return CheckTruck RELEASE_AND_FRONT_INVALID if entity idFront is different from idFront and entity idRelease is equal from idRelease`() =
        runTest {
            whenever(
                checkNetwork.isConnected()
            ).thenReturn(
                true
            )
            whenever(
                token()
            ).thenReturn(
                Result.success("token")
            )
            whenever(
                managerRepository.getIdFront()
            ).thenReturn(
                Result.success(1)
            )
            whenever(
                equipRepository.checkNroTruck("token", 100, 1)
            ).thenReturn(
                Result.success(
                    Equip(
                        id = 1,
                        nro = 200,
                        idOperClass = 1,
                        type = TypeEquip.TRUCK,
                        flagPreCEC = false,
                        idRelease = 1,
                        idFront = 2
                    )
                )
            )
            whenever(
                managerRepository.getIdRelease()
            ).thenReturn(
                Result.success(1)
            )
            val result = usecase("100")
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                CheckTruck.RELEASE_AND_FRONT_INVALID,
                result.getOrNull()!!
            )
        }

    @Test
    fun `Check return failure if dateHourExitMill is null`() =
        runTest {
            whenever(
                checkNetwork.isConnected()
            ).thenReturn(
                true
            )
            whenever(
                token()
            ).thenReturn(
                Result.success("token")
            )
            whenever(
                managerRepository.getIdFront()
            ).thenReturn(
                Result.success(1)
            )
            whenever(
                equipRepository.checkNroTruck("token", 100, 1)
            ).thenReturn(
                Result.success(
                    Equip(
                        id = 1,
                        nro = 200,
                        idOperClass = 1,
                        type = TypeEquip.TRUCK,
                        flagPreCEC = false,
                        idRelease = 1,
                        idFront = 1
                    )
                )
            )
            whenever(
                managerRepository.getIdRelease()
            ).thenReturn(
                Result.success(1)
            )
            val result = usecase("100")
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "ICheckNroTruck -> dateHourExitMill is required",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.NullPointerException: dateHourExitMill is required",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `Check return failure if dateHourArrivalField is null`() =
        runTest {
            whenever(
                checkNetwork.isConnected()
            ).thenReturn(
                true
            )
            whenever(
                token()
            ).thenReturn(
                Result.success("token")
            )
            whenever(
                managerRepository.getIdFront()
            ).thenReturn(
                Result.success(1)
            )
            whenever(
                equipRepository.checkNroTruck("token", 100, 1)
            ).thenReturn(
                Result.success(
                    Equip(
                        id = 1,
                        nro = 200,
                        idOperClass = 1,
                        type = TypeEquip.TRUCK,
                        flagPreCEC = false,
                        idRelease = 1,
                        idFront = 1,
                        dateHourExitMill = Date(1792093294000)
                    )
                )
            )
            whenever(
                managerRepository.getIdRelease()
            ).thenReturn(
                Result.success(1)
            )
            val result = usecase("100")
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "ICheckNroTruck -> dateHourArrivalField is required",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.NullPointerException: dateHourArrivalField is required",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `Check return failure if have error in NoteRepository setDate`() =
        runTest {
            whenever(
                checkNetwork.isConnected()
            ).thenReturn(
                true
            )
            whenever(
                token()
            ).thenReturn(
                Result.success("token")
            )
            whenever(
                managerRepository.getIdFront()
            ).thenReturn(
                Result.success(1)
            )
            whenever(
                equipRepository.checkNroTruck("token", 100, 1)
            ).thenReturn(
                Result.success(
                    Equip(
                        id = 1,
                        nro = 200,
                        idOperClass = 1,
                        type = TypeEquip.TRUCK,
                        flagPreCEC = false,
                        idRelease = 1,
                        idFront = 1,
                        dateHourExitMill = Date(1792093294000),
                        dateHourArrivalField = Date(1792093294000)
                    )
                )
            )
            whenever(
                managerRepository.getIdRelease()
            ).thenReturn(
                Result.success(1)
            )
            whenever(
                noteRepository.setDate(Date(1792093294000), Date(1792093294000))
            ).thenReturn(
                resultFailure(
                    "INoteRepository.setDate",
                    "-",
                    Exception()
                )
            )
            val result = usecase("100")
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "ICheckNroTruck -> INoteRepository.setDate",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }




}
