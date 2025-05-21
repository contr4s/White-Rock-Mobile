package com.contr4s.whiterock.data.repository

import com.contr4s.whiterock.data.model.SampleData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class FriendsRepositoryImplTest {
    
    private lateinit var repository: FriendsRepositoryImpl
    
    @Before
    fun setup() {
        repository = FriendsRepositoryImpl()
    }
    
    @Test
    fun `getFriends returns only friends of current user`() = runTest {
        val currentUser = SampleData.getCurrentUser()
        
        val friends = repository.getFriends()
        
        assert(friends.isNotEmpty())
        assert(friends.map { it.id }.containsAll(currentUser.friends))
    }
    
    @Test
    fun `searchFriends with empty query returns all friends`() = runTest {
        val allFriends = repository.getFriendsList()
        
        val searchResults = repository.searchFriends("")
        
        assert(searchResults.containsAll(allFriends) && searchResults.size == allFriends.size)
    }
    
    @Test
    fun `searchFriends with name query returns matching friends`() = runTest {
        val firstFriend = repository.getFriendsList().first()
        val searchQuery = firstFriend.name.substring(0, 3)
        
        val searchResults = repository.searchFriends(searchQuery)
        
        assert(searchResults.contains(firstFriend))
        assert(searchResults.all { 
            it.name.contains(searchQuery, ignoreCase = true) || 
            it.city.contains(searchQuery, ignoreCase = true) 
        })
    }
    
    @Test
    fun `searchFriends with city query returns matching friends`() = runTest {
        val firstFriend = repository.getFriendsList().first()
        val searchQuery = firstFriend.city.substring(0, 3)
        
        val searchResults = repository.searchFriends(searchQuery)
        
        assert(searchResults.contains(firstFriend))
        assert(searchResults.all { 
            it.name.contains(searchQuery, ignoreCase = true) || 
            it.city.contains(searchQuery, ignoreCase = true) 
        })
    }
}
