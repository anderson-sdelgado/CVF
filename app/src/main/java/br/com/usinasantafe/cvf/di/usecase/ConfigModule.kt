package br.com.usinasantafe.cvf.di.usecase

import br.com.usinasantafe.cvf.domain.usecases.config.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface ConfigModule {

    @Binds
    @Singleton
    fun bindUpdateConfig(usecase: IUpdateConfig): UpdateConfig

    @Binds
    @Singleton
    fun bindSetFinishUpdateAllTable(usecase: ISetFinishUpdateAllTable): SetFinishUpdateAllTable

    @Binds
    @Singleton
    fun bindGetConfig(usecase: IGetConfig): GetConfig

}