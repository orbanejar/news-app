package com.example.newsapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.data.model.NewsArticle
import com.example.newsapp.data.repository.NewsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for managing news articles and bookmarks.
 * Handles fetching, searching, and bookmarking news articles.
 */
class NewsViewModel : ViewModel() {
    // Enable this flag to use dummy data instead of calling the API (to limit API calling due to 200 max call per day)
    private val useDummyData = false
    private val dummyNewsList = listOf(
        NewsArticle(
            id = "1",
            title = "Breaking News: Kotlin Takes Over",
            description = "Developers around the world are switching to Kotlin for Android development.",
            mediaUrl = "https://images.unsplash.com/photo-1728044849248-e90f3ec6a889",
            sourceTitle = "Tech Daily",
            pubDate = "2025-03-20",
            isFeatured = true
        ),
        NewsArticle(
            id = "2",
            title = "AI Revolution in 2025 is transforming industries",
            description = "Artificial Intelligence is transforming industries faster than expected.",
            mediaUrl = "https://images.unsplash.com/photo-1742147550712-9c25dc0832aa",
            sourceTitle = "AI News",
            pubDate = "2025-03-19",
            isFeatured = true
        ),
        NewsArticle(
            id = "3",
            title = "SpaceX Launches New Rocket",
            description = "Elon Musk's company successfully launches a reusable rocket.",
            mediaUrl = "https://images.unsplash.com/photo-1742144897659-8a3e8a0a090c",
            sourceTitle = "Space Journal",
            pubDate = "2025-03-18",
            isFeatured = false
        ),
        NewsArticle(
            id = "4",
            title = "No Image News Sample from the world",
            description = "",
            mediaUrl = null,
            sourceTitle = "News TV",
            pubDate = "2025-01-01",
            isFeatured = false
        )
    )

    private val repository = NewsRepository()

    // StateFlow to hold the list of news articles
    private val _newsList = MutableStateFlow<List<NewsArticle>>(emptyList())
    val newsList: StateFlow<List<NewsArticle>> = _newsList

    // StateFlow to manage the search query
    val searchQuery = MutableStateFlow("")

    // StateFlow to store bookmarked news articles (temporary storage)
    private val _bookmarkedNews = MutableStateFlow<List<NewsArticle>>(emptyList())
    val bookmarkedNews: StateFlow<List<NewsArticle>> = _bookmarkedNews

    // For pagination: store the next_cursor
    private var nextCursor: String? = null
    var isLoading = false

    init {
        fetchNews() // Fetch news on initialization
    }

    // Fetch news data and update _newsList
    fun fetchNews() {
        if (isLoading) return

        isLoading = true
        viewModelScope.launch {
            if (useDummyData) {
                // Use dummy data instead of API
                _newsList.value = dummyNewsList
            } else {
                val response = if (searchQuery.value.isNotBlank()) {
                    repository.searchNews(query = searchQuery.value)
                } else {
                    repository.getNews(cursor = nextCursor)
                }

                if (response.isSuccessful) {
                    response.body()?.let { newsResponse ->
                        val articles = newsResponse.articles?.map { articleResponse ->
                            NewsArticle(
                                id = articleResponse.id ?: "${articleResponse.title}-${articleResponse.pubDate}",
                                title = articleResponse.title ?: "No Title",
                                sourceTitle = articleResponse.sourceTitle ?: "Unknown Source",
                                pubDate = articleResponse.pubDate ?: "",
                                mediaUrl = articleResponse.mediaUrl,
                                description = articleResponse.description ?: "",
                                isFeatured = articleResponse.mediaUrl != null
                            )
                        } ?: emptyList()

                        _newsList.value = _newsList.value + articles
                        nextCursor = newsResponse.nextCursor
                    }
                } else {
                    println("Error: ${response.errorBody()?.string()}")
                }
            }
            isLoading = false
        }
    }

    // Function to retrieve the current list of news articles
    fun getNewsList(): List<NewsArticle> {
        return _newsList.value
    }

    // Refresh news: clear current list and reset cursor
    fun refreshNews() {
        nextCursor = null
        _newsList.value = emptyList()
        fetchNews()
    }

    // Toggle bookmark status for a news article
    fun toggleBookmark(article: NewsArticle) {
        val currentList = _bookmarkedNews.value.toMutableList()
        if (currentList.any { it.id == article.id }) {
            currentList.removeAll { it.id == article.id }
        } else {
            currentList.add(article)
        }
        _bookmarkedNews.value = currentList
    }
}
