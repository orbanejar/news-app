package com.example.newsapp.data.repository

import com.example.newsapp.data.model.NewsResponse
import com.example.newsapp.data.remote.RetrofitInstance
import retrofit2.Response

/**
 * Repository class responsible for fetching news data from the API.
 */
class NewsRepository {
    // API key required for authentication. (Temp location; consider moving this to a secure place)
    private val apiKey = "ikv5W3Jlv951XLX0EipUJvcqarg8pm-_WCVKj72mGBk"

    /**
     * Fetches a list of news articles from the API.
     *
     * @param cursor The cursor for pagination (null for the first page).
     * @return A [Response] containing [NewsResponse].
     */
    suspend fun getNews(cursor: String? = null): Response<NewsResponse> {
        return RetrofitInstance.api.getNews(apiKey = apiKey, cursor = cursor)
    }

    /**
     * Searches for news articles that match the given query.
     *
     * @param query The search keyword(s).
     * @return A [Response] containing [NewsResponse].
     */
    suspend fun searchNews(query: String): Response<NewsResponse> {
        return RetrofitInstance.api.searchNews(apiKey = apiKey, query = query)
    }
}
