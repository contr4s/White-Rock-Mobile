package com.contr4s.whiterock.data.model

import java.util.UUID

data class UserStatistics(
    val userId: UUID,
    val totalRoutesClimbed: Int,
    val averageGrade: String,
    val hardestRoute: String,
    val favoriteGym: String
)