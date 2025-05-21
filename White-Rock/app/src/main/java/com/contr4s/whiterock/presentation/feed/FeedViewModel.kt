package com.contr4s.whiterock.presentation.feed

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.contr4s.whiterock.domain.usecase.GetPostsUseCase
import com.contr4s.whiterock.data.model.Post
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import com.contr4s.whiterock.data.model.SampleData

sealed class FeedIntent {
    object LoadPosts : FeedIntent()
    data class ChangeFeedType(val type: FeedType) : FeedIntent()
    data class ToggleLike(val postId: java.util.UUID) : FeedIntent()
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
        getPostsUseCase().collect { posts ->
            val filteredPosts = filterPostsByType(posts, state.feedType)
            reduce { state.copy(posts = filteredPosts, isLoading = false) }
        }
    }
    
    private fun toggleLike(postId: java.util.UUID) = intent {
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
    
    private fun changeFeedType(type: FeedType) = intent {
        reduce { state.copy(feedType = type) }
        getPostsUseCase().collect { posts ->
            val filteredPosts = filterPostsByType(posts, type)
            reduce { state.copy(posts = filteredPosts) }
        }
    }
    
    private fun addPost(post: Post) = intent {
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
