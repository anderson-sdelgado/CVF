package br.com.usinasantafe.cvf.di.provider

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.room.Room
import androidx.work.WorkManager
import br.com.usinasantafe.cvf.external.room.dao.DatabaseRoom
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import kotlin.jvm.java

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [PersistenceModule::class]
)
object PersistenceModuleTest {

    @Singleton
    @Provides
    @DefaultHttpClient
    fun provideDefaultHttpClient(): OkHttpClient {

        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .addInterceptor(logging)
            .build()
    }

    @Singleton
    @Provides
    @ShortTimeoutHttpClient
    fun provideShortTimeoutHttpClient(): OkHttpClient {

        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .addInterceptor(logging)
            .build()
    }


    @Singleton
    @Provides
    @DefaultRetrofit
    fun provideDefaultRetrofit(
        @DefaultHttpClient client: OkHttpClient,
        url: String
    ): Retrofit = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    @Singleton
    @Provides
    @ShortTimeoutRetrofit
    fun provideShortTimeoutRetrofit(
        @ShortTimeoutHttpClient client: OkHttpClient,
        url: String
    ): Retrofit = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    @Singleton
    @Provides
    fun provideRoom(@ApplicationContext appContext: Context): DatabaseRoom {
        return Room.inMemoryDatabaseBuilder(
            appContext, DatabaseRoom::class.java).allowMainThreadQueries().build()
    }

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext appContext: Context): SharedPreferences {
        return appContext.getSharedPreferences("test", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager {
        return WorkManager.getInstance(context)
    }
}

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [BaseUrlModule::class]
)
object BaseUrlModuleTest {

    var url: String = "http://localhost:8080/"

    @Provides
    @Singleton
    fun provideUrl(): String {
        Log.d("TestRetrofit", "Base URL: $url")
        return url
    }
}