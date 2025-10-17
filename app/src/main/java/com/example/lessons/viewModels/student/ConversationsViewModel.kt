package com.example.lessons.viewModels.student

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.lessons.modules.backendApi.ApiService
import com.example.lessons.modules.backendApi.data.remote.ConversationPagingSource
import com.example.lessons.ui.formDatas.ConversationsFormData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class ConversationsViewModel @Inject constructor(
    private val apiService: ApiService,
) : ViewModel() {
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _formData = MutableStateFlow<ConversationsFormData>(ConversationsFormData())
    val formData: StateFlow<ConversationsFormData> = _formData

    @OptIn(ExperimentalCoroutinesApi::class)
    val conversationFlow = formData.flatMapLatest { form ->
        Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { ConversationPagingSource(apiService, form) }
        ).flow
    }.cachedIn(viewModelScope)

    fun updateFormField(field: String, value: String) {
        _formData.value = _formData.value.copy(
            id = if (field == "id") value else _formData.value.id,
        )
    }
}