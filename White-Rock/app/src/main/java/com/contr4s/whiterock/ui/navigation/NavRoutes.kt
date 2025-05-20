package com.contr4s.whiterock.ui.navigation

object NavRoutes {
    const val FEED = "feed"
    const val GYMS = "gyms"
    const val GYM_DETAILS = "gym_details/{gymId}"
    const val FRIENDS = "friends"
    const val ROUTES = "routes"
    const val ROUTE_DETAILS = "route_details/{routeId}"
    const val PROFILE = "profile"
    const val PROFILE_ID = "profile/{userId}"
    
    fun gymDetails(gymId: String) = "gym_details/$gymId"
    fun routeDetails(routeId: String) = "route_details/$routeId"
    fun profile(userId: String) = "profile/$userId"
}