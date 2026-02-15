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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kolisnichenko2828.giphysearch.R
import com.kolisnichenko2828.giphysearch.core.components.ErrorMessage
import com.kolisnichenko2828.giphysearch.screens.main.components.GifsGrid
import com.kolisnichenko2828.giphysearch.screens.main.components.SearchInput

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
    onGifClick: (String) -> Unit
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    var searchQuery by remember { mutableStateOf(uiState.query) }
    var previousQuery by rememberSaveable { mutableStateOf(uiState.query) }
    val gridState = rememberLazyStaggeredGridState()
    val shouldLoadMore by remember {
        derivedStateOf {
            val totalItems = gridState.layoutInfo.totalItemsCount
            val lastVisibleItemIndex = gridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            val prefetchThreshold = 10
            totalItems > 0 && lastVisibleItemIndex >= (totalItems - prefetchThreshold)
        }
    }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore && !uiState.isLoadingMore && !uiState.isLoadingInitial) {
            viewModel.loadNextPage()
        }
    }

    LaunchedEffect(uiState.query) {
        if (previousQuery != uiState.query) {
            gridState.scrollToItem(0)
            previousQuery = uiState.query
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        SearchInput(
            query = searchQuery,
            onQueryChange = { newQuery ->
                searchQuery = newQuery
                viewModel.searchGifs(newQuery)
            }
        )
        if (uiState.query.isBlank() && uiState.gifs.isNotEmpty()) {
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
            if (uiState.isLoadingInitial) {
                CircularProgressIndicator()
            } else if (uiState.isEmpty) {
                Text(
                    text = stringResource(R.string.state_nothing_found),
                    style = MaterialTheme.typography.bodyLarge
                )
            } else if (uiState.error != null && uiState.gifs.isEmpty()) {
                ErrorMessage(
                    errorMessage = uiState.error!!,
                    onRetry = { viewModel.retry() }
                )
            } else {
                GifsGrid(
                    gifs = uiState.gifs,
                    gridState = gridState,
                    isLoadingMore = uiState.isLoadingMore,
                    onGifClick = onGifClick,
                    errorMessage = uiState.error,
                    onRetryClick = { viewModel.loadNextPage() }
                )
            }
        }
    }
}