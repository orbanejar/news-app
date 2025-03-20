package com.example.newsapp.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.newsapp.data.model.NewsArticle
import com.example.newsapp.ui.component.NewsCard

/**
 * A composable function that displays the list of bookmarked news articles in a grid layout.
 *
 * @param bookmarkedNews A list of news articles that have been bookmarked.
 * @param onNewsClick Callback triggered when a news article is clicked.
 * @param onBookmarkClick Callback triggered when the bookmark icon is clicked.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BookmarkScreen(
    bookmarkedNews: List<NewsArticle>, // List of bookmarked news articles
    onNewsClick: (NewsArticle) -> Unit, // Callback for handling article click events
    onBookmarkClick: (NewsArticle) -> Unit // Callback for handling bookmark toggle
) {
    val gridState = rememberLazyGridState()

    if (bookmarkedNews.isEmpty()) {
        // Display a message if no bookmarks are available
        Text(text = "No Bookmarks yet", modifier = Modifier.padding(16.dp))
    } else {
        // Display bookmarked articles in a 2-column grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            state = gridState,
            modifier = Modifier.padding(16.dp)
        ) {
            items(bookmarkedNews.size) { index ->
                val article = bookmarkedNews[index]
                NewsCard(
                    article = article,
                    isBookmarked = true, // All items in this screen are already bookmarked
                    onBookmarkClick = { onBookmarkClick(article) }, // Handle bookmark toggle
                    onClick = { onNewsClick(article) } // Handle article click
                )
            }
        }
    }
}
