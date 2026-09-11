package br.com.usinasantafe.cvf.di.datasource.room

import br.com.usinasantafe.cvf.external.room.datasource.variable.*
import br.com.usinasantafe.cvf.infra.datasource.room.variable.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface VariableRoomDatasourceModule {

    @Binds
    @Singleton
    fun bindHeaderRoomDatasource(datasource: IHeaderRoomDatasource): HeaderRoomDatasource

    @Binds
    @Singleton
    fun bindCartRoomDatasource(datasource: ICartRoomDatasource): CartRoomDatasource
}