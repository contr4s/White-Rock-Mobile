package com.contr4s.whiterock.presentation.feed

import com.contr4s.whiterock.data.model.Post
import com.contr4s.whiterock.domain.usecase.GetPostsUseCase
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.orbitmvi.orbit.test.test
import java.util.UUID

@ExperimentalCoroutinesApi
class FeedViewModelTest {
    
    private val testDispatcher = StandardTestDispatcher()
    private val getPostsUseCase = mockk<GetPostsUseCase>()
    private lateinit var viewModel: FeedViewModel
    
    private val mockPost1Id = UUID.fromString("a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11")
    private val mockPost2Id = UUID.fromString("a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12")
    
    private val mockPost1 = mockk<Post>(relaxed = true) {
        every { id } returns mockPost1Id
        every { isLiked } returns false
        every { likesCount } returns 10
        every { copy(isLiked = true, likesCount = 11) } returns mockk(relaxed = true) {
            every { id } returns mockPost1Id
            every { isLiked } returns true
            every { likesCount } returns 11
        }
        every { copy(isLiked = false, likesCount = 9) } returns mockk(relaxed = true) {
            every { id } returns mockPost1Id
            every { isLiked } returns false
            every { likesCount } returns 9
        }
    }
    
    private val mockPost2 = mockk<Post>(relaxed = true) {
        every { id } returns mockPost2Id
    }
    
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        every { getPostsUseCase() } returns flowOf(listOf())
        viewModel = FeedViewModel(getPostsUseCase)
    }
    
    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
    }
    
    @Test
    fun `when LoadPosts intent is dispatched, then state contains posts`() = runTest {
        // Given
        val mockPosts = listOf(mockPost1, mockPost2)
        every { getPostsUseCase() } returns flowOf(mockPosts)
        
        // When/Then
        viewModel.test(this) {
            // Initial state
            val initialState = awaitState()
            assertThat(initialState.isLoading).isFalse()
            assertThat(initialState.posts).isEmpty()
            
            // Dispatch intent
            viewModel.onIntent(FeedIntent.LoadPosts)
            testScheduler.advanceUntilIdle()
            
            // Check loading state
            val loadingState = awaitState()
            assertThat(loadingState.isLoading).isTrue()
            
            // Check final state with posts
            val finalState = awaitState()
            assertThat(finalState.posts).hasSize(mockPosts.size)
            assertThat(finalState.isLoading).isFalse()
        }
        
        verify { getPostsUseCase() }
    }
    
    @Test
    fun `when ChangeFeedType intent is dispatched, then feed type is updated`() = runTest {
        // Given
        val mockPosts = listOf(mockPost1, mockPost2)
        every { getPostsUseCase() } returns flowOf(mockPosts)
        
        // When/Then
        viewModel.test(this) {
            // Initial state
            val initialState = awaitState()
            assertThat(initialState.feedType).isEqualTo(FeedType.ALL)
            
            // Dispatch intent to change feed type
            viewModel.onIntent(FeedIntent.ChangeFeedType(FeedType.FRIENDS))
            testScheduler.advanceUntilIdle()
            
            // Check final state with updated feed type
            val finalState = awaitState()
            assertThat(finalState.feedType).isEqualTo(FeedType.FRIENDS)
        }
    }
    
    @Test
    fun `when ToggleLike intent is dispatched, then post like status is toggled`() = runTest {
        // Given
        val postId = mockPost1.id
        val posts = listOf(mockPost1, mockPost2)
        
        // Set up initial state with posts
        every { getPostsUseCase() } returns flowOf(posts)
        
        // When/Then
        viewModel.test(this) {
            // Load posts first to populate the state
            viewModel.onIntent(FeedIntent.LoadPosts)
            testScheduler.advanceUntilIdle()
            
            // Skip states until we have loaded posts
            var currentState = awaitState() // Initial
            currentState = awaitState() // Loading
            currentState = awaitState() // Loaded with posts
            
            assertThat(currentState.posts).hasSize(2)
            val initialLikeStatus = currentState.posts.first { it.id == postId }.isLiked
            val initialLikeCount = currentState.posts.first { it.id == postId }.likesCount
            
            // Toggle like
            viewModel.onIntent(FeedIntent.ToggleLike(postId))
            testScheduler.advanceUntilIdle()
            
            // Get updated state
            val updatedState = awaitState()
            val updatedPost = updatedState.posts.first { it.id == postId }
            
            assertThat(updatedPost.isLiked).isEqualTo(!initialLikeStatus)
            if (initialLikeStatus) {
                assertThat(updatedPost.likesCount).isEqualTo(initialLikeCount - 1)
            } else {
                assertThat(updatedPost.likesCount).isEqualTo(initialLikeCount + 1)
            }
        }
    }
    
    @Test
    fun `when AddPost intent is dispatched, then post is added to the list`() = runTest {
        // Given
        val newPost = mockk<Post>(relaxed = true)
        every { getPostsUseCase() } returns flowOf(emptyList())
        
        // When/Then
        viewModel.test(this) {
            // Check initial state is empty
            val initialState = awaitState()
            assertThat(initialState.posts).isEmpty()
            
            // Add a post
            viewModel.onIntent(FeedIntent.AddPost(newPost))
            testScheduler.advanceUntilIdle()
            
            // Check the post was added
            val updatedState = awaitState()
            assertThat(updatedState.posts).hasSize(1)
            assertThat(updatedState.posts.first()).isEqualTo(newPost)
        }
    }
}
