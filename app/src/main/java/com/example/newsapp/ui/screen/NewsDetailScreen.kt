package com.example.newsapp.ui.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.newsapp.data.model.NewsArticle
import com.example.newsapp.viewmodel.NewsViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Displays the detailed view of a news article.
 *
 * If an image is available, it is displayed with a bookmark overlay.
 * If there is no image, the article title and bookmark icon are shown in a row at the top.
 *
 * @param article The news article to display.
 * @param viewModel The NewsViewModel providing the bookmarked state.
 * @param onBookmarkClick Callback triggered when the bookmark icon is clicked.
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NewsDetailScreen(
    article: NewsArticle,
    viewModel: NewsViewModel,
    onBookmarkClick: () -> Unit
) {
    // Collect bookmarked news state from the ViewModel
    val bookmarkedNews by viewModel.bookmarkedNews.collectAsState()
    // Determine if the current article is bookmarked
    val isBookmarked = bookmarkedNews.any { it.id == article.id }
    val parsedDateTime = LocalDateTime.parse(article.pubDate)
    val formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy 'at' hh:mm a")
    val formattedDateTime = parsedDateTime.format(formatter)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()) // Enable scrolling for long content
            .padding(16.dp)
    ) {
        if (article.mediaUrl != null) {
            // When image is available, show the image container with bookmark overlay.
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            ) {
                AsyncImage(
                    model = article.mediaUrl,
                    contentDescription = article.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
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
            // Display the title below the image.
            Text(
                text = article.title,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
        } else {
            // If there's no image, display the title and bookmark icon in a row.
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = article.title,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                IconButton(
                    onClick = onBookmarkClick,
                    modifier = Modifier
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
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Source & Published Date
        Text(
            text = "Source: ${article.sourceTitle}",
            fontSize = 14.sp,
            color = Color.Gray
        )
        Text(
            text = "Published: $formattedDateTime",
            fontSize = 14.sp,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(12.dp))

        // Article Description
        Text(
            text = article.description.ifEmpty { "No description available" },
            fontSize = 16.sp,
            lineHeight = 22.sp
        )
    }
}
