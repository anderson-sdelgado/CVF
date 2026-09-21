package br.com.usinasantafe.cvf.domain.usecases.manager

import android.content.Context
import android.content.SharedPreferences
import br.com.usinasantafe.cvf.R
import br.com.usinasantafe.cvf.external.room.dao.DatabaseRoom
import br.com.usinasantafe.cvf.external.room.dao.stable.FrontDao
import br.com.usinasantafe.cvf.external.room.dao.stable.ReleaseDao
import br.com.usinasantafe.cvf.external.sharedPreferences.IManagerSharedPreferencesDatasource
import br.com.usinasantafe.cvf.infra.models.room.stable.FrontRoomModel
import br.com.usinasantafe.cvf.infra.models.room.stable.ReleaseRoomModel
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import kotlin.test.assertEquals

@HiltAndroidTest
class IGetTitleMenuTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var usecase: GetTitleMenu

    @Inject
    lateinit var releaseDao: ReleaseDao

    @Inject
    lateinit var frontDao: FrontDao

    @Inject
    lateinit var managerSharedPreferencesDatasource: IManagerSharedPreferencesDatasource

    @Inject
    lateinit var db: DatabaseRoom

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    @Inject
    @ApplicationContext
    lateinit var context: Context

    @Before
    fun setUp() {
        hiltRule.inject()
        db.clearAllTables()
        sharedPreferences.edit().clear().commit()
    }

    @Test
    fun check_flow_reactivity_when_data_updates() =
        runTest {
            // Pre-insert data in Room
            frontDao.insert(FrontRoomModel(1, 1, "Front1"))
            frontDao.insert(FrontRoomModel(2, 2, "Front2"))
            releaseDao.insert(ReleaseRoomModel(10, 100, 1, "Prop1", 1))
            releaseDao.insert(ReleaseRoomModel(20, 200, 1, "Prop2", 2))

            // Set initial state
            managerSharedPreferencesDatasource.update(1, 10, 3)
            
            val emissions = mutableListOf<String>()
            val job = launch {
                usecase().take(2).collect {
                    emissions.add(it)
                }
            }
            
            // Trigger update
            managerSharedPreferencesDatasource.update(2, 20, 3)
            
            job.join()
            
            assertEquals(2, emissions.size)
            val expected1 = context.getString(R.string.text_data_menu, "Front1", "10", "100", "Prop1")
            val expected2 = context.getString(R.string.text_data_menu, "Front2", "20", "200", "Prop2")
            
            assertEquals(expected1, emissions[0])
            assertEquals(expected2, emissions[1])
        }
}
