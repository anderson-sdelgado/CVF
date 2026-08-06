package br.com.usinasantafe.cvf.di.external.retrofit

import br.com.usinasantafe.cvf.external.retrofit.api.stable.ColabApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StableRetrofitModule {

    @Provides
    @Singleton
    fun colabApiRetrofit(
        retrofit: Retrofit
    ): ColabApi = retrofit.create(ColabApi::class.java)

}