package com.example.lessons.viewModels

import android.content.Context
import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lessons.auth.LoginRequest
import com.example.lessons.repositories.LoginRepository
import com.example.lessons.utils.LoggedUser
import com.example.lessons.utils.NavigationEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: LoginRepository): ViewModel() {
    var emailError = MutableStateFlow<String?>(null)
        private set

    var passwordError = MutableStateFlow<String?>(null)
        private set

    var loginState = MutableStateFlow<LoginState>(LoginState.Idle)
        private set

    var navigationEvent = MutableLiveData<NavigationEvent>()
        private set

    fun login(email: String, password: String, role: String) {
        viewModelScope.launch {
            loginState.value = LoginState.Loading
            clearErrors()

            val loginRequest = LoginRequest(email, password, role)

            repository.login(loginRequest) {
                val status = it?.status

                if (status == "success") {
                    loginState.value = LoginState.Success
                } else {
                    val responseData: Any? = it?.data
                    if (responseData is Map<*, *>) {
                        setErrors(responseData)
                        loginState.value = LoginState.Error("Login error")
                    }
                }
            }
        }
    }

    private fun setErrors(data: Map<*, *>) {
        data.forEach { i ->
            val value = i.value.toString()
            when (i.key) {
                "email" -> emailError.value = value
                "password" -> passwordError.value = value
            }
        }
    }

    private fun clearErrors() {
        emailError.value = null
        passwordError.value = null
    }

    fun redirectToDashboard(context: Context, destination: Class<*>,) {
        val intent = Intent(context, destination)
        navigationEvent.postValue(NavigationEvent.Redirect(intent))
    }
}

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    object Success : LoginState()
    data class Error(val message: String) : LoginState()
}

