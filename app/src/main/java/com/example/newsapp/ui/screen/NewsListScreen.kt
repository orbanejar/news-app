package com.example.newsapp.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.newsapp.ui.component.NewsCard
import com.example.newsapp.ui.component.SearchBar
import com.example.newsapp.viewmodel.NewsViewModel

/**
 * A composable function that displays a list of news articles in a grid layout.
 * It includes a search bar for filtering news and supports infinite scrolling.
 *
 * @param viewModel The ViewModel managing news data and state.
 * @param onNewsClick Callback function triggered when a news article is clicked.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NewsListScreen(
    viewModel: NewsViewModel,
    onNewsClick: (news: com.example.newsapp.data.model.NewsArticle) -> Unit
) {
    val newsList = viewModel.newsList.collectAsState().value
    val gridState = rememberLazyGridState()

    Column(modifier = Modifier.fillMaxSize()) {
        // Search bar to filter news articles
        // TODO: To modify behavior for empty search [to display featured news]
        SearchBar(
            query = viewModel.searchQuery.collectAsState().value,
            onQueryChanged = { viewModel.searchQuery.value = it },
            onSearch = { viewModel.refreshNews() }
        )

        // LazyVerticalGrid for displaying news articles in a 2-column layout
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            state = gridState,
            modifier = Modifier.fillMaxSize()
        ) {
            items(newsList.size) { index ->
                val article = newsList[index]
                NewsCard(
                    article = article,
                    isBookmarked = viewModel.bookmarkedNews.collectAsState().value.contains(article),
                    onBookmarkClick = { viewModel.toggleBookmark(article) },
                    onClick = { onNewsClick(article) }
                )
            }

            // Loading indicator for infinite scrolling
            if (viewModel.isLoading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }

    // Infinite scrolling effect to load more news when reaching the end
    LaunchedEffect(gridState) {
        snapshotFlow { gridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { index ->
                if (index != null && index >= newsList.size - 1 && !viewModel.isLoading) {
                    viewModel.fetchNews()
                }
            }
    }
}