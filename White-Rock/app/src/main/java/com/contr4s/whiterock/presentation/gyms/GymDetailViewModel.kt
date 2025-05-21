package com.contr4s.whiterock.presentation.gyms

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import com.contr4s.whiterock.data.model.ClimbingGym
import com.contr4s.whiterock.domain.usecase.GetGymDetailUseCase
import java.util.UUID
import com.contr4s.whiterock.ui.screens.gyms.RouteFilter
import com.contr4s.whiterock.data.model.Route

sealed class GymDetailIntent {
    data class LoadGym(val gymId: UUID) : GymDetailIntent()
    data class ChangeFilter(val filter: RouteFilter) : GymDetailIntent()
}

data class GymDetailState(
    val gym: ClimbingGym? = null,
    val isLoading: Boolean = false,
    val selectedFilter: RouteFilter = RouteFilter.ALL,
    val filteredRoutes: List<Route> = emptyList()
)

@HiltViewModel
class GymDetailViewModel @Inject constructor(
    private val getGymDetailUseCase: GetGymDetailUseCase
) : ViewModel(), ContainerHost<GymDetailState, Nothing> {
    override val container = container<GymDetailState, Nothing>(GymDetailState())
    
    fun onIntent(intent: GymDetailIntent) {
        when (intent) {
            is GymDetailIntent.LoadGym -> loadGym(intent.gymId)
            is GymDetailIntent.ChangeFilter -> changeFilter(intent.filter)
        }
    }
    
    private fun loadGym(gymId: UUID) = intent {
        reduce { state.copy(isLoading = true) }
        val gym = getGymDetailUseCase(gymId)
        val filtered = filterRoutes(gym?.routes.orEmpty(), state.selectedFilter)
        reduce { state.copy(gym = gym, isLoading = false, filteredRoutes = filtered) }
    }
    
    private fun changeFilter(filter: RouteFilter) = intent {
        val routes = state.gym?.routes.orEmpty()
        val filtered = filterRoutes(routes, filter)
        reduce { state.copy(selectedFilter = filter, filteredRoutes = filtered) }
    }

    private fun filterRoutes(routes: List<Route>, filter: RouteFilter): List<Route> {
        return when (filter) {
            RouteFilter.ALL -> routes
            RouteFilter.BOULDER -> routes.filter { it.type.contains("боулдеринг", ignoreCase = true) }
            RouteFilter.SPORT -> routes.filter { it.type.contains("трудность", ignoreCase = true) }
        }
    }
}
