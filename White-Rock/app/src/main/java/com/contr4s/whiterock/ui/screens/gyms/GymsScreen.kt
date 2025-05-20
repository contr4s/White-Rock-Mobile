package com.contr4s.whiterock.ui.screens.gyms

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.contr4s.whiterock.data.model.ClimbingGym
import com.contr4s.whiterock.data.model.SampleData
import com.contr4s.whiterock.ui.navigation.NavRoutes
import com.contr4s.whiterock.ui.theme.Blue
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GymsScreen(navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }
    val gyms = remember { SampleData.gyms }
    val filteredGyms = remember(searchQuery) {
        if (searchQuery.isBlank()) {
            gyms
        } else {
            gyms.filter {
                it.name.contains(searchQuery, ignoreCase = true) || 
                it.city.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Скалодромы") },
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
                placeholder = { Text("Поиск скалодромов...") },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Поиск") },
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Blue,
                    cursorColor = Blue
                )
            )

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(filteredGyms) { gym -> 
                    GymItem(
                        gym = gym,
                        onGymClick = {
                            navController.navigate(NavRoutes.gymDetails(it.id.toString()))
                        }
                    )
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                }
            }
        }
    }
}

@Composable
fun GymItem(
    gym: ClimbingGym,
    onGymClick: (ClimbingGym) -> Unit
) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onGymClick(gym) },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            val (imageRef, nameRef, cityRef, statsRef) = createRefs()

            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(gym.photoUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "Фото скалодрома ${gym.name}",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .constrainAs(imageRef) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        bottom.linkTo(parent.bottom)
                    }
            )

            Text(
                text = gym.name,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.constrainAs(nameRef) {
                    top.linkTo(parent.top)
                    start.linkTo(imageRef.end, margin = 16.dp)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                }
            )

            Text(
                text = "${gym.city}, ${gym.address}",
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.constrainAs(cityRef) {
                    top.linkTo(nameRef.bottom, margin = 4.dp)
                    start.linkTo(nameRef.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                }
            )

            Row(
                modifier = Modifier.constrainAs(statsRef) {
                    top.linkTo(cityRef.bottom, margin = 4.dp)
                    start.linkTo(nameRef.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                }
            ) {
                Text(
                    text = "${gym.routes.size} трасс",
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "★ ${gym.rating}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Blue
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GymsScreenPreview() {
    GymsScreen(navController = androidx.navigation.compose.rememberNavController())
}
