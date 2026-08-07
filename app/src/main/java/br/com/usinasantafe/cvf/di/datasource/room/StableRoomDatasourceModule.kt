package br.com.usinasantafe.cvf.di.datasource.room

import br.com.usinasantafe.cvf.external.room.datasource.stable.*
import br.com.usinasantafe.cvf.infra.datasource.room.stable.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface StableRoomDatasourceModule {

    @Binds
    @Singleton
    fun bindColabRoomDatasource(datasource: IColabRoomDatasource): ColabRoomDatasource

    @Binds
    @Singleton
    fun bindEquipRoomDatasource(datasource: IEquipRoomDatasource): EquipRoomDatasource

    @Binds
    @Singleton
    fun bindFrontRoomDatasource(datasource: IFrontRoomDatasource): FrontRoomDatasource

    @Binds
    @Singleton
    fun bindReleaseRoomDatasource(datasource: IReleaseRoomDatasource): ReleaseRoomDatasource

}