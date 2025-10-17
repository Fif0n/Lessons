package com.example.lessons.viewModels.teacher

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.lessons.modules.backendApi.ApiService
import com.example.lessons.modules.backendApi.data.remote.LessonHistoryPagingSource
import com.example.lessons.ui.formDatas.LessonsHistoryFormData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LessonsHistoryViewModel @Inject constructor(
    private val apiService: ApiService,
): ViewModel() {
    private val _formData = MutableStateFlow<LessonsHistoryFormData>(LessonsHistoryFormData())
    val formData: StateFlow<LessonsHistoryFormData> = _formData

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    @OptIn(ExperimentalCoroutinesApi::class)
    val lessonsHistoryFlow = formData.flatMapLatest { form ->
        Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { LessonHistoryPagingSource(apiService, form) }
        ).flow
    }.cachedIn(viewModelScope)

    private val _subjectEnum = MutableStateFlow<Map<String, String>>(emptyMap())
    val subjectEnum: StateFlow<Map<String, String>> = _subjectEnum

    init {
        viewModelScope.launch {
            val response = apiService.getLessonsHistoryEnums()
            _subjectEnum.value = response.data.subjects
            _isLoading.value = false
        }
    }

    fun updateFormField(field: String, value: String) {
        _formData.value = _formData.value.copy(
            name = if (field == "name") value else _formData.value.name,
            id = if (field == "id") value else _formData.value.id,
            subject = if (field == "subject") value else _formData.value.subject,
        )
    }
}