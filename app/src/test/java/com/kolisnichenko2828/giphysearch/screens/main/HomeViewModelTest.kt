package com.kolisnichenko2828.giphysearch.screens.main

import com.kolisnichenko2828.giphysearch.network.GiphyRepository
import com.kolisnichenko2828.giphysearch.util.MainDispatcherRule
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class HomeViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(StandardTestDispatcher())
    private val repository: GiphyRepository = mockk(relaxed = true)

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `searchGifs should update query stateflow`() = runTest(
        context = mainDispatcherRule.testDispatcher
    ) {
        val viewModel = HomeViewModel(repository)
        val searchQuery = "dog"

        viewModel.searchGifs(searchQuery)

        advanceUntilIdle()

        assertEquals(searchQuery, viewModel.query.value)
    }
}