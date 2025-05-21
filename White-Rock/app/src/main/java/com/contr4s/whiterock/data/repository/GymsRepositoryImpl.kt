package com.contr4s.whiterock.data.repository

import com.contr4s.whiterock.data.model.ClimbingGym
import com.contr4s.whiterock.data.model.SampleData
import java.util.UUID
import javax.inject.Inject

class GymsRepositoryImpl @Inject constructor() : GymsRepository {
    override suspend fun getGyms(): List<ClimbingGym> = SampleData.gyms
    override suspend fun getGymById(id: UUID): ClimbingGym? = SampleData.gyms.find { it.id == id }
}
