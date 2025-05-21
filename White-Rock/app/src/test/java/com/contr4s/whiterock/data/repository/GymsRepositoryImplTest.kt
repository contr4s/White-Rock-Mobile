package com.contr4s.whiterock.data.repository

import com.contr4s.whiterock.data.model.SampleData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.util.*

@ExperimentalCoroutinesApi
class GymsRepositoryImplTest {
    
    private lateinit var repository: GymsRepositoryImpl
    
    @Before
    fun setup() {
        repository = GymsRepositoryImpl()
    }
    
    @Test
    fun `getGyms returns all gyms from SampleData`() = runTest {
        val gyms = repository.getGyms()
        
        assert(gyms == SampleData.gyms)
        assert(gyms.isNotEmpty())
    }
    
    @Test
    fun `getGymById returns correct gym when id exists`() = runTest {
        val existingGymId = SampleData.gyms.first().id
        
        val gym = repository.getGymById(existingGymId)
        
        assert(gym != null)
        assert(gym?.id == existingGymId)
    }
    
    @Test
    fun `getGymById returns null when id does not exist`() = runTest {
        val nonExistingGymId = UUID.randomUUID()
        
        val gym = repository.getGymById(nonExistingGymId)
        
        assert(gym == null)
    }
}
