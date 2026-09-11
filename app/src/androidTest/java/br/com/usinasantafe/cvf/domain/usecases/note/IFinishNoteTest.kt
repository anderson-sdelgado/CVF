package br.com.usinasantafe.cvf.domain.usecases.note

import br.com.usinasantafe.cvf.external.room.dao.variable.CartDao
import br.com.usinasantafe.cvf.external.room.dao.variable.HeaderDao
import br.com.usinasantafe.cvf.external.sharedPreferences.ICartSharedPreferencesDatasource
import br.com.usinasantafe.cvf.external.sharedPreferences.IHeaderSharedPreferencesDatasource
import br.com.usinasantafe.cvf.infra.models.room.variable.HeaderRoomModel
import br.com.usinasantafe.cvf.infra.models.sharedpreferences.CartSharedPreferencesModel
import br.com.usinasantafe.cvf.infra.models.sharedpreferences.HeaderSharedPreferencesModel
import br.com.usinasantafe.cvf.lib.StatusSend
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import kotlin.test.assertEquals

@HiltAndroidTest
class IFinishNoteTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var usecase: FinishNote

    @Inject
    lateinit var headerSharedPreferencesDatasource: IHeaderSharedPreferencesDatasource

    @Inject
    lateinit var cartSharedPreferencesDatasource: ICartSharedPreferencesDatasource

    @Inject
    lateinit var headerDao: HeaderDao

    @Inject
    lateinit var cartDao: CartDao

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun check_return_failure_if_header_shared_preferences_is_empty() =
        runTest {
            val result = usecase()
            assertEquals(
                true,
                result.isFailure
            )
            assertEquals(
                "IFinishNote -> INoteRepository.finish -> regDriver is required",
                result.exceptionOrNull()!!.message,
            )
            assertEquals(
                "java.lang.NullPointerException: regDriver is required",
                result.exceptionOrNull()!!.cause.toString()
            )
        }

    @Test
    fun check_altered_and_delete_data_if_process_execute_successfully() =
        runTest {
            headerSharedPreferencesDatasource.save(
                HeaderSharedPreferencesModel(
                    regDriver = 123,
                    idTruck = 11
                )
            )
            cartSharedPreferencesDatasource.add(
                CartSharedPreferencesModel(
                    position = 1,
                    idCart = 10
                )
            )
            val result = usecase()
            assertEquals(
                true,
                result.isSuccess
            )
            val listHeader = headerDao.all()
            assertEquals(
                1,
                listHeader.size
            )
            val modelHeader = listHeader.first()
            assertEquals(
                HeaderRoomModel(
                    id = 1,
                    regDriver = 123,
                    idTruck = 11,
                    idServ = null,
                    statusSend = StatusSend.SENT,
                    dateHour = modelHeader.dateHour
                ),
                modelHeader
            )
            val listCart = cartDao.all()
            assertEquals(
                1,
                listCart.size
            )
            val modelCart = listCart.first()
            assertEquals(
                1,
                modelCart.id
            )
            assertEquals(
                1,
                modelCart.idHeader
            )
            assertEquals(
                1,
                modelCart.position
            )
            assertEquals(
                10,
                modelCart.idCart
            )
            val hasHeaderSharedPreferences = headerSharedPreferencesDatasource.has().getOrThrow()
            assertEquals(
                false,
                hasHeaderSharedPreferences
            )
            val listCartSharedPreferences = cartSharedPreferencesDatasource.list().getOrThrow()
            assertEquals(
                0,
                listCartSharedPreferences.size
            )
        }
}