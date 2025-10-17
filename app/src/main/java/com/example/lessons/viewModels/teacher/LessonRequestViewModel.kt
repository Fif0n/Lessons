package com.example.lessons.viewModels.teacher

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.lessons.models.LessonRequest
import com.example.lessons.modules.backendApi.ApiService
import com.example.lessons.modules.backendApi.requests.LessonRequestStatus
import com.example.lessons.modules.backendApi.requests.SetLessonRequestLink
import com.example.lessons.modules.backendApi.responses.LessonRequestResponse
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

    suspend fun acceptLessonRequest(context: Context, id: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            val body = LessonRequestStatus("accepted")
            try {
                val response = apiService.postLessonRequestStatus(id, body)

                if (response.status == "success") {
                    _lessonRequest.value = response.data.lessonRequest
                    Toast.makeText(context, "Lesson accepted", Toast.LENGTH_SHORT).show()
                    onSuccess()
                } else {
                    Toast.makeText(context, "Something went wrong. Please try again", Toast.LENGTH_SHORT).show()
                }
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val error = Gson().fromJson(errorBody, PostLessonRequestStatusResponse::class.java)
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            } catch (e: Throwable) {
                Toast.makeText(context, "Something went wrong. Please try again", Toast.LENGTH_SHORT).show()
            }
        }
    }

    suspend fun rejectLessonRequest(context: Context, id: String, message: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            val body = LessonRequestStatus("rejected", message)

            try {
                val response = apiService.postLessonRequestStatus(id, body)
                if (response.status == "success") {
                    _lessonRequest.value = response.data.lessonRequest
                    Toast.makeText(context, "Lesson rejected", Toast.LENGTH_SHORT).show()
                    onSuccess()
                } else {
                    Toast.makeText(context, "Something went wrong. Please try again", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Throwable) {
                Toast.makeText(context, "Something went wrong. Please try again", Toast.LENGTH_SHORT).show()
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

    suspend fun setLessonLink(context: Context, id: String, link: String) {
        viewModelScope.launch {
            val body = SetLessonRequestLink(link)

            try {
                val response = apiService.setLessonLink(id, body)
                if (response.status == "success") {
                    _lessonRequest.value = response.data?.lessonRequest
                    Toast.makeText(context, "Link has been set", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Something went wrong. Please try again", Toast.LENGTH_SHORT).show()
                }
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val error = Gson().fromJson(errorBody, LessonRequestResponse::class.java)
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            } catch (e: Throwable) {
                Toast.makeText(context, "Something went wrong. Please try again", Toast.LENGTH_SHORT).show()
            }
        }
    }
}