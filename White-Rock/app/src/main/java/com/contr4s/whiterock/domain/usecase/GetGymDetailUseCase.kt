package com.contr4s.whiterock.domain.usecase

import com.contr4s.whiterock.data.model.ClimbingGym
import com.contr4s.whiterock.data.repository.GymsRepository
import java.util.UUID
import javax.inject.Inject

class GetGymDetailUseCase @Inject constructor(private val repository: GymsRepository) {
    suspend operator fun invoke(id: UUID): ClimbingGym? = repository.getGymById(id)
}
