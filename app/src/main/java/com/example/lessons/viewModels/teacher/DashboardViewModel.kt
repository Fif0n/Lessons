package com.example.lessons.viewModels.teacher

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lessons.modules.backendApi.ApiService
import com.example.lessons.modules.backendApi.data.dto.CurrentWeekLessonsDto
import com.example.lessons.modules.backendApi.responses.EstimatedIncomeResponseResult
import com.example.lessons.modules.backendApi.responses.EstimatedIncomeResponseResultContent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.DayOfWeek
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

    private val _estimatedIncome = MutableStateFlow<EstimatedIncomeResponseResultContent?>(null)
    val estimatedIncome: StateFlow<EstimatedIncomeResponseResultContent?> = _estimatedIncome

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

            try {
                val incomeResponse = apiService.getEstimatedIncome()
                _estimatedIncome.value = incomeResponse.data.results
            } catch (e: Throwable) {
                _estimatedIncome.value = null
            }

            _isLoading.value = false
        }
    }
}