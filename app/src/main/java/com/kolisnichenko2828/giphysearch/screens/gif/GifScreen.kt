package com.kolisnichenko2828.giphysearch.screens.gif

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import com.kolisnichenko2828.giphysearch.R
import com.kolisnichenko2828.giphysearch.screens.main.MainViewModel

@Composable
fun GifScreen(
    initialIndex: Int,
    viewModel: MainViewModel,
    isNetworkAvailable: State<Boolean>,
    onBackClick: () -> Unit
) {
    val gifs = viewModel.gifsFlow.collectAsLazyPagingItems()
    val pagerState = rememberPagerState(
        initialPage = initialIndex,
        pageCount = { gifs.itemCount }
    )

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            val gif = gifs[page]

            if (gif != null) {
                GifPage(
                    originalUrl = gif.originalUrl,
                    isNetworkAvailable = isNetworkAvailable
                )
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }

        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.TopStart)
        ) {
            Icon(
                painter = painterResource(R.drawable.arrow_back_24px),
                contentDescription = stringResource(R.string.action_back)
            )
        }
    }
}