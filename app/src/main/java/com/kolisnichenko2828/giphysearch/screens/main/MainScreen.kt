package com.kolisnichenko2828.giphysearch.screens.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.kolisnichenko2828.giphysearch.R
import com.kolisnichenko2828.giphysearch.core.components.ErrorMessage
import com.kolisnichenko2828.giphysearch.screens.main.components.GifsGrid
import com.kolisnichenko2828.giphysearch.screens.main.components.SearchInput

@Composable
fun MainScreen(
    viewModel: MainViewModel,
    onGifClick: (Int) -> Unit
) {
    val query by viewModel.query.collectAsStateWithLifecycle()
    var previousQuery by rememberSaveable { mutableStateOf(query) }
    var displayedQuery by rememberSaveable { mutableStateOf(query) }
    var shouldScrollToTop by remember { mutableStateOf(false) }
    val gifsPagingItems = viewModel.gifsFlow.collectAsLazyPagingItems()
    val gridState = rememberLazyStaggeredGridState()

    LaunchedEffect(query) {
        if (previousQuery != query) {
            shouldScrollToTop = true
            previousQuery = query
        }
    }

    LaunchedEffect(gifsPagingItems.loadState.refresh) {
        if (shouldScrollToTop) {
            gridState.scrollToItem(0)
            shouldScrollToTop = false
            displayedQuery = query
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        SearchInput(
            query = query,
            onQueryChange = viewModel::searchGifs
        )
        if (displayedQuery.isBlank() && gifsPagingItems.itemCount > 0) {
            Text(
                text = stringResource(R.string.trending_title),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
            )
        }
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when (val refreshState = gifsPagingItems.loadState.refresh) {
                is LoadState.Loading -> {
                    CircularProgressIndicator()
                }
                is LoadState.Error -> {
                    ErrorMessage(
                        errorMessage = refreshState.error.localizedMessage ?: stringResource(R.string.unknown_error),
                        onRetry = gifsPagingItems::retry
                    )
                }
                is LoadState.NotLoading -> {
                    if (gifsPagingItems.itemCount == 0) {
                        Text(
                            text = stringResource(R.string.state_nothing_found),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    } else {
                        GifsGrid(
                            gifs = gifsPagingItems,
                            gridState = gridState,
                            onGifClick = onGifClick,
                        )
                    }
                }
            }
        }
    }
}