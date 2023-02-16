package com.franklinharper.demo.giphy.di

import com.franklinharper.demo.giphy.data.restapi.GiphyApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
// In this demo app we can instantiate all of the dependencies for the entire app lifecycle.
// But in a large scale app we would want to use finer grained scoping.
@InstallIn(SingletonComponent::class)
object DataModule {

    private const val apiKey = "EEjeWKnay8eNwJ091mC2ffGuQe96tdBN"
    private const val baseUrl = "https://api.giphy.com/v1/"

    @Provides
    @Singleton
    fun provideGiphyApiService(): GiphyApiService {

        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = (HttpLoggingInterceptor.Level.BODY)
        }

        val apiKeyInterceptor = { chain: Interceptor.Chain ->
            val request = chain.request()

            val httpUrlWithApiKey = request
                .url
                .newBuilder()
                .addQueryParameter("api_key", apiKey)
                .build()

            val requestWithApiKey = request
                .newBuilder()
                .url(httpUrlWithApiKey)
                .build()

            chain.proceed(requestWithApiKey)
        }

        val httpClient = OkHttpClient
            .Builder()
            .addInterceptor(apiKeyInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()

        return Retrofit
            .Builder()
            .baseUrl(baseUrl)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GiphyApiService::class.java)
    }

}
