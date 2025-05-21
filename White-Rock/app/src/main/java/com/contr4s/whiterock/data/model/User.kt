package com.contr4s.whiterock.data.model

import java.util.UUID

data class User(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val profilePictureUrl: String = "https://placehold.co/100x100/png",
    val city: String,
    val friends: List<UUID> = emptyList()
)