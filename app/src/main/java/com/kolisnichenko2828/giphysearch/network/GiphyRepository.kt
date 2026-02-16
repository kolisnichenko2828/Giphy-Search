package com.kolisnichenko2828.giphysearch.network

import com.kolisnichenko2828.giphysearch.screens.main.states.GifItemState
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class GiphyRepository @Inject constructor(
    private val api: GiphyApi
) {
    suspend fun searchGifs(
        query: String,
        limit: Int,
        offset: Int
    ): Result<List<GifItemState>> {
        try {
            val responseDto = api.searchGifs(
                query = query,
                limit = limit,
                offset = offset
            )

            val gifStateList = responseDto.data.map { it.toItemState() }
            return Result.success(gifStateList)
        } catch (_: IOException) {
            return Result.failure(Exception("Check network connection"))
        } catch (e: HttpException) {
            if (e.code() == 429) {
                return Result.failure(Exception("Rate limit exceeded. Try again later"))
            }
            return Result.failure(Exception("HTTP error: ${e.code()}"))
        } catch (e: Exception) {
            return Result.failure(Exception("Error: ${e.javaClass.simpleName} / ${e.message}"))
        }
    }

    suspend fun getTrendingGifs(
        limit: Int,
        offset: Int
    ): Result<List<GifItemState>> {
        try {
            val responseDto = api.getTrendingGifs(
                limit = limit,
                offset = offset
            )

            val gifStateList = responseDto.data.map { it.toItemState() }
            return Result.success(gifStateList)
        } catch (_: IOException) {
            return Result.failure(Exception("Check network connection"))
        } catch (e: HttpException) {
            if (e.code() == 429) {
                return Result.failure(Exception("Rate limit exceeded. Try again later"))
            }
            return Result.failure(Exception("HTTP error: ${e.code()}"))
        } catch (e: Exception) {
            return Result.failure(Exception("Error: ${e.javaClass.simpleName} / ${e.message}"))
        }
    }
}