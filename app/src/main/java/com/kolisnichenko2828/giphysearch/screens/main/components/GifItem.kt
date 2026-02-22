package com.kolisnichenko2828.giphysearch.screens.main.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import coil3.request.ImageRequest
import com.kolisnichenko2828.giphysearch.screens.main.states.GifItemState

@Composable
fun GifItem(
    gif: GifItemState,
    sharedShimmerOffset: State<Float>,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(gif.aspectRatio)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(gif.previewUrl)
                .build(),
            contentDescription = gif.title,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillWidth
        ) {
            val painterState by painter.state.collectAsState()

            Crossfade(
                targetState = painterState,
                animationSpec = tween(durationMillis = 1000),
                label = "gif_crossfade"
            ) { state ->
                when (state) {
                    is AsyncImagePainter.State.Success -> {
                        SubcomposeAsyncImageContent(modifier = Modifier.fillMaxSize())
                    }
                    else -> {
                        Box(modifier = Modifier
                            .fillMaxSize()
                            .shimmerEffect(sharedShimmerOffset)
                        )
                    }
                }
            }
        }
    }
}