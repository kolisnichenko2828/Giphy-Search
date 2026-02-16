package com.kolisnichenko2828.giphysearch.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.kolisnichenko2828.giphysearch.screens.gif.GifScreen
import com.kolisnichenko2828.giphysearch.screens.main.MainScreen

@Composable
fun GiphyNavHost() {
    val backStack = rememberSaveable { mutableStateListOf<Screen>(Screen.MainScreen) }

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = { key: Screen ->
            when (key) {
                is Screen.MainScreen -> NavEntry(key) {
                    MainScreen(
                        onGifClick = { originalUrl ->
                            backStack.add(Screen.GifScreen(originalUrl))
                        }
                    )
                }
                is Screen.GifScreen -> NavEntry(key) {
                    GifScreen(
                        originalUrl = key.originalUrl,
                        onBackClick = {
                            backStack.removeLastOrNull()
                        }
                    )
                }
            }
        }
    )
}