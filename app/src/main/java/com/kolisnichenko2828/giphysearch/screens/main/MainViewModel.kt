package com.kolisnichenko2828.giphysearch.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kolisnichenko2828.giphysearch.network.GiphyRepository
import com.kolisnichenko2828.giphysearch.screens.main.states.MainScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val giphyRepository: GiphyRepository
) : ViewModel() {
    private val _state = MutableStateFlow(MainScreenState())
    val state = _state.asStateFlow()
    private var searchJob: Job? = null
    private val PAGE_SIZE = 25
    private var currentOffset = 0

    init {
        getTrendingGifs()
    }

    private fun getTrendingGifs() {
        viewModelScope.launch {
            currentOffset = 0
            _state.update {
                it.copy(
                    query = "",
                    gifs = emptyList(),
                    isLoadingInitial = true,
                    error = null,
                    isEndReached = false
                )
            }
            fetchGifs()
        }
    }

    fun searchGifs(query: String) {
        if (query == _state.value.query) return
        searchJob?.cancel()
        if (query.isBlank()) {
            getTrendingGifs()
            return
        }

        searchJob = viewModelScope.launch {
            delay(500)

            currentOffset = 0
            _state.update {
                it.copy(
                    query = query,
                    gifs = emptyList(),
                    isLoadingInitial = true,
                    error = null,
                    isEndReached = false
                )
            }

            fetchGifs()
        }
    }

    fun loadNextPage() {
        val state = _state.value
        if (state.isLoadingInitial || state.isLoadingMore || state.isEndReached) {
            return
        }

        _state.update {
            it.copy(
                isLoadingMore = true,
                error = null
            )
        }

        viewModelScope.launch {
            fetchGifs()
        }
    }

    private suspend fun fetchGifs() {
        val currentState = _state.value

        val result = if (currentState.query.isBlank()) {
            giphyRepository.getTrendingGifs(
                limit = PAGE_SIZE,
                offset = currentOffset
            )
        } else {
            giphyRepository.searchGifs(
                query = currentState.query,
                limit = PAGE_SIZE,
                offset = currentOffset
            )
        }

        result.onSuccess { gifs ->
            currentOffset += PAGE_SIZE

            _state.update {
                it.copy(
                    gifs = it.gifs + gifs,
                    isLoadingInitial = false,
                    isLoadingMore = false,
                    isEndReached = gifs.size < PAGE_SIZE
                )
            }
        }.onFailure { error ->
            _state.update {
                it.copy(
                    isLoadingInitial = false,
                    isLoadingMore = false,
                    error = error.message ?: "Unknown error"
                )
            }
        }
    }

    fun retry() {
        val currentQuery = _state.value.query
        if (currentQuery.isBlank()) {
            getTrendingGifs()
        } else {
            searchJob?.cancel()
            searchJob = viewModelScope.launch {
                currentOffset = 0
                _state.update {
                    it.copy(
                        gifs = emptyList(),
                        isLoadingInitial = true,
                        error = null,
                        isEndReached = false
                    )
                }
                fetchGifs()
            }
        }
    }
}