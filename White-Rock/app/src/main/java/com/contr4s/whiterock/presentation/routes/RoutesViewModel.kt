package com.contr4s.whiterock.presentation.routes

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import java.util.UUID
import com.contr4s.whiterock.data.model.Route
import com.contr4s.whiterock.data.model.SampleData
import com.contr4s.whiterock.domain.usecase.GetRoutesUseCase

sealed class RoutesIntent {
    object LoadRoutes : RoutesIntent()
    data class UpdateSearchQuery(val query: String) : RoutesIntent()
    data class UpdateSelectedGym(val gymId: UUID?) : RoutesIntent()
    data class UpdateSelectedDifficulty(val difficulty: String?) : RoutesIntent()
    object ShowFilterDialog : RoutesIntent()
    object HideFilterDialog : RoutesIntent()
    object ResetFilters : RoutesIntent()
}

data class RoutesState(
    val routes: List<Route> = emptyList(),
    val filteredRoutes: List<Route> = emptyList(),
    val searchQuery: String = "",
    val selectedGymId: UUID? = null,
    val selectedDifficulty: String? = null,
    val showFilterDialog: Boolean = false,
    val isLoading: Boolean = false
)

@HiltViewModel
class RoutesViewModel @Inject constructor(
    private val getRoutesUseCase: GetRoutesUseCase
) : ViewModel(), ContainerHost<RoutesState, Nothing> {
    override val container = container<RoutesState, Nothing>(RoutesState())

    fun onIntent(intent: RoutesIntent) {
        when (intent) {
            is RoutesIntent.LoadRoutes -> loadRoutes()
            is RoutesIntent.UpdateSearchQuery -> updateSearchQuery(intent.query)
            is RoutesIntent.UpdateSelectedGym -> updateSelectedGym(intent.gymId)
            is RoutesIntent.UpdateSelectedDifficulty -> updateSelectedDifficulty(intent.difficulty)
            is RoutesIntent.ShowFilterDialog -> showFilterDialog()
            is RoutesIntent.HideFilterDialog -> hideFilterDialog()
            is RoutesIntent.ResetFilters -> resetFilters()
        }
    }

    private fun loadRoutes() = intent {
        reduce { state.copy(isLoading = true) }
        val routes = getRoutesUseCase()
        reduce {
            state.copy(
                routes = routes,
                filteredRoutes = filterRoutes(routes, state.searchQuery, state.selectedGymId, state.selectedDifficulty),
                isLoading = false
            )
        }
    }

    private fun updateSearchQuery(query: String) = intent {
        val filtered = filterRoutes(state.routes, query, state.selectedGymId, state.selectedDifficulty)
        reduce { state.copy(searchQuery = query, filteredRoutes = filtered) }
    }

    private fun updateSelectedGym(gymId: UUID?) = intent {
        val filtered = filterRoutes(state.routes, state.searchQuery, gymId, state.selectedDifficulty)
        reduce { state.copy(selectedGymId = gymId, filteredRoutes = filtered) }
    }

    private fun updateSelectedDifficulty(difficulty: String?) = intent {
        val filtered = filterRoutes(state.routes, state.searchQuery, state.selectedGymId, difficulty)
        reduce { state.copy(selectedDifficulty = difficulty, filteredRoutes = filtered) }
    }

    private fun showFilterDialog() = intent {
        reduce { state.copy(showFilterDialog = true) }
    }

    private fun hideFilterDialog() = intent {
        reduce { state.copy(showFilterDialog = false) }
    }

    private fun resetFilters() = intent {
        val filtered = filterRoutes(state.routes, "", null, null)
        reduce { 
            state.copy(
                selectedGymId = null, 
                selectedDifficulty = null, 
                searchQuery = "",
                filteredRoutes = filtered, 
                showFilterDialog = false
            ) 
        }
    }

    private fun filterRoutes(
        routes: List<Route>,
        searchQuery: String,
        selectedGymId: UUID?,
        selectedDifficulty: String?
    ): List<Route> {
        return routes.filter { route ->
            val matchesSearch = searchQuery.isEmpty() || route.name.contains(searchQuery, ignoreCase = true)
            val matchesGym = selectedGymId == null || route.gymId == selectedGymId
            val matchesDifficulty = selectedDifficulty == null || route.grade.startsWith(selectedDifficulty)
            matchesSearch && matchesGym && matchesDifficulty
        }
    }
}
