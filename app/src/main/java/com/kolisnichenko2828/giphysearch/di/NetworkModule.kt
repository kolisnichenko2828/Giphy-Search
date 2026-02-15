package com.kolisnichenko2828.giphysearch.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
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
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val BASE_URL = "https://api.giphy.com/"

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(GiphyApiKeyInterceptor())
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
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