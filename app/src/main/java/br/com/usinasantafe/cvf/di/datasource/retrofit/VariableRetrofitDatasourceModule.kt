package br.com.usinasantafe.cvf.di.datasource.retrofit

import br.com.usinasantafe.cvf.external.retrofit.datasource.variable.*
import br.com.usinasantafe.cvf.infra.datasource.retrofit.variable.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface VariableRetrofitDatasourceModule {

    @Binds
    @Singleton
    fun bindConfigRetrofitDatasource(datasource: IConfigRetrofitDatasource): ConfigRetrofitDatasource

    @Binds
    @Singleton
    fun bindManagerRetrofitDatasource(datasource: IManagerRetrofitDatasource): ManagerRetrofitDatasource

    @Binds
    @Singleton
    fun bindNoteRetrofitDatasource(datasource: INoteRetrofitDatasource): NoteRetrofitDatasource

}