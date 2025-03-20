package com.example.newsapp.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import com.example.newsapp.data.model.NewsArticle

/**
 * A composable function that displays a news article in a card format.
 *
 * @param article The news article to be displayed.
 * @param isBookmarked A boolean indicating whether the article is bookmarked.
 * @param onBookmarkClick Callback triggered when the bookmark icon is clicked.
 * @param onClick Callback triggered when the card is clicked.
 */
@Composable
fun NewsCard(
    article: NewsArticle,
    isBookmarked: Boolean,
    onBookmarkClick: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = 8.dp,
        modifier = Modifier
            .padding(8.dp)
            .clickable { onClick() }
    ) {
        // Image section
        Column(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp) // Fixed height for image container
            ) {
                if (article.mediaUrl != null) {
                    AsyncImage(
                        model = article.mediaUrl,
                        contentDescription = article.title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    // Placeholder for missing image
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                            .background(Color.LightGray),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No Image",
                            style = MaterialTheme.typography.caption,
                            color = Color.DarkGray
                        )
                    }
                }

                // Bookmark Icon positioned at the top-right
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .background(
                            brush = Brush.radialGradient(
                                listOf(Color.Black.copy(alpha = 0.2f), Color.Transparent)
                            ),
                            shape = CircleShape
                        )
                ) {
                    IconButton(onClick = onBookmarkClick) {
                        Icon(
                            imageVector = if (isBookmarked) Icons.Outlined.Bookmark else Icons.Outlined.BookmarkBorder,
                            contentDescription = "Bookmark",
                            tint = if (isBookmarked) Color(0xFFFFA500) else Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            // Article details
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                // Title
                Text(
                    text = article.title,
                    style = MaterialTheme.typography.subtitle1,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))

                // Article source
                Text(
                    text = article.sourceTitle,
                    style = MaterialTheme.typography.caption
                )
                Spacer(modifier = Modifier.height(4.dp))

                // Featured tag or reserved space
                if (article.isFeatured) {
                    Text(
                        text = "FEATURED",
                        style = MaterialTheme.typography.overline,
                        color = MaterialTheme.colors.primary
                    )
                } else {
                    // Reserve space when not featured (to have uniform card height)
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}
