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
import com.contr4s.whiterock.presentation.friends.FriendsIntent
import com.contr4s.whiterock.presentation.friends.FriendsViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendsScreen(navController: NavController, viewModel: FriendsViewModel = hiltViewModel()) {
    val state = viewModel.container.stateFlow.collectAsState().value

    LaunchedEffect(Unit) {
        viewModel.onIntent(FriendsIntent.LoadFriends)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        OutlinedTextField(
            value = state.searchQuery,
            onValueChange = { viewModel.onIntent(FriendsIntent.Search(it)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            placeholder = { Text("Поиск друзей...") },
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Поиск") },
            singleLine = true,
            shape = RoundedCornerShape(8.dp)
        )

        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(state.friends) { user ->
                    FriendItem(user = user, onActionClick = { userId ->
                        navController.navigate("profile/${userId}")
                    })
                }
            }
        }
    }
}

@Composable
fun FriendItem(user: com.contr4s.whiterock.data.model.User, onActionClick: (UUID) -> Unit) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onActionClick(user.id) }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(user.profilePictureUrl)
                .crossfade(true)
                .build(),
            contentDescription = "Аватар пользователя",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
        )
        Column(modifier = Modifier.padding(start = 12.dp)) {
            Text(text = user.name, fontWeight = FontWeight.Bold)
            Text(text = user.city, style = MaterialTheme.typography.bodySmall)
        }
        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = { onActionClick(user.id) }) {
            Icon(Icons.Filled.PersonAdd, contentDescription = "Добавить в друзья")
        }
    }
}