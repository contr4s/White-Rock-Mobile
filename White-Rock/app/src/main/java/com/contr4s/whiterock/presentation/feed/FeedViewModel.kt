package com.contr4s.whiterock.presentation.feed

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.contr4s.whiterock.domain.usecase.GetPostsUseCase
import com.contr4s.whiterock.data.model.Post
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import com.contr4s.whiterock.data.model.SampleData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import java.util.UUID

sealed class FeedIntent {
    object LoadPosts : FeedIntent()
    data class ChangeFeedType(val type: FeedType) : FeedIntent()
    data class ToggleLike(val postId: UUID) : FeedIntent()
    data class AddPost(val post: Post) : FeedIntent()
}

data class FeedState(
    val posts: List<Post> = emptyList(),
    val feedType: FeedType = FeedType.ALL,
    val isLoading: Boolean = false
)

enum class FeedType { ALL, FRIENDS }

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val getPostsUseCase: GetPostsUseCase
) : ViewModel(), ContainerHost<FeedState, Nothing> {
    override val container = container<FeedState, Nothing>(FeedState())
    
    fun onIntent(intent: FeedIntent) {
        when (intent) {
            is FeedIntent.LoadPosts -> loadPosts()
            is FeedIntent.ChangeFeedType -> changeFeedType(intent.type)
            is FeedIntent.ToggleLike -> toggleLike(intent.postId)
            is FeedIntent.AddPost -> addPost(intent.post)
        }
    }
    
    private fun loadPosts() = intent {
        reduce { state.copy(isLoading = true) }
        
        try {
            val posts = getPostsUseCase().first() // Get first emission for tests
            val filteredPosts = filterPostsByType(posts, state.feedType)
            reduce { 
                state.copy(
                    posts = filteredPosts,
                    isLoading = false
                ) 
            }
        } catch (e: Exception) {
            // Handle any errors
            reduce { state.copy(isLoading = false) }
        }
    }
    
    private fun changeFeedType(type: FeedType) = intent {
        // First update the feed type immediately
        reduce { state.copy(feedType = type) }
        
        try {
            // Then fetch and filter posts
            val posts = getPostsUseCase().first()
            val filteredPosts = filterPostsByType(posts, type)
            reduce { state.copy(posts = filteredPosts) }
        } catch (e: Exception) {
            // Handle any errors
        }
    }
    
    private fun toggleLike(postId: UUID) = intent {
        val updatedPosts = state.posts.map { post ->
            if (post.id == postId) {
                val liked = !post.isLiked
                post.copy(
                    isLiked = liked,
                    likesCount = post.likesCount + if (liked) 1 else -1
                )
            } else post
        }
        reduce { state.copy(posts = updatedPosts) }
    }
    
    private fun addPost(post: Post) = intent {
        // Add the new post at the beginning of the list
        val updatedPosts = listOf(post) + state.posts
        reduce { state.copy(posts = updatedPosts) }
    }
    
    private fun filterPostsByType(posts: List<Post>, type: FeedType): List<Post> {
        return when (type) {
            FeedType.ALL -> posts
            FeedType.FRIENDS -> {
                val currentUser = SampleData.getCurrentUser()
                val friendIds = currentUser.friends
                posts.filter { post -> friendIds.contains(post.user.id) }
            }
        }
    }
}
