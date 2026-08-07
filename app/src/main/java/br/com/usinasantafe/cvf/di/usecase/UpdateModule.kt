package br.com.usinasantafe.cvf.di.usecase

import br.com.usinasantafe.cvf.domain.usecases.update.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface UpdateModule {

    @Binds
    @Singleton
    fun bindUpdateTableColab(usecase: IUpdateTableColab): UpdateTableColab

    @Binds
    @Singleton
    fun bindUpdateTableEquip(usecase: IUpdateTableEquip): UpdateTableEquip

    @Binds
    @Singleton
    fun bindUpdateTableFront(usecase: IUpdateTableFront): UpdateTableFront

    @Binds
    @Singleton
    fun bindUpdateTableRelease(usecase: IUpdateTableRelease): UpdateTableRelease

}