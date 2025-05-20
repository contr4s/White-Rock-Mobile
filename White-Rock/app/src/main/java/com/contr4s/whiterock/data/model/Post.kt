package com.contr4s.whiterock.data.model

import java.util.UUID

data class Post(
    val id: UUID = UUID.randomUUID(),
    val user: User,
    val gym: ClimbingGym,
    val timestamp: Long,
    val routesCompleted: List<RouteAttempt>,
    val photoUrl: String? = "https://placehold.co/400x300/png",
    val comment: String? = null,
)

data class RouteAttempt(
    val route: Route,
    val attempts: Int,
    val completed: Boolean = false,
)