package br.com.usinasantafe.cvf.di.repository

import br.com.usinasantafe.cvf.domain.repositories.variable.*
import br.com.usinasantafe.cvf.infra.repositories.variable.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface VariableRepositoryModule {

    @Binds
    @Singleton
    fun bindConfigRepository(repository: IConfigRepository): ConfigRepository

    @Binds
    @Singleton
    fun bindManagerRepository(repository: IManagerRepository): ManagerRepository

    @Binds
    @Singleton
    fun bindNoteRepository(repository: INoteRepository): NoteRepository

}