package com.kolisnichenko2828.giphysearch.screens.main

import com.kolisnichenko2828.giphysearch.network.GiphyRepository
import com.kolisnichenko2828.giphysearch.screens.main.states.GifItemState
import com.kolisnichenko2828.giphysearch.screens.util.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class MainViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(StandardTestDispatcher())
    private val repository: GiphyRepository = mockk()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `init should load trending gifs and update state`() = runTest(
        context = mainDispatcherRule.testDispatcher
    ) {
        val mockGifs = listOf(
            GifItemState(
                id = "1",
                title = "Cat",
                previewUrl = "url1",
                originalUrl = "url2",
                aspectRatio = 1f
            )
        )
        coEvery {
            repository.getTrendingGifs(
                limit = any(),
                offset = any()
            )
        } returns Result.success(mockGifs)

        val viewModel = MainViewModel(repository)
        advanceUntilIdle()

        val currentState = viewModel.state.value
        assertEquals(mockGifs, currentState.gifs)
        assertEquals(false, currentState.isLoadingInitial)
        assertEquals(null, currentState.error)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `searchGifs should update query and load search results`() = runTest(
        context = mainDispatcherRule.testDispatcher
    ) {
        val searchQuery = "dog"
        val mockSearchGifs = listOf(
            GifItemState(
                id = "2",
                title = "Dog",
                previewUrl = "url3",
                originalUrl = "url4",
                aspectRatio = 1f
            )
        )
        coEvery {
            repository.getTrendingGifs(any(), any())
        } returns Result.success(emptyList())
        coEvery {
            repository.searchGifs(
                query = searchQuery,
                limit = any(),
                offset = any()
            )
        } returns Result.success(mockSearchGifs)

        val viewModel = MainViewModel(repository)
        advanceUntilIdle()
        viewModel.searchGifs(searchQuery)
        advanceUntilIdle()

        val currentState = viewModel.state.value
        assertEquals(searchQuery, currentState.query)
        assertEquals(mockSearchGifs, currentState.gifs)
    }
}