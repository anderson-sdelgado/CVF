package br.com.usinasantafe.cvf.domain.usecases.note

import br.com.usinasantafe.cvf.external.room.dao.variable.HeaderDao
import br.com.usinasantafe.cvf.infra.models.room.variable.HeaderRoomModel
import br.com.usinasantafe.cvf.infra.models.sharedpreferences.ConfigSharedPreferencesModel
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
class IHasSendNoteTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var usecase: HasSendNote

    @Inject
    lateinit var headerDao: HeaderDao

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun check_return_false_if_note_is_not_send() =
        runTest {
            headerDao.insert(
                HeaderRoomModel(
                    regDriver = 19759,
                    idTruck = 120,
                    statusSend = StatusSend.SENT
                )
            )
            val result = usecase()
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
    fun check_return_true_if_note_is_send() =
        runTest {
            headerDao.insert(
                HeaderRoomModel(
                    regDriver = 19759,
                    idTruck = 120,
                    statusSend = StatusSend.SEND
                )
            )
            val result = usecase()
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