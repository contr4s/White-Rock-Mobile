package com.contr4s.whiterock.domain.usecase

import com.contr4s.whiterock.data.model.User
import com.contr4s.whiterock.data.repository.FriendsRepository
import javax.inject.Inject

class GetFriendsUseCase @Inject constructor(
    private val repository: FriendsRepository
) {
    suspend operator fun invoke(): List<User> = repository.getFriends()
}
