package br.com.usinasantafe.cvf.di.external.retrofit

import br.com.usinasantafe.cvf.external.retrofit.api.stable.ColabApi
import br.com.usinasantafe.cvf.external.retrofit.api.stable.EquipApi
import br.com.usinasantafe.cvf.external.retrofit.api.stable.FrontApi
import br.com.usinasantafe.cvf.external.retrofit.api.stable.ReleaseApi
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

    @Provides
    @Singleton
    fun equipApiRetrofit(
        retrofit: Retrofit
    ): EquipApi = retrofit.create(EquipApi::class.java)

    @Provides
    @Singleton
    fun frontApiRetrofit(
        retrofit: Retrofit
    ): FrontApi = retrofit.create(FrontApi::class.java)

    @Provides
    @Singleton
    fun releaseApiRetrofit(
        retrofit: Retrofit
    ): ReleaseApi = retrofit.create(ReleaseApi::class.java)

}