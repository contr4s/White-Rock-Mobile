package com.contr4s.whiterock.domain.usecase

import com.contr4s.whiterock.data.model.ClimbingGym
import com.contr4s.whiterock.data.repository.GymsRepository
import javax.inject.Inject

class GetGymsUseCase @Inject constructor(private val repository: GymsRepository) {
    suspend operator fun invoke(): List<ClimbingGym> = repository.getGyms()
}
