package br.com.usinasantafe.cvf.di.repository

import br.com.usinasantafe.cvf.domain.repositories.stable.*
import br.com.usinasantafe.cvf.infra.repositories.stable.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface StableRepositoryModule {

    @Binds
    @Singleton
    fun bindColabRepository(repository: IColabRepository): ColabRepository

}