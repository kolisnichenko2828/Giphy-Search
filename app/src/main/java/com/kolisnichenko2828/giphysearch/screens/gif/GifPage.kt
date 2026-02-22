package com.kolisnichenko2828.giphysearch.screens.gif

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.kolisnichenko2828.giphysearch.R
import com.kolisnichenko2828.giphysearch.core.components.ErrorMessage

@Composable
fun GifPage(originalUrl: String) {
    var gifState by remember { mutableStateOf(GifState.LOADING) }
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(originalUrl)
            .crossfade(500)
            .build()
    )

    LaunchedEffect(painter.state) {
        painter.state.collect { currentState ->
            gifState = when(currentState) {
                is AsyncImagePainter.State.Loading -> GifState.LOADING
                is AsyncImagePainter.State.Success -> GifState.SUCCESS
                is AsyncImagePainter.State.Error -> GifState.ERROR
                else -> GifState.ERROR
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painter,
            contentDescription = stringResource(R.string.full_screen_gif_content_description),
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit
        )

        if (gifState == GifState.LOADING) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        if (gifState == GifState.ERROR) {
            ErrorMessage(
                errorMessage = stringResource(R.string.error_full_screen_title),
                onRetry = { painter.restart() }
            )
        }
    }
}