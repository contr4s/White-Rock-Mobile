package com.contr4s.whiterock.data.model

import java.util.UUID

data class ClimbingGym(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val city: String,
    val address: String,
    val description: String? = null,
    val rating: Double,
    val routes: List<Route>,
    val photoUrl: String = "https://placehold.co/600x400/png",
    val workingHours: String,
    val phone: String,
    val website: String? = null,
    val priceList: List<String>? = null,
    val amenities: List<String>? = null
)