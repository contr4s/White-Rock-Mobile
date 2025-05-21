package com.contr4s.whiterock.presentation.friends

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import com.contr4s.whiterock.data.model.User
import com.contr4s.whiterock.domain.usecase.GetFriendsUseCase

sealed class FriendsIntent {
    object LoadFriends : FriendsIntent()
    data class Search(val query: String) : FriendsIntent()
}

data class FriendsState(
    val friends: List<User> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = false
)

@HiltViewModel
class FriendsViewModel @Inject constructor(
    private val getFriendsUseCase: GetFriendsUseCase
) : ViewModel(), ContainerHost<FriendsState, Nothing> {
    override val container = container<FriendsState, Nothing>(FriendsState())
    
    fun onIntent(intent: FriendsIntent) {
        when (intent) {
            is FriendsIntent.LoadFriends -> loadFriends()
            is FriendsIntent.Search -> search(intent.query)
        }
    }
    
    private fun loadFriends() = intent {
        reduce { state.copy(isLoading = true) }
        val friends = getFriendsUseCase()
        reduce { state.copy(friends = friends, isLoading = false) }
    }
    
    private fun search(query: String) = intent {
        reduce { state.copy(searchQuery = query) }
        val allFriends = getFriendsUseCase()
        val filteredFriends = if (query.isBlank()) {
            allFriends
        } else {
            allFriends.filter { 
                it.name.contains(query, ignoreCase = true) || 
                it.city.contains(query, ignoreCase = true)
            }
        }
        reduce { state.copy(friends = filteredFriends) }
    }
}
