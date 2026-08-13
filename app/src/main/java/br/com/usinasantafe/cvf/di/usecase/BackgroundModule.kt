package br.com.usinasantafe.cvf.di.usecase

import br.com.usinasantafe.cvf.domain.usecases.background.IStartWorkManager
import br.com.usinasantafe.cvf.domain.usecases.background.StartWorkManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface BackgroundModule {

    @Binds
    @Singleton
    fun bindStartWorkManager(usecase: IStartWorkManager): StartWorkManager

}