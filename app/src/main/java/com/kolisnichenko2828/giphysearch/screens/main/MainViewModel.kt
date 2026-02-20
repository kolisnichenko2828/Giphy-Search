package com.kolisnichenko2828.giphysearch.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.kolisnichenko2828.giphysearch.network.GiphyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class MainViewModel @Inject constructor(
    private val giphyRepository: GiphyRepository
) : ViewModel() {
    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()
    val gifsFlow = _query
        .debounce(500L)
        .distinctUntilChanged()
        .flatMapLatest { q ->
            giphyRepository.getGifs(q)
        }
        .cachedIn(viewModelScope)

    fun searchGifs(newQuery: String) {
        _query.value = newQuery
    }
}