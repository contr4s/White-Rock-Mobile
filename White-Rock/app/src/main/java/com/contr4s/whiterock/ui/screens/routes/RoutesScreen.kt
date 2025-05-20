package com.contr4s.whiterock.ui.screens.routes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.contr4s.whiterock.data.model.Route
import com.contr4s.whiterock.data.model.SampleData
import com.contr4s.whiterock.ui.navigation.NavRoutes
import com.contr4s.whiterock.ui.theme.Blue
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutesScreen(navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedGymId by remember { mutableStateOf<UUID?>(null) }
    var selectedDifficulty by remember { mutableStateOf<String?>(null) }
    var showFilterDialog by remember { mutableStateOf(false) }
    
    val routes = remember { SampleData.routes }
    val filteredRoutes = remember(routes, searchQuery, selectedGymId, selectedDifficulty) {
        val difficulty = selectedDifficulty
        routes.filter { route ->
            val matchesSearch = searchQuery.isEmpty() || 
                route.name.contains(searchQuery, ignoreCase = true)
            val matchesGym = selectedGymId == null || route.gymId == selectedGymId
            val matchesDifficulty = difficulty == null || 
                route.grade.startsWith(difficulty)
            matchesSearch && matchesGym && matchesDifficulty
        }
    }
    
    val difficultyLevels = listOf("3", "4", "5", "6", "7", "8", "9")
    val gyms = remember { 
        SampleData.gyms.map { it.id to it.name }.sortedBy { it.second } 
    }
    
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Трассы", style = MaterialTheme.typography.headlineMedium) },
                actions = {
                    IconButton(onClick = { showFilterDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = "Фильтр"
                        )
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
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                placeholder = { Text("Поиск трасс...") },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Поиск") },
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Blue,
                    cursorColor = Blue
                )
            )
            
            if (selectedGymId != null || selectedDifficulty != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    selectedGymId?.let { gymId ->
                        val gymName = gyms.first { it.first == gymId }.second
                        FilterChip(
                            selected = true,
                            onClick = { selectedGymId = null },
                            label = { Text(gymName) },
                            trailingIcon = {
                                Icon(
                                    Icons.Default.FilterList, 
                                    contentDescription = "Очистить фильтр"
                                )
                            }
                        )
                    }
                    
                    selectedDifficulty?.let { difficulty ->
                        FilterChip(
                            selected = true,
                            onClick = { selectedDifficulty = null },
                            label = { Text("Категория $difficulty") },
                            trailingIcon = {
                                Icon(
                                    Icons.Default.FilterList, 
                                    contentDescription = "Очистить фильтр"
                                )
                            }
                        )
                    }
                }
            }
            
            if (filteredRoutes.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Трассы не найдены",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(filteredRoutes) { route ->
                        RouteItem(
                            route = route,
                            onRouteClick = { clickedRoute ->
                                navController.navigate(NavRoutes.routeDetails(clickedRoute.id.toString()))
                            }
                        )
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                    }
                }
            }
        }
        
        if (showFilterDialog) {
            AlertDialog(
                onDismissRequest = { showFilterDialog = false },
                title = { Text("Фильтр трасс") },
                text = {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Выберите скалодром:",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                        
                        gyms.forEach { (id, name) ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { 
                                        selectedGymId = if (selectedGymId == id) null else id
                                    }
                                    .padding(vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = selectedGymId == id,
                                    onClick = { 
                                        selectedGymId = if (selectedGymId == id) null else id
                                    }
                                )
                                Text(
                                    text = name,
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }
                        }
                        
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                        
                        Text(
                            text = "Выберите категорию сложности:",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            difficultyLevels.forEach { difficulty ->
                                FilterChip(
                                    selected = selectedDifficulty == difficulty,
                                    onClick = { 
                                        selectedDifficulty = if (selectedDifficulty == difficulty) null else difficulty
                                    },
                                    label = { Text(difficulty) }
                                )
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(onClick = { showFilterDialog = false }) {
                        Text("Применить")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        selectedGymId = null
                        selectedDifficulty = null
                        showFilterDialog = false
                    }) {
                        Text("Сбросить")
                    }
                }
            )
        }
    }
}

@Composable
fun RouteItem(
    route: Route,
    onRouteClick: (Route) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onRouteClick(route) }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .padding(end = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier.size(40.dp),
                shape = RoundedCornerShape(20.dp),
                color = getGradeColor(route.grade)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = route.grade,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = route.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(4.dp))
            
            val gym = SampleData.getGymById(route.gymId, SampleData.gyms)
            Text(
                text = "${route.type} • ${gym?.name ?: "Неизвестный зал"}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = "★ ${route.rating}",
                style = MaterialTheme.typography.labelLarge,
                color = Blue
            )
        }
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
