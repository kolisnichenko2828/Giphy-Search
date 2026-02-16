package com.kolisnichenko2828.giphysearch.navigation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed interface Screen : Parcelable {
    @Parcelize
    data object MainScreen : Screen
    @Parcelize
    data class GifScreen(val originalUrl: String) : Screen
}