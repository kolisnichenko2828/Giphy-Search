package com.kolisnichenko2828.giphysearch.navigation

import kotlinx.serialization.Serializable

sealed interface Screen {
    @Serializable
    data object MainScreen : Screen
    @Serializable
    data class GifScreen(val originalUrl: String) : Screen
}