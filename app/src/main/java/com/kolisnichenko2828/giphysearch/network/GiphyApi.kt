package com.kolisnichenko2828.giphysearch.network

import retrofit2.http.GET
import retrofit2.http.Query

interface GiphyApi {
    @GET("v1/gifs/search")
    suspend fun searchGifs(
        @Query("q") query: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): GiphyResponseDto

    @GET("v1/gifs/trending")
    suspend fun getTrendingGifs(
        @Query("limit") limit: Int = 25,
        @Query("offset") offset: Int = 0
    ): GiphyResponseDto
}