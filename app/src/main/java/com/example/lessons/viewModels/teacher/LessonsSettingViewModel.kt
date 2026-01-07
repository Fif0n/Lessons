package com.example.lessons.viewModels.teacher

import android.content.Context
import android.location.Geocoder
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lessons.R
import com.example.lessons.models.Location
import com.example.lessons.modules.backendApi.responses.LessonsSettingsResponse
import com.example.lessons.repositories.UserRepository
import com.example.lessons.utils.UiEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.util.Locale

class LessonsSettingViewModel(private val repository: UserRepository): ViewModel() {
    private val _formData = MutableStateFlow<LessonsSettingsFormData>(LessonsSettingsFormData())
    val formData: StateFlow<LessonsSettingsFormData> = _formData

    private val _subjectEnums = MutableStateFlow<Map<String, String>>(emptyMap())
    val subjectsEnum: StateFlow<Map<String, String>> = _subjectEnums

    private val _schoolLevelEnum = MutableStateFlow<Map<String, String>>(emptyMap())
    val schoolLevelEnum: StateFlow<Map<String, String>> = _schoolLevelEnum

    private val _lessonPlaceEnum = MutableStateFlow<Map<String, String>>(emptyMap())
    val lessonPlaceEnum: StateFlow<Map<String, String>> = _lessonPlaceEnum

    var subjectError = MutableStateFlow<String?>(null)
        private set

    var schoolLevelError = MutableStateFlow<String?>(null)
        private set

    var lessonPlaceError = MutableStateFlow<String?>(null)
        private set

    var lessonMoneyRateError = MutableStateFlow<String?>(null)
        private set

    var lessonLengthError = MutableStateFlow<String?>(null)
        private set

    var lessonsPlatformError = MutableStateFlow<String?>(null)
        private set

    var addressError = MutableStateFlow<String?>(null)
        private set

    private val _uiEvent = MutableStateFlow<UiEvent?>(null)
    val uiEvent: StateFlow<UiEvent?> = _uiEvent

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        viewModelScope.launch {
            val response = repository.getLessonsSettings()
            _subjectEnums.value = response.data.enums.subjects
            _schoolLevelEnum.value = response.data.enums.schoolLevels
            _lessonPlaceEnum.value = response.data.enums.lessonPlaces

            setDefaultData(response)
        }
    }

    fun updateFormField(field: String, value: String) {
        _formData.value = _formData.value.copy(
            lessonMoneyRate = if (field == "lessonMoneyRate") value else _formData.value.lessonMoneyRate,
            lessonLength = if (field == "lessonLength") value else _formData.value.lessonLength,
            lessonsPlatform = if (field == "lessonsPlatform") value else _formData.value.lessonsPlatform,
        )
    }

    fun updateFormFieldMap(field: String, value: Map<String, String>) {
        _formData.value = _formData.value.copy(
            subject = if (field == "subject") value else _formData.value.subject,
            lessonPlace = if (field == "lessonPlace") value else _formData.value.lessonPlace,
            schoolLevel = if (field == "schoolLevel") value else _formData.value.schoolLevel,
        )
    }

    fun getLatLngFromAddress(address: String, context: Context): Pair<Double, Double>? {
        return try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses = geocoder.getFromLocationName(address, 1)
            if (addresses?.isNotEmpty() == true) {
                val location = addresses[0]
                Pair(location.latitude, location.longitude)
            } else null
        } catch (e: Exception) {
            null
        }
    }

    fun updateLocation(location: Location?) {
        _formData.value = _formData.value.copy(
            location = location
        )
    }

    fun updateLessonsData(formData: LessonsSettingsFormData, panelViewModel: PanelViewModel) {
        viewModelScope.launch {
            _isLoading.value = true
            clearError()
            val response = try {
                repository.updateUserLessonsSetting(formData)
            } catch (e: Exception) {
                null
            }

            _isLoading.value = false

            if (response == null) {
                _uiEvent.value = UiEvent.ShowMessage(R.string.error)
            } else if (response.status == "success") {
                _uiEvent.value = UiEvent.ShowMessage(R.string.settings_updated)
                panelViewModel.checkVerification()
            } else if (response.data.data != null) {
                setErrors(response.data.data)
            }
        }
    }

    private fun clearError() {
        subjectError.value = null
        schoolLevelError.value = null
        lessonPlaceError.value = null
        lessonMoneyRateError.value = null
        lessonLengthError.value = null
        lessonsPlatformError.value = null
        addressError.value = null
    }

    private fun setErrors(data: Map<String, String>) {
        data.forEach { i ->
            val value = i.value
            when (i.key) {
                "subject" -> subjectError.value = value
                "schoolLevel" -> schoolLevelError.value = value
                "lessonPlace" -> lessonPlaceError.value = value
                "lessonMoneyRate" -> lessonMoneyRateError.value = value
                "lessonLength" -> lessonLengthError.value = value
                "lessonsPlatform" -> lessonsPlatformError.value = value
                "location.address" -> addressError.value = value
            }
        }
    }

    private fun setDefaultData(response: LessonsSettingsResponse) {
        _formData.value = _formData.value.copy(
            subject = response.data.enums.subjects.filterKeys  { key -> response.data.user.subject?.contains(key) == true },
            schoolLevel = response.data.enums.schoolLevels.filterKeys  { key -> response.data.user.schoolLevel?.contains(key) == true },
            lessonPlace = response.data.enums.lessonPlaces.filterKeys  { key -> response.data.user.lessonPlace?.contains(key) == true },
            lessonMoneyRate = (response.data.user.lessonMoneyRate ?: "").toString(),
            lessonLength = (response.data.user.lessonLength ?: "").toString(),
            lessonsPlatform = response.data.user.lessonsPlatform ?: "",
            location = response.data.user.location
        )
    }

    fun clearUiEvent() {
        _uiEvent.value = null
    }
}

data class LessonsSettingsFormData(
    val scenario: String = "updateLessonsSettings",
    val subject: Map<String, String> = emptyMap(),
    val schoolLevel: Map<String, String> = emptyMap(),
    val lessonPlace: Map<String, String> = emptyMap(),
    val location: Location? = null,
    val lessonsPlatform: String = "",
    val lessonMoneyRate: String = "",
    val lessonLength: String = "",
)