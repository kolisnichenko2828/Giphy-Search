package com.kolisnichenko2828.giphysearch.network

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kolisnichenko2828.giphysearch.core.error.AppException
import com.kolisnichenko2828.giphysearch.screens.main.states.GifItemState
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

class GiphyPagingSource(
    private val api: GiphyApi,
    private val query: String
) : PagingSource<Int, GifItemState>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GifItemState> {
        val offset = params.key ?: 0
        val limit = params.loadSize

        try {
            val responseDto = if (query.isBlank()) {
                api.getTrendingGifs(
                    limit = limit,
                    offset = offset
                )
            } else {
                api.searchGifs(
                    query = query,
                    limit = limit,
                    offset = offset
                )
            }
            val gifs = responseDto.data.map { it.toItemState() }

            return LoadResult.Page(
                data = gifs,
                prevKey = if (offset == 0) null else offset - limit,
                nextKey = if (gifs.isEmpty()) null else offset + limit
            )
        } catch (e: Exception) {
            val appException = when (e) {
                is SocketTimeoutException -> AppException.Timeout(e)
                is IOException -> AppException.NoInternetConnection(e)
                is HttpException -> {
                    when (e.code()) {
                        429 -> AppException.RateLimitExceeded(e)
                        in 500..599 -> AppException.ServerError(e)
                        else -> AppException.Unknown(e)
                    }
                }
                else -> AppException.Unknown(e)
            }
            return LoadResult.Error(appException)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, GifItemState>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val anchorPage = state.closestPageToPosition(anchorPosition)
        val prevKey = anchorPage?.prevKey?.plus(state.config.pageSize)
        val nextKey = anchorPage?.nextKey?.minus(state.config.pageSize)
        return prevKey ?: nextKey
    }
}