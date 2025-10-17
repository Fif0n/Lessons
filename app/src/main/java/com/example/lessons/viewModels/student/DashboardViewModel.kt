package com.example.lessons.viewModels.student


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lessons.modules.backendApi.ApiService
import com.example.lessons.modules.backendApi.data.dto.CurrentWeekLessonsDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.temporal.WeekFields
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
class DashboardViewModel(
    private val apiService: ApiService
): ViewModel() {
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _lessons = MutableStateFlow<List<CurrentWeekLessonsDto>?>(null)
    val lessons: StateFlow<List<CurrentWeekLessonsDto>?> = _lessons

    private var _year: Int? = null

    private var _week: Int? = null

    init {
        viewModelScope.launch {
            val today = LocalDate.now()
            val weekFields = WeekFields.ISO

            _year = today.get(weekFields.weekBasedYear())
            _week = today.get(weekFields.weekOfWeekBasedYear())

            val response = apiService.getIncomingLessons(_year!!, _week!!)
            _lessons.value = response

            _isLoading.value = false
        }
    }
}