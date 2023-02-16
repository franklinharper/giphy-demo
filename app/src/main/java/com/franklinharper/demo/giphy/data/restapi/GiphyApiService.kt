package com.franklinharper.demo.giphy.data.restapi

import retrofit2.Response
import retrofit2.http.GET

interface GiphyApiService {
    @GET("gifs/trending")
    suspend fun getTrending(): Response<ApiGiphyTrending>
}