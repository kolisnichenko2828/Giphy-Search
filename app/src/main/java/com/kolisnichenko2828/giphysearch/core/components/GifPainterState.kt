package com.kolisnichenko2828.giphysearch.core.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.kolisnichenko2828.giphysearch.screens.gif.GifState

@Composable
fun rememberGifPainterState(originalUrl: String): GifLoadState {
    val context = LocalContext.current

    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context)
            .data(originalUrl)
            .crossfade(500)
            .build()
    )

    val painterState by painter.state.collectAsState()

    val gifState = when (painterState) {
        is AsyncImagePainter.State.Loading -> GifState.LOADING
        is AsyncImagePainter.State.Success -> GifState.SUCCESS
        is AsyncImagePainter.State.Error -> GifState.ERROR
        else -> GifState.ERROR
    }

    return GifLoadState(
        painter = painter,
        state = gifState
    )
}

data class GifLoadState(
    val painter: AsyncImagePainter,
    val state: GifState
)