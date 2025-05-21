package com.contr4s.whiterock.presentation.gyms

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import com.contr4s.whiterock.data.model.ClimbingGym
import com.contr4s.whiterock.domain.usecase.GetGymsUseCase

sealed class GymsIntent {
    object LoadGyms : GymsIntent()
    data class UpdateSearchQuery(val query: String) : GymsIntent()
}

data class GymsState(
    val gyms: List<ClimbingGym> = emptyList(),
    val filteredGyms: List<ClimbingGym> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = false
)

@HiltViewModel
class GymsViewModel @Inject constructor(
    private val getGymsUseCase: GetGymsUseCase
) : ViewModel(), ContainerHost<GymsState, Nothing> {
    override val container = container<GymsState, Nothing>(GymsState())
    fun onIntent(intent: GymsIntent) {
        when (intent) {
            is GymsIntent.LoadGyms -> loadGyms()
            is GymsIntent.UpdateSearchQuery -> updateSearchQuery(intent.query)
        }
    }
    
    private fun loadGyms() = intent {
        reduce { state.copy(isLoading = true) }
        val gyms = getGymsUseCase()
        reduce {
            state.copy(
                gyms = gyms,
                filteredGyms = filterGyms(gyms, state.searchQuery),
                isLoading = false
            )
        }
    }
    
    private fun updateSearchQuery(query: String) = intent {
        val filtered = filterGyms(state.gyms, query)
        reduce { state.copy(searchQuery = query, filteredGyms = filtered) }
    }

    private fun filterGyms(gyms: List<ClimbingGym>, query: String): List<ClimbingGym> {
        if (query.isBlank()) return gyms
        return gyms.filter {
            it.name.contains(query, ignoreCase = true) ||
            it.city.contains(query, ignoreCase = true)
        }
    }
}
