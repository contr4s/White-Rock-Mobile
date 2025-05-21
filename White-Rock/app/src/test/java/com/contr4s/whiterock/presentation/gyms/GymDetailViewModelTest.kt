package com.contr4s.whiterock.presentation.gyms

import com.contr4s.whiterock.data.model.ClimbingGym
import com.contr4s.whiterock.data.model.Route
import com.contr4s.whiterock.domain.usecase.GetGymDetailUseCase
import com.contr4s.whiterock.ui.screens.gyms.RouteFilter
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
class GymDetailViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val getGymDetailUseCase = mockk<GetGymDetailUseCase>()
    private lateinit var viewModel: GymDetailViewModel

    private val gymId = UUID.randomUUID()
    private val routes = listOf<Route>(mockk(relaxed = true), mockk(relaxed = true))
    private val gym = mockk<ClimbingGym> {
        every { id } returns gymId
        every { name } returns "Test Gym"
    }
    init {
        every { gym.routes } returns routes
    }

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = GymDetailViewModel(getGymDetailUseCase)
        
        coEvery { getGymDetailUseCase(any()) } returns gym
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    @Test
    fun `when LoadGym intent is dispatched, then state is updated with gym details`() = runTest {
        coEvery { getGymDetailUseCase(gymId) } returns gym

        viewModel.test(this) {
            val initialState = awaitState()
            assertThat(initialState.isLoading).isFalse()
            
            viewModel.onIntent(GymDetailIntent.LoadGym(gymId))
            
            testScheduler.advanceUntilIdle()
            
            val loadingState = awaitState()
            assertThat(loadingState.isLoading).isTrue()
            
            val finalState = awaitState()
            assertThat(finalState.gym).isEqualTo(gym)
            assertThat(finalState.isLoading).isFalse()
            assertThat(finalState.filteredRoutes).hasSize(routes.size)
        }
        
        coVerify { getGymDetailUseCase(gymId) }
    }
    
    @Test
    fun `when ChangeFilter intent is dispatched, routes are filtered correctly`() = runTest {
        coEvery { getGymDetailUseCase(gymId) } returns gym
        
        viewModel.onIntent(GymDetailIntent.LoadGym(gymId))
        testScheduler.advanceUntilIdle()
        
        viewModel.test(this) {
            awaitState()
            
            viewModel.onIntent(GymDetailIntent.ChangeFilter(RouteFilter.BOULDER))
            
            val filteredState = awaitState()
            assertThat(filteredState.selectedFilter).isEqualTo(RouteFilter.BOULDER)
        }
    }
}
