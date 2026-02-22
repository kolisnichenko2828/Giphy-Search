package com.kolisnichenko2828.giphysearch.screens.gif

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import coil3.compose.AsyncImagePainter
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.kolisnichenko2828.giphysearch.R
import com.kolisnichenko2828.giphysearch.core.components.ErrorMessage

@Composable
fun GifPage(originalUrl: String) {
    SubcomposeAsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(originalUrl)
            .crossfade(500)
            .build(),
        contentDescription = stringResource(R.string.full_screen_gif_content_description),
        contentScale = ContentScale.Fit,
        modifier = Modifier.fillMaxSize(),
    ) {
        val painterState by painter.state.collectAsState()

        when (painterState) {
            is AsyncImagePainter.State.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is AsyncImagePainter.State.Success -> {
                SubcomposeAsyncImageContent()
            }
            is AsyncImagePainter.State.Error -> {
                ErrorMessage(
                    errorMessage = stringResource(R.string.error_full_screen_title),
                    onRetry = { painter.restart() }
                )
            }
            is AsyncImagePainter.State.Empty -> {
                ErrorMessage(
                    errorMessage = stringResource(R.string.error_full_screen_title),
                    onRetry = { painter.restart() }
                )
            }
        }
    }
}