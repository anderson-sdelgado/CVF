package br.com.usinasantafe.cvf.di.provider

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import androidx.work.WorkManager
import br.com.usinasantafe.cvf.R
import br.com.usinasantafe.cvf.external.room.dao.DatabaseRoom
import br.com.usinasantafe.cvf.lib.BASE_DB
import br.com.usinasantafe.cvf.lib.BASE_SHARED_PREFERENCES
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

@Module
@InstallIn(SingletonComponent::class)
object PersistenceModule {

    @Provides
    @Singleton
    @DefaultHttpClient
    fun provideDefaultHttpClient(): OkHttpClient {

        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        return getUnsafeOkHttpClient(
            connectTimeout = 1,
            readTimeout = 1,
            writeTimeout = 1,
            timeUnit = TimeUnit.MINUTES,
            logging = logging
        )
    }

    @Provides
    @Singleton
    @ShortTimeoutHttpClient
    fun provideShortTimeoutHttpClient(): OkHttpClient {

        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        return getUnsafeOkHttpClient(
            connectTimeout = 10,
            readTimeout = 10,
            writeTimeout = 10,
            timeUnit = TimeUnit.SECONDS,
            logging = logging
        )
    }

    @Provides
    @Singleton
    @DefaultRetrofit
    fun provideDefaultRetrofit(
        @DefaultHttpClient client: OkHttpClient,
        url: String
    ): Retrofit {

        return Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    @ShortTimeoutRetrofit
    fun provideShortTimeoutRetrofit(
        @ShortTimeoutHttpClient client: OkHttpClient,
        url: String
    ): Retrofit {

        return Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    @Singleton
    @Provides
    fun provideRoom(@ApplicationContext appContext: Context): DatabaseRoom {
        return Room.databaseBuilder(
            appContext,
            DatabaseRoom::class.java,
            BASE_DB
        )
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration(false)
            .build()
    }

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext appContext: Context): SharedPreferences {
        return appContext.getSharedPreferences(BASE_SHARED_PREFERENCES, Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager {
        return WorkManager.getInstance(context)
    }

}

@Module
@InstallIn(SingletonComponent::class)
object BaseUrlModule {

    @Provides
    @Singleton
    fun provideUrl(@ApplicationContext appContext: Context): String = appContext.getString(R.string.base_url)
}

@SuppressLint("CustomX509TrustManager")
fun getUnsafeOkHttpClient(
    connectTimeout: Long,
    readTimeout: Long,
    writeTimeout: Long,
    timeUnit: TimeUnit,
    logging: HttpLoggingInterceptor
): OkHttpClient {

    val trustAllCertificates = arrayOf<TrustManager>(
        @SuppressLint("TrustAllX509TrustManager")
        object : X509TrustManager {

            @SuppressLint("TrustAllX509TrustManager")
            override fun checkClientTrusted(
                chain: Array<out X509Certificate>?,
                authType: String?
            ) {
            }

            @SuppressLint("TrustAllX509TrustManager")
            override fun checkServerTrusted(
                chain: Array<out X509Certificate>?,
                authType: String?
            ) {
            }

            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf()
            }
        }
    )

    val trustManager = trustAllCertificates[0] as X509TrustManager

    val sslContext = SSLContext.getInstance("TLS")

    sslContext.init(
        null,
        arrayOf<TrustManager>(trustManager),
        SecureRandom()
    )

    return OkHttpClient.Builder()
        .sslSocketFactory(
            sslContext.socketFactory,
            trustManager
        )
        .hostnameVerifier { _, _ ->
            true
        }
        .addInterceptor(logging)
        .connectTimeout(connectTimeout, timeUnit)
        .readTimeout(readTimeout, timeUnit)
        .writeTimeout(writeTimeout, timeUnit)
        .build()
}