package com.contr4s.whiterock.data.repository

import com.contr4s.whiterock.data.model.User

interface FriendsRepository {
    suspend fun getFriends(): List<User>
}
