package com.kolisnichenko2828.giphysearch.screens.main.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.kolisnichenko2828.giphysearch.R
import com.kolisnichenko2828.giphysearch.core.components.toUserReadableMessage
import com.kolisnichenko2828.giphysearch.screens.main.states.GifItemState

@Composable
fun GifsGrid(
    gifs: LazyPagingItems<GifItemState>,
    gridState: LazyStaggeredGridState,
    onGifClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val sharedShimmerOffset = rememberSharedShimmerState()
    val appendState = gifs.loadState.append

    LazyVerticalStaggeredGrid(
        state = gridState,
        columns = StaggeredGridCells.Fixed(2),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
        verticalItemSpacing = 8.dp,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.fillMaxSize()
    ) {
        items(
            count = gifs.itemCount,
            key = { index ->
                val gif = gifs.peek(index)
                if (gif != null) "${gif.id}_$index" else "$index"
            }
        ) { index ->
            val gif = gifs[index]
            if (gif != null) {
                GifItem(
                    gif = gif,
                    sharedShimmerOffset = sharedShimmerOffset,
                    onClick = { onGifClick(index) }
                )
            }
        }
        when (appendState) {
            is LoadState.Loading -> {
                item(span = StaggeredGridItemSpan.FullLine) {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
            is LoadState.Error -> {
                item(span = StaggeredGridItemSpan.FullLine) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = appendState.error.toUserReadableMessage(),
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center
                        )
                        Button(
                            onClick = { gifs.retry() }
                        ) {
                            Text(stringResource(R.string.action_retry))
                        }
                    }
                }
            }
            is LoadState.NotLoading -> Unit
        }
    }
}