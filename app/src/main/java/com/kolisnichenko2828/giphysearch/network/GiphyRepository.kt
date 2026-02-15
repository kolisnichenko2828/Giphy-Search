package com.kolisnichenko2828.giphysearch.network

import android.util.Log
import coil3.network.HttpException
import com.kolisnichenko2828.giphysearch.screens.main.states.GifItemState
import okio.IOException
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
            return Result.failure(Exception("Server error: ${e.cause}"))
        } catch (e: Exception) {
            Log.d("error", e.message.toString())
            return Result.failure(Exception("Unknown error"))
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
            return Result.failure(Exception("Server error: ${e.cause}"))
        } catch (e: Exception) {
            Log.d("error", e.message.toString())
            return Result.failure(Exception("Unknown error"))
        }
    }
}