package com.example.newsapp.data.model

import com.google.gson.annotations.SerializedName

/**
 * Represents a news article with various attributes.
 *
 * @property id Unique identifier for the article.
 * @property title Title of the news article.
 * @property sourceTitle Name of the source publishing the article.
 * @property pubDate Publication date of the article.
 * @property mediaUrl URL to the media associated with the article, if any.
 * @property description Brief description or summary of the article.
 * @property isFeatured Indicates if the article is marked as featured.
 */
data class NewsArticle(
    val id: String,
    val title: String,
    val sourceTitle: String,
    val pubDate: String,
    val mediaUrl: String?,
    val description: String,
    val isFeatured: Boolean = false
)

/**
 * Represents the response received from the news API.
 *
 * @property articles List of news articles retrieved.
 * @property nextCursor Cursor for fetching the next set of results.
 * @property totalResults Total number of results available.
 * @property perPage Number of results per page.
 */
data class NewsResponse(
    @SerializedName("data")
    val articles: List<NewsArticleResponse>?,
    @SerializedName("next_cursor")
    val nextCursor: String?,
    @SerializedName("total_results")
    val totalResults: Int,
    @SerializedName("per_page")
    val perPage: Int
)

/**
 * Represents a news article as received from the API response.
 *
 * @property id Unique identifier for the article.
 * @property title Title of the news article.
 * @property sourceTitle Name of the source publishing the article.
 * @property pubDate Publication date of the article.
 * @property mediaUrl URL to the media associated with the article, if any.
 * @property description Brief description or summary of the article.
 */
data class NewsArticleResponse(
    @SerializedName("id")
    val id: String?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("source_title")
    val sourceTitle: String?,
    @SerializedName("pub_date")
    val pubDate: String?,
    @SerializedName("media_url")
    val mediaUrl: String?,
    @SerializedName("description")
    val description: String?
)