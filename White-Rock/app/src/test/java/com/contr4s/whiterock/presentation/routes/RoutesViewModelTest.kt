package com.contr4s.whiterock.presentation.routes

import com.contr4s.whiterock.data.model.Route
import com.contr4s.whiterock.domain.usecase.GetRoutesUseCase
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.orbitmvi.orbit.test.test
import java.util.UUID

@ExperimentalCoroutinesApi
class RoutesViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val getRoutesUseCase = mockk<GetRoutesUseCase>()
    private lateinit var viewModel: RoutesViewModel
    
    private val route1 = mockk<Route>(relaxed = true) {
        every { name } returns "Test Route 1"
        every { gymId } returns UUID.randomUUID()
        every { grade } returns "6A"
    }
    private val route2 = mockk<Route>(relaxed = true) {
        every { name } returns "Another Route"
        every { gymId } returns UUID.randomUUID()
        every { grade } returns "7A"
    }

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = RoutesViewModel(getRoutesUseCase)
        
        coEvery { getRoutesUseCase() } returns listOf(route1, route2)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    @Test
    fun `when LoadRoutes intent is dispatched, then state contains routes`() = runTest {
        val routes = listOf(route1, route2)
        coEvery { getRoutesUseCase() } returns routes

        viewModel.test(this) {
            val initialState = awaitState()
            assertThat(initialState.isLoading).isFalse()
            assertThat(initialState.routes).isEmpty()
            
            viewModel.onIntent(RoutesIntent.LoadRoutes)
            
            testScheduler.advanceUntilIdle()
            
            val loadingState = awaitState()
            assertThat(loadingState.isLoading).isTrue()
            
            val finalState = awaitState()
            assertThat(finalState.routes).hasSize(routes.size)
            assertThat(finalState.isLoading).isFalse()
        }
        
        coVerify { getRoutesUseCase() }
    }

    @Test
    fun `when UpdateSearchQuery intent is dispatched, then routes are filtered by query`() = runTest {
        val routes = listOf(route1, route2)
        coEvery { getRoutesUseCase() } returns routes
        
        viewModel.test(this) {
            val initialState = awaitState()
            
            viewModel.onIntent(RoutesIntent.LoadRoutes)
            testScheduler.advanceUntilIdle()
            
            awaitState()
            awaitState()
            
            viewModel.onIntent(RoutesIntent.UpdateSearchQuery("Test"))
            testScheduler.advanceUntilIdle()
            
            val state = awaitState()
            assertThat(state.searchQuery).isEqualTo("Test")
            assertThat(state.filteredRoutes).hasSize(1)
            assertThat(state.filteredRoutes.first().name).contains("Test")
        }
    }
    
    @Test
    fun `when UpdateSelectedGym intent is dispatched, then routes are filtered by gym`() = runTest {
        val gymId = route1.gymId
        
        viewModel.test(this) {
            val initialState = awaitState()
            
            viewModel.onIntent(RoutesIntent.LoadRoutes)
            testScheduler.advanceUntilIdle()
            
            awaitState()
            awaitState()
            
            viewModel.onIntent(RoutesIntent.UpdateSelectedGym(gymId))
            testScheduler.advanceUntilIdle()
            
            val state = awaitState()
            assertThat(state.selectedGymId).isEqualTo(gymId)
            assertThat(state.filteredRoutes).hasSize(1)
            assertThat(state.filteredRoutes.first().gymId).isEqualTo(gymId)
        }
    }
    
    @Test
    fun `when UpdateSelectedDifficulty intent is dispatched, then routes are filtered by difficulty`() = runTest {
        val difficulty = "7A"
        
        viewModel.test(this) {
            val initialState = awaitState()
            
            viewModel.onIntent(RoutesIntent.LoadRoutes)
            testScheduler.advanceUntilIdle()
            
            awaitState()
            awaitState()
            
            viewModel.onIntent(RoutesIntent.UpdateSelectedDifficulty(difficulty))
            testScheduler.advanceUntilIdle()
            
            val state = awaitState()
            assertThat(state.selectedDifficulty).isEqualTo(difficulty)
            assertThat(state.filteredRoutes).hasSize(1)
            assertThat(state.filteredRoutes.first().grade).isEqualTo(difficulty)
        }
    }
    
    @Test
    fun `when ResetFilters intent is dispatched, then all filters are removed`() = runTest {
        viewModel.test(this) {
            val initialState = awaitState()
            
            viewModel.onIntent(RoutesIntent.LoadRoutes)
            testScheduler.advanceUntilIdle()
            
            awaitState()
            awaitState()
            
            viewModel.onIntent(RoutesIntent.UpdateSelectedGym(route1.gymId))
            testScheduler.advanceUntilIdle()
            awaitState()
            
            viewModel.onIntent(RoutesIntent.UpdateSelectedDifficulty("7A"))
            testScheduler.advanceUntilIdle()
            awaitState()
            
            viewModel.onIntent(RoutesIntent.UpdateSearchQuery("Test"))
            testScheduler.advanceUntilIdle()
            awaitState()
            
            viewModel.onIntent(RoutesIntent.ResetFilters)
            testScheduler.advanceUntilIdle()
            
            val finalState = awaitState()
            assertThat(finalState.selectedGymId).isNull()
            assertThat(finalState.selectedDifficulty).isNull()
            assertThat(finalState.searchQuery).isEmpty()
            assertThat(finalState.filteredRoutes).hasSize(2)
        }
    }
}
