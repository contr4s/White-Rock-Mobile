package com.contr4s.whiterock.data.repository

import com.contr4s.whiterock.data.model.SampleData
import com.contr4s.whiterock.data.model.User
import javax.inject.Inject

class FriendsRepositoryImpl @Inject constructor() : FriendsRepository {
    override suspend fun getFriends(): List<User> {
        val currentUser = SampleData.getCurrentUser()
        return SampleData.users.filter { user -> 
            currentUser.friends.contains(user.id) 
        }
    }
    
    fun searchFriends(query: String): List<User> {
        val friends = getFriendsList()
        return if (query.isBlank()) {
            friends
        } else {
            friends.filter {
                it.name.contains(query, ignoreCase = true) ||
                it.city.contains(query, ignoreCase = true)
            }
        }
    }
    
    private fun getFriendsList(): List<User> {
        val currentUser = SampleData.getCurrentUser()
        return SampleData.users.filter { user -> 
            currentUser.friends.contains(user.id) 
        }
    }
}
