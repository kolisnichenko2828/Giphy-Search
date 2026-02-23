package com.kolisnichenko2828.giphysearch.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.kolisnichenko2828.giphysearch.screens.gif.GifScreen
import com.kolisnichenko2828.giphysearch.screens.main.MainScreen
import com.kolisnichenko2828.giphysearch.screens.main.MainViewModel

@Composable
fun GiphyNavHost(
    mainViewModel: MainViewModel,
    isNetworkAvailable: State<Boolean>
) {
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
                        MainScreen(
                            viewModel = mainViewModel,
                            isNetworkAvailable = isNetworkAvailable,
                            onGifClick = { index ->
                                backStack.add(Screen.GifScreen(index))
                            }
                        )
                    }

                    is Screen.GifScreen -> NavEntry(key) {
                        GifScreen(
                            onBackClick = { backStack.removeLastOrNull() },
                            viewModel = mainViewModel,
                            isNetworkAvailable = isNetworkAvailable,
                            initialIndex = key.initialIndex
                        )
                    }
                }
            }
        )
    }
}