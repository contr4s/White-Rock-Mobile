package com.contr4s.whiterock.data.repository

import com.contr4s.whiterock.data.model.ClimbingGym
import java.util.UUID

interface GymsRepository {
    suspend fun getGyms(): List<ClimbingGym>
    suspend fun getGymById(id: UUID): ClimbingGym?
}
