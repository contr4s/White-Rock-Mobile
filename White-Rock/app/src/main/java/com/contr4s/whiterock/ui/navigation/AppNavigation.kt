package com.contr4s.whiterock.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.contr4s.whiterock.ui.screens.feed.FeedScreen
import com.contr4s.whiterock.ui.screens.friends.FriendsScreen
import com.contr4s.whiterock.ui.screens.gyms.GymDetailScreen
import com.contr4s.whiterock.ui.screens.gyms.GymsScreen
import com.contr4s.whiterock.ui.screens.profile.EditProfileScreen
import com.contr4s.whiterock.ui.screens.profile.ProfileScreen
import com.contr4s.whiterock.ui.screens.routes.RouteDetailsScreen
import com.contr4s.whiterock.ui.screens.routes.RoutesScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = NavRoutes.FEED,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(NavRoutes.FEED) {
                FeedScreen(navController)
            }
            composable(NavRoutes.GYMS) {
                GymsScreen(navController)
            }
            composable(
                route = NavRoutes.GYM_DETAILS,
                arguments = listOf(navArgument("gymId") { type = NavType.StringType })
            ) { backStackEntry ->
                val gymId = backStackEntry.arguments?.getString("gymId") ?: ""
                GymDetailScreen(navController, gymId)
            }
            composable(NavRoutes.FRIENDS) {
                FriendsScreen(navController)
            }
            composable(NavRoutes.ROUTES) {
                RoutesScreen(navController)
            }
            composable(
                route = NavRoutes.ROUTE_DETAILS,
                arguments = listOf(navArgument("routeId") { type = NavType.StringType })
            ) { backStackEntry ->
                val routeId = backStackEntry.arguments?.getString("routeId") ?: ""
                RouteDetailsScreen(navController, routeId)
            }
            composable(NavRoutes.PROFILE) {
                ProfileScreen(navController, null)
            }
            composable(
                route = NavRoutes.PROFILE_ID,
                arguments = listOf(navArgument("userId") { type = NavType.StringType })
            ) { backStackEntry ->
                val userId = backStackEntry.arguments?.getString("userId") ?: ""
                ProfileScreen(navController, userId)
            }
            composable(NavRoutes.EDIT_PROFILE) {
                EditProfileScreen(navController)
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem.Feed,
        BottomNavItem.Gyms,
        BottomNavItem.Routes,
        BottomNavItem.Friends,
        BottomNavItem.Profile
    )
    
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val isMainRoute = items.any { it.route == currentRoute }
    
    if (isMainRoute) {
        NavigationBar(
            modifier = Modifier.testTag("BottomNavigationBar")
        ) {
            items.forEach { item ->
                val selected = currentRoute == item.route
                NavigationBarItem(
                    selected = selected,
                    onClick = { 
                        if (!selected) {
                            navController.navigate(item.route) {
                                popUpTo(NavRoutes.FEED) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    },
                    icon = { 
                        Icon(
                            imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                            contentDescription = item.title
                        )
                    },
                    label = { 
                        Text(
                            text = item.title,
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                        ) 
                    }
                )
            }
        }
    }
}