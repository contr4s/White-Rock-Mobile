package com.contr4s.whiterock.ui.screens.feed

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.contr4s.whiterock.presentation.feed.FeedType
import com.contr4s.whiterock.presentation.feed.FeedViewModel
import com.contr4s.whiterock.presentation.feed.FeedIntent
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(navController: NavController, viewModel: FeedViewModel = hiltViewModel()) {
    val state = viewModel.container.stateFlow.collectAsStateWithLifecycle().value
    
    // Bottom sheet state
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.onIntent(FeedIntent.LoadPosts)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Лента") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showBottomSheet = true },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Добавить тренировку",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                FilterChip(
                    selected = state.feedType == FeedType.ALL,
                    onClick = { viewModel.onIntent(FeedIntent.ChangeFeedType(FeedType.ALL)) },
                    label = { Text("Все") }
                )
                Spacer(modifier = Modifier.width(8.dp))
                FilterChip(
                    selected = state.feedType == FeedType.FRIENDS,
                    onClick = { viewModel.onIntent(FeedIntent.ChangeFeedType(FeedType.FRIENDS)) },
                    label = { Text("Друзья") }
                )
            }

            if (state.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(state.posts) { post ->
                        PostItem(
                            post = post,
                            isLiked = post.isLiked,
                            likesCount = post.likesCount,
                            onUserClick = { userId ->
                                navController.navigate(NavRoutes.profile(userId.toString()))
                            },
                            onGymClick = { gymId ->
                                navController.navigate(NavRoutes.gymDetails(gymId.toString()))
                            },
                            onLikeClick = { postId ->
                                viewModel.onIntent(FeedIntent.ToggleLike(postId))
                            }
                        )
                    }
                }
            }
        }
    }
    
    // Show bottom sheet when requested
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState
        ) {
            AddSessionSheet(
                onDismissRequest = { showBottomSheet = false },
                onSessionAdded = { newPost ->
                    viewModel.onIntent(FeedIntent.AddPost(newPost))
                    showBottomSheet = false
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FeedScreenPreview() {
    FeedScreen(navController = androidx.navigation.compose.rememberNavController())
}
