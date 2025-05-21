package com.contr4s.whiterock.domain.usecase

import com.contr4s.whiterock.data.model.ClimbingGym
import com.contr4s.whiterock.data.repository.GymsRepository
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GetGymsUseCaseTest {
    
    private val gymsRepository = mockk<GymsRepository>()
    private lateinit var getGymsUseCase: GetGymsUseCase
    
    @Before
    fun setup() {
        getGymsUseCase = GetGymsUseCase(gymsRepository)
    }
    
    @After
    fun tearDown() {
        clearAllMocks()
    }
    
    @Test
    fun `invoke calls repository and returns gyms`() = runTest {
        // Given
        val mockGyms = listOf(
            mockk<ClimbingGym>(relaxed = true),
            mockk<ClimbingGym>(relaxed = true)
        )
        coEvery { gymsRepository.getGyms() } returns mockGyms
        
        // When
        val result = getGymsUseCase()
        
        // Then
        assert(result == mockGyms)
        coVerify(exactly = 1) { gymsRepository.getGyms() }
    }
}
