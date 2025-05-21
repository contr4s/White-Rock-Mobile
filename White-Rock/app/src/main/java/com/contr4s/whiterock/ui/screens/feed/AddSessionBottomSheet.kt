package com.contr4s.whiterock.ui.screens.feed

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.contr4s.whiterock.data.model.ClimbingGym
import com.contr4s.whiterock.data.model.Post
import com.contr4s.whiterock.data.model.Route
import com.contr4s.whiterock.data.model.RouteAttempt
import com.contr4s.whiterock.data.model.SampleData
import com.contr4s.whiterock.ui.theme.Blue
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSessionSheet(
    onDismissRequest: () -> Unit,
    onSessionAdded: (Post) -> Unit
) {
    var selectedGym by remember { mutableStateOf<ClimbingGym?>(null) }
    var showGymSelector by remember { mutableStateOf(false) }
    var comment by remember { mutableStateOf("") }
    var selectedRoutes by remember { mutableStateOf<List<RouteAttempt>>(emptyList()) }
    var showRouteSelector by remember { mutableStateOf(false) }
    
    val scrollState = rememberScrollState()
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(
                onClick = onDismissRequest,
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(Icons.Default.Close, contentDescription = "Закрыть")
            }
            
            Text(
                text = "Новая тренировка",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.align(Alignment.Center)
            )
            
            Button(
                onClick = {
                    // Create a new post from the entered data
                    if (selectedGym != null && (selectedRoutes.isNotEmpty() || comment.isNotEmpty())) {
                        val currentUser = SampleData.getCurrentUser()
                        val newPost = Post(
                            id = UUID.randomUUID(),
                            user = currentUser,
                            gym = selectedGym!!,
                            timestamp = System.currentTimeMillis(),
                            routesCompleted = selectedRoutes,
                            comment = comment.takeIf { it.isNotEmpty() },
                            photoUrl = null, // Could add photo functionality later
                            isLiked = false,
                            likesCount = 0
                        )
                        onSessionAdded(newPost)
                    }
                },
                modifier = Modifier.align(Alignment.CenterEnd),
                enabled = selectedGym != null && (selectedRoutes.isNotEmpty() || comment.isNotEmpty())
            ) {
                Text("Сохранить")
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        OutlinedCard(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showGymSelector = true }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Outlined.Place,
                    contentDescription = null,
                    tint = Blue
                )
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Скалодром",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = selectedGym?.name ?: "Выберите скалодром",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = if (selectedGym == null) FontWeight.Normal else FontWeight.Bold
                    )
                }
                
                Icon(
                    Icons.Outlined.KeyboardArrowRight,
                    contentDescription = "Выбрать"
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Пройденные трассы",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        if (selectedRoutes.isEmpty()) {
            OutlinedCard(
                onClick = { 
                    if (selectedGym != null) showRouteSelector = true 
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = selectedGym != null
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Добавить трассу",
                        tint = if (selectedGym != null) Blue else Color.Gray
                    )
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    Text(
                        text = if (selectedGym != null) "Добавить трассу" else "Сначала выберите скалодром",
                        color = if (selectedGym != null) Blue else Color.Gray
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                selectedRoutes.forEachIndexed { index, routeAttempt ->
                    RouteAttemptItem(
                        routeAttempt = routeAttempt,
                        onRemove = {
                            selectedRoutes = selectedRoutes.toMutableList().also {
                                it.removeAt(index)
                            }
                        },
                        onCompleteToggle = {
                            selectedRoutes = selectedRoutes.toMutableList().also {
                                it[index] = routeAttempt.copy(completed = !routeAttempt.completed)
                            }
                        },
                        onAttemptsChange = { attempts ->
                            selectedRoutes = selectedRoutes.toMutableList().also {
                                it[index] = routeAttempt.copy(attempts = attempts.coerceIn(1, 99))
                            }
                        }
                    )
                    
                    if (index < selectedRoutes.size - 1) {
                        Divider(modifier = Modifier.padding(vertical = 8.dp))
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                OutlinedButton(
                    onClick = { showRouteSelector = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Добавить еще трассу")
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Комментарий",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        OutlinedTextField(
            value = comment,
            onValueChange = { comment = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            placeholder = { Text("Напишите что-нибудь о тренировке...") }
        )
        
        Spacer(modifier = Modifier.height(16.dp))
    }
    
    if (showGymSelector) {
        AlertDialog(
            onDismissRequest = { showGymSelector = false },
            title = { Text("Выберите скалодром") },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    SampleData.gyms.forEach { gym ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedGym = gym
                                    showGymSelector = false
                                }
                                .padding(vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = gym == selectedGym,
                                onClick = {
                                    selectedGym = gym
                                    showGymSelector = false
                                }
                            )
                            
                            Spacer(modifier = Modifier.width(8.dp))
                            
                            Text(
                                text = gym.name,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showGymSelector = false }) {
                    Text("Отмена")
                }
            }
        )
    }
    
    if (showRouteSelector && selectedGym != null) {
        AlertDialog(
            onDismissRequest = { showRouteSelector = false },
            title = { Text("Выберите трассу") },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val gymRoutes = SampleData.routes.filter { it.gymId == selectedGym?.id }
                    
                    gymRoutes.forEach { route ->
                        val alreadyAdded = selectedRoutes.any { it.route == route }
                        
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    if (!alreadyAdded) {
                                        selectedRoutes = selectedRoutes + RouteAttempt(
                                            route = route,
                                            attempts = 1,
                                            completed = false
                                        )
                                        showRouteSelector = false
                                    }
                                }
                                .padding(vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = if (alreadyAdded) Color.Gray else getGradeColor(route.grade),
                                modifier = Modifier
                                    .size(30.dp)
                                    .clip(RoundedCornerShape(4.dp))
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = route.grade,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                            
                            Spacer(modifier = Modifier.width(16.dp))
                            
                            Text(
                                text = route.name,
                                style = MaterialTheme.typography.bodyLarge,
                                color = if (alreadyAdded) Color.Gray else Color.Unspecified
                            )
                            
                            if (alreadyAdded) {
                                Text(
                                    text = " (уже добавлено)",
                                    color = Color.Gray,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showRouteSelector = false }) {
                    Text("Закрыть")
                }
            }
        )
    }
}

@Composable
private fun RouteAttemptItem(
    routeAttempt: RouteAttempt,
    onRemove: () -> Unit,
    onCompleteToggle: () -> Unit,
    onAttemptsChange: (Int) -> Unit
) {
    var attemptsText by remember(routeAttempt.attempts) {
        mutableStateOf(routeAttempt.attempts.toString())
    }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(4.dp),
                color = getGradeColor(routeAttempt.route.grade),
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(4.dp))
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = routeAttempt.route.grade,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = routeAttempt.route.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
                
                Text(
                    text = routeAttempt.route.type,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { 
                        val currentAttempts = attemptsText.toIntOrNull() ?: 1
                        if (currentAttempts > 1) {
                            val newAttempts = currentAttempts - 1
                            attemptsText = newAttempts.toString()
                            onAttemptsChange(newAttempts)
                        }
                    },
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        Icons.Outlined.Remove,
                        contentDescription = "Уменьшить",
                        modifier = Modifier.size(16.dp)
                    )
                }
                
                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(36.dp)
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outline,
                            shape = RoundedCornerShape(4.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    BasicTextField(
                        value = attemptsText,
                        onValueChange = { value ->
                            val filteredValue = value.take(2).filter { it.isDigit() }
                            attemptsText = filteredValue
                            val attempts = filteredValue.toIntOrNull() ?: 1
                            if (attempts > 0) {
                                onAttemptsChange(attempts)
                            }
                        },
                        textStyle = LocalTextStyle.current.copy(
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            fontSize = MaterialTheme.typography.bodyMedium.fontSize
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        decorationBox = { innerTextField ->
                            innerTextField()
                        }
                    )
                }
                
                IconButton(
                    onClick = { 
                        val currentAttempts = attemptsText.toIntOrNull() ?: 1
                        val newAttempts = currentAttempts + 1
                        attemptsText = newAttempts.toString()
                        onAttemptsChange(newAttempts)
                    },
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Увеличить",
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            
            Checkbox(
                checked = routeAttempt.completed,
                onCheckedChange = { onCompleteToggle() }
            )
            
            IconButton(
                onClick = onRemove
            ) {
                Icon(
                    Icons.Outlined.Delete,
                    contentDescription = "Удалить",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
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
