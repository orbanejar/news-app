package com.example.newsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Book
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.newsapp.ui.screen.BookmarkScreen
import com.example.newsapp.ui.screen.NewsDetailScreen
import com.example.newsapp.ui.screen.NewsListScreen
import com.example.newsapp.viewmodel.NewsViewModel

/**
 * MainActivity - The entry point of the News App.
 * Uses Jetpack Compose and Navigation Component.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NewsApp()
        }
    }
}

@Composable
fun NewsApp() {
    val navController = rememberNavController()
    val newsViewModel = remember { NewsViewModel() } // Ensure ViewModel survives recompositions

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("News App") },
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding(),
                backgroundColor = MaterialTheme.colors.primarySurface,
                contentColor = Color.White,
                elevation = 0.dp,
                actions = {
                    IconButton(onClick = { navController.navigate("bookmark") }) {
                        Icon(
                            imageVector = Icons.Outlined.Book,
                            contentDescription = "Bookmarks"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "newsList",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("newsList") {
                NewsListScreen(
                    viewModel = newsViewModel,
                    onNewsClick = { selectedNews ->
                        navController.navigate("newsDetail/${selectedNews.title}")
                    }
                )
            }
            composable("newsDetail/{newsTitle}") { backStackEntry ->
                val newsTitle = backStackEntry.arguments?.getString("newsTitle") ?: ""
                val article = newsViewModel.getNewsList().firstOrNull { it.title == newsTitle }
                if (article != null) {
                    NewsDetailScreen(
                        article = article,
                        viewModel = newsViewModel,
                        onBookmarkClick = { newsViewModel.toggleBookmark(article) }
                    )
                }
            }
            composable("bookmark") {
                // Pass the ViewModel to BookmarkScreen for reactive state collection.
                BookmarkScreen(
                    viewModel = newsViewModel,
                    onNewsClick = { selectedNews ->
                        navController.navigate("newsDetail/${selectedNews.title}")
                    },
                    onBookmarkClick = { newsViewModel.toggleBookmark(it) }
                )
            }
        }
    }
}