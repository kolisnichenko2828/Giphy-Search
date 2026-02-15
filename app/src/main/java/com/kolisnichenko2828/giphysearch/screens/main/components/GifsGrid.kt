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
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kolisnichenko2828.giphysearch.R
import com.kolisnichenko2828.giphysearch.screens.main.states.GifItemState

@Composable
fun GifsGrid(
    gifs: List<GifItemState>,
    gridState: LazyStaggeredGridState,
    isLoadingMore: Boolean,
    onGifClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    errorMessage: String?,
    onRetryClick: () -> Unit
) {
    LazyVerticalStaggeredGrid(
        state = gridState,
        columns = StaggeredGridCells.Fixed(2),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
        verticalItemSpacing = 8.dp,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.fillMaxSize()
    ) {
        items(
            items = gifs,
            key = { gif -> gif.id }
        ) { gif ->
            GifItem(
                gif = gif,
                onClick = { onGifClick(gif.originalUrl) }
            )
        }
        if (isLoadingMore) {
            item(
                span = StaggeredGridItemSpan.FullLine
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        } else if (errorMessage != null && gifs.isNotEmpty()) {
            item(span = StaggeredGridItemSpan.FullLine) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error
                    )
                    Button(
                        onClick = onRetryClick
                    ) {
                        Text(stringResource(R.string.action_retry))
                    }
                }
            }
        }
    }
}