package br.com.usinasantafe.cvf.di.external.retrofit

import br.com.usinasantafe.cvf.di.provider.DefaultApi
import br.com.usinasantafe.cvf.di.provider.DefaultRetrofit
import br.com.usinasantafe.cvf.di.provider.ShortTimeoutApi
import br.com.usinasantafe.cvf.di.provider.ShortTimeoutRetrofit
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
    @DefaultApi
    fun colabDefaultApiRetrofit(
        @DefaultRetrofit retrofit: Retrofit
    ): ColabApi = retrofit.create(ColabApi::class.java)

    @Provides
    @Singleton
    @ShortTimeoutApi
    fun colabShortTimeoutApiRetrofit(
        @ShortTimeoutRetrofit retrofit: Retrofit
    ): ColabApi = retrofit.create(ColabApi::class.java)

    @Provides
    @Singleton
    @DefaultApi
    fun equipDefaultApiRetrofit(
        @DefaultRetrofit retrofit: Retrofit
    ): EquipApi = retrofit.create(EquipApi::class.java)

    @Provides
    @Singleton
    @ShortTimeoutApi
    fun equipShortTimeoutApiRetrofit(
        @ShortTimeoutRetrofit retrofit: Retrofit
    ): EquipApi = retrofit.create(EquipApi::class.java)

    @Provides
    @Singleton
    @DefaultApi
    fun frontApiRetrofit(
        @DefaultRetrofit retrofit: Retrofit
    ): FrontApi = retrofit.create(FrontApi::class.java)

    @Provides
    @Singleton
    @DefaultApi
    fun releaseApiRetrofit(
        @DefaultRetrofit retrofit: Retrofit
    ): ReleaseApi = retrofit.create(ReleaseApi::class.java)

}