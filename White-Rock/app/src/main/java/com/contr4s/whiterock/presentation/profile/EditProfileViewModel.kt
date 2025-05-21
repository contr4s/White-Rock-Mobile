package com.contr4s.whiterock.presentation.profile

import androidx.lifecycle.ViewModel
import com.contr4s.whiterock.data.model.SampleData
import com.contr4s.whiterock.data.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

sealed class EditProfileIntent {
    object LoadProfile : EditProfileIntent()
    data class UpdateName(val name: String) : EditProfileIntent()
    data class UpdateCity(val city: String) : EditProfileIntent()
    data class UpdateProfilePicture(val url: String) : EditProfileIntent()
    object SaveProfile : EditProfileIntent()
}

data class EditProfileState(
    val user: User? = null,
    val name: String = "",
    val city: String = "",
    val profilePictureUrl: String = "",
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class EditProfileViewModel @Inject constructor() : ViewModel(), ContainerHost<EditProfileState, Nothing> {
    override val container = container<EditProfileState, Nothing>(EditProfileState())

    fun onIntent(intent: EditProfileIntent) {
        when (intent) {
            is EditProfileIntent.LoadProfile -> loadCurrentProfile()
            is EditProfileIntent.UpdateName -> updateName(intent.name)
            is EditProfileIntent.UpdateCity -> updateCity(intent.city)
            is EditProfileIntent.UpdateProfilePicture -> updateProfilePicture(intent.url)
            is EditProfileIntent.SaveProfile -> saveProfile()
        }
    }

    private fun loadCurrentProfile() = intent {
        reduce { state.copy(isLoading = true) }
        val currentUser = SampleData.getCurrentUser()
        reduce {
            state.copy(
                user = currentUser,
                name = currentUser.name,
                city = currentUser.city,
                profilePictureUrl = currentUser.profilePictureUrl,
                isLoading = false
            )
        }
    }

    private fun updateName(name: String) = intent {
        reduce { state.copy(name = name) }
    }

    private fun updateCity(city: String) = intent {
        reduce { state.copy(city = city) }
    }

    private fun updateProfilePicture(url: String) = intent {
        reduce { state.copy(profilePictureUrl = url) }
    }

    private fun saveProfile() = intent {
        reduce { state.copy(isSaving = true) }
        
        try {
            kotlinx.coroutines.delay(1000)
            reduce { 
                state.copy(
                    isSaving = false,
                    isSuccess = true
                )
            }
        } catch (e: Exception) {
            reduce { 
                state.copy(
                    isSaving = false,
                    error = e.message ?: "Unknown error occurred"
                )
            }
        }
    }
}
