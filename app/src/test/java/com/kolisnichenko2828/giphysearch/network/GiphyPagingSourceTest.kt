package com.kolisnichenko2828.giphysearch.network

import androidx.paging.PagingSource
import com.kolisnichenko2828.giphysearch.screens.main.states.GifItemState
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Test

class GiphyPagingSourceTest {
    private val api: GiphyApi = mockk()

    @Test
    fun `load trending gifs returns Page when successful`() = runTest {
        val mockDto = GiphyResponseDto(
            data = listOf(
                GifDto(
                    id = "1",
                    title = "Cat",
                    images = ImagesDto(
                        original = ImageUrlDto("url_orig"),
                        fixedHeight = ImageUrlDto("url_preview")
                    )
                )
            )
        )

        coEvery {
            api.getTrendingGifs(
                limit = 25,
                offset = 0
            )
        } returns mockDto

        val pagingSource = GiphyPagingSource(api, query = "")

        val expectedResult = PagingSource.LoadResult.Page(
            data = listOf(
                GifItemState(
                    id = "1",
                    title = "Cat",
                    previewUrl = "url_preview",
                    originalUrl = "url_orig",
                    aspectRatio = 1f
                )
            ),
            prevKey = null,
            nextKey = 25
        )

        val actualResult = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 25,
                placeholdersEnabled = false
            )
        )

        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `load returns Error on exception`() = runTest {
        val exception = RuntimeException("Network error")
        coEvery {
            api.getTrendingGifs(
                limit = any(),
                offset = any()
            )
        } throws exception

        val pagingSource = GiphyPagingSource(api, query = "")

        val actualResult = pagingSource.load(
            PagingSource.LoadParams.Refresh(key = null, loadSize = 25, placeholdersEnabled = false)
        )

        assertTrue(actualResult is PagingSource.LoadResult.Error)
        assertEquals(
            exception,
            (actualResult as PagingSource.LoadResult.Error).throwable
        )
    }
}