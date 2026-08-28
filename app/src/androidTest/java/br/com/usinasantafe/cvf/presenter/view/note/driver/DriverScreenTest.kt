package br.com.usinasantafe.cvf.presenter.view.note.driver

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import br.com.usinasantafe.cav.utils.waitUntilTimeout
import br.com.usinasantafe.cvf.HiltTestActivity
import br.com.usinasantafe.cvf.external.room.dao.stable.FrontDao
import br.com.usinasantafe.cvf.external.room.dao.stable.ReleaseDao
import br.com.usinasantafe.cvf.external.sharedPreferences.IHeaderSharedPreferencesDatasource
import br.com.usinasantafe.cvf.external.sharedPreferences.IManagerSharedPreferencesDatasource
import br.com.usinasantafe.cvf.infra.models.room.stable.FrontRoomModel
import br.com.usinasantafe.cvf.infra.models.room.stable.ReleaseRoomModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class DriverScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()

    @Inject
    lateinit var managerSharedPreferencesDatasource: IManagerSharedPreferencesDatasource

    @Inject
    lateinit var releaseDao: ReleaseDao

    @Inject
    lateinit var frontDao: FrontDao

    @Inject
    lateinit var headerSharedPreferencesDatasource: IHeaderSharedPreferencesDatasource

    @Test
    fun check_open_screen_and_msg_failure_if_manager_table_is_empty() =
        runTest {

            hiltRule.inject()

            setContent()

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertIsDisplayed()
            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertTextEquals("FALHA INESPERADA NO APLICATIVO! POR FAVOR ENTRE EM CONTATO COM TI. DriverViewModel.updateState -> UiStatusStateUpdateKt -> DriverViewModel.recoverData -> IGetTitleMenu -> idRelease is required -> java.lang.NullPointerException: idRelease is required")

            composeTestRule.waitUntilTimeout(30_000)

        }

    @Test
    fun check_open_screen_and_msg_failure_if_idFront_is_null() =
        runTest {

            hiltRule.inject()

            initialRegister()

            setContent()

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertIsDisplayed()
            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertTextEquals("FALHA INESPERADA NO APLICATIVO! POR FAVOR ENTRE EM CONTATO COM TI. DriverViewModel.updateState -> UiStatusStateUpdateKt -> DriverViewModel.recoverData -> IGetTitleMenu -> idFront is required -> java.lang.NullPointerException: idFront is required")

            composeTestRule.waitUntilTimeout(20_000)

        }

    @Test
    fun check_open_screen_and_failure_if_release_table_is_empty() =
        runTest {

            hiltRule.inject()

            initialRegister(2)

            setContent()

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertIsDisplayed()
            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertTextEquals("FALHA INESPERADA NO APLICATIVO! POR FAVOR ENTRE EM CONTATO COM TI. DriverViewModel.updateState -> UiStatusStateUpdateKt -> DriverViewModel.recoverData -> IGetTitleMenu -> IReleaseRepository.getById -> IReleaseRoomDatasource.getById -> java.lang.IllegalStateException: The query result was empty, but expected a single row to return a NON-NULL object of type 'br.com.usinasantafe.cvf.infra.models.room.stable.ReleaseRoomModel'.")

            composeTestRule.waitUntilTimeout(20_000)

        }

    @Test
    fun check_open_screen_and_failure_if_front_table_is_empty() =
        runTest {

            hiltRule.inject()

            initialRegister(3)

            setContent()

            composeTestRule.waitUntilTimeout()

            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertIsDisplayed()
            composeTestRule.onNodeWithTag("text_alert_dialog_simple").assertTextEquals("FALHA INESPERADA NO APLICATIVO! POR FAVOR ENTRE EM CONTATO COM TI. DriverViewModel.updateState -> UiStatusStateUpdateKt -> DriverViewModel.recoverData -> IGetTitleMenu -> IFrontRepository.getById -> IFrontRoomDatasource.getById -> java.lang.IllegalStateException: The query result was empty, but expected a single row to return a NON-NULL object of type 'br.com.usinasantafe.cvf.infra.models.room.stable.FrontRoomModel'.")

            composeTestRule.waitUntilTimeout(20_000)

        }

    @Test
    fun check_open_screen_correct() =
        runTest {

            hiltRule.inject()

            initialRegister(4)

            setContent()

            composeTestRule.waitUntilTimeout(20_000)

        }

    @Test
    fun check_open_screen_correct_and_reg_driver_is_not_null() =
        runTest {

            hiltRule.inject()

            initialRegister(4)

            headerSharedPreferencesDatasource.setRegDriver(19759)

            setContent()

            composeTestRule.waitUntilTimeout(20_000)

        }

    private fun setContent(){
        composeTestRule.setContent {
            DriverScreen(
                onNavTruck = {},
                onNavPassword = {}
            )
        }
    }

    private suspend fun initialRegister(level: Int = 1) {

        managerSharedPreferencesDatasource.setIdRelease(1)

        if (level == 1) return

        managerSharedPreferencesDatasource.setIdFront(2)

        if (level == 2) return

        releaseDao.insertAll(
            listOf(
                ReleaseRoomModel(
                    id = 1,
                    nroOS = 1,
                    idPropAgr = 1,
                    descPropAgr = "Test1",
                    idFront = 2
                ),
                ReleaseRoomModel(
                    id = 2,
                    nroOS = 2,
                    idPropAgr = 2,
                    descPropAgr = "Test2",
                    idFront = 2
                ),
                ReleaseRoomModel(
                    id = 3,
                    nroOS = 3,
                    idPropAgr = 3,
                    descPropAgr = "Test3",
                    idFront = 2
                ),
            )
        )

        if (level == 3) return

        frontDao.insertAll(
            listOf(
                FrontRoomModel(
                    id = 1,
                    cd = 1,
                    description = "Test1"
                ),
                FrontRoomModel(
                    id = 2,
                    cd = 2,
                    description = "Test2"
                ),
                FrontRoomModel(
                    id = 3,
                    cd = 3,
                    description = "Test3"
                ),
            )
        )

        if (level == 4) return

    }


}