package br.com.usinasantafe.cvf.di.external.room

import br.com.usinasantafe.cvf.external.room.dao.DatabaseRoom
import br.com.usinasantafe.cvf.external.room.dao.variable.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object VariableRoomModule {

    @Provides
    @Singleton
    fun provideCartDao(database: DatabaseRoom): CartDao {
        return database.cartDao()
    }

    @Provides
    @Singleton
    fun provideHeaderDao(database: DatabaseRoom): HeaderDao {
        return database.headerDao()
    }

}