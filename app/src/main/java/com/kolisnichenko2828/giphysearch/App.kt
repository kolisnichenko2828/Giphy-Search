package com.kolisnichenko2828.giphysearch

import android.app.Application
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import com.kolisnichenko2828.giphysearch.di.BaseOkHttpClient
import dagger.hilt.android.HiltAndroidApp
import okhttp3.OkHttpClient
import javax.inject.Inject

@HiltAndroidApp
class App : Application(), SingletonImageLoader.Factory {
    @Inject
    @BaseOkHttpClient
    lateinit var okHttpClient: OkHttpClient

    override fun newImageLoader(context: PlatformContext): ImageLoader {
        return ImageLoader.Builder(context)
            .components {
                add(
                    OkHttpNetworkFetcherFactory(
                        callFactory = { okHttpClient }
                    )
                )
            }
            .build()
    }

}