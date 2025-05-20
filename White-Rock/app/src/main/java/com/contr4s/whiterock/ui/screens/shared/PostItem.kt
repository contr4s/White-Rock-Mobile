package com.contr4s.whiterock.ui.screens.shared

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.contr4s.whiterock.R
import com.contr4s.whiterock.data.model.Post
import com.contr4s.whiterock.data.model.SampleData
import com.contr4s.whiterock.ui.theme.Blue
import com.contr4s.whiterock.ui.theme.DarkGray
import com.contr4s.whiterock.ui.theme.Orange
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun PostItem(
    post: Post,
    onUserClick: (UUID) -> Unit,
    onGymClick: (UUID) -> Unit,
    onLikeClick: (UUID) -> Unit
) {
    val context = LocalContext.current
    val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
    val formattedDate = dateFormat.format(Date(post.timestamp))
    
    var isLiked by remember { mutableStateOf(false) }
    var likesCount by remember { mutableStateOf(0) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(post.user.profilePictureUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Аватар ${post.user.name}",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .clickable { onUserClick(post.user.id) }
                )
                
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 12.dp)
                        .clickable { onUserClick(post.user.id) }
                ) {
                    Text(
                        text = post.user.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = formattedDate,
                        style = MaterialTheme.typography.bodySmall,
                        color = DarkGray
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable { onGymClick(post.gym.id) }
                    .padding(vertical = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Place,
                    contentDescription = "Скалодром",
                    tint = Blue,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = post.gym.name,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Blue,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
            
            if (post.routesCompleted.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Трассы:",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
                
                post.routesCompleted.forEach { route ->
                    val statusText = if (route.completed) 
                        "пролез с ${route.attempts} попытки" 
                    else 
                        "не пролез (${route.attempts} попытки)"
                    
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Text(
                            text = "• ${route.route.grade}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = getGradeColor(route.route.grade)
                        )
                        Text(
                            text = " - $statusText",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
            
            if (!post.comment.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = post.comment,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            
            if (!post.photoUrl.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(post.photoUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Фото скалолазания",
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { 
                        isLiked = !isLiked
                        likesCount = if (isLiked) likesCount + 1 else likesCount - 1
                        onLikeClick(post.id) 
                    }
                ) {
                    Icon(
                        imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = "Мне нравится",
                        tint = if (isLiked) Orange else MaterialTheme.colorScheme.onSurface
                    )
                }
                Text(
                    text = "$likesCount",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun getGradeColor(grade: String): Color {
    return when {
        grade.startsWith("3") || grade.startsWith("4") -> Color(0xFF4CAF50)
        grade.startsWith("5") -> Color(0xFF2196F3)
        grade.startsWith("6") -> Color(0xFFFF9800)
        grade.startsWith("7") -> Color(0xFFE53935)
        grade.startsWith("8") || grade.startsWith("9") -> Color(0xFF212121)
        else -> MaterialTheme.colorScheme.onSurface
    }
}