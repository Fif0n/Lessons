package com.example.lessons.viewModels.student

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lessons.repositories.UploadAvatarRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ProfileSettingsViewModel(
    val name: String,
    val surname: String,
    private val uploadAvatarRepository: UploadAvatarRepository,
    private val context: Context
): ViewModel() {
    var selectedImageUri = MutableStateFlow<Uri?>(null)
        private set

    var uploadImageError = MutableStateFlow<String?>(null)
        private set

    init {
        viewModelScope.launch {
            val userAvatar = uploadAvatarRepository.getUserAvatar()
            selectedImageUri.value = userAvatar
        }
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
                        Toast.makeText(context, "Avatar uploaded", Toast.LENGTH_SHORT).show()
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
}