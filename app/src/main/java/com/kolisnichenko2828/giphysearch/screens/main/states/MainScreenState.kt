package com.kolisnichenko2828.giphysearch.screens.main.states

data class MainScreenState(
    val query: String = "",
    val gifs: List<GifItemState> = emptyList(),
    val isLoadingInitial: Boolean = false,
    val isLoadingMore: Boolean = false,
    val error: String? = null,
    val isEndReached: Boolean = false
) {
    val isEmpty: Boolean
        get() = gifs.isEmpty() && !isLoadingInitial && error == null && query.isNotEmpty()
}