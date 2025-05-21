package com.contr4s.whiterock.data.repository

import com.contr4s.whiterock.data.model.Route
import com.contr4s.whiterock.data.model.SampleData
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

interface RoutesRepository {
    fun getAllRoutes(): List<Route>
    fun getRouteById(id: UUID): Route?
    fun getRoutesByGym(gymId: UUID): List<Route>
    fun getRoutesByDifficulty(difficulty: String): List<Route>
}

@Singleton
class RoutesRepositoryImpl @Inject constructor() : RoutesRepository {
    override fun getAllRoutes(): List<Route> {
        return SampleData.routes
    }

    override fun getRouteById(id: UUID): Route? {
        return SampleData.getRouteById(id)
    }

    override fun getRoutesByGym(gymId: UUID): List<Route> {
        return SampleData.routes.filter { it.gymId == gymId }
    }

    override fun getRoutesByDifficulty(difficulty: String): List<Route> {
        return SampleData.routes.filter { it.grade.startsWith(difficulty, ignoreCase = true) }
    }
}
