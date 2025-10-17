package com.example.lessons.viewModels.teacher

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lessons.repositories.UserRepository
import com.example.lessons.ui.formDatas.UpdatePasswordFormData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UpdatePasswordViewModel(private val repository: UserRepository, val context: Context): ViewModel() {
    private val _formData = MutableStateFlow<UpdatePasswordFormData>(UpdatePasswordFormData())
    val formData: StateFlow<UpdatePasswordFormData> = _formData

    var passwordError = MutableStateFlow<String?>(null)
        private set
    var passwordConfirmError = MutableStateFlow<String?>(null)
        private set
    var currentPasswordError = MutableStateFlow<String?>(null)
        private set

    fun updateFormField(field: String, value: String) {
        _formData.value = _formData.value.copy(
            password = if (field == "password") value else _formData.value.password,
            passwordConfirm = if (field == "passwordConfirm") value else _formData.value.passwordConfirm,
            currentPassword = if (field == "currentPassword") value else _formData.value.currentPassword,
        )
    }

    private fun clearErrors() {
        passwordError.value = null
        passwordConfirmError.value = null
        currentPasswordError.value = null
    }

    private fun setErrors(data: Map<String, String>) {
        data.forEach { i ->
            val value = i.value
            when (i.key) {
                "password" -> passwordError.value = value
                "passwordConfirm" -> passwordConfirmError.value = value
                "currentPassword" -> currentPasswordError.value = value
            }
        }
    }

    fun updatePassword(formData: UpdatePasswordFormData) {
        viewModelScope.launch {
            clearErrors()

            val response = repository.updatePassword(formData)

            if (response == null) {
                Toast.makeText(context, "Something went wrong. Please try again", Toast.LENGTH_SHORT).show()
            } else {
                if (response.status == "success") {
                    Toast.makeText(context, "Password updated", Toast.LENGTH_SHORT).show()
                    clearInputs()
                } else if (response.data.data != null) {
                    setErrors(response.data.data)
                }
            }
        }
    }

    private fun clearInputs() {
        _formData.value = _formData.value.copy(
            password = "",
            passwordConfirm = "",
            currentPassword = "",
        )
    }
}