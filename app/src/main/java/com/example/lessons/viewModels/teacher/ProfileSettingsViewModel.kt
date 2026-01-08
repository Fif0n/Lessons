package com.example.lessons.viewModels.teacher

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lessons.R
import com.example.lessons.modules.backendApi.responses.ProfileDataResponse
import com.example.lessons.repositories.UploadAvatarRepository
import com.example.lessons.repositories.UserRepository
import com.example.lessons.utils.UiEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileSettingsViewModel(
    val name: String,
    val surname: String,
    private val userRepository: UserRepository,
    private val uploadAvatarRepository: UploadAvatarRepository,
    private val context: Context
): ViewModel() {
    private val _actionsNeeded = MutableStateFlow<List<String>>(emptyList())
    val actionsNeeded: StateFlow<List<String>> = _actionsNeeded.asStateFlow()

    var selectedImageUri = MutableStateFlow<Uri?>(null)
        private set

    var uploadImageError = MutableStateFlow<String?>(null)
        private set

    private val _uiEvent = MutableStateFlow<UiEvent?>(null)
    val uiEvent: StateFlow<UiEvent?> = _uiEvent

    init {
        viewModelScope.launch {
            val response = getProfileData()

            if (response != null) {
                _actionsNeeded.value = response.data.neededActions ?: emptyList()
            }

            val userAvatar = uploadAvatarRepository.getUserAvatar()
            selectedImageUri.value = userAvatar
        }
    }

    private suspend fun getProfileData(): ProfileDataResponse? {
        return userRepository.getProfileData()
    }

    fun getFullName(): String {
        return "$name $surname"
    }

    fun uploadAvatar(uri: Uri?, onDismiss: () -> Unit) {
        if (uri == null) return

        viewModelScope.launch {
            uploadAvatarRepository.uploadAvatar(uri) {
                if (it == null) {
                    setUploadError("Something went wrong. Please try again")
                } else {
                    if (it.status == "success") {
                        selectedImageUri.value = uri
                        _uiEvent.value = UiEvent.ShowMessage(R.string.avatar_updated)
                        onDismiss()
                    } else {
                        setUploadError("Something went wrong while uploading image. Please try again")
                    }
                }
            }
        }
    }

    private fun setUploadError(error: String) {
        uploadImageError.value = error
    }

    fun clearUiEvent() {
        _uiEvent.value = null
    }

}