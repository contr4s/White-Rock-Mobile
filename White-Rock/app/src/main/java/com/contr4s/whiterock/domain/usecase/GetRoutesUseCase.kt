package com.contr4s.whiterock.domain.usecase

import com.contr4s.whiterock.data.model.Route
import com.contr4s.whiterock.data.repository.RoutesRepository
import javax.inject.Inject

class GetRoutesUseCase @Inject constructor(
    private val repository: RoutesRepository
) {
    operator fun invoke(): List<Route> {
        return repository.getAllRoutes()
    }
}
