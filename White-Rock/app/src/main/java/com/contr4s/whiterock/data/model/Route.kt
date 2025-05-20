package com.contr4s.whiterock.data.model

import java.util.UUID

data class Route(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val gymId: UUID,
    val grade: String,
    val color: String,
    val type: String,
    val imageUrl: String? = "https://placehold.co/300x200/png",
    val description: String? = null,
    val creationDate: Long,
    val setter: String?,
    val rating: Float = 0f
)