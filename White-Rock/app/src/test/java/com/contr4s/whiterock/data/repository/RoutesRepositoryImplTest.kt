package com.contr4s.whiterock.data.repository

import com.contr4s.whiterock.data.model.SampleData
import org.junit.Before
import org.junit.Test
import java.util.*

class RoutesRepositoryImplTest {
    
    private lateinit var repository: RoutesRepositoryImpl
    
    @Before
    fun setup() {
        repository = RoutesRepositoryImpl()
    }
    
    @Test
    fun `getAllRoutes returns all routes from SampleData`() {
        val routes = repository.getAllRoutes()
        
        assert(routes == SampleData.routes)
        assert(routes.isNotEmpty())
    }
    
    @Test
    fun `getRouteById returns correct route when id exists`() {
        val existingRouteId = SampleData.routes.first().id
        
        val route = repository.getRouteById(existingRouteId)
        
        assert(route != null)
        assert(route?.id == existingRouteId)
    }
    
    @Test
    fun `getRouteById returns null when id does not exist`() {
        val nonExistingRouteId = UUID.randomUUID()
        
        val route = repository.getRouteById(nonExistingRouteId)
        
        assert(route == null)
    }
    
    @Test
    fun `getRoutesByGym returns only routes for specified gym`() {
        val gymId = SampleData.routes.first().gymId
        
        val routes = repository.getRoutesByGym(gymId)
        
        assert(routes.isNotEmpty())
        assert(routes.all { it.gymId == gymId })
    }
    
    @Test
    fun `getRoutesByDifficulty returns only routes with specified difficulty`() {
        val difficulty = SampleData.routes.first().grade.substring(0, 2)
        
        val routes = repository.getRoutesByDifficulty(difficulty)
        
        assert(routes.isNotEmpty())
        assert(routes.all { it.grade.startsWith(difficulty) })
    }
}
