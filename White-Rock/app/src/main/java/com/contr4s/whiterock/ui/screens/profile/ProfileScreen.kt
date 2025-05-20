package com.contr4s.whiterock.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.contr4s.whiterock.data.model.SampleData
import com.contr4s.whiterock.data.model.User
import com.contr4s.whiterock.ui.screens.shared.PostItem
import com.contr4s.whiterock.ui.theme.Blue
import com.contr4s.whiterock.ui.theme.DarkGray
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController, userId: String?) {
    val isCurrentUser = userId == null || userId == SampleData.CURRENT_USER_ID.toString()
    
    val user = remember {
        if (isCurrentUser) {
            SampleData.getCurrentUser(SampleData.users)
        } else {
            try {
                val uuid = UUID.fromString(userId)
                SampleData.getUserById(uuid, SampleData.users) ?: SampleData.getCurrentUser(SampleData.users)
            } catch (e: Exception) {
                SampleData.getCurrentUser(SampleData.users)
            }
        }
    }
    
    val userPosts = remember {
        SampleData.getPostsByUserId(user.id, SampleData.posts).sortedByDescending { it.timestamp }
    }
    
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Посты", "Статистика")
    
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(user.name) },
                actions = {
                    if (isCurrentUser) {
                        IconButton(onClick = {  }) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "Настройки"
                            )
                        }
                    } else {
                        IconButton(onClick = {  }) {
                            Icon(
                                imageVector = Icons.Default.PersonAdd,
                                contentDescription = "Добавить в друзья"
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            ProfileHeader(
                user = user, 
                isCurrentUser = isCurrentUser,
                onEditProfileClick = {  }
            )
            
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = Blue,
                indicator = { tabPositions ->
                    SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        height = 2.dp,
                        color = Blue
                    )
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = title,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    )
                }
            }
            
            when (selectedTab) {
                0 -> {
                    if (userPosts.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (isCurrentUser) "У вас пока нет постов" else "У пользователя пока нет постов",
                                style = MaterialTheme.typography.bodyLarge,
                                color = DarkGray
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(bottom = 16.dp)
                        ) {
                            items(userPosts) { post ->
                                PostItem(
                                    post = post,
                                    onUserClick = {  },
                                    onGymClick = { gymId ->
                                    },
                                    onLikeClick = { postId ->
                                    }
                                )
                            }
                        }
                    }
                }
                1 -> {
                    UserStatistics(user = user)
                }
            }
        }
    }
}

@Composable
fun ProfileHeader(
    user: User,
    isCurrentUser: Boolean,
    onEditProfileClick: () -> Unit
) {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(user.profilePictureUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Фото профиля",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            if (isCurrentUser) {
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = onEditProfileClick,
                    shape = RoundedCornerShape(20.dp),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 6.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Blue)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Редактировать профиль",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Редактировать профиль",
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = user.name,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            
            Text(
                text = "@${SampleData.getUserUsername(user.id)}",
                style = MaterialTheme.typography.bodyLarge,
                color = DarkGray,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                InfoChip(
                    icon = Icons.Outlined.LocationOn,
                    text = user.city
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                InfoChip(
                    icon = Icons.Outlined.Star,
                    text = SampleData.getUserLevel(user.id)
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                InfoChip(
                    icon = Icons.Outlined.Timer,
                    text = SampleData.getUserExperience(user.id)
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                val visitCount = SampleData.getPostsByUserId(user.id, SampleData.posts).size
                StatItem(count = "$visitCount", title = "Посещений")
                
                val completedRoutes = SampleData.getPostsByUserId(user.id, SampleData.posts)
                    .flatMap { it.routesCompleted }
                    .count { it.completed }
                StatItem(count = "$completedRoutes", title = "Трасс")
                
                val friendsCount = user.friends.size
                StatItem(count = "$friendsCount", title = "Друзей")
            }
        }
    }
}

@Composable
fun InfoChip(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String
) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun StatItem(
    count: String,
    title: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = count,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall,
            color = DarkGray
        )
    }
}

@Composable
fun UserStatistics(user: User) {
    val statistics = SampleData.getUserStatistics(user.id, SampleData.posts)
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Общая статистика",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    StatRow(
                        title = "Пройдено трасс:",
                        value = "${statistics.totalRoutesClimbed}"
                    )
                    
                    StatRow(
                        title = "Средняя категория:",
                        value = statistics.averageGrade
                    )
                    
                    StatRow(
                        title = "Самая сложная трасса:",
                        value = statistics.hardestRoute
                    )
                    
                    StatRow(
                        title = "Любимый скалодром:",
                        value = statistics.favoriteGym
                    )
                }
            }
        }
        
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "График прогресса",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "График прогресса пользователя",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StatRow(
    title: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(navController = androidx.navigation.compose.rememberNavController(), userId = null)
}
