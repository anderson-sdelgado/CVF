package br.com.usinasantafe.cvf.domain.usecases.note

import br.com.usinasantafe.cvf.external.sharedPreferences.IHeaderSharedPreferencesDatasource
import br.com.usinasantafe.cvf.external.sharedPreferences.ITrailerSharedPreferencesDatasource
import br.com.usinasantafe.cvf.infra.models.sharedpreferences.HeaderSharedPreferencesModel
import br.com.usinasantafe.cvf.infra.models.sharedpreferences.TrailerSharedPreferencesModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import kotlin.test.assertEquals

@HiltAndroidTest
class IDeleteNoteTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var usecase: DeleteNote

    @Inject
    lateinit var headerSharedPreferencesDatasource: IHeaderSharedPreferencesDatasource

    @Inject
    lateinit var trailerSharedPreferencesDatasource: ITrailerSharedPreferencesDatasource

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun check_return_failure_if_not_have_data() =
        runTest {
            trailerSharedPreferencesDatasource.add(
                TrailerSharedPreferencesModel(
                    position = 1,
                    idTrailer = 1
                )
            )
            val resultTrailerBefore = trailerSharedPreferencesDatasource.list()
            assertEquals(
                true,
                resultTrailerBefore.isSuccess
            )
            val listBefore = resultTrailerBefore.getOrNull()!!
            assertEquals(
                1,
                listBefore.size
            )
            headerSharedPreferencesDatasource.save(
                HeaderSharedPreferencesModel(
                    regDriver = 19759
                )
            )
            val resultHeaderBefore = headerSharedPreferencesDatasource.getRegDriver()
            assertEquals(
                true,
                resultHeaderBefore.isSuccess
            )
            assertEquals(
                19759,
                resultHeaderBefore.getOrNull()
            )
            val result = usecase()
            assertEquals(
                true,
                result.isSuccess
            )
            val resultTrailerAfter = trailerSharedPreferencesDatasource.list()
            assertEquals(
                true,
                resultTrailerAfter.isSuccess
            )
            val listAfter = resultTrailerAfter.getOrNull()!!
            assertEquals(
                0,
                listAfter.size
            )
            val resultHeaderAfter = headerSharedPreferencesDatasource.getRegDriver()
            assertEquals(
                true,
                resultHeaderAfter.isSuccess
            )
            assertEquals(
                null,
                resultHeaderAfter.getOrNull()
            )
        }



}