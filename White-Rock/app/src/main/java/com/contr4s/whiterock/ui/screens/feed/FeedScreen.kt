package com.contr4s.whiterock.ui.screens.feed

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.contr4s.whiterock.data.model.Post
import com.contr4s.whiterock.data.model.SampleData
import com.contr4s.whiterock.ui.navigation.NavRoutes
import com.contr4s.whiterock.ui.screens.shared.PostItem
import com.contr4s.whiterock.ui.theme.DarkGray
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(navController: NavController) {
    val currentUser = remember { SampleData.getCurrentUser() }

    var allPosts by remember { mutableStateOf<List<Post>>(emptyList()) }
    LaunchedEffect(Unit) {
        allPosts = SampleData.getAllPosts().sortedByDescending { it.timestamp }
    }

    var feedType by remember { mutableStateOf(FeedType.ALL) }

    val filteredPosts = remember(allPosts, feedType) {
        when (feedType) {
            FeedType.ALL -> allPosts
            FeedType.FRIENDS -> {
                val friendIds = currentUser.friends
                allPosts.filter { post -> friendIds.contains(post.user.id) }
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Лента") },
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
            TabRow(
                selectedTabIndex = feedType.ordinal,
                contentColor = MaterialTheme.colorScheme.primary,
                divider = {}
            ) {
                FeedType.values().forEachIndexed { index, type ->
                    Tab(
                        selected = feedType.ordinal == index,
                        onClick = { feedType = type },
                        text = { Text(type.title) }
                    )
                }
            }

            if (filteredPosts.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = when (feedType) {
                            FeedType.ALL -> "В ленте пока нет постов"
                            FeedType.FRIENDS -> "Ваши друзья ещё не публиковали посты"
                        },
                        style = MaterialTheme.typography.bodyLarge,
                        color = DarkGray
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(filteredPosts) { post ->
                        PostItem(
                            post = post,
                            onUserClick = { userId ->
                                navController.navigate(NavRoutes.profile(userId.toString()))
                            },
                            onGymClick = { gymId ->
                                navController.navigate(NavRoutes.gymDetails(gymId.toString()))
                            },
                            onLikeClick = { postId ->
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FeedScreenPreview() {
    FeedScreen(navController = androidx.navigation.compose.rememberNavController())
}

enum class FeedType(val title: String) {
    ALL("Все"),
    FRIENDS("Друзья")
}
