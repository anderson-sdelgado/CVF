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
    fun bindGetDriver(usecase: IGetRegDriver): GetRegDriver

    @Binds
    @Singleton
    fun bindSetDriver(usecase: ISetRegDriver): SetRegDriver

    @Binds
    @Singleton
    fun bindHasSendNote(usecase: IHasSendNote): HasSendNote

    @Binds
    @Singleton
    fun bindSendNote(usecase: ISendNote): SendNote

}