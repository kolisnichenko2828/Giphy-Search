package com.kolisnichenko2828.giphysearch.screens.main.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

fun Modifier.shimmerEffect(
    sharedOffset: State<Float>,
    showShimmer: Boolean = true
): Modifier {
    if (!showShimmer) return this

    return this.drawBehind {
        val offset = sharedOffset.value

        val brush = Brush.linearGradient(
            colors = ShimmerColors,
            start = Offset.Zero,
            end = Offset(x = offset, y = offset)
        )
        drawRect(brush = brush)
    }
}

private val ShimmerColors = listOf(
    Color.LightGray.copy(alpha = 0.6f),
    Color.LightGray.copy(alpha = 0.2f),
    Color.LightGray.copy(alpha = 0.6f)
)

@Composable
fun rememberSharedShimmerState(): State<Float> {
    val transition = rememberInfiniteTransition("shared_shimmer_transition")

    return transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "shared_shimmer_effect"
    )
}