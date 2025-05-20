package com.contr4s.whiterock.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Route
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.Route
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    object Feed : BottomNavItem(
        route = NavRoutes.FEED,
        title = "Лента",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home
    )
    
    object Gyms : BottomNavItem(
        route = NavRoutes.GYMS,
        title = "Скалодромы",
        selectedIcon = Icons.Filled.Place,
        unselectedIcon = Icons.Outlined.Place
    )
    
    object Routes : BottomNavItem(
        route = NavRoutes.ROUTES,
        title = "Трассы",
        selectedIcon = Icons.Filled.Route,
        unselectedIcon = Icons.Outlined.Route
    )
    
    object Friends : BottomNavItem(
        route = NavRoutes.FRIENDS,
        title = "Друзья",
        selectedIcon = Icons.Filled.People,
        unselectedIcon = Icons.Outlined.People
    )
    
    object Profile : BottomNavItem(
        route = NavRoutes.PROFILE,
        title = "Профиль",
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person
    )
}