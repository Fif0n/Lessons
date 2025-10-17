package com.example.lessons.viewModels.student

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.lessons.modules.backendApi.ApiService
import com.example.lessons.modules.backendApi.data.remote.LessonRequestPagingSource
import com.example.lessons.ui.formDatas.LessonRequestsFormData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LessonRequestsViewModel @Inject constructor(
    private val apiService: ApiService,
) : ViewModel() {
    private val _formData = MutableStateFlow<LessonRequestsFormData>(LessonRequestsFormData())
    val formData: StateFlow<LessonRequestsFormData> = _formData

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    @OptIn(ExperimentalCoroutinesApi::class)
    val lessonRequestFlow = formData.flatMapLatest { form ->
        Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { LessonRequestPagingSource(apiService, form) }
        ).flow
    }.cachedIn(viewModelScope)

    private val _statusEnums = MutableStateFlow<Map<String, String>>(emptyMap())
    val statusEnums: StateFlow<Map<String, String>> = _statusEnums

    init {
        viewModelScope.launch {
            val response = apiService.getLessonRequestEnums()
            _statusEnums.value = response.data.status
            _isLoading.value = false
        }
    }

    fun updateFormField(field: String, value: String) {
        _formData.value = _formData.value.copy(
            status = if (field == "status") value else _formData.value.status,
            id = if (field == "id") value else _formData.value.id,
        )
    }
}
