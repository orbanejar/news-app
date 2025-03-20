package com.example.newsapp.data.remote

import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Response
import com.example.newsapp.data.model.NewsResponse
import retrofit2.http.Header

/**
 * Defines the API endpoints for fetching news articles.
 */
interface ApiService {

    // Endpoint for news list.
    @GET("v1/news")
    suspend fun getNews(
        @Header("x-api-key") apiKey: String,
        @Query("cursor") cursor: String? = null,
        @Query("per_page") perPage: Int = 40
    ): Response<NewsResponse>

    // Search news articles by keywords.
    @GET("v1/news")
    suspend fun searchNews(
        @Header("x-api-key") apiKey: String,
        @Query("q") query: String,
        @Query("per_page") perPage: Int = 40
    ): Response<NewsResponse>
}

