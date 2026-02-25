package com.kolisnichenko2828.giphysearch.screens.main.components

import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.platform.LocalContext
import androidx.paging.compose.LazyPagingItems
import coil3.imageLoader
import coil3.request.Disposable
import coil3.request.ImageRequest
import com.kolisnichenko2828.giphysearch.screens.main.states.GifItemState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlin.collections.set

@OptIn(FlowPreview::class)
@Composable
fun GifListPrefetcher(
    gifs: LazyPagingItems<GifItemState>,
    gridState: LazyStaggeredGridState
) {
    val context = LocalContext.current
    val prefetchDisposables = remember { mutableMapOf<String, Disposable>() }

    LaunchedEffect(gridState, gifs.itemCount) {
        snapshotFlow { gridState.firstVisibleItemIndex }
            .debounce(150L)
            .collectLatest {

                val visibleItems = gridState.layoutInfo.visibleItemsInfo
                if (visibleItems.isEmpty()) return@collectLatest

                val firstVisibleIndex = visibleItems.first().index
                val lastVisibleIndex = visibleItems.last().index

                val startWindow = (firstVisibleIndex - 10).coerceAtLeast(0)
                val endWindow = lastVisibleIndex + 10
                val prefetchWindow = startWindow..endWindow

                val targetUrls = prefetchWindow.mapNotNull { index ->
                    if (index < gifs.itemCount) {
                        gifs.peek(index)?.stillUrl
                    } else null
                }.toSet()

                val urlsToCancel = prefetchDisposables.keys - targetUrls
                urlsToCancel.forEach { url ->
                    prefetchDisposables[url]?.dispose()
                    prefetchDisposables.remove(url)
                }

                targetUrls.forEach { url ->
                    if (!prefetchDisposables.containsKey(url)) {
                        val request = ImageRequest.Builder(context)
                            .data(url)
                            .memoryCacheKey(url)
                            .build()

                        prefetchDisposables[url] = context.imageLoader.enqueue(request)
                    }
                }
            }
    }
}