package com.kolisnichenko2828.giphysearch.screens.main.states

import androidx.compose.runtime.Immutable

@Immutable
data class GifItemState(
    val id: String,
    val title: String,
    val previewUrl: String,
    val originalUrl: String,
    val aspectRatio: Float
)