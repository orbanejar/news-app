package com.example.newsapp.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import com.example.newsapp.data.model.NewsArticle

/**
 * A composable function that displays the detailed view of a news article.
 *
 * @param article The news article to be displayed.
 * @param isBookmarked Indicates if the article is bookmarked.
 * @param onBookmarkClick Callback triggered when the bookmark icon is clicked.
 */
@Composable
fun NewsDetailScreen(
    article: NewsArticle,
    isBookmarked: Boolean,
    onBookmarkClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()) // Enables scrolling for longer content
            .padding(16.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            // Display the news image
            AsyncImage(
                model = article.mediaUrl,
                contentDescription = article.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            // Bookmark button overlay (Top-right corner)
            // TODO: To modify behavior when bookmark icon is clicked
            IconButton(
                onClick = onBookmarkClick,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp)
                    .background(Color.White.copy(alpha = 0.8f), shape = RoundedCornerShape(50))
                    .padding(8.dp)
            ) {
                Icon(
                    imageVector = if (isBookmarked) Icons.Outlined.Bookmark else Icons.Outlined.BookmarkBorder,
                    contentDescription = "Bookmark",
                    tint = if (isBookmarked) Color(0xFFFFA500) else Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Article title
        Text(
            text = article.title,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Source & Published Date
        // TODO: To modify date display format
        Text(
            text = "Source: ${article.sourceTitle}",
            fontSize = 14.sp,
            color = Color.Gray
        )
        Text(
            text = "Published: ${article.pubDate}",
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Article Description
        Text(
            text = article.description ?: "No description available",
            fontSize = 16.sp,
            lineHeight = 22.sp
        )
    }
}
