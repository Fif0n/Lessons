package com.example.lessons.viewModels.student

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.lessons.models.LessonRequest
import com.example.lessons.modules.backendApi.ApiService
import com.example.lessons.modules.backendApi.requests.LessonRequestStatus
import com.example.lessons.modules.backendApi.responses.PostLessonRequestStatusResponse
import com.example.lessons.modules.backendApi.responses.UpdateUserResponse
import com.example.lessons.ui.teacher.navigation.Screen
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LessonRequestViewModel(
    private val apiService: ApiService,
    private val id: String?
): ViewModel() {
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _lessonRequest = MutableStateFlow<LessonRequest?>(null)
    val lessonRequest: StateFlow<LessonRequest?> = _lessonRequest

    init {
        viewModelScope.launch {
            if (id != null) {
                val response = apiService.getLessonRequest(id)

                _lessonRequest.value = response.data?.lessonRequest
                _isLoading.value = false
            }
        }
    }

    suspend fun sendMessage(context: Context, id: String, message: String, navController: NavController) {
        viewModelScope.launch {
            val body = LessonRequestStatus("pending", message)

            try {
                val response = apiService.postLessonRequestStatus(id, body)
                if (response.status == "success") {
                    _lessonRequest.value = response.data.lessonRequest

                    if (response.data.conversation != null) {
                        navController.navigate(Screen.Conversation.createRoute(response.data.conversation._id))
                    }
                } else {
                    Toast.makeText(context, "Something went wrong. Please try again", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Throwable) {
                Toast.makeText(context, "Something went wrong. Please try again", Toast.LENGTH_SHORT).show()
            }
        }
    }
}