package com.kolisnichenko2828.giphysearch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kolisnichenko2828.giphysearch.core.network.NetworkConnectivityObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    networkConnectivityObserver: NetworkConnectivityObserver
) : ViewModel() {
    val isNetworkAvailable = networkConnectivityObserver.isConnected
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = true
        )
}