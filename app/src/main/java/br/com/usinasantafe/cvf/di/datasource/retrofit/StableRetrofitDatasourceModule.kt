package br.com.usinasantafe.cvf.di.datasource.retrofit

import br.com.usinasantafe.cvf.external.retrofit.datasource.stable.*
import br.com.usinasantafe.cvf.infra.datasource.retrofit.stable.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface StableRetrofitDatasourceModule {

    @Binds
    @Singleton
    fun bindColabRetrofitDatasource(datasource: IColabRetrofitDatasource): ColabRetrofitDatasource

}