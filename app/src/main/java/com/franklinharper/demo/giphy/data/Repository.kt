package com.franklinharper.demo.giphy.data

import com.franklinharper.demo.giphy.coroutine.CoroutineDispatchers
import com.franklinharper.demo.giphy.data.domain.Gif
import com.franklinharper.demo.giphy.data.restapi.ApiGiphyTrending
import com.franklinharper.demo.giphy.data.restapi.GiphyApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import timber.log.Timber
import javax.inject.Inject

class Repository @Inject constructor(
    // Pass in dispatchers so that we can write unit tests for this Repository
    private val dispatchers: CoroutineDispatchers,
    private val service: GiphyApiService,
) {

    /**
     * @param parentScope
     * @return A List of Gifs; or null if an error occurred.
     */
    suspend fun loadTrendingGifs(
        parentScope: CoroutineScope,
    ): List<Gif>? {

        val gifs =
            withContext(dispatchers.io) {
                // To be "main-thread" safe we are executing this coroutine on dispatchers.io.

                // TODO store the data returned by the Api in a local DB.
                // Having data stored locally would make the app faster and more robust.
                val apiGiphyTrending =
                    loadFromApi(parentScope)
                        ?.trendingGifs
                        ?.map { data ->
                            Gif(
                                headline = data.title,
                                url = data.images.downsized.url,
                            )
                        }

                // In this demo app we'll trust that the data returned from the Api is valid.
                // In a real app we'd validate the data and log anomalies so that errors in
                // the upstream data source can be fixed.
                //  val validatedData = validateApiDataAndLogAnomalies(apiGiphyTrending)

                return@withContext apiGiphyTrending
            }

        return gifs
    }

    private suspend fun loadFromApi(parentScope: CoroutineScope): ApiGiphyTrending? {
        val apiGiphyTrending = parentScope.async {
            Timber.d("network call: start")
            service.getTrending().body().also { apiGiphyTrending: ApiGiphyTrending? ->
                Timber.d("network call finished. data size: ${apiGiphyTrending?.trendingGifs?.size}")
            }
        }
        // When we want to parallelize the execution of more than one network request; we
        // would add more async blocks similar to the one above.

        return apiGiphyTrending.await()
    }

}
