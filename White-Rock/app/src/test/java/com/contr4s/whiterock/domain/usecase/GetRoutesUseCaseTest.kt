package com.contr4s.whiterock.domain.usecase

import com.contr4s.whiterock.data.model.Route
import com.contr4s.whiterock.data.repository.RoutesRepository
import io.mockk.*
import org.junit.After
import org.junit.Before
import org.junit.Test

class GetRoutesUseCaseTest {
    
    private val routesRepository = mockk<RoutesRepository>()
    private lateinit var getRoutesUseCase: GetRoutesUseCase
    
    @Before
    fun setup() {
        getRoutesUseCase = GetRoutesUseCase(routesRepository)
    }
    
    @After
    fun tearDown() {
        clearAllMocks()
    }
    
    @Test
    fun `invoke calls repository and returns routes`() {
        val mockRoutes = listOf(
            mockk<Route>(relaxed = true),
            mockk<Route>(relaxed = true)
        )
        every { routesRepository.getAllRoutes() } returns mockRoutes
        
        val result = getRoutesUseCase()
        
        assert(result == mockRoutes)
        verify(exactly = 1) { routesRepository.getAllRoutes() }
    }
}
