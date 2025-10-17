package com.example.lessons.viewModels.teacher

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lessons.modules.backendApi.ApiService
import com.example.lessons.modules.backendApi.data.dto.CurrentWeekLessonsDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.WeekFields
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
class CalendarViewModel(
    private val apiService: ApiService
): ViewModel() {
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _lessons = MutableStateFlow<List<CurrentWeekLessonsDto>?>(null)
    val lessons: StateFlow<List<CurrentWeekLessonsDto>?> = _lessons

    private val _year = MutableStateFlow<Int?>(null)
    val year: StateFlow<Int?> = _year

    private val _week = MutableStateFlow<Int?>(null)
    val week: StateFlow<Int?> = _week

    private val _startingDate = MutableStateFlow<LocalDate?>(null)
    val startingDate: StateFlow<LocalDate?> = _startingDate

    private val _endingDate = MutableStateFlow<LocalDate?>(null)
    val endingDate: StateFlow<LocalDate?> = _endingDate

    init {
        viewModelScope.launch {
            val today = LocalDate.now()
            val weekFields = WeekFields.ISO

            _year.value = today.get(weekFields.weekBasedYear())
            _week.value = today.get(weekFields.weekOfWeekBasedYear())
            setWeekRange()

            if (_year.value != null && _week.value != null) {
                val response = apiService.getIncomingLessons(_year.value!!, _week.value!!)
                _lessons.value = response

                _isLoading.value = false
            }
        }
    }

    private fun setWeekRange() {
        val weekFields = WeekFields.ISO
        val today = LocalDate.now()

        val year = _year.value ?: today.get(weekFields.weekBasedYear())
        val week = _week.value ?: today.get(weekFields.weekBasedYear())

        val startOfWeek = LocalDate
            .now()
            .withYear(year)
            .with(weekFields.weekOfWeekBasedYear(), week.toLong())
            .with(weekFields.dayOfWeek(), DayOfWeek.MONDAY.value.toLong())

        val endOfWeek = startOfWeek.plusDays(6)

        _startingDate.value = startOfWeek
        _endingDate.value = endOfWeek

        viewModelScope.launch {
            val response = apiService.getIncomingLessons(_year.value!!, _week.value!!)
            _lessons.value = response
        }
    }

    fun nextWeek() {
        val weekFields = WeekFields.ISO

        if (_year.value != null && _week.value != null) {
            val currentWeekStart = LocalDate
                .now()
                .withYear(_year.value!!)
                .with(weekFields.weekOfWeekBasedYear(), _week.value!!.toLong())
                .with(weekFields.dayOfWeek(), DayOfWeek.MONDAY.value.toLong())

            val nextWeekStart = currentWeekStart.plusWeeks(1)

            _year.value = nextWeekStart.get(weekFields.weekBasedYear())
            _week.value = nextWeekStart.get(weekFields.weekOfWeekBasedYear())
            setWeekRange()
        }
    }

    fun previousWeek() {
        val weekFields = WeekFields.ISO

        if (_year.value != null && _week.value != null) {
            val currentWeekStart = LocalDate
                .now()
                .withYear(_year.value!!)
                .with(weekFields.weekOfWeekBasedYear(), _week.value!!.toLong())
                .with(weekFields.dayOfWeek(), DayOfWeek.MONDAY.value.toLong())

            val nextWeekStart = currentWeekStart.plusWeeks(-1)

            _year.value = nextWeekStart.get(weekFields.weekBasedYear())
            _week.value = nextWeekStart.get(weekFields.weekOfWeekBasedYear())
            setWeekRange()
        }
    }
}