package com.kolisnichenko2828.giphysearch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kolisnichenko2828.giphysearch.core.components.NetworkStatusBar
import com.kolisnichenko2828.giphysearch.core.theme.GiphySearchTheme
import com.kolisnichenko2828.giphysearch.navigation.GiphyNavHost
import com.kolisnichenko2828.giphysearch.screens.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GiphySearchTheme {
                val mainViewModel: MainViewModel = hiltViewModel()
                val isNetworkAvailable = mainViewModel.isNetworkAvailable.collectAsStateWithLifecycle()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        Column {
                            NetworkStatusBar(isNetworkAvailable)
                            Spacer(modifier = Modifier.navigationBarsPadding())
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        GiphyNavHost(
                            mainViewModel = mainViewModel,
                            isNetworkAvailable = isNetworkAvailable
                        )
                    }
                }
            }
        }
    }
}