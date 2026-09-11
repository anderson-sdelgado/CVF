package br.com.usinasantafe.cvf.di.usecase

import br.com.usinasantafe.cvf.domain.usecases.common.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface CommonModule {

    @Binds
    @Singleton
    fun bindGetToken(usecase: IToken): Token

    @Binds
    @Singleton
    fun bindStartApp(usecase: IStartApp): StartApp

}