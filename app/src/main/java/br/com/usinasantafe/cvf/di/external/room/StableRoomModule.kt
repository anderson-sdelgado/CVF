package br.com.usinasantafe.cvf.di.external.room

import br.com.usinasantafe.cvf.external.room.dao.DatabaseRoom
import br.com.usinasantafe.cvf.external.room.dao.stable.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StableRoomModule {

    @Provides
    @Singleton
    fun provideColabDao(database: DatabaseRoom): ColabDao {
        return database.colabDao()
    }

    @Provides
    @Singleton
    fun provideEquipDao(database: DatabaseRoom): EquipDao {
        return database.equipDao()
    }

    @Provides
    @Singleton
    fun provideFrontDao(database: DatabaseRoom): FrontDao {
        return database.frontDao()
    }

    @Provides
    @Singleton
    fun provideReleaseDao(database: DatabaseRoom): ReleaseDao {
        return database.releaseDao()
    }

}