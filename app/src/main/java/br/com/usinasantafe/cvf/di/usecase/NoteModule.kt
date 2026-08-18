package br.com.usinasantafe.cvf.di.usecase

import br.com.usinasantafe.cvf.domain.usecases.note.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface NoteModule {

    @Binds
    @Singleton
    fun bindGetDriver(usecase: IGetDriver): GetDriver

    @Binds
    @Singleton
    fun bindSetDriver(usecase: ISetDriver): SetDriver

}