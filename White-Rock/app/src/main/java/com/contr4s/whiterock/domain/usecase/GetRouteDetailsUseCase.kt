package com.contr4s.whiterock.domain.usecase

import com.contr4s.whiterock.data.model.Route
import com.contr4s.whiterock.data.repository.RoutesRepository
import java.util.UUID
import javax.inject.Inject

class GetRouteDetailsUseCase @Inject constructor(
    private val repository: RoutesRepository
) {
    operator fun invoke(routeId: UUID): Route? {
        return repository.getRouteById(routeId)
    }
}
