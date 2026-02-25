package com.kolisnichenko2828.giphysearch.screens.gif

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import com.kolisnichenko2828.giphysearch.R
import com.kolisnichenko2828.giphysearch.core.network.LocalNetworkStatus

@Composable
fun GifPage(
    title: String,
    stillUrl: String,
    originalUrl: String,
) {
    val isNetworkAvailable = LocalNetworkStatus.current

    SubcomposeAsyncImage(
        model = originalUrl,
        contentDescription = stringResource(R.string.full_screen_gif_content_description),
        contentScale = ContentScale.Fit,
        modifier = Modifier.fillMaxSize(),
    ) {
        val currentPainter = painter
        val painterState by currentPainter.state.collectAsState()

        LaunchedEffect(isNetworkAvailable) {
            val isError = painterState is AsyncImagePainter.State.Error
            if (isNetworkAvailable && isError) {
                painter.restart()
            }
        }

        when (painterState) {
            is AsyncImagePainter.State.Loading -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    AsyncImage(
                        model = stillUrl,
                        contentDescription = title,
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .fillMaxSize()
                            .blur(radius = 12.dp)
                    )
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
            is AsyncImagePainter.State.Success -> {
                SubcomposeAsyncImageContent()
            }
            is AsyncImagePainter.State.Error  -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    AsyncImage(
                        model = stillUrl,
                        contentDescription = title,
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .fillMaxSize()
                            .blur(radius = 12.dp)
                    )
                    IconButton(
                        onClick = { currentPainter.restart() },
                        modifier = Modifier.align(Alignment.Center)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.refresh_24px),
                            contentDescription = stringResource(R.string.action_retry),
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }
            }
            else -> Unit
        }
    }
}