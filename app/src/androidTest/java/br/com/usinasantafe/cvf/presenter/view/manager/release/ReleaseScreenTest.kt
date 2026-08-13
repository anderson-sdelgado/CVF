package br.com.usinasantafe.cvf.presenter.view.manager.release

import android.annotation.SuppressLint
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.lifecycle.SavedStateHandle
import br.com.usinasantafe.cav.utils.waitUntilTimeout
import br.com.usinasantafe.cvf.HiltTestActivity
import br.com.usinasantafe.cvf.domain.usecases.manager.ListRelease
import br.com.usinasantafe.cvf.domain.usecases.manager.SaveManager
import br.com.usinasantafe.cvf.domain.usecases.update.UpdateTableRelease
import br.com.usinasantafe.cvf.external.room.dao.stable.ReleaseDao
import br.com.usinasantafe.cvf.infra.models.room.stable.ReleaseRoomModel
import br.com.usinasantafe.cvf.presenter.navigation.Args.ID_FRONT_ARG
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import javax.inject.Inject

@HiltAndroidTest
class ReleaseScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()

    @Inject
    lateinit var listRelease: ListRelease

    @Inject
    lateinit var updateTableRelease: UpdateTableRelease

    @Inject
    lateinit var saveManager: SaveManager

    @Inject
    lateinit var releaseDao: ReleaseDao

    val list = listOf(
            ReleaseRoomModel(
                id = 1,
                nroOS = 1,
                idPropAgr = 1,
                descPropAgr = "Test1",
                idFront = 1
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
                idFront = 3
            ),
            ReleaseRoomModel(
                id = 4,
                nroOS = 4,
                idPropAgr = 4,
                descPropAgr = "Test4",
                idFront = 3
            )
        )

    @Test
    fun check_open_screen_and_list_empty() =
        runTest {

            hiltRule.inject()

            setContent()

            composeTestRule.waitUntilTimeout(20_000)

        }

    @Test
    fun check_open_screen_and_list_empty_if_idFront_is_non_existent() =
        runTest {

            hiltRule.inject()

            releaseDao.insertAll(list)

            setContent(4)

            composeTestRule.waitUntilTimeout(20_000)

        }

    @Test
    fun check_open_screen_and_list_if_idFront_is_existent() =
        runTest {

            hiltRule.inject()

            releaseDao.insertAll(list)

            setContent(3)

            composeTestRule.waitUntilTimeout(20_000)

        }

    @SuppressLint("ViewModelConstructorInComposable")
    private fun setContent(idFront: Int = 1) {
        composeTestRule.setContent {
            ReleaseScreen (
                viewModel = ReleaseViewModel(
                    savedStateHandle = SavedStateHandle(
                        mapOf(ID_FRONT_ARG to idFront)
                    ),
                    listRelease = listRelease,
                    updateTableRelease = updateTableRelease,
                    saveManager = saveManager
                ),
                onNavFront = {},
                onNavColab = {}
            )
        }
    }

}