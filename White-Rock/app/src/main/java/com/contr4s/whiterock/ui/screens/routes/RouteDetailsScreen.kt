package com.contr4s.whiterock.ui.screens.routes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Person
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
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.contr4s.whiterock.data.model.SampleData
import com.contr4s.whiterock.ui.navigation.NavRoutes
import com.contr4s.whiterock.ui.theme.Blue
import com.contr4s.whiterock.ui.theme.DarkGray
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteDetailsScreen(navController: NavController, routeId: String) {
    val route = remember { 
        try {
            val uuid = UUID.fromString(routeId)
            SampleData.getRouteById(uuid)
        } catch (e: Exception) {
            null
        }
    }
    
    val comments = remember { 
        if (route != null) {
            try {
                val uuid = UUID.fromString(routeId)
                SampleData.getCommentsByRouteId(uuid)
            } catch (e: Exception) {
                emptyList()
            }
        } else {
            emptyList()
        }
    }
    
    var commentText by remember { mutableStateOf("") }
    var userRating by remember { mutableStateOf<Int?>(null) }
    
    val scrollState = rememberScrollState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(route?.name ?: "Маршрут") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { paddingValues ->
        if (route == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("Маршрут не найден")
            }
            return@Scaffold
        }
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            modifier = Modifier.size(48.dp),
                            shape = RoundedCornerShape(24.dp),
                            color = getGradeColor(route.grade)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = route.grade,
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.width(16.dp))
                        
                        Column {
                            Text(
                                text = route.name,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${route.type} • ${route.color}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    val gym = SampleData.getGymById(route.gymId)
                    if (gym != null) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { navController.navigate(NavRoutes.gymDetails(gym.id.toString())) },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Place,
                                contentDescription = null,
                                tint = Blue
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = gym.name,
                                style = MaterialTheme.typography.bodyLarge,
                                color = Blue
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Person,
                            contentDescription = null,
                            tint = DarkGray
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Поставил: ${route.setter}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    if (route.description != null) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.Top
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Info,
                                contentDescription = null,
                                tint = DarkGray,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = route.description,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Рейтинг: ",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${route.rating}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Row {
                            repeat(5) { index ->
                                val starColor = if (index < route.rating) Blue else Color.LightGray
                                Icon(
                                    imageVector = Icons.Filled.Star,
                                    contentDescription = null,
                                    tint = starColor,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                    
                    HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
                    
                    Text(
                        text = "Оценить трассу:",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(modifier = Modifier.fillMaxWidth()) {
                        repeat(5) { index ->
                            val starFilled = userRating != null && index < userRating!!
                            Icon(
                                imageVector = Icons.Filled.Star,
                                contentDescription = "Звезда ${index + 1}",
                                tint = if (starFilled) Blue else Color.LightGray.copy(alpha = 0.7f),
                                modifier = Modifier
                                    .size(32.dp)
                                    .clickable {
                                        userRating = index + 1
                                    }
                                    .padding(4.dp)
                            )
                        }
                    }
                }
            }
            
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Комментарии (${comments.size})",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    OutlinedTextField(
                        value = commentText,
                        onValueChange = { commentText = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Добавьте комментарий...") },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    commentText = ""
                                },
                                enabled = commentText.isNotBlank()
                            ) {
                                Icon(
                                    Icons.AutoMirrored.Filled.Send,
                                    contentDescription = "Отправить",
                                    tint = if (commentText.isNotBlank()) Blue else Color.Gray
                                )
                            }
                        },
                        maxLines = 3
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    if (comments.isEmpty()) {
                        Text(
                            text = "Еще нет комментариев к этой трассе",
                            style = MaterialTheme.typography.bodyMedium,
                            color = DarkGray
                        )
                    } else {
                        comments.forEach { comment ->
                            CommentItem(comment = comment)
                            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun CommentItem(comment: SampleData.Comment) {
    val context = LocalContext.current
    val user = comment.user
    val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
    
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(user.profilePictureUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "Аватар пользователя",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = user.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = dateFormat.format(Date(comment.timestamp)),
                    style = MaterialTheme.typography.bodySmall,
                    color = DarkGray
                )
            }
            
            if (comment.rating != null) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = null,
                        tint = Blue,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${comment.rating}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
        
        Text(
            text = comment.text,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(start = 48.dp, top = 8.dp)
        )
    }
}

private fun getGradeColor(grade: String): Color {
    return when {
        grade.startsWith("3") -> Color(0xFF4CAF50)
        grade.startsWith("4") -> Color(0xFF8BC34A)
        grade.startsWith("5") -> Color(0xFFCDDC39)
        grade.startsWith("6A") || grade.startsWith("6a") -> Color(0xFFFFEB3B)
        grade.startsWith("6B") || grade.startsWith("6b") -> Color(0xFFFFC107)
        grade.startsWith("6C") || grade.startsWith("6c") -> Color(0xFFFF9800)
        grade.startsWith("7A") || grade.startsWith("7a") -> Color(0xFFFF5722)
        grade.startsWith("7B") || grade.startsWith("7b") -> Color(0xFFF44336)
        grade.startsWith("7C") || grade.startsWith("7c") -> Color(0xFFE91E63)
        grade.startsWith("8") -> Color(0xFF9C27B0)
        grade.startsWith("9") -> Color(0xFF673AB7)
        else -> Color(0xFF212121)
    }
}

