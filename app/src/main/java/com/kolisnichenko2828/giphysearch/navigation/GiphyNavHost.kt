package com.kolisnichenko2828.giphysearch.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import androidx.paging.compose.collectAsLazyPagingItems
import com.kolisnichenko2828.giphysearch.screens.gif.GifScreen
import com.kolisnichenko2828.giphysearch.screens.main.HomeScreen
import com.kolisnichenko2828.giphysearch.screens.main.HomeViewModel

@Composable
fun GiphyNavHost() {
    val backStack = rememberSaveable { mutableStateListOf<Screen>(Screen.MainScreen) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        NavDisplay(
            backStack = backStack,
            onBack = { backStack.removeLastOrNull() },
            entryProvider = { key: Screen ->
                when (key) {
                    is Screen.MainScreen -> NavEntry(key) {
                        val homeViewModel: HomeViewModel = hiltViewModel()
                        HomeScreen(
                            viewModel = homeViewModel,
                            onGifClick = { index ->
                                backStack.add(Screen.GifScreen(index))
                            }
                        )
                    }

                    is Screen.GifScreen -> NavEntry(key) {
                        val homeViewModel: HomeViewModel = hiltViewModel()
                        val gifsPagingItems = homeViewModel.gifsFlow.collectAsLazyPagingItems()

                        GifScreen(
                            initialIndex = key.initialIndex,
                            gifs = gifsPagingItems,
                            onBackClick = { backStack.removeLastOrNull() }
                        )
                    }
                }
            }
        )
    }
}