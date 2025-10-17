package com.example.lessons.viewModels.student

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lessons.models.Conversation
import com.example.lessons.modules.backendApi.ApiService
import com.example.lessons.modules.backendApi.requests.SendMessageRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ConversationViewModel(
    private val apiService: ApiService,
    private val id: String?
): ViewModel() {
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _conversation = MutableStateFlow<Conversation?>(null)
    val conversation: StateFlow<Conversation?> = _conversation

    init {
        viewModelScope.launch {
            if (id != null) {
                val response = apiService.getConversation(id)

                if (response.status == "success") {
                    _conversation.value = response.data.conversation
                    _isLoading.value = false
                }
            }
        }
    }

    fun sendMessage(message: String, id: String, context: Context, onSuccess: () -> Unit) {
        viewModelScope.launch {
            val body = SendMessageRequest(message)

            try {
                val response = apiService.sendMessage(id, body)
                if (response.status == "success") {
                    _conversation.value = response.data.conversation
                    onSuccess()
                } else {
                    Toast.makeText(context, "Something went wrong. Please try again", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Throwable) {
                Toast.makeText(context, "Something went wrong. Please try again", Toast.LENGTH_SHORT).show()
            }
        }
    }
}