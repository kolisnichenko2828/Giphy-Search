package com.kolisnichenko2828.giphysearch.screens.gif

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import com.kolisnichenko2828.giphysearch.R
import com.kolisnichenko2828.giphysearch.core.components.ErrorMessage
import com.kolisnichenko2828.giphysearch.core.components.rememberGifPainterState

@Composable
fun GifPage(originalUrl: String) {
    val gifLoadState = rememberGifPainterState(originalUrl)

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = gifLoadState.painter,
            contentDescription = stringResource(R.string.full_screen_gif_content_description),
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit
        )

        if (gifLoadState.state == GifState.LOADING) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        if (gifLoadState.state == GifState.ERROR) {
            ErrorMessage(
                errorMessage = stringResource(R.string.error_full_screen_title),
                onRetry = { gifLoadState.painter.restart() }
            )
        }
    }
}