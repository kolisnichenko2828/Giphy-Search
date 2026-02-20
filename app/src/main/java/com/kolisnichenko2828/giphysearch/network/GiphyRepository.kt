package com.kolisnichenko2828.giphysearch.network

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.kolisnichenko2828.giphysearch.screens.main.states.GifItemState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GiphyRepository @Inject constructor(
    private val api: GiphyApi
) {
    fun getGifs(query: String): Flow<PagingData<GifItemState>> {
        return Pager(
            config = PagingConfig(
                pageSize = 25,
                prefetchDistance = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                GiphyPagingSource(
                    api = api,
                    query = query
                )
            }
        ).flow
    }
}