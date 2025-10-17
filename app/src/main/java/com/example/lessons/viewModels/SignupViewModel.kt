package com.example.lessons.viewModels

import android.content.Context
import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lessons.auth.SignupRequest
import com.example.lessons.repositories.SignupRepository
import com.example.lessons.utils.NavigationEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SignupViewModel(private val repository: SignupRepository): ViewModel() {
    var nameError = MutableStateFlow<String?>(null)
        private set

    var surnameError = MutableStateFlow<String?>(null)
        private set

    var emailError = MutableStateFlow<String?>(null)
        private set

    var passwordError = MutableStateFlow<String?>(null)
        private set

    var passwordConfirmError = MutableStateFlow<String?>(null)
        private set

    var signupState = MutableStateFlow<SignupState>(SignupState.Idle)
        private set

    var navigationEvent = MutableLiveData<NavigationEvent>()
        private set

    fun signup(name: String, surname: String, email: String, password: String, passwordConfirm: String, role: String) {
        viewModelScope.launch {
            signupState.value = SignupState.Loading
            clearErrors()

            val signupRequest = SignupRequest(name, surname, email, password, passwordConfirm, role)

            repository.signUp(signupRequest) {
                val status = it?.status
                if (status == "success") {
                    signupState.value = SignupState.Success
                } else {
                    val responseData: Any? = it?.data
                    if (responseData is Map<*, *>) {
                        setErrors(responseData)
                        signupState.value = SignupState.Error("Signup error")
                    }
                }
            }

        }
    }

    private fun setErrors(data: Map<*, *>) {
        data.forEach { i ->
            val value = i.value.toString()
            when (i.key) {
                "name" -> nameError.value = value
                "surname" -> surnameError.value = value
                "email" -> emailError.value = value
                "password" -> passwordError.value = value
                "passwordConfirm" -> passwordConfirmError.value = value
            }
        }
    }

    private fun clearErrors() {
        nameError.value = null
        surnameError.value = null
        emailError.value = null
        passwordError.value = null
        passwordConfirmError.value = null
    }

    fun redirectToLogin(context: Context, destination: Class<*>,) {
        val intent = Intent(context, destination)
        navigationEvent.postValue(NavigationEvent.Redirect(intent))
    }
}

sealed class SignupState {
    object Idle : SignupState()
    object Loading : SignupState()
    object Success : SignupState()
    data class Error(val message: String) : SignupState()
}