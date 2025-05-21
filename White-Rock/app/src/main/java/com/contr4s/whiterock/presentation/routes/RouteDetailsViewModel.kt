package com.contr4s.whiterock.presentation.routes

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import com.contr4s.whiterock.data.model.Route
import com.contr4s.whiterock.data.model.SampleData
import com.contr4s.whiterock.data.model.SampleData.Comment
import java.util.UUID

sealed class RouteDetailsIntent {
    data class LoadRoute(val routeId: UUID) : RouteDetailsIntent()
    data class UpdateCommentText(val text: String) : RouteDetailsIntent()
    data class UpdateUserRating(val rating: Int?) : RouteDetailsIntent()
    object SendComment : RouteDetailsIntent()
}

data class RouteDetailsState(
    val route: Route? = null,
    val comments: List<Comment> = emptyList(),
    val commentText: String = "",
    val userRating: Int? = null,
    val isLoading: Boolean = false
)

@HiltViewModel
class RouteDetailsViewModel @Inject constructor() : ViewModel(), ContainerHost<RouteDetailsState, Nothing> {
    override val container = container<RouteDetailsState, Nothing>(RouteDetailsState())
    
    fun onIntent(intent: RouteDetailsIntent) {
        when (intent) {
            is RouteDetailsIntent.LoadRoute -> loadRoute(intent.routeId)
            is RouteDetailsIntent.UpdateCommentText -> updateCommentText(intent.text)
            is RouteDetailsIntent.UpdateUserRating -> updateUserRating(intent.rating)
            is RouteDetailsIntent.SendComment -> sendComment()
        }
    }
    
    private fun loadRoute(routeId: UUID) = intent {
        reduce { state.copy(isLoading = true) }
        val route = SampleData.getRouteById(routeId)
        val comments = SampleData.getCommentsByRouteId(routeId)
        reduce { state.copy(route = route, comments = comments, isLoading = false) }
    }
    
    private fun updateCommentText(text: String) = intent {
        reduce { state.copy(commentText = text) }
    }
    
    private fun updateUserRating(rating: Int?) = intent {
        reduce { state.copy(userRating = rating) }
    }
    
    private fun sendComment() = intent {
        val route = state.route ?: return@intent
        val currentUser = SampleData.getCurrentUser()
        val newComment = Comment(
            user = currentUser,
            route = route,
            text = state.commentText,
            timestamp = System.currentTimeMillis(),
            rating = state.userRating
        )
        
        val updatedComments = state.comments + newComment
        reduce { state.copy(comments = updatedComments, commentText = "") }
    }
}
