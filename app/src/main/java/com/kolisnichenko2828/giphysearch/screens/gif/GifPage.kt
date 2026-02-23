package com.kolisnichenko2828.giphysearch.screens.gif

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import coil3.compose.AsyncImagePainter
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import com.kolisnichenko2828.giphysearch.R
import com.kolisnichenko2828.giphysearch.core.components.ErrorMessage
import com.kolisnichenko2828.giphysearch.core.components.toUserReadableMessage

@Composable
fun GifPage(
    originalUrl: String,
    isNetworkAvailable: State<Boolean>
) {
    SubcomposeAsyncImage(
        model = originalUrl,
        contentDescription = stringResource(R.string.full_screen_gif_content_description),
        contentScale = ContentScale.Fit,
        modifier = Modifier.fillMaxSize(),
    ) {
        val painterState by painter.state.collectAsState()

        LaunchedEffect(isNetworkAvailable.value) {
            val isError = painterState is AsyncImagePainter.State.Error
            if (isNetworkAvailable.value && isError) {
                painter.restart()
            }
        }

        when (val state = painterState) {
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
            is AsyncImagePainter.State.Error  -> {
                ErrorMessage(
                    errorMessage = state.result.throwable.toUserReadableMessage(),
                    onRetry = { painter.restart() }
                )
            }
            else -> Unit
        }
    }
}