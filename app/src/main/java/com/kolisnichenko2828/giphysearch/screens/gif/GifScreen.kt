package com.kolisnichenko2828.giphysearch.screens.gif

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.kolisnichenko2828.giphysearch.R
import com.kolisnichenko2828.giphysearch.core.components.ErrorMessage

@Composable
fun GifScreen(
    originalUrl: String,
    onBackClick: () -> Unit
) {
    var gifState by remember { mutableStateOf(GifState.LOADING) }
    var retryHash by remember { mutableIntStateOf(0) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        key(retryHash) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(originalUrl)
                    .crossfade(true)
                    .crossfade(500)
                    .build(),
                contentDescription = stringResource(R.string.full_screen_gif_content_description),
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit,
                onLoading = { gifState = GifState.LOADING },
                onSuccess = { gifState = GifState.SUCCESS },
                onError = { gifState = GifState.ERROR }
            )
        }

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
                onRetry = {
                    gifState = GifState.LOADING
                    retryHash++
                }
            )
        }

        IconButton(
            onClick = onBackClick,
            modifier = Modifier.padding(8.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.arrow_back_24px),
                contentDescription = stringResource(R.string.action_back)
            )
        }
    }
}