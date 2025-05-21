package com.contr4s.whiterock.presentation.profile

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import java.util.UUID

sealed class ProfileIntent {
    data class LoadProfile(val userId: UUID?) : ProfileIntent()
    data class AddFriend(val userId: UUID) : ProfileIntent()
    data class ToggleLike(val postId: UUID) : ProfileIntent()
}

data class ProfileState(
    val user: com.contr4s.whiterock.data.model.User? = null,
    val isLoading: Boolean = false,
    val friendRequests: Set<UUID> = emptySet(),
    val likedPosts: Set<UUID> = emptySet()
)

@HiltViewModel
class ProfileViewModel @Inject constructor() : ViewModel(), ContainerHost<ProfileState, Nothing> {
    override val container = container<ProfileState, Nothing>(ProfileState())

    fun onIntent(intent: ProfileIntent) {
        when (intent) {
            is ProfileIntent.LoadProfile -> loadProfile(intent.userId)
            is ProfileIntent.AddFriend -> addFriend(intent.userId)
            is ProfileIntent.ToggleLike -> toggleLike(intent.postId)
        }
    }

    private fun loadProfile(userId: UUID?) = intent {
        reduce { state.copy(isLoading = true) }
        val user = if (userId == null) {
            com.contr4s.whiterock.data.model.SampleData.getCurrentUser()
        } else {
            com.contr4s.whiterock.data.model.SampleData.getUserById(userId) ?:
            com.contr4s.whiterock.data.model.SampleData.getCurrentUser()
        }
        reduce { state.copy(user = user, isLoading = false) }
    }
    
    private fun addFriend(userId: UUID) = intent {
        val updatedFriendRequests = state.friendRequests + userId
        reduce { state.copy(friendRequests = updatedFriendRequests) }
    }
    
    private fun toggleLike(postId: UUID) = intent {
        val updatedLikedPosts = if (state.likedPosts.contains(postId)) {
            state.likedPosts - postId
        } else {
            state.likedPosts + postId
        }
        reduce { state.copy(likedPosts = updatedLikedPosts) }
    }
}
