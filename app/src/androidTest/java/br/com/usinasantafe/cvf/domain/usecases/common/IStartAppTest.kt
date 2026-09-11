package br.com.usinasantafe.cvf.domain.usecases.common

import br.com.usinasantafe.cvf.external.room.dao.variable.CartDao
import br.com.usinasantafe.cvf.external.room.dao.variable.HeaderDao
import br.com.usinasantafe.cvf.external.sharedPreferences.ICartSharedPreferencesDatasource
import br.com.usinasantafe.cvf.external.sharedPreferences.IHeaderSharedPreferencesDatasource
import br.com.usinasantafe.cvf.external.sharedPreferences.IManagerSharedPreferencesDatasource
import br.com.usinasantafe.cvf.infra.datasource.sharedpreferences.ConfigSharedPreferencesDatasource
import br.com.usinasantafe.cvf.infra.models.room.variable.CartRoomModel
import br.com.usinasantafe.cvf.infra.models.room.variable.HeaderRoomModel
import br.com.usinasantafe.cvf.infra.models.sharedpreferences.CartSharedPreferencesModel
import br.com.usinasantafe.cvf.infra.models.sharedpreferences.ConfigSharedPreferencesModel
import br.com.usinasantafe.cvf.infra.models.sharedpreferences.HeaderSharedPreferencesModel
import br.com.usinasantafe.cvf.infra.models.sharedpreferences.ManagerSharedPreferencesModel
import br.com.usinasantafe.cvf.lib.FlowApp
import br.com.usinasantafe.cvf.lib.StatusSend
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.Date
import javax.inject.Inject
import kotlin.test.assertEquals

@HiltAndroidTest
class IStartAppTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var usecase: StartApp

    @Inject
    lateinit var configSharedPreferencesDatasource: ConfigSharedPreferencesDatasource

    @Inject
    lateinit var managerSharedPreferencesDatasource: IManagerSharedPreferencesDatasource

    @Inject
    lateinit var headerSharedPreferencesDatasource: IHeaderSharedPreferencesDatasource

    @Inject
    lateinit var cartSharedPreferencesDatasource: ICartSharedPreferencesDatasource

    @Inject
    lateinit var headerDao: HeaderDao

    @Inject
    lateinit var cartDao: CartDao

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun check_return_flowApp_CONFIG_if_not_have_data() =
        runTest {
            val result = usecase()
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                FlowApp.CONFIG,
                result.getOrNull()!!
            )
        }

    @Test
    fun check_return_flowApp_CONFIG_if_flagUpdate_is_false() =
        runTest {
            configSharedPreferencesDatasource.save(
                ConfigSharedPreferencesModel(
                    number = 16997597840,
                    password = "12345",
                    idServ = 1,
                    version = "1.0",
                    flagUpdate = false
                )
            )
            val result = usecase()
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                FlowApp.CONFIG,
                result.getOrNull()!!
            )
        }

    @Test
    fun check_return_flowApp_FRONT_if_flagUpdate_is_true_and_manager_is_empty() =
        runTest {
            configSharedPreferencesDatasource.save(
                ConfigSharedPreferencesModel(
                    number = 16997597840,
                    password = "12345",
                    idServ = 1,
                    version = "1.0",
                    flagUpdate = true
                )
            )
            val result = usecase()
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                FlowApp.FRONT,
                result.getOrNull()!!
            )
        }

    @Test
    fun check_return_flowApp_RELEASE_if_flagUpdate_is_true_and_statusSend_of_manager_is_STARTED() =
        runTest {
            configSharedPreferencesDatasource.save(
                ConfigSharedPreferencesModel(
                    number = 16997597840,
                    password = "12345",
                    idServ = 1,
                    version = "1.0",
                    flagUpdate = true
                )
            )
            managerSharedPreferencesDatasource.save(
                ManagerSharedPreferencesModel(
                    idFront = 1,
                    idRelease = 2
                )
            )
            val result = usecase()
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                FlowApp.RELEASE,
                result.getOrNull()!!
            )
        }

    @Test
    fun check_return_flowApp_NOTE_and_delete_data_if_process_executed_successfully() =
        runTest {
            configSharedPreferencesDatasource.save(
                ConfigSharedPreferencesModel(
                    number = 16997597840,
                    password = "12345",
                    idServ = 1,
                    version = "1.0",
                    flagUpdate = true
                )
            )
            managerSharedPreferencesDatasource.save(
                ManagerSharedPreferencesModel(
                    idFront = 1,
                    idRelease = 2,
                    statusSend = StatusSend.SEND
                )
            )
            headerSharedPreferencesDatasource.save(
                HeaderSharedPreferencesModel(
                    regDriver = 123,
                    idTruck = 456
                )
            )
            cartSharedPreferencesDatasource.add(
                CartSharedPreferencesModel(
                    position = 1,
                    idCart = 25
                )
            )
            cartSharedPreferencesDatasource.add(
                CartSharedPreferencesModel(
                    position = 2,
                    idCart = 63
                )
            )
            headerDao.insert(
                HeaderRoomModel(
                    id = 10,
                    regDriver = 19759,
                    idTruck = 10,
                    dateHour = Date(1786369416000),
                    statusSend = StatusSend.SENT
                )
            )
            headerDao.insert(
                HeaderRoomModel(
                    id = 11,
                    regDriver = 18017,
                    idTruck = 100,
                    dateHour = Date(1787838216000),
                    statusSend = StatusSend.SENT
                )
            )
            cartDao.insert(
                CartRoomModel(
                    idHeader = 10,
                    position = 1,
                    idCart = 25
                )
            )
            cartDao.insert(
                CartRoomModel(
                    idHeader = 10,
                    position = 2,
                    idCart = 63
                )
            )
            cartDao.insert(
                CartRoomModel(
                    idHeader = 11,
                    position = 1,
                    idCart = 145
                )
            )
            val result = usecase()
            assertEquals(
                true,
                result.isSuccess
            )
            assertEquals(
                FlowApp.NOTE,
                result.getOrNull()!!
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
            val listHeaderRoom = headerDao.all()
            assertEquals(
                1,
                listHeaderRoom.size
            )
            val modelHeaderRoom = listHeaderRoom[0]
            assertEquals(
                HeaderRoomModel(
                    id = 11,
                    regDriver = 18017,
                    idTruck = 100,
                    dateHour = Date(1787838216000),
                    statusSend = StatusSend.SENT
                ),
                modelHeaderRoom
            )
            val listCartRoom = cartDao.all()
            assertEquals(
                1,
                listCartRoom.size
            )
            val modelCartRoom = listCartRoom[0]
            assertEquals(
                CartRoomModel(
                    id = 3,
                    idHeader = 11,
                    position = 1,
                    idCart = 145
                ),
                modelCartRoom
            )
        }


}