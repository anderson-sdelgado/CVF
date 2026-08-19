package br.com.usinasantafe.cvf.di.external.retrofit

import br.com.usinasantafe.cvf.di.provider.DefaultApi
import br.com.usinasantafe.cvf.di.provider.DefaultRetrofit
import br.com.usinasantafe.cvf.external.retrofit.api.variable.ConfigApi
import br.com.usinasantafe.cvf.external.retrofit.api.variable.ManagerApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object VariableRetrofitModule {

    @Provides
    @Singleton
    @DefaultApi
    fun configApiRetrofit(
        @DefaultRetrofit retrofit: Retrofit
    ): ConfigApi = retrofit.create(ConfigApi::class.java)

    @Provides
    @Singleton
    @DefaultApi
    fun managerApiRetrofit(
        @DefaultRetrofit retrofit: Retrofit
    ): ManagerApi = retrofit.create(ManagerApi::class.java)

}