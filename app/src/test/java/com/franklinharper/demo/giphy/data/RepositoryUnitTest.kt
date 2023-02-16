package com.franklinharper.demo.giphy.data

import com.franklinharper.demo.giphy.coroutine.CoroutineDispatchers
import com.franklinharper.demo.giphy.data.domain.Gif
import com.franklinharper.demo.giphy.data.restapi.*
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Test
import retrofit2.Response

internal class RepositoryUnitTest {

    private val testDispatchers = object : CoroutineDispatchers {
        override val io = Dispatchers.IO
    }

    // TODO add more tests
    //
    // * Tests for the other repository functions.
    // * Tests for data validation anomaly slogging.
    // * Tests for Api error responses (checking both the results returned and the logging calls).

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `loadTrendingGifs() when Rest Api responds without errors`() = runTest {
        // Arrange
        val repository = createFakeRepository(
            apiResponse = Response.success(
                ApiGiphyTrending(
                    trendingGifs = listOf(
                        Data(
                            title = "Title1",
                            images = Images(
                                downsized = Downsized(
                                    url = "https://media3.giphy.com/media/3o7TKoWXm3okO1kgHC/giphy-downsized.gif",
                                )
                            )
                        )
                    )
                )
            ),
        )

        // Act
        val dataFromApi = repository.loadTrendingGifs(parentScope = this)
        advanceUntilIdle()

        // Assert
        val expected = listOf(
            Gif(
                headline = "Title1",
                url = "https://media3.giphy.com/media/3o7TKoWXm3okO1kgHC/giphy-downsized.gif",
            ),
        )

        assertEquals(expected, dataFromApi)
    }

    // I prefer using Fakes rather than mocking.
    // Creating fakes generally requires more upfront effort.
    // But in my experience the investment in Fakes pays off quickly because:
    //   * the tests are more independent of the implementation
    //   * fakes are more easily re-used than mocks.
    private fun createFakeRepository(
        apiResponse: Response<ApiGiphyTrending>,
    ): Repository {
        return Repository(
            dispatchers = testDispatchers,
            service = FakeApiService(apiResponse),
        )
    }
}