package com.contr4s.whiterock.data.model

import java.util.UUID

data class Comment(
    val id: UUID = UUID.randomUUID(),
    val route: Route,
    val user: User,
    val text: String,
    val timestamp: Long,
    val rating: Int? = null
)