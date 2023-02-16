package com.franklinharper.demo.giphy.data.restapi

import retrofit2.Response

class FakeApiService(
    private val getTrendingResponse: Response<ApiGiphyTrending>,
) : GiphyApiService {
    override suspend fun getTrending() = getTrendingResponse
}