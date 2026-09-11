package br.com.usinasantafe.cvf.infra.repositories.variable

import br.com.usinasantafe.cvf.domain.entities.variable.Cart
import br.com.usinasantafe.cvf.infra.datasource.retrofit.variable.NoteRetrofitDatasource
import br.com.usinasantafe.cvf.infra.datasource.room.variable.CartRoomDatasource
import br.com.usinasantafe.cvf.infra.datasource.room.variable.HeaderRoomDatasource
import br.com.usinasantafe.cvf.infra.datasource.sharedpreferences.HeaderSharedPreferencesDatasource
import br.com.usinasantafe.cvf.infra.datasource.sharedpreferences.CartSharedPreferencesDatasource
import br.com.usinasantafe.cvf.infra.models.retrofit.variable.CartRetrofitModelOutput
import br.com.usinasantafe.cvf.infra.models.retrofit.variable.HeaderRetrofitModelInput
import br.com.usinasantafe.cvf.infra.models.retrofit.variable.HeaderRetrofitModelOutput
import br.com.usinasantafe.cvf.infra.models.room.variable.CartRoomModel
import br.com.usinasantafe.cvf.infra.models.room.variable.HeaderRoomModel
import br.com.usinasantafe.cvf.infra.models.sharedpreferences.CartSharedPreferencesModel
import br.com.usinasantafe.cvf.infra.models.sharedpreferences.HeaderSharedPreferencesModel
import br.com.usinasantafe.cvf.lib.StatusSend
import br.com.usinasantafe.cvf.utils.resultFailure
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.argThat
import org.mockito.kotlin.atLeastOnce
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.util.Date
import kotlin.test.assertEquals

class INoteRepositoryTest {

    private val headerSharedPreferencesDatasource = mock<HeaderSharedPreferencesDatasource>()
    private val cartSharedPreferencesDatasource = mock<CartSharedPreferencesDatasource>()
    private val headerRoomDatasource = mock<HeaderRoomDatasource>()
    private val cartRoomDatasource = mock<CartRoomDatasource>()
    private val noteRetrofitDatasource = mock<NoteRetrofitDatasource>()
    private val repository = INoteRepository(
        headerSharedPreferencesDatasource = headerSharedPreferencesDatasource,
        cartSharedPreferencesDatasource = cartSharedPreferencesDatasource,
        headerRoomDatasource = headerRoomDatasource,
        cartRoomDatasource = cartRoomDatasource,
        noteRetrofitDatasource = noteRetrofitDatasource
    )

    private val headerRoomModelList =
        listOf(
            HeaderRoomModel(
                id = 10,
                regDriver = 19759,
                idTruck = 10
            ),
            HeaderRoomModel(
                id = 11,
                regDriver = 18017,
                idTruck = 100
            )
        )

    private val cartRoomModelList1 =
        listOf(
            CartRoomModel(
                id = 1,
                idHeader = 10,
                position = 1,
                idCart = 1
            ),
            CartRoomModel(
                id = 2,
                idHeader = 10,
                position = 2,
                idCart = 2
            )
        )

    private val cartRoomModelList2 =
        listOf(
            CartRoomModel(
                id = 3,
                idHeader = 11,
                position = 1,
                idCart = 10
            ),
            CartRoomModel(
                id = 4,
                idHeader = 11,
                position = 2,
                idCart = 20
            ),
            CartRoomModel(
                id = 5,
                idHeader = 11,
                position = 3,
                idCart = 30
            )
        )

    private val headerRetrofitModelOutputList =
        listOf(
            HeaderRetrofitModelOutput(
                id = 10,
                regDriver = 19759,
                idTruck = 10,
                idConfigServ = 10,
                cartList = listOf(
                    CartRetrofitModelOutput(
                        id = 1,
                        position = 1,
                        idCart = 1
                    ),
                    CartRetrofitModelOutput(
                        id = 2,
                        position = 2,
                        idCart = 2
                    )
                ),
            ),
            HeaderRetrofitModelOutput(
                id = 11,
                regDriver = 18017,
                idTruck = 100,
                idConfigServ = 10,
                cartList = listOf(
                    CartRetrofitModelOutput(
                        id = 3,
                        position = 1,
                        idCart = 10
                    ),
                    CartRetrofitModelOutput(
                        id = 4,
                        position = 2,
                        idCart = 20
                    ),
                    CartRetrofitModelOutput(
                        id = 5,
                        position = 3,
                        idCart = 30
                    )
                ),
            )
        )

