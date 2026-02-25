package com.kolisnichenko2828.giphysearch.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.kolisnichenko2828.giphysearch.BuildConfig
import com.kolisnichenko2828.giphysearch.network.GiphyApi
import com.kolisnichenko2828.giphysearch.network.GiphyApiKeyInterceptor
import com.kolisnichenko2828.giphysearch.network.GiphyRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BaseOkHttpClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RetrofitOkHttpClient

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val BASE_URL = "https://api.giphy.com/"

    @Provides
    @Singleton
    @BaseOkHttpClient
    fun provideBaseOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    @RetrofitOkHttpClient
    fun provideRetrofitOkHttpClient(@BaseOkHttpClient baseClient: OkHttpClient): OkHttpClient {
        val builder = baseClient
            .newBuilder()
            .addInterceptor(GiphyApiKeyInterceptor())

        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.HEADERS
            }
            builder.addInterceptor(loggingInterceptor)
        }

        return builder.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(@RetrofitOkHttpClient okHttpClient: OkHttpClient): Retrofit {
        val networkJson = Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }
        val contentType = "application/json".toMediaType()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(networkJson.asConverterFactory(contentType))
            .build()
    }

    @Provides
    @Singleton
    fun provideGiphyApi(retrofit: Retrofit): GiphyApi {
        return retrofit.create(GiphyApi::class.java)
    }

    @Provides
    @Singleton
    fun provideGiphyRepository(giphyApi: GiphyApi): GiphyRepository {
        return GiphyRepository(giphyApi)
    }
}