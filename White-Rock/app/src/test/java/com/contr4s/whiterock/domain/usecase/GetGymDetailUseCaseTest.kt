package com.contr4s.whiterock.domain.usecase

import com.contr4s.whiterock.data.model.ClimbingGym
import com.contr4s.whiterock.data.repository.GymsRepository
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.UUID

@ExperimentalCoroutinesApi
class GetGymDetailUseCaseTest {
    
    private val gymsRepository = mockk<GymsRepository>()
    private lateinit var getGymDetailUseCase: GetGymDetailUseCase
    
    @Before
    fun setup() {
        getGymDetailUseCase = GetGymDetailUseCase(gymsRepository)
    }
    
    @After
    fun tearDown() {
        clearAllMocks()
    }
    
    @Test
    fun `invoke calls repository with correct id and returns gym`() = runTest {
        // Given
        val gymId = UUID.randomUUID()
        val mockGym = mockk<ClimbingGym>(relaxed = true)
        coEvery { gymsRepository.getGymById(gymId) } returns mockGym
        
        // When
        val result = getGymDetailUseCase(gymId)
        
        // Then
        assert(result == mockGym)
        coVerify(exactly = 1) { gymsRepository.getGymById(gymId) }
    }
    
    @Test
    fun `invoke returns null when repository returns null`() = runTest {
        // Given
        val gymId = UUID.randomUUID()
        coEvery { gymsRepository.getGymById(gymId) } returns null
        
        // When
        val result = getGymDetailUseCase(gymId)
        
        // Then
        assert(result == null)
        coVerify(exactly = 1) { gymsRepository.getGymById(gymId) }
    }
}
