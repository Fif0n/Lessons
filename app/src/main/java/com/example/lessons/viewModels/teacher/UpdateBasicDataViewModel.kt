package com.example.lessons.viewModels.teacher

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lessons.R
import com.example.lessons.models.User
import com.example.lessons.repositories.UserRepository
import com.example.lessons.ui.formDatas.BasicFormData
import com.example.lessons.utils.UiEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UpdateBasicDataViewModel(private val repository: UserRepository, val context: Context): ViewModel() {
    private val _formData = MutableStateFlow<BasicFormData>(BasicFormData())
    val formData: StateFlow<BasicFormData> = _formData

    var emailError = MutableStateFlow<String?>(null)
        private set
    var nameError = MutableStateFlow<String?>(null)
        private set
    var surnameError = MutableStateFlow<String?>(null)
        private set
    var phoneNumberError = MutableStateFlow<String?>(null)
        private set
    var yourselfDescriptionError = MutableStateFlow<String?>(null)
        private set

    private var user: User? = null

    private val _uiEvent = MutableStateFlow<UiEvent?>(null)
    val uiEvent: StateFlow<UiEvent?> = _uiEvent

    init {
        viewModelScope.launch {
            user = repository.getLoggedUserData()
            fillFormData()
        }
    }

    private fun createFilledFormData(): BasicFormData {
        return BasicFormData(
            name = user?.name ?: "",
            surname = user?.surname ?: "",
            email = user?.email ?: "",
            phoneNumber = user?.phoneNumber ?: "",
            yourselfDescription = user?.yourselfDescription ?: ""
        )
    }

    fun updateFormField(field: String, value: String) {
        _formData.value = _formData.value.copy(
            name = if (field == "name") value else _formData.value.name,
            email = if (field == "email") value else _formData.value.email,
            phoneNumber = if (field == "phoneNumber") value else _formData.value.phoneNumber,
            surname = if (field == "surname") value else _formData.value.surname,
            yourselfDescription = if (field == "yourselfDescription") value else _formData.value.yourselfDescription,
        )
    }

    fun updateUserData(formData: BasicFormData, panelViewModel: PanelViewModel) {
        viewModelScope.launch {
            clearErrors()

            val response = repository.updateUser(formData)

            if (response == null) {
                _uiEvent.value = UiEvent.ShowMessage(R.string.error)
            } else {
                if (response.status == "success") {
                    _uiEvent.value = UiEvent.ShowMessage(R.string.data_updated)
                    updateUser()
                    panelViewModel.checkVerification()
                } else if (response.data.data != null) {
                    setErrors(response.data.data)
                }
            }
        }
    }

    private fun clearErrors() {
        emailError.value = null
        nameError.value = null
        surnameError.value = null
        phoneNumberError.value = null
        yourselfDescriptionError.value = null
    }

    private fun setErrors(data: Map<String, String>) {
        data.forEach { i ->
            val value = i.value
            when (i.key) {
                "email" -> emailError.value = value
                "name" -> nameError.value = value
                "surname" -> surnameError.value = value
                "phoneNumber" -> phoneNumberError.value = value
                "yourselfDescription" -> yourselfDescriptionError.value = value
            }
        }
    }

    private suspend fun updateUser() {
        val user = repository.getLoggedUserData()
        if (user != null) {
            this.user = user
            fillFormData()
        }
    }

    private fun fillFormData() {
        val formData = createFilledFormData()
        _formData.value = formData
    }

    fun clearUiEvent() {
        _uiEvent.value = null
    }
}