package br.com.usinasantafe.cvf.di.usecase

import br.com.usinasantafe.cvf.domain.usecases.manager.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface ManagerModule {

    @Binds
    @Singleton
    fun bindHasManager(usecase: IHasManager): HasManager

    @Binds
    @Singleton
    fun bindListFront(usecase: IListFront): ListFront

    @Binds
    @Singleton
    fun bindListRelease(usecase: IListRelease): ListRelease

    @Binds
    @Singleton
    fun bindSaveManager(usecase: ISaveManager): SaveManager
}