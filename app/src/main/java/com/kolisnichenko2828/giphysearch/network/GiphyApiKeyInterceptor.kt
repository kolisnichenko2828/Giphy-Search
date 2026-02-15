package com.kolisnichenko2828.giphysearch.network

import com.kolisnichenko2828.giphysearch.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class GiphyApiKeyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalUrl = originalRequest.url

        val newUrl = originalUrl.newBuilder()
            .addQueryParameter("api_key", BuildConfig.GIPHY_API)
            .build()

        val newRequest = originalRequest.newBuilder()
            .url(newUrl)
            .build()

        return chain.proceed(newRequest)
    }
}