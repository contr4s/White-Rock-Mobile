package com.contr4s.whiterock.ui.screens.friends

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.contr4s.whiterock.data.model.User
import com.contr4s.whiterock.ui.navigation.NavRoutes
import com.contr4s.whiterock.ui.theme.Blue
import com.contr4s.whiterock.ui.theme.DarkGray
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.contr4s.whiterock.data.model.SampleData
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendsScreen(navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }
    var showFindFriends by remember { mutableStateOf(false) }
    var selectedFilter by remember { mutableStateOf(FriendFilter.ALL) }
    
    val currentUser = remember { SampleData.getCurrentUser(SampleData.users) }
    val currentFriends = remember { SampleData.getFriendsByUserId(currentUser.id, SampleData.users) }
    val suggestedFriends = remember { 
        SampleData.users.filter { it.id != currentUser.id && !currentUser.friends.contains(it.id) }
    }
    
    val displayedFriends = when {
        showFindFriends -> suggestedFriends
        else -> currentFriends.filter {
            when (selectedFilter) {
                FriendFilter.ALL -> true
                FriendFilter.CITY -> it.city == currentUser.city
            }
        }
    }
    
    val filteredFriends = remember(displayedFriends, searchQuery) {
        if (searchQuery.isBlank()) {
            displayedFriends
        } else {
            displayedFriends.filter {
                it.name.contains(searchQuery, ignoreCase = true) ||
                it.city.contains(searchQuery, ignoreCase = true) ||
                SampleData.getUserLevel(it.id).contains(searchQuery, ignoreCase = true)
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Друзья", style = MaterialTheme.typography.headlineMedium) },
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
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                placeholder = { Text("Поиск друзей...") },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Поиск") },
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Blue,
                    cursorColor = Blue
                )
            )
            
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (!showFindFriends) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FriendFilter.values().forEach { filter ->
                            FilterChip(
                                selected = selectedFilter == filter,
                                onClick = { selectedFilter = filter },
                                label = { Text(filter.label) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Blue,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }
                }
                
                TextButton(
                    onClick = { showFindFriends = !showFindFriends },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Blue
                    )
                ) {
                    Icon(
                        imageVector = if (showFindFriends) Icons.Outlined.Person else Icons.Filled.PersonAdd,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(if (showFindFriends) "Мои друзья" else "Найти друзей")
                }
            }
            
            Text(
                text = if (showFindFriends) "Рекомендуемые друзья" else "Мои друзья",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            
            if (filteredFriends.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (showFindFriends) 
                            "Нет рекомендуемых друзей" 
                        else 
                            "Нет друзей, соответствующих поиску",
                        style = MaterialTheme.typography.bodyMedium,
                        color = DarkGray
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(filteredFriends) { user ->
                        FriendItem(
                            user = user,
                            isFriend = !showFindFriends,
                            onFriendClick = { userId ->
                                navController.navigate(NavRoutes.profile(userId.toString()))
                            },
                            onActionClick = { userId ->
                            }
                        )
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun FriendItem(
    user: User,
    isFriend: Boolean,
    onFriendClick: (UUID) -> Unit,
    onActionClick: (UUID) -> Unit
) {
    val context = LocalContext.current
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onFriendClick(user.id) }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(user.profilePictureUrl)
                .crossfade(true)
                .build(),
            contentDescription = "Фото ${user.name}",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
        )
        
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = user.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${user.city} • ${SampleData.getUserLevel(user.id)}",
                style = MaterialTheme.typography.bodyMedium,
                color = DarkGray
            )
        }
        
        IconButton(
            onClick = { onActionClick(user.id) }
        ) {
            Icon(
                imageVector = if (isFriend) Icons.Outlined.Close else Icons.Filled.PersonAdd,
                contentDescription = if (isFriend) "Удалить из друзей" else "Добавить в друзья",
                tint = if (isFriend) DarkGray else Blue
            )
        }
    }
}

enum class FriendFilter(val label: String) {
    ALL("Все"),
    CITY("Мой город")
}

@Preview(showBackground = true)
@Composable
fun FriendsScreenPreview() {
    FriendsScreen(navController = androidx.navigation.compose.rememberNavController())
}