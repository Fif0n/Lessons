package com.example.lessons.viewModels.student

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.lessons.modules.backendApi.ApiService
import com.example.lessons.modules.backendApi.data.remote.TeacherPagingSource
import com.example.lessons.ui.formDatas.FindTeacherFormData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FindTeacherViewModel @Inject constructor(
    private val apiService: ApiService,
) : ViewModel() {
    private val _formData = MutableStateFlow<FindTeacherFormData>(FindTeacherFormData())
    val formData: StateFlow<FindTeacherFormData> = _formData

    @OptIn(ExperimentalCoroutinesApi::class)
    val teacherFlow = formData.flatMapLatest { form ->
        Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { TeacherPagingSource(apiService, form) }
        ).flow
    }.cachedIn(viewModelScope)

    private val _subjectEnums = MutableStateFlow<Map<String, String>>(emptyMap())
    val subjectsEnum: StateFlow<Map<String, String>> = _subjectEnums

    private val _schoolLevelEnum = MutableStateFlow<Map<String, String>>(emptyMap())
    val schoolLevelEnum: StateFlow<Map<String, String>> = _schoolLevelEnum

    private val _lessonPlaceEnum = MutableStateFlow<Map<String, String>>(emptyMap())
    val lessonPlaceEnum: StateFlow<Map<String, String>> = _lessonPlaceEnum

    init {
        viewModelScope.launch {
            val response = apiService.getLessonsSettings()
            _subjectEnums.value = response.data.enums.subjects
            _schoolLevelEnum.value = response.data.enums.schoolLevels
            _lessonPlaceEnum.value = response.data.enums.lessonPlaces
        }
    }

    fun updateFormFieldMap(field: String, value: Map<String, String>) {
        _formData.value = _formData.value.copy(
            subject = if (field == "subject") value else _formData.value.subject,
            lessonPlace = if (field == "lessonPlace") value else _formData.value.lessonPlace,
            schoolLevel = if (field == "schoolLevel") value else _formData.value.schoolLevel,
        )
    }

    fun updateFormField(field: String, value: String) {
        _formData.value = _formData.value.copy(
            moneyRate = if (field == "moneyRate") value else _formData.value.moneyRate,
            minLessonLength = if (field == "minLessonLength") value else _formData.value.minLessonLength,
            maxLessonLength = if (field == "maxLessonLength") value else _formData.value.maxLessonLength,
        )
    }
}