    private val headerRoomModelWithIdList =
        listOf(
            HeaderRoomModel(
                id = 10,
                regDriver = 19759,
                idTruck = 10,
                idServ = 24
            ),
            HeaderRoomModel(
                id = 11,
                regDriver = 18017,
                idTruck = 100,
                idServ = 25
            )
        )

    private val headerRetrofitModelInputList =
        listOf(
            HeaderRetrofitModelInput(
                idServ = 24,
                id = 10
            ),
            HeaderRetrofitModelInput(
                idServ = 25,
                id = 11
            )
        )

    private val headerDeleteRoomModelList =
        listOf(
            HeaderRoomModel(
                id = 10,
                regDriver = 19759,
                idTruck = 10,
                dateHour = Date(1786369416000),
                statusSend = StatusSend.SENT
            ),
            HeaderRoomModel(
                id = 11,
                regDriver = 18017,
                idTruck = 100,
                dateHour = Date(1787838216000),
                statusSend = StatusSend.SENT
            )
        )

    @Test
    fun `getRegDriver - Check return failure if have error in PreCECSharedPreferencesDatasource getRegDriver`() =
        runTest {
            whenever(
                headerSharedPreferencesDatasource.getRegDriver()
            ).thenReturn(
                resultFailure(
                    "IPreCECSharedPreferencesDatasource.getRegDriver",
                    "-",
                    Exception()
                )
            )
            val result = repository.getRegDriver()
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "INoteRepository.getRegDriver -> IPreCECSharedPreferencesDatasource.getRegDriver",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `getRegDriver - Check return correct if regDriver is null`() =
        runTest {
            whenever(
                headerSharedPreferencesDatasource.getRegDriver()
            ).thenReturn(
                Result.success(null)
            )
            val result = repository.getRegDriver()
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
    fun `getRegDriver - Check return correct if function execute successfully`() =
        runTest {
            whenever(
                headerSharedPreferencesDatasource.getRegDriver()
            ).thenReturn(
                Result.success(19759)
            )
            val result = repository.getRegDriver()
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                19759,
                result.getOrNull()!!
            )
        }

    @Test
    fun `setRegDriver - Check return failure if have error in HeaderSharedPreferencesDatasource setRegDriver`() =
        runTest {
            whenever(
                headerSharedPreferencesDatasource.setRegDriver(19759)
            ).thenReturn(
                resultFailure(
                    "IHeaderSharedPreferencesDatasource.setRegDriver",
                    "-",
                    Exception()
                )
            )
            val result = repository.setRegDriver(19759)
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "INoteRepository.setRegDriver -> IHeaderSharedPreferencesDatasource.setRegDriver",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }
    
    @Test
    fun `setRegDriver - Check return correct if function execute successfully`() =
        runTest {
            val result = repository.setRegDriver(19759)
            verify(headerSharedPreferencesDatasource, atLeastOnce()).setRegDriver(19759)
            assertEquals(
                true,
                result.isSuccess
            )
        }

    @Test
    fun `deleteNote - Check return failure if have error in TrailerSharedPreferencesDatasource clean`() =
        runTest {
            whenever(
                cartSharedPreferencesDatasource.clean()
            ).thenReturn(
                resultFailure(
                    "ITrailerSharedPreferencesDatasource.clean",
                    "-",
                    Exception()
                )
            )
            val result = repository.deleteNote()
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "INoteRepository.deleteNote -> ITrailerSharedPreferencesDatasource.clean",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `deleteNote - Check return failure if have error in HeaderSharedPreferencesDatasource clean`() =
        runTest {
            whenever(
                headerSharedPreferencesDatasource.clean()
            ).thenReturn(
                resultFailure(
                    "IHeaderSharedPreferencesDatasource.clean",
                    "-",
                    Exception()
                )
            )
            val result = repository.deleteNote()
            verify(cartSharedPreferencesDatasource, atLeastOnce()).clean()
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "INoteRepository.deleteNote -> IHeaderSharedPreferencesDatasource.clean",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `deleteNote - Check return correct if function execute successfully`() =
        runTest {
            val result = repository.deleteNote()
            verify(cartSharedPreferencesDatasource, atLeastOnce()).clean()
            verify(headerSharedPreferencesDatasource, atLeastOnce()).clean()
            assertEquals(
                true,
                result.isSuccess
            )
        }

    @Test
    fun `getIdTruck - Check return failure if have error in HeaderSharedPreferencesDatasource getIdTruck`() =
        runTest {
            whenever(
                headerSharedPreferencesDatasource.getIdTruck()
            ).thenReturn(
                resultFailure(
                    "IHeaderSharedPreferencesDatasource.getIdTruck",
                    "-",
                    Exception()
                )
            )
            val result = repository.getIdTruck()
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "INoteRepository.getIdTruck -> IHeaderSharedPreferencesDatasource.getIdTruck",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `getIdTruck - Check return correct if idTruck is null`() =
        runTest {
            whenever(
                headerSharedPreferencesDatasource.getIdTruck()
            ).thenReturn(
                Result.success(null)
            )
            val result = repository.getIdTruck()
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
    fun `getIdTruck - Check return correct if function execute successfully`() =
        runTest {
            whenever(
                headerSharedPreferencesDatasource.getIdTruck()
            ).thenReturn(
                Result.success(100)
            )
            val result = repository.getIdTruck()
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                100,
                result.getOrNull()!!
            )
        }

    @Test
    fun `hasSend - Check return failure if have error in HeaderRoomDatasource hasByStatusSend`() =
        runTest {
            whenever(
                headerRoomDatasource.hasByStatusSend(StatusSend.SEND)
            ).thenReturn(
                resultFailure(
                    "IHeaderRoomDatasource.hasByStatusSend",
                    "-",
                    Exception()
                )
            )
            val result = repository.hasSend()
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "INoteRepository.hasSend -> IHeaderRoomDatasource.hasByStatusSend",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `hasSend - Check return correct if function execute successfully`() =
        runTest {
            whenever(
                headerRoomDatasource.hasByStatusSend(StatusSend.SEND)
            ).thenReturn(
                Result.success(false)
            )
            val result = repository.hasSend()
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
    fun `send - Check return failure if have error in HeaderRoomDatasource listByStatusSend`() =
        runTest {
            whenever(
                headerRoomDatasource.listByStatusSend(StatusSend.SEND)
            ).thenReturn(
                resultFailure(
                    "IHeaderRoomDatasource.listByStatusSend",
                    "-",
                    Exception()
                )
            )
            val result = repository.send("token", 1)
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "INoteRepository.send -> IHeaderRoomDatasource.listByStatusSend",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `send - Check return failure if have error in CartRoomDatasource listByIdHeader`() =
        runTest {
            whenever(
                headerRoomDatasource.listByStatusSend(StatusSend.SEND)
            ).thenReturn(
                Result.success(headerRoomModelList)
            )
            whenever(
                cartRoomDatasource.listByIdHeader(10)
            ).thenReturn(
                resultFailure(
                    "ICartRoomDatasource.listByIdHeader",
                    "-",
                    Exception()
                )
            )
            val result = repository.send("token", 1)
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "INoteRepository.send -> ICartRoomDatasource.listByIdHeader",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `send - Check return failure if have error in NoteRetrofitDatasource send`() =
        runTest {
            whenever(
                headerRoomDatasource.listByStatusSend(StatusSend.SEND)
            ).thenReturn(
                Result.success(headerRoomModelList)
            )
            whenever(
                cartRoomDatasource.listByIdHeader(10)
            ).thenReturn(
                Result.success(cartRoomModelList1)
            )
            whenever(
                cartRoomDatasource.listByIdHeader(11)
            ).thenReturn(
                Result.success(cartRoomModelList2)
            )
            whenever(
                noteRetrofitDatasource.send("token", headerRetrofitModelOutputList)
            ).thenReturn(
                resultFailure(
                    "INoteRetrofitDatasource.send",
                    "-",
                    Exception()
                )
            )
            val result = repository.send("token", 10)
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "INoteRepository.send -> INoteRetrofitDatasource.send",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `send - Check return failure if have error in HeaderRoomDatasource updateStatusSend`() =
        runTest {
            whenever(
                headerRoomDatasource.listByStatusSend(StatusSend.SEND)
            ).thenReturn(
                Result.success(headerRoomModelList)
            )
            whenever(
                cartRoomDatasource.listByIdHeader(10)
            ).thenReturn(
                Result.success(cartRoomModelList1)
            )
            whenever(
                cartRoomDatasource.listByIdHeader(11)
            ).thenReturn(
                Result.success(cartRoomModelList2)
            )
            whenever(
                noteRetrofitDatasource.send("token", headerRetrofitModelOutputList)
            ).thenReturn(
                Result.success(headerRetrofitModelInputList)
            )
            whenever(
                headerRoomDatasource.updateStatusSend(StatusSend.SENT, headerRoomModelWithIdList)
            ).thenReturn(
                resultFailure(
                    "IHeaderRoomDatasource.updateStatusSend",
                    "-",
                    Exception()
                )
            )
            val result = repository.send("token", 10)
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "INoteRepository.send -> IHeaderRoomDatasource.updateStatusSend",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `send - Check return correct if function execute successfully`() =
        runTest {
            whenever(
                headerRoomDatasource.listByStatusSend(StatusSend.SEND)
            ).thenReturn(
                Result.success(headerRoomModelList)
            )
            whenever(
                cartRoomDatasource.listByIdHeader(10)
            ).thenReturn(
                Result.success(cartRoomModelList1)
            )
            whenever(
                cartRoomDatasource.listByIdHeader(11)
            ).thenReturn(
                Result.success(cartRoomModelList2)
            )
            whenever(
                noteRetrofitDatasource.send("token", headerRetrofitModelOutputList)
            ).thenReturn(
                Result.success(headerRetrofitModelInputList)
            )
            val result = repository.send("token", 10)
            verify(headerRoomDatasource, atLeastOnce()).updateStatusSend(StatusSend.SENT, headerRoomModelWithIdList)
            assertEquals(
                true,
                result.isSuccess
            )
        }

    @Test
    fun `finish - Check return failure if have error in HeaderSharedPreferencesDatasource get`() =
        runTest {
            whenever(
                headerSharedPreferencesDatasource.get()
            ).thenReturn(
                resultFailure(
                    "IHeaderSharedPreferencesDatasource.get",
                    "-",
                    Exception()
                )
            )
            val result = repository.finish()
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "INoteRepository.finish -> IHeaderSharedPreferencesDatasource.get",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `finish - Check return failure if have error in HeaderRoomDatasource save`() =
        runTest {
            whenever(
                headerSharedPreferencesDatasource.get()
            ).thenReturn(
                Result.success(
                    HeaderSharedPreferencesModel(
                        regDriver = 19759,
                        idTruck = 10
                    )
                )
            )
            whenever(
                headerRoomDatasource.save(
                    argThat {
                        this.regDriver == 19759L && this.idTruck == 10
                    }
                )
            ).thenReturn(
                resultFailure(
                    "IHeaderRoomDatasource.save",
                    "-",
                    Exception()
                )
            )
            val result = repository.finish()
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "INoteRepository.finish -> IHeaderRoomDatasource.save",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }
    
    @Test
    fun `finish - Check return failure if have error in CartSharedPreferencesDatasource list`() =
        runTest {
            whenever(
                headerSharedPreferencesDatasource.get()
            ).thenReturn(
                Result.success(
                    HeaderSharedPreferencesModel(
                        regDriver = 19759,
                        idTruck = 10
                    )
                )
            )
            whenever(
                headerRoomDatasource.save(
                    argThat {
                        this.regDriver == 19759L && this.idTruck == 10
                    }
                )
            ).thenReturn(
                Result.success(12)
            )
            whenever(
                cartSharedPreferencesDatasource.list()
            ).thenReturn(
                resultFailure(
                    "ICartSharedPreferencesDatasource.list",
                    "-",
                    Exception()
                )
            )
            val result = repository.finish()
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "INoteRepository.finish -> ICartSharedPreferencesDatasource.list",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `finish - Check return failure if have error in CartRoomDatasource save`() =
        runTest {
            whenever(
                headerSharedPreferencesDatasource.get()
            ).thenReturn(
                Result.success(
                    HeaderSharedPreferencesModel(
                        regDriver = 19759,
                        idTruck = 10
                    )
                )
            )
            whenever(
                headerRoomDatasource.save(
                    argThat {
                        this.regDriver == 19759L && this.idTruck == 10
                    }
                )
            ).thenReturn(
                Result.success(12)
            )
            whenever(
                cartSharedPreferencesDatasource.list()
            ).thenReturn(
                Result.success(
                    listOf(
                        CartSharedPreferencesModel(
                            position = 1,
                            idCart = 1
                        ),
                        CartSharedPreferencesModel(
                            position = 2,
                            idCart = 2
                        )
                    )
                )
            )
            whenever(
                cartRoomDatasource.save(
                    CartRoomModel(
                        idHeader = 12,
                        position = 1,
                        idCart = 1
                    )
                )
            ).thenReturn(
                resultFailure(
                    "ICartRoomDatasource.save",
                    "-",
                    Exception()
                )
            )
            val result = repository.finish()
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "INoteRepository.finish -> ICartRoomDatasource.save",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `finish - Check return failure if have error in HeaderSharedPreferencesDatasource clean`() =
        runTest {
            whenever(
                headerSharedPreferencesDatasource.get()
            ).thenReturn(
                Result.success(
                    HeaderSharedPreferencesModel(
                        regDriver = 19759,
                        idTruck = 10
                    )
                )
            )
            whenever(
                headerRoomDatasource.save(
                    argThat {
                        this.regDriver == 19759L && this.idTruck == 10
                    }
                )
            ).thenReturn(
                Result.success(12)
            )
            whenever(
                cartSharedPreferencesDatasource.list()
            ).thenReturn(
                Result.success(
                    listOf(
                        CartSharedPreferencesModel(
                            position = 1,
                            idCart = 1
                        ),
                        CartSharedPreferencesModel(
                            position = 2,
                            idCart = 2
                        )
                    )
                )
            )
            whenever(
                headerSharedPreferencesDatasource.clean()
            ).thenReturn(
                resultFailure(
                    "IHeaderSharedPreferencesDatasource.clean",
                    "-",
                    Exception()
                )
            )
            val result = repository.finish()
            verify(cartRoomDatasource, atLeastOnce()).save(
                CartRoomModel(
                    idHeader = 12,
                    position = 1,
                    idCart = 1
                )
            )
            verify(cartRoomDatasource, atLeastOnce()).save(
                CartRoomModel(
                    idHeader = 12,
                    position = 2,
                    idCart = 2
                )
            )
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "INoteRepository.finish -> IHeaderSharedPreferencesDatasource.clean",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `finish - Check return failure if have error in CartSharedPreferencesDatasource clean`() =
        runTest {
            whenever(
                headerSharedPreferencesDatasource.get()
            ).thenReturn(
                Result.success(
                    HeaderSharedPreferencesModel(
                        regDriver = 19759,
                        idTruck = 10
                    )
                )
            )
            whenever(
                headerRoomDatasource.save(
                    argThat {
                        this.regDriver == 19759L && this.idTruck == 10
                    }
                )
            ).thenReturn(
                Result.success(12)
            )
            whenever(
                cartSharedPreferencesDatasource.list()
            ).thenReturn(
                Result.success(
                    listOf(
                        CartSharedPreferencesModel(
                            position = 1,
                            idCart = 1
                        ),
                        CartSharedPreferencesModel(
                            position = 2,
                            idCart = 2
                        )
                    )
                )
            )
            whenever(
                cartSharedPreferencesDatasource.clean()
            ).thenReturn(
                resultFailure(
                    "ICartSharedPreferencesDatasource.clean",
                    "-",
                    Exception()
                )
            )
            val result = repository.finish()
            verify(cartRoomDatasource, atLeastOnce()).save(
                CartRoomModel(
                    idHeader = 12,
                    position = 1,
                    idCart = 1
                )
            )
            verify(cartRoomDatasource, atLeastOnce()).save(
                CartRoomModel(
                    idHeader = 12,
                    position = 2,
                    idCart = 2
                )
            )
            verify(headerSharedPreferencesDatasource, atLeastOnce()).clean()
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "INoteRepository.finish -> ICartSharedPreferencesDatasource.clean",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `finish - Check return correct if function execute successfully`() =
        runTest {
            whenever(
                headerSharedPreferencesDatasource.get()
            ).thenReturn(
                Result.success(
                    HeaderSharedPreferencesModel(
                        regDriver = 19759,
                        idTruck = 10
                    )
                )
            )
            whenever(
                headerRoomDatasource.save(
                    argThat {
                        this.regDriver == 19759L && this.idTruck == 10
                    }
                )
            ).thenReturn(
                Result.success(12)
            )
            whenever(
                cartSharedPreferencesDatasource.list()
            ).thenReturn(
                Result.success(
                    listOf(
                        CartSharedPreferencesModel(
                            position = 1,
                            idCart = 1
                        ),
                        CartSharedPreferencesModel(
                            position = 2,
                            idCart = 2
                        )
                    )
                )
            )
            val result = repository.finish()
            verify(cartRoomDatasource, atLeastOnce()).save(
                CartRoomModel(
                    idHeader = 12,
                    position = 1,
                    idCart = 1
                )
            )
            verify(cartRoomDatasource, atLeastOnce()).save(
                CartRoomModel(
                    idHeader = 12,
                    position = 2,
                    idCart = 2
                )
            )
            verify(headerSharedPreferencesDatasource, atLeastOnce()).clean()
            verify(cartSharedPreferencesDatasource, atLeastOnce()).clean()
            assertEquals(
                true,
                result.isSuccess
            )
        }

    @Test
    fun `cartList - Check return failure if have error in CartSharedPreferencesDatasource list`() =
        runTest {
            whenever(
                cartSharedPreferencesDatasource.list()
            ).thenReturn(
                resultFailure(
                    "ICartSharedPreferencesDatasource.list",
                    "-",
                    Exception()
                )
            )
            val result = repository.cartList()
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "INoteRepository.cartList -> ICartSharedPreferencesDatasource.list",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `cartList - Check return correct if function execute successfully`() =
        runTest {
            whenever(
                cartSharedPreferencesDatasource.list()
            ).thenReturn(
                Result.success(
                    listOf(
                        CartSharedPreferencesModel(
                            position = 1,
                            idCart = 1
                        ),
                        CartSharedPreferencesModel(
                            position = 2,
                            idCart = 2
                        )
                    )
                )
            )
            val result = repository.cartList()
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                listOf(
                    Cart(
                        position = 1,
                        idCart = 1
                    ),
                    Cart(
                        position = 2,
                        idCart = 2
                    )
                ),
                result.getOrNull()!!
            )
        }

    @Test
    fun `setCart - Check return failure if have error in CartSharedPreferencesDatasource add`() =
        runTest {
            whenever(
                cartSharedPreferencesDatasource.add(
                    CartSharedPreferencesModel(
                        position = 1,
                        idCart = 1
                    )
                )
            ).thenReturn(
                resultFailure(
                    "ICartSharedPreferencesDatasource.add",
                    "-",
                    Exception()
                )
            )
            val result = repository.setCart(
                Cart(
                    position = 1,
                    idCart = 1
                )
            )
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "INoteRepository.setCart -> ICartSharedPreferencesDatasource.add",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `setCart - Check return correct if function execute successfully`() =
        runTest {
            val result = repository.setCart(
                Cart(
                    position = 1,
                    idCart = 1
                )
            )
            verify(cartSharedPreferencesDatasource, atLeastOnce()).add(
                CartSharedPreferencesModel(
                    position = 1,
                    idCart = 1
                )
            )
            assertEquals(
                true,
                result.isSuccess
            )
        }

    @Test
    fun `setIdTruck - Check return failure if have error in HeaderSharedPreferencesDatasource setIdTruck`() =
        runTest {
            whenever(
                headerSharedPreferencesDatasource.setIdTruck(200)
            ).thenReturn(
                resultFailure(
                    "IHeaderSharedPreferencesDatasource.setIdTruck",
                    "-",
                    Exception()
                )
            )
            val result = repository.setIdTruck(200)
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "INoteRepository.setIdTruck -> IHeaderSharedPreferencesDatasource.setIdTruck",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `setIdTruck - Check return correct if function execute successfully`() =
        runTest {
            val result = repository.setIdTruck(200)
            verify(headerSharedPreferencesDatasource, atLeastOnce()).setIdTruck(200)
            assertEquals(
                true,
                result.isSuccess
            )
        }

    @Test
    fun `clean - Check return failure if have error in HeaderRoomDatasource listByStatusSend`() =
        runTest {
            whenever(
                headerRoomDatasource.listByStatusSend(StatusSend.SENT)
            ).thenReturn(
                resultFailure(
                    "IHeaderRoomDatasource.listByStatusSend",
                    "-",
                    Exception()
                )
            )
            val result = repository.clean()
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "INoteRepository.clean -> IHeaderRoomDatasource.listByStatusSend",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `clean - Check return failure if have error in CartRoomDatasource deleteByIdHeader`() =
        runTest {
            whenever(
                headerRoomDatasource.listByStatusSend(StatusSend.SENT)
            ).thenReturn(
                Result.success(headerDeleteRoomModelList)
            )
            whenever(
                cartRoomDatasource.deleteByIdHeader(10)
            ).thenReturn(
                resultFailure(
                    "ICartRoomDatasource.deleteByIdHeader",
                    "-",
                    Exception()
                )
            )
            val result = repository.clean()
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "INoteRepository.clean -> ICartRoomDatasource.deleteByIdHeader",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `clean - Check return failure if have error in HeaderRoomDatasource delete`() =
        runTest {
            whenever(
                headerRoomDatasource.listByStatusSend(StatusSend.SENT)
            ).thenReturn(
                Result.success(headerDeleteRoomModelList)
            )
            whenever(
                headerRoomDatasource.delete(10)
            ).thenReturn(
                resultFailure(
                    "IHeaderRoomDatasource.delete",
                    "-",
                    Exception()
                )
            )
            val result = repository.clean()
            verify(cartRoomDatasource, atLeastOnce()).deleteByIdHeader(10)
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "INoteRepository.clean -> IHeaderRoomDatasource.delete",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `clean - Check return failure if have error in HeaderSharedPreferencesDatasource clean`() =
        runTest {
            whenever(
                headerRoomDatasource.listByStatusSend(StatusSend.SENT)
            ).thenReturn(
                Result.success(headerDeleteRoomModelList)
            )
            whenever(
                headerSharedPreferencesDatasource.clean()
            ).thenReturn(
                resultFailure(
                    "IHeaderSharedPreferencesDatasource.clean",
                    "-",
                    Exception()
                )
            )
            val result = repository.clean()
            verify(cartRoomDatasource, atLeastOnce()).deleteByIdHeader(10)
            verify(cartRoomDatasource, never()).deleteByIdHeader(11)
            verify(headerRoomDatasource, atLeastOnce()).delete(10)
            verify(cartRoomDatasource, never()).deleteByIdHeader(11)
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "INoteRepository.clean -> IHeaderSharedPreferencesDatasource.clean",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `clean - Check return failure if have error in CartSharedPreferencesDatasource clean`() =
        runTest {
            whenever(
                headerRoomDatasource.listByStatusSend(StatusSend.SENT)
            ).thenReturn(
                Result.success(headerDeleteRoomModelList)
            )
            whenever(
                cartSharedPreferencesDatasource.clean()
            ).thenReturn(
                resultFailure(
                    "ICartSharedPreferencesDatasource.clena",
                    "-",
                    Exception()
                )
            )
            val result = repository.clean()
            verify(headerSharedPreferencesDatasource, atLeastOnce()).clean()
            verify(cartRoomDatasource, atLeastOnce()).deleteByIdHeader(10)
            verify(cartRoomDatasource, never()).deleteByIdHeader(11)
            verify(headerRoomDatasource, atLeastOnce()).delete(10)
            verify(cartRoomDatasource, never()).deleteByIdHeader(11)
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "INoteRepository.clean -> ICartSharedPreferencesDatasource.clena",
                result.exceptionOrNull()!!.message
            )
            assertEquals(
                "java.lang.Exception",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun `clean - Check return correct if function execute successfully`() =
        runTest {
            whenever(
                headerRoomDatasource.listByStatusSend(StatusSend.SENT)
            ).thenReturn(
                Result.success(headerDeleteRoomModelList)
            )
            val result = repository.clean()
            verify(headerSharedPreferencesDatasource, atLeastOnce()).clean()
            verify(cartSharedPreferencesDatasource, atLeastOnce()).clean()
            verify(cartRoomDatasource, atLeastOnce()).deleteByIdHeader(10)
            verify(cartRoomDatasource, never()).deleteByIdHeader(11)
            verify(headerRoomDatasource, atLeastOnce()).delete(10)
            verify(cartRoomDatasource, never()).deleteByIdHeader(11)
            assertEquals(
                true,
                result.isSuccess
            )
        }

}